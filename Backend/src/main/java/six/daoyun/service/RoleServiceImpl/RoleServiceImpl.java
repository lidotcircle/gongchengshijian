package six.daoyun.service.RoleServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import six.daoyun.controller.exception.HttpNotFound;
import six.daoyun.entity.PermEntry;
import six.daoyun.entity.Role;
import six.daoyun.exception.Forbidden;
import six.daoyun.exception.NotFound;
import six.daoyun.repository.PermEntryRepository;
import six.daoyun.repository.RoleRepository;
import six.daoyun.service.RoleService;
import six.daoyun.utils.ObjUitl;

@Service
public class RoleServiceImpl implements RoleService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RoleServiceImpl.class);
    @Autowired
    private PermEntryRepository permEntryRepository;

    @Autowired
    private RoleRepository roleRepository;

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
        Role role = this.getRoleByRoleName(oldRoleName).orElseThrow(() -> new HttpNotFound());
        role.setRoleName(newRoleName);
        this.roleRepository.save(role);
	} //}

	@Override
	public void deleteRole(String roleName) //{
    {
        final Role role = this.getRoleByRoleName(roleName)
            .orElseThrow(() -> new NotFound());
        this.roleRepository.delete(role);
	} //}

	@Override
	public boolean hasPermission(Role role, PermEntry perm) //{
    {
        return this.permEntryRepository.findByLinkAndRoles(perm.getLink(), role).isPresent();
	} //}

    private Role getRole(String roleName) //{
    {
        return this.getRoleByRoleName(roleName)
            .orElseThrow(() -> new NotFound());
    } //}

	@Override
	public void enablePermEntry(String roleName, String descriptor) //{
    {
        Role role = this.getRole(roleName);
        PermEntry perm = this.permEntryRepository.findByDescriptor(descriptor)
            .orElseThrow(() -> new NotFound());
        if(role.getPermEntries().contains(perm)) {
            throw new Forbidden();
        }

        role.getPermEntries().add(perm);
        this.roleRepository.save(role);
	} //}

	@Override
    public void disablePermEntry(String roleName, String descriptor) //{
    {
        Role role = this.getRole(roleName);
        PermEntry perm = this.permEntryRepository.findByDescriptor(descriptor)
            .orElseThrow(() -> new NotFound());
        if(!role.getPermEntries().contains(perm)) {
            throw new Forbidden();
        }

        role.getPermEntries().remove(perm);
        this.roleRepository.save(role);
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
            .orElseThrow(() -> new NotFound());

        Collection<PermEntry> permlist = new ArrayList<>();
        permlist.add(perm);
        this.getTreeDescendants(perm, permlist);

        for(final PermEntry sperm: permlist) {
            if(!role.getPermEntries().contains(sperm)) {
                role.getPermEntries().add(perm);
            }
        }
        this.roleRepository.save(role);
	} //}

	@Override
    public void disablePermEntryRecursively(String roleName, String descriptor) //{
    {
        Role role = this.getRole(roleName);
        PermEntry perm = this.permEntryRepository.findByDescriptor(descriptor)
            .orElseThrow(() -> new NotFound());

        Collection<PermEntry> permlist = new ArrayList<>();
        permlist.add(perm);
        this.getTreeDescendants(perm, permlist);

        for(final PermEntry sperm: permlist) {
            if(role.getPermEntries().contains(sperm)) {
                role.getPermEntries().remove(perm);
            }
        }
        this.roleRepository.save(role);
    } //}

    private boolean isChildDescriptor(String parentDescriptor, String childDescriptor) //{
    {
        if(!childDescriptor.startsWith(parentDescriptor + ".")) {
            return false;
        }

        final String rml = childDescriptor.substring(parentDescriptor.length() + 1);
        return Pattern.matches("[a-zA-Z][a-zA-Z0-9_]*", rml);
    } //}
    private PermEntry getPermEntry(String descriptor) //{
    {
        return this.permEntryRepository.findByDescriptor(descriptor)
            .orElseThrow(() -> new NotFound());
    } //}

	@Override
	public void addPermEntry(String parentDescriptor, PermEntryItem permx) //{
    {
        if(this.permEntryRepository.findByDescriptor(permx.getDescriptor()).isPresent()) {
            throw new Forbidden("Descriptor 已存在");
        }

        PermEntry perm = new PermEntry();
        ObjUitl.assignFields(perm, permx);
        if(parentDescriptor != null) {
            if(this.isChildDescriptor(parentDescriptor, permx.getDescriptor())) {
                throw new Forbidden("bad descriptor");
            }

            final PermEntry entry = this.getPermEntry(parentDescriptor);
            entry.getChildren().add(perm);
            this.permEntryRepository.save(entry);
        } else {
            this.permEntryRepository.save(perm);
        }
	} //}

	@Override
	public void removePermEntry(String descriptor) //{
    {
        final PermEntry entry = this.getPermEntry(descriptor);
        if(entry.getChildren() == null || entry.getChildren().size() > 0) {
            throw new Forbidden("");
        }

        this.permEntryRepository.delete(entry);
	} //}

	@Override
	public void removePermEntryRecursively(String descriptor) //{
    {
        ArrayList<PermEntry> entries = new ArrayList<>();
        final PermEntry entry = this.getPermEntry(descriptor);
        entries.add(entry);
        this.getTreeDescendants(entry, entries);
        ListIterator<PermEntry> l = entries.listIterator(entries.size());
        while(l.hasPrevious()) {
            this.permEntryRepository.delete(l.previous());
        }
	} //}

	@Override
	public void updatePermEntry(String descriptor, PermEntryItem perm) //{
    {
        final PermEntry entry = this.getPermEntry(descriptor);
        ObjUitl.assignFields(entry, perm);
        this.permEntryRepository.save(entry);
	} //}

    private PermEntryTree PermEntryToPermEntryTree(PermEntry entry) //{
    {
        PermEntryTree ans = new PermEntryTree();
        ans.setChildren(new ArrayList<>());
        ObjUitl.assignFields(ans, entry);

        for(final PermEntry e: entry.getChildren()) {
            ans.getChildren().add(this.PermEntryToPermEntryTree(e));
        }

        return ans;
    } //}
	@Override
	public Collection<PermEntryTree> getPermEntryTree() //{
    {
        final Collection<PermEntryTree> ans = new ArrayList<>();
        Collection<PermEntry> rootEntries = this.permEntryRepository.findByParentIsNull();

        for(final PermEntry e: rootEntries) {
            ans.add(this.PermEntryToPermEntryTree(e));
        }

		return ans;
	} //}

	@Override
	public Collection<PermEntryItem> getPermEntries(String roleName) //{
    {
        Collection<PermEntryItem> ans = new ArrayList<>();
        Collection<PermEntry> entries = this.permEntryRepository.findByRoles(this.getRole(roleName));
        entries.forEach(entry -> {
            PermEntryItem item = new PermEntryItem();
            ObjUitl.assignFields(item, entry);
            ans.add(item);
        });
        return ans;
	} //}

	@Override
	public PermEntryItem getPermEntryItemByDescriptor(String descriptor) //{
    {
        PermEntryItem item = new PermEntryItem();
        ObjUitl.assignFields(item, this.getPermEntry(descriptor));
        return item;
	} //}
}

