package six.daoyun.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;

import six.daoyun.entity.Role;
import six.daoyun.entity.User;
import six.daoyun.exchange.UserInfo;

public interface UserService {
    void createUser(User user);
    Optional<User> getUser(String username);
    void updateUser(User user);
    void deleteUser(String username);

    Optional<UserInfo> getUserInfo(String username);
    Optional<User> getUserByPhone(String phone);

    Collection<User> getAllUsers();

    Page<User> getUserPage(int pageno, int size, String sortKey, boolean desc, String filter);

    void addRole(User user, Role role);
    void removeRole(User user, Role role);
    boolean hasPermission(User user, String link);
}

