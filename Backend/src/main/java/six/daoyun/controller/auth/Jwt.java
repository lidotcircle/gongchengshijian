package six.daoyun.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import six.daoyun.controller.auth.proto.LoginByUsernameRequest;
import six.daoyun.controller.auth.proto.LoginResponse;
import six.daoyun.controller.exception.HttpUnauthorized;
import six.daoyun.entity.RefreshToken;
import six.daoyun.entity.User;
import six.daoyun.service.RefreshTokenService;
import six.daoyun.service.UserService;

@RestController()
class Jwt {
    @Autowired
    private UserService UserService;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @GetMapping("/apis/auth/jwt")
    private void getJwt(@RequestBody LoginResponse request) {
    }
}

