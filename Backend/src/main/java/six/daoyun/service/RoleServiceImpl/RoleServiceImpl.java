package six.daoyun.service.RoleServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import six.daoyun.controller.exception.HttpNotFound;
import six.daoyun.entity.PermEntry;
import six.daoyun.entity.Role;
import six.daoyun.exception.Forbidden;
import six.daoyun.exception.NotFound;
import six.daoyun.repository.PermEntryRepository;
import six.daoyun.repository.RoleRepository;
import six.daoyun.service.RoleService;
import six.daoyun.utils.ObjUtil;

@Service
public class RoleServiceImpl implements RoleService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RoleServiceImpl.class);
    @Autowired
    private PermEntryRepository permEntryRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${strictadmin.enable}")
    private Boolean strictadmin;
    @Value("${strictadmin.admin}")
    private String adminRole;

	@Override
	public void createRole(String roleName) //{
    {
        log.info("create new role: {}", roleName);
        Role newRole = new Role();
        newRole.setRoleName(roleName);
        this.roleRepository.save(newRole);
	} //}

	@Override
	public Optional<Role> getRoleByRoleName(String roleName) //{
    {
        Role ans = this.roleRepository.getRoleByRoleName(roleName);
        return Optional.ofNullable(ans);
	} //}

	@Override
	public boolean hasRole(String role) //{
    {
        return this.roleRepository.getRoleByRoleName(role) != null;
	} //}

	@Override
	public Collection<Role> getAllRoles() //{
    {
        ArrayList<Role> ans = new ArrayList<Role>();
        this.roleRepository.findAll().forEach(ans::add);
        return ans;
	} //}

	@Override
	public void updateRoleName(String oldRoleName, String newRoleName) //{
    {
        Role role = this.getRoleByRoleName(oldRoleName)
            .orElseThrow(() -> new HttpNotFound("角色不存在"));
        role.setRoleName(newRoleName);
        this.roleRepository.save(role);
	} //}

	@Override
	public void deleteRole(String roleName) //{
    {
        if(this.strictadmin && roleName.equals(this.adminRole)) {
            throw new Forbidden("管理员角色不可以删除");
        }

        final Role role = this.getRoleByRoleName(roleName)
            .orElseThrow(() -> new NotFound(String.format("角色 %s 不存在", roleName)));
        this.roleRepository.delete(role);
	} //}

    private Role getRole(String roleName) //{
    {
        return this.getRoleByRoleName(roleName)
            .orElseThrow(() -> new NotFound(String.format("角色 %s 不存在", roleName)));
    } //}

	@Override
	public void enablePermEntry(String roleName, String descriptor) //{
    {
        Role role = this.getRole(roleName);
        PermEntry perm = this.permEntryRepository.findByDescriptor(descriptor)
            .orElseThrow(() -> new NotFound(String.format("权限项 %s 不存在", descriptor)));
        if(perm.getRoles().contains(role)) {
            throw new Forbidden("角色已拥有该菜单: " + descriptor);
        }

        perm.getRoles().add(role);
        this.permEntryRepository.save(perm);
	} //}

	@Override
    public void disablePermEntry(String roleName, String descriptor) //{
    {
        if(this.strictadmin && roleName.equals(this.adminRole)) {
            throw new Forbidden("不可以禁用修改管理员角色的权限");
        }

        Role role = this.getRole(roleName);
        PermEntry perm = this.permEntryRepository.findByDescriptor(descriptor)
            .orElseThrow(() -> new NotFound(String.format("权限项 %s 不存在", descriptor)));
        if(!perm.getRoles().contains(role)) {
            throw new Forbidden(String.format("角色 %s 无权限 %s", roleName, descriptor));
        }

        perm.getRoles().add(role);
        this.permEntryRepository.save(perm);
	} //}

    private void getTreeDescendants(PermEntry entry, Collection<PermEntry> list) //{
    {
        for(final PermEntry sentry: entry.getChildren()) {
            list.add(sentry);
            this.getTreeDescendants(sentry, list);
        }
    } //}

	@Override
    public void enablePermEntryRecursively(String roleName, String descriptor) //{
    {
        Role role = this.getRole(roleName);
        PermEntry perm = this.permEntryRepository.findByDescriptor(descriptor)
            .orElseThrow(() -> new NotFound(String.format("权限项 %s 不存在", descriptor)));

        Collection<PermEntry> permlist = new ArrayList<>();
        permlist.add(perm);
        this.getTreeDescendants(perm, permlist);

        for(final PermEntry sperm: permlist) {
            if(!sperm.getRoles().contains(role)) {
                sperm.getRoles().add(role);
            }
        }
        this.permEntryRepository.saveAll(permlist);
	} //}

	@Override
    public void disablePermEntryRecursively(String roleName, String descriptor) //{
    {
        if(this.strictadmin && roleName.equals(this.adminRole)) {
            throw new Forbidden("不可以禁用修改管理员角色的权限");
        }

        Role role = this.getRole(roleName);
        PermEntry perm = this.permEntryRepository.findByDescriptor(descriptor)
            .orElseThrow(() -> new NotFound(String.format("权限项 %s 不存在", descriptor)));

        Collection<PermEntry> permlist = new ArrayList<>();
        permlist.add(perm);
        this.getTreeDescendants(perm, permlist);

        for(final PermEntry sperm: permlist) {
            if(sperm.getRoles().contains(role)) {
                sperm.getRoles().remove(role);
            }
        }
        this.permEntryRepository.saveAll(permlist);
    } //}

    private boolean isChildDescriptor(String parentDescriptor, String childDescriptor) //{
    {
        if(parentDescriptor != null && !childDescriptor.startsWith(parentDescriptor + ".")) {
            return false;
        }

        final String rml = parentDescriptor == null ? childDescriptor : childDescriptor.substring(parentDescriptor.length() + 1);
        return Pattern.matches("[a-zA-Z][a-zA-Z0-9_]+", rml);
    } //}
    private PermEntry getPermEntry(String descriptor) //{
    {
        return this.permEntryRepository.findByDescriptor(descriptor)
            .orElseThrow(() -> new NotFound(String.format("权限项 %s 不存在", descriptor)));
    } //}

	@Override
	public void addPermEntry(String parentDescriptor, PermEntryItem permx) //{
    {
        this.cachetree = null;
        if(this.permEntryRepository.findByDescriptor(permx.getDescriptor()).isPresent()) {
            throw new Forbidden("Descriptor 已存在");
        }

        PermEntry perm = new PermEntry();
        ObjUtil.assignFields(perm, permx);
        if(!this.isChildDescriptor(parentDescriptor, permx.getDescriptor())) {
            throw new Forbidden("bad descriptor");
        }

        if(parentDescriptor != null) {
            final PermEntry parentEntry = this.getPermEntry(parentDescriptor);
            perm.setParent(parentEntry);
        }

        this.permEntryRepository.save(perm);
	} //}

	@Override
	public void removePermEntry(String descriptor) //{
    {
        this.cachetree = null;
        final PermEntry entry = this.getPermEntry(descriptor);
        if(entry.getChildren() == null || entry.getChildren().size() > 0) {
            throw new Forbidden("节点存在子节点");
        }

        this.permEntryRepository.delete(entry);
	} //}

	@Override
	public void removePermEntryRecursively(String descriptor) //{
    {
        this.cachetree = null;
        ArrayList<PermEntry> entries = new ArrayList<>();
        final PermEntry entry = this.getPermEntry(descriptor);
        entries.add(entry);
        this.getTreeDescendants(entry, entries);
        log.info("n = {}", entries.size());
        this.permEntryRepository.deleteAll(entries);
	} //}

	@Override
	public void updatePermEntry(String descriptor, PermEntryItem perm) //{
    {
        this.cachetree = null;
        perm.setDescriptor(descriptor);
        final PermEntry entry = this.getPermEntry(descriptor);
        ObjUtil.assignFields(entry, perm);
        this.permEntryRepository.save(entry);
	} //}

    static Map<String, Integer> type2value = new HashMap<>();
    static {
        type2value.put("menu",   Integer.valueOf(10000));
        type2value.put("page",   Integer.valueOf(100));
        type2value.put("button", Integer.valueOf(1));
    }
    private PermEntryTree PermEntryToPermEntryTree(PermEntry entry) //{
    {
        PermEntryTree ans = new PermEntryTree();
        ArrayList<PermEntryTree> children = new ArrayList<>();
        ObjUtil.assignFields(ans, entry);

        final Collection<PermEntry> childrenx = entry.getChildren();
        if(childrenx != null) {
            for(final PermEntry e: childrenx) {
                children.add(this.PermEntryToPermEntryTree(e));
            }
        }

        children.sort((a, b) -> {
            long av = type2value.get(a.getEntryType());
            av += a.getSortOrder() * 100000;
            av += a.getChildren().size();

            long bv = type2value.get(b.getEntryType());
            bv += a.getSortOrder() * 100000;
            bv += b.getChildren().size();

            return av > bv ? -1 : av == bv ? 0 : 1;
        });
        ans.setChildren(children);
        return ans;
    } //}
	@Override
	public Collection<PermEntryTree> getPermEntryTree() //{
    {
        if(this.cachetree != null) return this.cachetree;

        final Collection<PermEntryTree> ans = new ArrayList<>();
        Collection<PermEntry> rootEntries = this.permEntryRepository.findByParentIsNull();

        for(final PermEntry e: rootEntries) {
            ans.add(this.PermEntryToPermEntryTree(e));
        }

        this.cachetree = ans;
		return ans;
	} //}
    private Collection<PermEntryTree> cachetree;

	@Override
	public Collection<PermEntryItem> getPermEntries(String roleName) //{
    {
        Collection<PermEntryItem> ans = new ArrayList<>();
        Collection<PermEntry> entries = this.permEntryRepository.findByRoles(this.getRole(roleName));
        entries.forEach(entry -> {
            PermEntryItem item = new PermEntryItem();
            ObjUtil.assignFields(item, entry);
            ans.add(item);
        });
        return ans;
	} //}

	@Override
	public PermEntryItem getPermEntryItemByDescriptor(String descriptor) //{
    {
        PermEntryItem item = new PermEntryItem();
        ObjUtil.assignFields(item, this.getPermEntry(descriptor));
        return item;
	} //}

	@Override
	public boolean hasPermission(String roleName, String descriptor) //{
    {
        return this.permEntryRepository.findByDescriptorAndRoles_roleName(descriptor, roleName).isPresent();
	} //}

    private boolean __hasPermissionInLink(String roleName, String link, String method) {
        PermEntry entry = this.permEntryRepository.findByLinkAndMethod(link, method)
            .orElseThrow(() -> new NotFound("找不到权限入口"));
        return this.hasPermission(roleName, entry.getDescriptor());
    }

	@Override
	public boolean hasPermissionInLink(String roleName, String link, String method) //{
    {
        try {
            if(this.__hasPermissionInLink(roleName, link, "ALL")) {
                return true;
            }
        } catch (NotFound e) {}

        return this.__hasPermissionInLink(roleName, link, method);
	} //}

	@Override
	public Collection<Role> getRolesByPermEntry(String descriptor) {
        return this.getPermEntry(descriptor).getRoles();
	}
}

