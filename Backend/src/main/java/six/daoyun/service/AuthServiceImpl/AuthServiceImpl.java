package six.daoyun.service.AuthServiceImpl;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import six.daoyun.controller.exception.HttpNotFound;
import six.daoyun.controller.exception.HttpUnauthorized;
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
    @Autowired
    private RedisTemplate<String, String> resetTokenCache;
    @Value("${daoyun.reset.validMs}")
    private long resetTokenValid_ms;

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

    private static String keyname(String token) {
        return "rest_pass_" + token;
    }

	@Override
	public String requestResetPassword(User user) {
        UUID ans = UUID.randomUUID();
        ValueOperations<String, String> operation = this.resetTokenCache.opsForValue();
        final String key = keyname(ans.toString());
        operation.set(key, user.getUserName(), this.resetTokenValid_ms, TimeUnit.MILLISECONDS);
        return key;
	}

	@Override
	public String requestResetPassword(String phone) {
        User user = this.userService.getUserByPhone(phone).orElseThrow(() -> new HttpNotFound("手机号未找到"));
        return this.requestResetPassword(user);
	}

	@Override
	public void resetPassword(String resetToken, String newpass) {
        final ValueOperations<String, String> operation = this.resetTokenCache.opsForValue();
        final String key = keyname(resetToken);
        final String username = operation.get(key);
        if(username == null) {
            throw new HttpUnauthorized("invalid reset token");
        }

        final User user = this.userService.getUser(username).get();
        user.setPassword(this.passwordEncoder.encode(newpass));
        this.userService.updateUser(user);
	}
}

