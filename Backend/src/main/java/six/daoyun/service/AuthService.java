package six.daoyun.service;


import six.daoyun.entity.RefreshToken;
import six.daoyun.entity.User;

public interface AuthService {
    RefreshToken login(User user);
    void logout(String user);

    void signup(User user);

    String requestResetPassword(User user);
    String requestResetPassword(String phone);

    void resetPassword(String resetToken, String newpass);
}

