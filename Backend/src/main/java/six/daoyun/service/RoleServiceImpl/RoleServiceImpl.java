package six.daoyun.service.RoleServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import six.daoyun.entity.Role;
import six.daoyun.repository.RoleRepository;
import six.daoyun.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
    private Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RedisTemplate<String, Role> redisRoles;

	@Override
	public void createRole(String roleName) {
        Role newRole = new Role();
        newRole.setRoleName(roleName);
        this.roleRepository.save(newRole);
	}

    static String RoleNamePrefix = "Role_";

	@Override
	public Role getRoleByRoleName(String roleName) {
        ValueOperations<String, Role> operation =  this.redisRoles.opsForValue();
        if(this.redisRoles.hasKey(RoleServiceImpl.RoleNamePrefix + roleName)) {
            this.logger.info("redis HIT Role");
            return operation.get(roleName);
        }

        Role ans = this.roleRepository.getRoleByRoleName(roleName);
        if(ans != null) {
            operation.set(RoleServiceImpl.RoleNamePrefix + roleName, ans, 10, TimeUnit.SECONDS);
        }
        return ans;
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
}

