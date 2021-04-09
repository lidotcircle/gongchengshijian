package six.daoyun.service.RoleServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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
    @Autowired
    private RedisTemplate<String, Role> redisRoles;
    @Autowired
    private ObjectMapper objectMapper;

	@Override
	public void createRole(String roleName) {
        Role newRole = new Role();
        newRole.setRoleName(roleName);
        this.roleRepository.save(newRole);
	}

    private static String RoleNamePrefix = "Role_";
    private static String keyname(String roleName) {
        return RoleNamePrefix + roleName;
    }

	@Override
	public Optional<Role> getRoleByRoleName(String roleName) {
        ValueOperations<String, Role> operation =  this.redisRoles.opsForValue();
        String redisKey = keyname(roleName);
        if(this.redisRoles.hasKey(redisKey)) {
            log.info("redis HIT Role");
            Role c = operation.get(redisKey);
            String json;
			try {
				json = this.objectMapper.writeValueAsString(c);
                log.info(json);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
            return Optional.of(c);
        }

        Role ans = this.roleRepository.getRoleByRoleName(roleName);
        if(ans != null) {
            operation.set(redisKey, ans, 10, TimeUnit.SECONDS);
        }
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

