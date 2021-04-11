package six.daoyun.repository;

import org.springframework.data.repository.CrudRepository;
import six.daoyun.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role getRoleByRoleName(String roleName);
}

