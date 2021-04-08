package six.daoyun.controller.auth.proto;

import javax.validation.constraints.NotNull;

public class LoginByUsernameRequest {
    @NotNull(message = "require username")
    private String username;
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @NotNull(message = "require password")
    private String password;
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}

