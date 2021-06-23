package six.daoyun.controller.auth;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import six.daoyun.controller.exception.HttpBadRequest;
import six.daoyun.controller.exception.HttpNotFound;
import six.daoyun.controller.exception.HttpRequireCaptcha;
import six.daoyun.controller.exception.HttpUnauthorized;
import six.daoyun.entity.RefreshToken;
import six.daoyun.entity.User;
import six.daoyun.service.AuthService;
import six.daoyun.service.CaptchaService;
import six.daoyun.service.MessageCodeService;
import six.daoyun.service.UserService;
import six.daoyun.service.MessageCodeService.MessageCodeType;

@RestController()
@RequestMapping("/apis/auth")
class Login {
    @Autowired
    private UserService userService;
    @Autowired
    private MessageCodeService mcodeService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthService authService;
    @Autowired
    private CaptchaService captchaService;

    static public class LoginByUsernameRequest //{
    {
        @NotNull(message = "require userName")
        private String userName;
        public String getUserName() {
            return this.userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }

        @NotNull(message = "require password")
        private String password;
        public String getPassword() {
            return this.password;
        }
        public void setPassword(String password) {
            this.password = password;
        }

        private String captcha;
        public String getCaptcha() {
            return this.captcha;
        }
        public void setCaptcha(String captcha) {
            this.captcha = captcha;
        }
    } //}

    static public class LoginByMessage //{
    {
        @NotNull
        @Pattern(regexp = "1[3456789]\\d{9}", message = "请输入11位手机号")
        private String phone;
        public String getPhone() {
            return this.phone;
        }
        public void setPhone(String phone) {
            this.phone = phone;
        }

        @NotNull
        private String messageCode;
        public String getMessageCode() {
            return this.messageCode;
        }
        public void setMessageCode(String messageCode) {
            this.messageCode = messageCode;
        }

        @NotNull
        private String messageCodeToken;
        public String getMessageCodeToken() {
            return this.messageCodeToken;
        }
        public void setMessageCodeToken(String messageCodeToken) {
            this.messageCodeToken = messageCodeToken;
        }
    } //}

    static public class QuickLoginDTO extends LoginByMessage {}

    static public class LoginResponse //{
    {
        @NotNull(message = "require token")
        private String token;
        public String getToken() {
            return this.token;
        }
        public void setToken(String token) {
            this.token = token;
        }
    } //}

    @PostMapping("refresh-token")
    private LoginResponse login(@RequestBody @Valid LoginByUsernameRequest request, HttpServletRequest httpreq) //{
    {
        if(!this.captchaService.validate(httpreq.getRemoteAddr() + request.getUserName(), request.captcha)) {
            if(request.getCaptcha() == null) {
                throw new HttpRequireCaptcha();
            } else {
                throw new HttpUnauthorized("验证码错误");
            }
        }

        User user;

        if(java.util.regex.Pattern.matches("\\d{11}", request.getUserName())) {
            user = this.userService.getUserByPhone(request.getUserName())
                                   .orElseThrow(() -> new HttpBadRequest("手机号未注册"));
        } else {
            user = this.userService.getUser(request.getUserName())
                                   .orElseThrow(() -> new HttpBadRequest("用户不存在"));
        }
        LoginResponse resp = new LoginResponse();

        if(this.passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            RefreshToken token = this.authService.login(user);
            resp.setToken(token.getToken());
        } else {
            throw new HttpUnauthorized("密码错误");
        }

        return resp;
    } //}

    @PostMapping("/refresh-token/message")
    private LoginResponse loginByMessage(@RequestBody @Valid LoginByMessage req) //{
    {
        if(!this.mcodeService.validate(req.getMessageCodeToken(), 
                                       req.getPhone(), 
                                       req.getMessageCode(), 
                                       MessageCodeService.MessageCodeType.login)) {
            throw new HttpUnauthorized("验证码错误");
        }

        LoginResponse resp = new LoginResponse();
        User user = this.userService.getUserByPhone(req.getPhone())
            .orElseThrow(() -> new HttpNotFound("手机号未注册"));
        RefreshToken token = this.authService.login(user);
        resp.setToken(token.getToken());
        return resp;
    } //}

    @PostMapping("/refresh-token/quick")
    private LoginResponse quickLogin(@RequestBody @Valid QuickLoginDTO req) {
        if(!this.mcodeService.validate(req.getMessageCodeToken(), 
                    req.getPhone(), 
                    req.getMessageCode(), 
                    MessageCodeType.login)) {
            throw new HttpUnauthorized("验证码错误");
        }


        User user;
        if(!this.userService.getUserByPhone(req.getPhone()).isPresent()) {
            user = new User();
            user.setPhone(req.getPhone());
            user.setUserName("DAOYUN_" + UUID.randomUUID().toString());
            user.setPassword("uv");
            user.setThirdPartyAccountType("none");
            this.userService.createUser(user);
        } else {
            user = this.userService.getUserByPhone(req.getPhone()).get();
        }
        RefreshToken token = this.authService.login(user);
        LoginResponse resp = new LoginResponse();
        resp.setToken(token.getToken());
        return resp;
    }

    @DeleteMapping("/refresh-token")
    private void logout(@RequestParam("refreshToken") String refreshToken) {
        this.authService.logout(refreshToken);
    }
}

