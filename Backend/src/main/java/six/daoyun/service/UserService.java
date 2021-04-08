package six.daoyun.service;

import six.daoyun.entity.User;
import java.util.Collection;

public interface UserService {
    void createUser(User user);
    User getUserByUserName(String username);
    User getUserByUserId(Integer userId);
    void updateUser(String username, User user);
    void deleteUser(String username);

    Collection<User> getAllUsers();
}

