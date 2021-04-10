package six.daoyun.controller.user;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import six.daoyun.controller.exception.HttpBadRequest;
import six.daoyun.controller.exception.HttpInternalServerError;
import six.daoyun.controller.exception.HttpUnauthorized;
import six.daoyun.controller.user.proto.UserUpdating;
import six.daoyun.controller.user.proto.UserUpdatingPriv;
import six.daoyun.entity.User;
import six.daoyun.service.UserService;


@RestController
public class UserRud {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private Collection<String> modifyUserInfoGen(User user, Object request) {
        Collection<String> ans = new ArrayList<>();

        for(Field field: request.getClass().getDeclaredFields()) {
            final String fieldName = field.getName();

            try {
                Field userField = User.class.getDeclaredField(fieldName);
                if(userField.getType() != field.getType()) {
                    continue;
                }
                field.setAccessible(true);
                userField.setAccessible(true);

                Object v = field.get(request);
                if(v != null) {
                    userField.set(user, v);
                    ans.add(fieldName);
                }
            } catch (NoSuchFieldException e) {
                continue;
            } catch (IllegalAccessException e) {
                throw new HttpInternalServerError(e.getMessage());
            }
        }

        return ans;
    }

    @PutMapping("/apis/user")
    @ResponseBody
    public Collection<String> modifyUserInfo(HttpServletRequest httpreq, @RequestBody UserUpdating request) {
        String username = (String) httpreq.getAttribute("username");
        User user = this.userService.getUserByUserName(username).orElseThrow(() -> new HttpBadRequest("user not found: " + username));
        Collection<String> ans = this.modifyUserInfoGen(user, request);

        if(request.getBirthday() != null) {
            Date birthday = new Date(request.getBirthday());
            user.setBirthday(birthday);
            ans.add("birthday");
        }

        if(ans.isEmpty()) {
            throw new HttpBadRequest("nothing changed, but expect something");
        } else {
            this.userService.updateUser(user);
        }

        return ans;
    }


    @PutMapping("/apis/user/privileged")
    @ResponseBody
    public Collection<String> modifyUserInfoPriv(HttpServletRequest httpreq, @RequestBody UserUpdatingPriv request) {
        String username = (String) httpreq.getAttribute("username");
        User user = this.userService.getUserByUserName(username).orElseThrow(() -> new HttpBadRequest("user not found: " + username));

        if(!this.passwordEncoder.matches(request.getRequiredPassword(), user.getPassword())) {
            throw new HttpUnauthorized("密码错误");
        }
        Collection<String> ans = this.modifyUserInfoGen(user, request);

        if(request.getPassword() != null) {
            user.setPassword(this.passwordEncoder.encode(request.getPassword()));
        }

        if(ans.isEmpty()) {
            throw new HttpBadRequest("nothing changed, but expect something");
        } else {
            this.userService.updateUser(user);
        }

        return ans;
    }
}

