package six.daoyun.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import six.daoyun.entity.User;
import six.daoyun.exchange.UserInfo;
import six.daoyun.repository.UserRepository;
import six.daoyun.service.UserService;
import six.daoyun.utils.ObjUitl;

@Service
public class UserServiceImpl implements UserService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private RedisTemplate<String, UserInfo> userinfoCache;
    private static String userinfoKey(String username) {
        return "userinfo_" + username;
    }

    @Autowired
    private UserRepository userRepository;

	@Override
	public void createUser(final User user) {
        log.info("create new user: {}", user.getUserName());
        this.userRepository.save(user);
	}

	@Override
	public Optional<User> getUser(final String userName) {
        final User user = this.userRepository.getUserByUserName(userName);
        return Optional.ofNullable(user);
	}

    private void clearKey(String username) //{
    {
        final String key = userinfoKey(username);
        if(this.userinfoCache.hasKey(key)) {
            this.userinfoCache.delete(key);
        }
    } //}

	@Override
	public void updateUser(User user) {
        this.clearKey(user.getUserName());
        this.userRepository.save(user);
	}

	@Override
	public void deleteUser(final String username) {
        this.clearKey(username);
        this.userRepository.deleteByUserName(username);
	}

	@Override
	public Collection<User> getAllUsers() {
        final ArrayList<User> ans = new ArrayList<User>();
        this.userRepository.findAll().forEach(ans::add);
        return ans;
	}

	@Override
	public Optional<UserInfo> getUserInfo(String username) //{
    {
        final String key = userinfoKey(username);
        ValueOperations<String, UserInfo> operation = this.userinfoCache.opsForValue();

        if(this.userinfoCache.hasKey(key)) {
            return Optional.of(operation.get(key));
        } else {
            final UserInfo userinfo = new UserInfo();
            try {
                final User user = this.getUser(username).get();
                ObjUitl.assignFields(userinfo, user);

                userinfo.setBirthday(user.getBirthday().getTime());
                if(user.getProfilePhoto() != null && user.getProfilePhoto().length > 0) {
                    userinfo.setPhoto(new String(user.getProfilePhoto()));
                }

                Collection<String> roles = new ArrayList<>();
                user.getRoles().forEach(role -> roles.add(role.getRoleName()));
                userinfo.setRoles(roles);

                operation.set(key, userinfo);
                return Optional.of(userinfo);
            } catch (NoSuchElementException ex){}
        }

		return Optional.empty();
    } //}

	@Override
	public Optional<User> getUserByPhone(String phone) {
        return Optional.ofNullable(this.userRepository.getUserByPhone(phone));
	}
}

