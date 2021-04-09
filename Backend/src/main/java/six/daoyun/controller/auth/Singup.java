package six.daoyun.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.validation.Valid;

import six.daoyun.entity.User;
import six.daoyun.service.UserService;
import six.daoyun.controller.auth.proto.RegisterByPhone;

@RestController()
class Signup {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/apis/auth/signup")
    private void createUserByPhone(@RequestBody @Valid RegisterByPhone userinfo) {
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

