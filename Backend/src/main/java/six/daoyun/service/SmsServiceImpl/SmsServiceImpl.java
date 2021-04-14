package six.daoyun.service.SmsServiceImpl;


import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import six.daoyun.service.MessageCodeService.MessageCodeType;
import six.daoyun.service.SmsService;

@Service
public class SmsServiceImpl implements SmsService {
    @Autowired
    private Client aliSmsClient;
    @Value("${AliyunSmsSignName:}")
    private String aliyunSignName;

	@Override
	public void sendMessageCode(String phone, String code, MessageCodeType type) throws SendMessasgeException {
        final SendSmsRequest req = new SendSmsRequest();
        req.setPhoneNumbers(phone);
        req.setSignName(this.aliyunSignName);
        req.setTemplateCode(type.toString());
        req.setTemplateParam("{\"code\": \"" + code + "\"}");

        try {
            this.aliSmsClient.sendSms(req);
        } catch (Exception ex) {
            throw new SendMessasgeException();
        }
	}
}

