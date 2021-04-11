package six.daoyun.controller.user.proto;

import javax.validation.constraints.Pattern;

import six.daoyun.exchange.UserPrivileged;

public class UserUpdatingPriv extends UserPrivileged {
    @Pattern(regexp = ".{6,}", message = "密码至少为6位")
    private String requiredPassword;
    public String getRequiredPassword() {
        return this.requiredPassword;
    }
    public void setRequiredPassword(String requiredPassword) {
        this.requiredPassword = requiredPassword;
    }
}

