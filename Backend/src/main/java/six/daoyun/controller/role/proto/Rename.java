package six.daoyun.controller.role.proto;

import javax.validation.constraints.NotNull;

public class Rename {
    @NotNull
    private String oldRoleName;
    public String getOldRoleName() {
        return this.oldRoleName;
    }
    public void setOldRoleName(String oldRoleName) {
        this.oldRoleName = oldRoleName;
    }

    @NotNull
    private String newRoleName;
    public String getNewRoleName() {
        return this.newRoleName;
    }
    public void setNewRoleName(String newRoleName) {
        this.newRoleName = newRoleName;
    }
}

