package six.daoyun.service;


public interface MessageCodeService {
    public enum MessageCodeType {
        signup, login, reset, bindPhoneNum
    }

    public class MessageNeedWait extends Exception {
		private static final long serialVersionUID = 1L;
    }

    String sendTo(String phone, MessageCodeType type) throws MessageNeedWait;
    boolean validate(String token, String phone, String messageCode, MessageCodeType type);
    void removeToken(String token);

    boolean captcha(String captchaResponse);
}

