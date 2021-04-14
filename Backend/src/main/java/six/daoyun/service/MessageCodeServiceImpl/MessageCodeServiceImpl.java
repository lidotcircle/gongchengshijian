package six.daoyun.service.MessageCodeServiceImpl;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import six.daoyun.service.MessageCodeService;
import six.daoyun.service.SmsService;

@Service
public class MessageCodeServiceImpl implements MessageCodeService {
    @Autowired
    private RedisTemplate<String, Long> prevCache;
    @Autowired
    private RedisTemplate<String, TokenCache> tokenCache;
    @Autowired
    private SmsService smsService;
    @Value("${daoyun.message.validMs}")
    private long codeValidDur_ms;
    @Value("${daoyun.message.waitMs}")
    private long codeRequestWait_ms;
    @Value("${daoyun.message.enable}")
    private boolean enable;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MessageCodeServiceImpl.class);

    static class TokenCache implements Serializable //{
    {
		private static final long serialVersionUID = 1L;

		private String phone;
        public String getPhone() {
            return this.phone;
        }
        public void setPhone(String phone) {
            this.phone = phone;
        }

        private String code;
        public String getCode() {
            return this.code;
        }
        public void setCode(String code) {
            this.code = code;
        }

        private MessageCodeType type;
        public MessageCodeType getType() {
            return this.type;
        }
        public void setType(MessageCodeType type) {
            this.type = type;
        }
    } //}

    private static String one2nine = "123456789";
    private String generateDigitString(int n) //{
    {
        String ans = "";
        for(;n>0;n--) {
            final int idx = (int)Math.floor(Math.random() * 100) % 9;
            ans += one2nine.charAt(idx);
        }

        return ans;
    } //}

	@Override
	public String sendTo(String phone, MessageCodeType type) throws MessageNeedWait {
        log.info("发送短信到: {}, {}", phone, type);
        final String key = "message_code_phone_" + phone;
        final long now = (new Date()).getTime();
        ValueOperations<String, Long> operation = this.prevCache.opsForValue();
        if(this.prevCache.hasKey(key)) {
            Long prev = operation.get(key);

            if(now - prev <= codeRequestWait_ms) {
                throw new MessageNeedWait();
            }
        }
        operation.set(key, now, codeValidDur_ms, TimeUnit.MILLISECONDS);

        String code = "666666";
        if(this.enable) {
            code = this.generateDigitString(6);
            try {
                this.smsService.sendMessageCode(phone, code, type);
            } catch (Exception ex) {
                throw new RuntimeException("发送验证码失败");
            }
        }

        final TokenCache token = new TokenCache();
        token.setType(type);
        token.setCode(code);
        token.setPhone(phone);
        UUID ans = UUID.randomUUID();

        ValueOperations<String, TokenCache> top = this.tokenCache.opsForValue();
        top.set(ans.toString(), token, codeValidDur_ms, TimeUnit.MILLISECONDS);

        return ans.toString();
	}

	@Override
	public boolean validate(String token, String phone, String messageCode, MessageCodeType type) {
        ValueOperations<String, TokenCache> op = this.tokenCache.opsForValue();
        if(!this.tokenCache.hasKey(token)) {
            return false;
        }

        TokenCache tc = op.get(token);
        if(!tc.getPhone().equals(phone) || !tc.getCode().equals(messageCode) || tc.getType() != type) {
            return false;
        }

		return true;
	}

	@Override
	public boolean captcha(String captchaResponse) {
        // TODO
        return "helloworld".equals(captchaResponse);
	}

	@Override
	public void removeToken(String token) {
        this.tokenCache.delete(token);
	}
}

