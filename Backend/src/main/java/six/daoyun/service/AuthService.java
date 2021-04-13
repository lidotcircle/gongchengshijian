package six.daoyun.service;


import six.daoyun.entity.RefreshToken;
import six.daoyun.entity.User;

public interface AuthService {
    RefreshToken login(User user);
    void logout(String user);

    void signup(User user);

    void resetPassword(User user, String newpass);
    void resetPassword(String phone, String newpass);
}

