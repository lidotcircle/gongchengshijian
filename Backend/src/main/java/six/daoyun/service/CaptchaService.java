package six.daoyun.service;


public interface CaptchaService {
    boolean validate(String feature, String captchaResponse);
}

