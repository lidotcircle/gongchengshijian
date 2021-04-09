package six.daoyun.controller.auth.proto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class RegisterByPhone {
    @Pattern(regexp = "[a-zA-Z]\\w{3,20}", message = "username must begin with a letter and ...")
    private String userName;
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Pattern(regexp = "\\w{6,20}", message = "password must contains at least 6 character")
    private String password;
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @NotNull(message = "invalid phone")
    private String phone;
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String role;
    public String getRole() {
        return this.role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    private String trueName;
    public String getTrueName() {
        return this.trueName;
    }
    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    private String school;
    public String getSchool() {
        return this.school;
    }
    public void setSchool(String school) {
        this.school = school;
    }

    private String college;
    public String getCollege() {
        return this.college;
    }
    public void setCollege(String college) {
        this.college = college;
    }

    private String studentTeacherId;
    public String getStudentTeacherId() {
        return this.studentTeacherId;
    }
    public void setStudentTeacherId(String studentTeacherId) {
        this.studentTeacherId = studentTeacherId;
    }

    private String major;
    public String getMajor() {
        return this.major;
    }
    public void setMajor(String major) {
        this.major = major;
    }

    private Date birthdate;
    public Date getBirthdate() {
        return this.birthdate;
    }
    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    private String gender;
    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
}

