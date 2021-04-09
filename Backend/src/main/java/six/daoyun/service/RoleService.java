package six.daoyun.service;

import six.daoyun.entity.Role;
import java.util.Collection;
import java.util.Optional;

public interface RoleService {
    void createRole(String roleName);
    Optional<Role> getRoleByRoleName(String roleName);
    boolean hasRole(String roleName);
    Collection<Role> getAllRoles();

    void updateRoleName(String oldRoleName, String newRoleName);
}

