package six.daoyun.controller.auth;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import six.daoyun.controller.exception.HttpBadRequest;
import six.daoyun.controller.exception.HttpForbidden;
import six.daoyun.controller.exception.HttpUnauthorized;
import six.daoyun.entity.RefreshToken;
import six.daoyun.entity.User;
import six.daoyun.service.MessageCodeService;
import six.daoyun.service.RefreshTokenService;
import six.daoyun.service.UserService;

@RestController()
class Login {
    @Autowired
    private UserService UserService;
    @Autowired
    private MessageCodeService mcodeService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    static public class LoginByUsernameRequest //{
    {
        @NotNull(message = "require userName")
        private String userName;
        public String getUserName() {
            return this.userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }

        @NotNull(message = "require password")
        private String password;
        public String getPassword() {
            return this.password;
        }
        public void setPassword(String password) {
            this.password = password;
        }

        @Pattern(regexp = ".{1,}")
        private String captcha;
        public String getCaptcha() {
            return this.captcha;
        }
        public void setCaptcha(String captcha) {
            this.captcha = captcha;
        }
    } //}

    static public class LoginResponse //{
    {
        @NotNull(message = "require token")
        private String token;
        public String getToken() {
            return this.token;
        }
        public void setToken(String token) {
            this.token = token;
        }
    } //}

    @PostMapping("/apis/auth/login")
    private LoginResponse login(@RequestBody @Valid LoginByUsernameRequest request) {
        if(!this.mcodeService.captcha(request.captcha)) {
            throw new HttpForbidden("验证码错误");
        }

        User user = this.UserService.getUser(request.getUserName())
                                    .orElseThrow(() -> new HttpBadRequest("用户不存在"));
        LoginResponse resp = new LoginResponse();

        if(this.passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            RefreshToken token = this.refreshTokenService.createRefreshToken(user);
            resp.setToken(token.getToken());
        } else {
            throw new HttpUnauthorized("密码错误");
        }

        return resp;
    }

    @DeleteMapping("/apis/auth/login")
    private void logout(@RequestParam("refreshToken") String refreshToken) {
        this.refreshTokenService.deleteRefreshTokenByToken(refreshToken);
    }
}

