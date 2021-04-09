package six.daoyun.controller.auth.proto;

import javax.validation.constraints.NotNull;

public class LoginByUsernameRequest {
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
}

