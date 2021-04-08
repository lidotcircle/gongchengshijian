package six.daoyun.controller.auth.proto;

import javax.validation.constraints.NotNull;

public class LoginResponse {
    @NotNull(message = "require token")
    private String token;
    public String getToken() {
        return this.token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}

