package six.daoyun.service;

import java.util.Collection;
import java.util.Optional;

import six.daoyun.entity.User;

public interface UserService {
    void createUser(User user);
    Optional<User> getUserByUserName(String username);
    Optional<User> getUserByUserId(Integer userId);
    void updateUser(User user);
    void deleteUser(String username);

    Collection<User> getAllUsers();
}

