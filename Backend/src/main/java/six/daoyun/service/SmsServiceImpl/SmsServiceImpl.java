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
    @Value("${daoyun.aliyun.AliyunSmsSignName}")
    private String aliyunSignName;
    @Value("${daoyun.aliyun.TemplateCode.login}")
    private String loginTemplateCode;
    @Value("${daoyun.aliyun.TemplateCode.signup}")
    private String signupTemplateCode;
    @Value("${daoyun.aliyun.TemplateCode.reset}")
    private String resetTemplateCode;
    @Value("${daoyun.aliyun.TemplateCode.bindPhoneNum}")
    private String bindPhoneNumTemplateCode;

    static class BadTemplateCode extends RuntimeException {private static final long serialVersionUID = 6875523021342018204L;}

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SmsServiceImpl.class);
	@Override
	public void sendMessageCode(String phone, String code, MessageCodeType type) throws SendMessasgeException {
        final SendSmsRequest req = new SendSmsRequest();
        req.setPhoneNumbers(phone);
        req.setSignName(this.aliyunSignName);

        String templateCode;
        switch(type) {
            case login:
                templateCode = this.loginTemplateCode; break;
            case signup:
                templateCode = this.loginTemplateCode; break;
            case reset:
                templateCode = this.loginTemplateCode; break;
            case bindPhoneNum:
                templateCode = this.loginTemplateCode; break;
            default:
                throw new BadTemplateCode();
        }

        req.setTemplateCode(templateCode);
        req.setTemplateParam("{\"code\": \"" + code + "\"}");

        try {
            log.info("signname: {}, phone: {}, templateCode: {}, templateParam:", 
                    req.getSignName(), req.getPhoneNumbers(), req.getTemplateCode(), req.getTemplateParam());
            this.aliSmsClient.sendSms(req);
            log.info("发送成功");
        } catch (Exception ex) {
            throw new SendMessasgeException();
        }
	}
}

