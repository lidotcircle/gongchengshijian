package six.daoyun.service;

import six.daoyun.entity.Role;
import java.util.Collection;

public interface RoleService {
    void createRole(String roleName);
    Role getRoleByRoleName(String roleName);
    boolean hasRole(String roleName);
    Collection<Role> getAllRoles();
}

