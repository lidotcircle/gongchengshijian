package six.daoyun.controller.auth;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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

    static class RequestResetPassReq //{
    {
        @NotNull
        @Pattern(regexp = "1[3456789]\\d{9}", message = "请输入11位手机号码")
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
    static class RequestResetPassResp //{
    {
        private String resetToken;
        public String getResetToken() {
            return this.resetToken;
        }
        public void setResetToken(String resetToken) {
            this.resetToken = resetToken;
        }
    } //}
    static class ResetPassReq //{
    {
        @NotNull
        private String resetToken;
        public String getResetToken() {
            return this.resetToken;
        }
        public void setResetToken(String resetToken) {
            this.resetToken = resetToken;
        }

        @NotNull
        private String password;
        public String getPassword() {
            return this.password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
    } //}

    @PostMapping("/apis/auth/password/reset-token")
    private RequestResetPassResp requestResetPassword(@RequestBody @Valid RequestResetPassReq req) {
        if(!this.mcodeService.validate(req.getMessageCodeToken(), 
                                       req.getPhone(), 
                                       req.getMessageCode(), 
                                       MessageCodeService.MessageCodeType.reset)) {
            throw new HttpUnauthorized("验证码错误");
        }

        final RequestResetPassResp ans = new RequestResetPassResp();
        ans.setResetToken(this.authService.requestResetPassword(req.getPhone()));
        return ans;
    }

    @PutMapping("/apis/auth/password")
    private void ResetPassword(@RequestBody @Valid ResetPassReq req) {

        this.authService.resetPassword(req.getResetToken(), req.getPassword());
    }
}

