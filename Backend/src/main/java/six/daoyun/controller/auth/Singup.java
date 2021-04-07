package six.daoyun.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import six.daoyun.entity.User;
import six.daoyun.service.UserService;

@RestController()
class Signup {
    @Autowired
    private UserService UserService;

    @PostMapping("/apis/auth/signup")
    private void newUsers(@RequestBody User User) {
        this.UserService.createUser(User);
    }
}

