package six.daoyun.service.AuthServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import six.daoyun.controller.exception.HttpNotFound;
import six.daoyun.entity.RefreshToken;
import six.daoyun.entity.User;
import six.daoyun.service.AuthService;
import six.daoyun.service.RefreshTokenService;
import six.daoyun.service.UserService;


@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;

	@Override
	public RefreshToken login(User user) {
        return this.refreshTokenService.createRefreshToken(user);
	}

	@Override
	public void logout(String token) {
        this.refreshTokenService.deleteRefreshTokenByToken(token);
	}

	@Override
	public void signup(User user) {
        this.userService.createUser(user);
	}

	@Override
	public void resetPassword(User user, String newpass) {
        user.setPassword(this.passwordEncoder.encode(newpass));
	}

	@Override
	public void resetPassword(String phone, String newpass) {
        User user = this.userService.getUserByPhone(phone).orElseThrow(() -> new HttpNotFound("手机号未找到"));
        this.resetPassword(user, newpass);
	}
}

