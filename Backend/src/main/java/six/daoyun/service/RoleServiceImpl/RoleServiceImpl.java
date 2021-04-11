package six.daoyun.service.RoleServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import six.daoyun.controller.exception.HttpNotFound;
import six.daoyun.entity.Role;
import six.daoyun.repository.RoleRepository;
import six.daoyun.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleRepository roleRepository;

	@Override
	public void createRole(String roleName) {
        log.info("create new role: {}", roleName);
        Role newRole = new Role();
        newRole.setRoleName(roleName);
        this.roleRepository.save(newRole);
	}

	@Override
	public Optional<Role> getRoleByRoleName(String roleName) {
        Role ans = this.roleRepository.getRoleByRoleName(roleName);
        return Optional.ofNullable(ans);
	}

	@Override
	public boolean hasRole(String role) {
        return this.roleRepository.getRoleByRoleName(role) != null;
	}

	@Override
	public Collection<Role> getAllRoles() {
        ArrayList<Role> ans = new ArrayList<Role>();
        this.roleRepository.findAll().forEach(ans::add);
        return ans;
	}

	@Override
	public void updateRoleName(String oldRoleName, String newRoleName) {
        Role role = this.getRoleByRoleName(oldRoleName).orElseThrow(() -> new HttpNotFound());
        role.setRoleName(newRoleName);
        this.roleRepository.save(role);
	}
}

