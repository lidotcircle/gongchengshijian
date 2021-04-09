package six.daoyun.controller.auth;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import six.daoyun.config.JwtTokenUtil;
import six.daoyun.controller.auth.proto.JwtResponse;
import six.daoyun.controller.auth.proto.LoginResponse;
import six.daoyun.controller.exception.HttpBadRequest;
import six.daoyun.entity.RefreshToken;
import six.daoyun.service.RefreshTokenService;

@RestController()
class Jwt {
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private JwtTokenUtil jwtUtil;

    @GetMapping("/apis/auth/jwt")
    private JwtResponse getJwt(@RequestBody @Valid LoginResponse request) {
        RefreshToken token = this.refreshTokenService.getRefreshTokenByToken(request.getToken())
                                                     .orElseThrow(() -> new HttpBadRequest());
        JwtResponse resp = new JwtResponse();
        resp.setJwtToken(this.jwtUtil.generateToken(token.getUser().getUserName()));
        return resp;
    }
}

