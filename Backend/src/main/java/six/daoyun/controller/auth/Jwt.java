package six.daoyun.controller.auth;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import six.daoyun.config.JwtTokenUtil;
import six.daoyun.controller.exception.HttpBadRequest;
import six.daoyun.entity.RefreshToken;
import six.daoyun.service.RefreshTokenService;

@RestController()
class Jwt {
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private JwtTokenUtil jwtUtil;

    static public class JwtResponse {
        @NotNull(message = "require jwtToken")
        private String jwtToken;
        public String getJwtToken() {
            return this.jwtToken;
        }
        public void setJwtToken(String jwtToken) {
            this.jwtToken = jwtToken;
        }
    }

    @GetMapping("/apis/auth/jwt")
    private JwtResponse getJwt(@RequestParam("refreshToken") String refreshToken) {
        RefreshToken token = this.refreshTokenService.getRefreshTokenByToken(refreshToken)
                                                     .orElseThrow(() -> new HttpBadRequest("无效的Token"));
        JwtResponse resp = new JwtResponse();
        resp.setJwtToken(this.jwtUtil.generateToken(token.getUser().getUserName()));
        return resp;
    }
}

