package six.daoyun.controller.auth.proto;

import javax.validation.constraints.NotNull;

public class JwtResponse {
    @NotNull(message = "require jwtToken")
    private String jwtToken;
    public String getJwtToken() {
        return this.jwtToken;
    }
    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}

