package six.daoyun.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import six.daoyun.controller.exception.HttpForbidden;
import six.daoyun.controller.exception.HttpRequireCaptcha;
import six.daoyun.service.CaptchaService;
import six.daoyun.service.MessageCodeService;
import six.daoyun.service.UserService;

@RestController
@RequestMapping("/apis/message")
public class MessageCode {
    @Autowired
    private MessageCodeService mcodeService;
    @Autowired
    private UserService userService;
    @Autowired
    private CaptchaService captchaService;

    private static Map<String, MessageCodeService.MessageCodeType> type2type;
    static {
        type2type = new HashMap<>();
        type2type.put("signup", MessageCodeService.MessageCodeType.signup);
        type2type.put("login", MessageCodeService.MessageCodeType.login);
        type2type.put("reset", MessageCodeService.MessageCodeType.reset);
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

    @PostMapping()
    private MessageCodeResp getMessageCodeToken(@RequestBody @Valid MessageCodeReq req) {
        if(!this.captchaService.validate(req.getPhone() + req.getType(), req.getCaptcha())) {
            if(req.getCaptcha() == null) {
                throw new HttpRequireCaptcha();
            } else {
                throw new HttpForbidden("验证码错误");
            }
        }

        MessageCodeService.MessageCodeType type = type2type.get(req.getType());
        switch(type) {
            case reset:
                if(this.userService.getUserByPhone(req.getPhone()).isEmpty()) {
                    throw new HttpForbidden("手机号未注册");
                }
                break;
            case signup:
                if(this.userService.getUserByPhone(req.getPhone()).isPresent()) {
                    throw new HttpForbidden("手机号已注册");
                }
                break;

            case login:
            default: break;
        }

        try {
            final String token = this.mcodeService.sendTo(req.phone, type);
            MessageCodeResp resp = new MessageCodeResp();
            resp.setCodeToken(token);
            return resp;
        } catch (MessageCodeService.MessageNeedWait ex) {
            throw new HttpForbidden("请求过于频繁");
        }
    }
}

