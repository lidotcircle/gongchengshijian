package six.daoyun.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import six.daoyun.controller.exception.HttpNotFound;
import six.daoyun.entity.User;
import six.daoyun.service.UserService;

@Component
public class DYUtil {
    private static UserService userService;

    @Autowired
    public DYUtil(UserService duserService) {
        userService = duserService;
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DYUtil.class);
    public static User getHttpRequestUser(HttpServletRequest request) {
        final String userName = (String)request.getAttribute("username");
        log.info("{} access", userName);
        return userService.getUser(userName)
            .orElseThrow(() -> new HttpNotFound());
    }
}

