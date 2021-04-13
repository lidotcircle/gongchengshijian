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
import six.daoyun.service.AuthService;
import six.daoyun.service.MessageCodeService;

@RestController()
class Signup {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
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

        @NotNull
        private String messageCodeToken;
        public String getMessageCodeToken() {
            return this.messageCodeToken;
        }
        public void setMessageCodeToken(String messageCodeToken) {
            this.messageCodeToken = messageCodeToken;
        }

        @NotNull
        private String messageCode;
        public String getMessageCode() {
            return this.messageCode;
        }
        public void setMessageCode(String messageCode) {
            this.messageCode = messageCode;
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

    @PostMapping("/apis/auth/user")
    private void createUserByPhone(@RequestBody @Valid RegisterByPhone req) {
        if(!this.mcodeService.validate(req.getMessageCodeToken(), 
                                       req.getPhone(), 
                                       req.getMessageCode(), 
                                       MessageCodeService.MessageCodeType.signup)) {
            throw new HttpForbidden("验证错误, 注册失败");
        }

        if(!this.userService.getUser(req.getUserName()).isEmpty()) {
            throw new HttpForbidden("用户 '" + req.getUserName() + "' 已存在");
        }

        User user = new User();
        user.setUserName(req.getUserName());
        user.setPassword(this.passwordEncoder.encode(req.getPassword()));
        user.setPhone(req.getPhone());
        user.setStudentTeacherId(req.getStudentTeacherId());
        user.setName(req.getTrueName());
        user.setMajor(req.getMajor());
        user.setPhone(req.getPhone());
        user.setGender(req.getGender());
        user.setSchool(req.getSchool());
        user.setCollege(req.getCollege());
        user.setThirdPartyAccountType("none");
        if(req.getBirthdate() != null) {
            user.setBirthday(new java.sql.Date(req.getBirthdate().getTime()));
        }
        this.mcodeService.removeToken(req.getMessageCodeToken());
        this.authService.signup(user);
    }
}

