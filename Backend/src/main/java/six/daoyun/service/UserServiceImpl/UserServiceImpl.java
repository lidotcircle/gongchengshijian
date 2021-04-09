package six.daoyun.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import six.daoyun.entity.User;
import six.daoyun.repository.UserRepository;
import six.daoyun.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

	@Override
	public void createUser(final User user) {
        log.info("create new user: {}", user.getUserName());
        this.userRepository.save(user);
	}

	@Override
	public Optional<User> getUserByUserName(final String userName) {
        final User user = this.userRepository.getUserByUserName(userName);
        return Optional.ofNullable(user);
	}

	@Override
	public void updateUser(final String name, final User User) {
	}

	@Override
	public void deleteUser(final String name) {
        this.userRepository.deleteByUserName(name);
	}

	@Override
	public Collection<User> getAllUsers() {
        final ArrayList<User> ans = new ArrayList<User>();
        this.userRepository.findAll().forEach(ans::add);
        return ans;
	}

	@Override
	public Optional<User> getUserByUserId(final Integer userId) {
        return this.userRepository.findById(userId);
	}
}

