package six.daoyun.controller.user;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import six.daoyun.controller.exception.HttpBadRequest;
import six.daoyun.controller.exception.HttpInternalServerError;
import six.daoyun.controller.user.proto.UserUpdating;
import six.daoyun.entity.User;
import six.daoyun.service.UserService;


@RestController
public class UserRud {
    @Autowired
    private UserService userService;

    @PutMapping("/apis/user")
    @ResponseBody
    public Collection<String> modifyUserInfo(HttpServletRequest httpreq, @RequestBody UserUpdating request) {
        String username = (String) httpreq.getAttribute("username");
        User user = this.userService.getUserByUserName(username).orElseThrow(() -> new HttpBadRequest("user not found: " + username));
        Collection<String> ans = new ArrayList<>();

        for(Field field: UserUpdating.class.getDeclaredFields()) {
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
}

