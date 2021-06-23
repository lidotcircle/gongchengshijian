package six.daoyun.service;


public interface SmsService {
    public class SendMessasgeException extends Exception {
		private static final long serialVersionUID = 7461805778281789019L;
    }

    void sendMessageCode(String phone, String code, MessageCodeService.MessageCodeType type) throws SendMessasgeException;
}

