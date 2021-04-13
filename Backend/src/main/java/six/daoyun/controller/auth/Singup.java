package six.daoyun.controller.auth;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import six.daoyun.controller.exception.*;
import six.daoyun.entity.User;
import six.daoyun.service.UserService;
import six.daoyun.service.MessageCodeService;

@RestController()
class Signup {
    @Autowired
    private UserService userService;
    @Autowired
    private MessageCodeService mcodeService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    static public class RegisterByPhone //{
    {
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

        @Pattern(regexp = ".{1,}")
        private String captcha;
        public String getCaptcha() {
            return this.captcha;
        }
        public void setCaptcha(String captcha) {
            this.captcha = captcha;
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
    } //}

    @PostMapping("/apis/auth/signup")
    private void createUserByPhone(@RequestBody @Valid RegisterByPhone userinfo) {
        if(!this.mcodeService.captcha(userinfo.getCaptcha())) {
            throw new HttpForbidden("验证码错误");
        }

        User user = new User();
        user.setUserName(userinfo.getUserName());
        user.setPassword(this.passwordEncoder.encode(userinfo.getPassword()));
        user.setPhone(userinfo.getPhone());
        user.setStudentTeacherId(userinfo.getStudentTeacherId());
        user.setName(userinfo.getTrueName());
        user.setMajor(userinfo.getMajor());
        user.setPhone(userinfo.getPhone()); // TODO
        user.setGender(userinfo.getGender());
        user.setSchool(userinfo.getSchool());
        user.setCollege(userinfo.getCollege());
        user.setThirdPartyAccountType("none");
        if(userinfo.getBirthdate() != null) {
            user.setBirthday(new java.sql.Date(userinfo.getBirthdate().getTime()));
        }
        this.userService.createUser(user);
    }
}

