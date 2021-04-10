package six.daoyun.controller.user.proto;

import javax.validation.constraints.Pattern;

public class UserUpdatingPriv {
    @Pattern(regexp = ".{6,}", message = "密码至少为6位")
    private String requiredPassword;
    public String getRequiredPassword() {
        return this.requiredPassword;
    }
    public void setRequiredPassword(String requiredPassword) {
        this.requiredPassword = requiredPassword;
    }

    private String password;
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    private String phone;
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}

