package six.daoyun.service;

import java.util.Collection;
import java.util.Optional;

import six.daoyun.entity.User;
import six.daoyun.exchange.UserInfo;

public interface UserService {
    void createUser(User user);
    Optional<User> getUser(String username);
    void updateUser(User user);
    void deleteUser(String username);

    Optional<UserInfo> getUserInfo(String username);

    Collection<User> getAllUsers();
}

