package six.daoyun.service.MessageCodeServiceImpl;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import six.daoyun.service.MessageCodeService;

@Service
public class MessageCodeServiceImpl implements MessageCodeService {
    @Autowired
    private RedisTemplate<String, Long> prevCache;
    @Autowired
    private RedisTemplate<String, TokenCache> tokenCache;
    private static long codeValidDur_ms = 5 * 1000 * 60;
    private static long codeRequestWait_ms = 1 * 1000 * 60;
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

        // TODO 发送短信
        String code = "666666";
        final TokenCache token = new TokenCache();
        token.setType(type);
        token.setCode(code);
        token.setPhone(phone);
        UUID ans = UUID.randomUUID();

        ValueOperations<String, TokenCache> top = this.tokenCache.opsForValue();
        log.info("1发送短信到: {}, {}", phone, type);
        top.set(ans.toString(), token, codeValidDur_ms, TimeUnit.MILLISECONDS);
        log.info("2发送短信到: {}, {}", phone, type);

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

