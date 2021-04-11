package six.daoyun.controller.role.proto;

import javax.validation.constraints.NotNull;

public class GetRole {
    @NotNull(message = "require roleName")
    private String roleName;
    public String getRoleName() {
        return this.roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}

