package six.daoyun.controller.auth;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import six.daoyun.controller.auth.proto.LoginByUsernameRequest;
import six.daoyun.controller.auth.proto.LoginResponse;
import six.daoyun.controller.exception.HttpBadRequest;
import six.daoyun.controller.exception.HttpUnauthorized;
import six.daoyun.entity.RefreshToken;
import six.daoyun.entity.User;
import six.daoyun.service.RefreshTokenService;
import six.daoyun.service.UserService;

@RestController()
class Login {
    @Autowired
    private UserService UserService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/apis/auth/login")
    private LoginResponse login(@RequestBody @Valid LoginByUsernameRequest request) {
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

