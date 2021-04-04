package six.daoyun.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import six.daoyun.entity.User;
import six.daoyun.repository.UserRepository;
import six.daoyun.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RedisTemplate<String, User> redisUsers;

	@Override
	public void createUser(User User) {
        this.userRepository.save(User);
	}

	@Override
	public User getUserByUserName(String userName) {
        ValueOperations<String, User> operation =  this.redisUsers.opsForValue();
        if(this.redisUsers.hasKey(userName)) {
            this.logger.info("redis HIT {}", userName);
            return operation.get(userName);
        }

        for(User User: this.userRepository.findAll()) {
            if(User.getName().equals(userName)) {
                operation.set(userName, User, 10, TimeUnit.SECONDS);
                return User;
            }
        }

        return null;
	}

	@Override
	public void updateUser(String name, User User) {
	}

	@Override
	public void deleteUser(String name) {
	}

	@Override
	public Collection<User> getAllUsers() {
        ArrayList<User> ans = new ArrayList<User>();
        this.userRepository.findAll().forEach(ans::add);
        return ans;
	}
}

