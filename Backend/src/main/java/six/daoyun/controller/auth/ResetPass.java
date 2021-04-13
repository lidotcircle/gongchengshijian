package six.daoyun.controller.auth;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import six.daoyun.controller.exception.HttpUnauthorized;
import six.daoyun.service.AuthService;
import six.daoyun.service.MessageCodeService;

@RestController
public class ResetPass {
    @Autowired
    private MessageCodeService mcodeService;
    @Autowired
    private AuthService authService;

    static class ResetPassRequest //{
    {
        @NotNull
        @Pattern(regexp = "\\d{11}", message = "请输入11位手机号码")
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

        @NotNull
        @Pattern(regexp = ".{6,}", message = "密码至少包含6字符")
        private String password;
        public String getPassword() {
            return this.password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    } //}

    @PutMapping("/apis/auth/password")
    private void resetPassword(@RequestBody @Valid ResetPassRequest req) {
        if(!this.mcodeService.validate(req.getMessageCodeToken(), 
                                       req.getPhone(), 
                                       req.getMessageCode(), 
                                       MessageCodeService.MessageCodeType.reset)) {
            throw new HttpUnauthorized();
        }

        this.authService.resetPassword(req.getPhone(), req.getPassword());
    }
}

