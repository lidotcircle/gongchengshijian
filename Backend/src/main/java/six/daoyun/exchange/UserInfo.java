package six.daoyun.exchange;

import java.util.Collection;

public class UserInfo extends UserUnprivileged {
    private String userName;
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String phone;
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String email;
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    private Collection<String> roles;
    public Collection<String> getRoles() {
        return this.roles;
    }
    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }
}

