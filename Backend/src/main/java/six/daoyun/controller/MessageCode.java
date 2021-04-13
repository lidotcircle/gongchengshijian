package six.daoyun.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import six.daoyun.controller.exception.HttpForbidden;
import six.daoyun.service.MessageCodeService;

public class MessageCode {
    @Autowired
    private MessageCodeService mcodeService;

    private static Map<String, MessageCodeService.MessageCodeType> type2type;
    static {
        type2type = new HashMap<>();
    }

    static class MessageCodeReq //{
    {
        @Pattern(regexp = "\\d{11}", message = "手机号不合法")
        private String phone;
        public String getPhone() {
            return this.phone;
        }
        public void setPhone(String phone) {
            this.phone = phone;
        }

        @Pattern(regexp = "signup|reset|login")
        private String type;
        public String getType() {
            return this.type;
        }
        public void setType(String type) {
            this.type = type;
        }

        @Pattern(regexp = ".{1,}")
        private String captcha;
        public String getCaptcha() {
            return this.captcha;
        }
        public void setCaptcha(String captcha) {
            this.captcha = captcha;
        }
    } //}

    static class MessageCodeResp //{
    {
        private String codeToken;
        public String getCodeToken() {
            return this.codeToken;
        }
        public void setCodeToken(String codeToken) {
            this.codeToken = codeToken;
        }
    } //}

    @PostMapping("/apis/message")
    private MessageCodeResp getMessageCodeToken(@RequestBody @Valid MessageCodeReq req) {
        if(!this.mcodeService.captcha(req.getCaptcha())) {
            throw new HttpForbidden("验证码错误");
        }

        return null;
    }
}

