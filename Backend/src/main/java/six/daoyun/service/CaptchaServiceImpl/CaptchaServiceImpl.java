package six.daoyun.service.CaptchaServiceImpl;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import six.daoyun.service.CaptchaService;


@Service
public class CaptchaServiceImpl implements CaptchaService {
    @Value("${daoyun.hcaptcha.sitesecret}")
    private String sitesecret;
    @Value("${daoyun.hcaptcha.endpoint}")
    private String endpoint;
    @Value("${daoyun.hcaptcha.allowNum}")
    private int allowNum;
    @Value("${daoyun.hcaptcha.retainS}")
    private int retainS;
    @Autowired
    private RedisTemplate<String, Integer> accessCache;

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class HCaptchaResp {
        private boolean success;
        public boolean getSuccess() {
            return this.success;
        }
        public void setSuccess(boolean success) {
            this.success = success;
        }
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CaptchaServiceImpl.class);

    private boolean captchaValidate(String captchaResponse) {
        log.info("captcha: {}", captchaResponse);
        String params = "response=" + captchaResponse + "&secret=" + this.sitesecret;
        byte[] bytes = params.getBytes(StandardCharsets.UTF_8);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
            .newBuilder(URI.create(this.endpoint))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("charset", "utf-8")
            .POST(BodyPublishers.ofByteArray(bytes))
            .build();

        try {
            HttpResponse<String> resp = client.send(request, BodyHandlers.ofString());
            if(resp.statusCode() != 200) {
                return false;
            } else {
                ObjectMapper om = new ObjectMapper();
                HCaptchaResp hresp = om.readValue(resp.body(), HCaptchaResp.class);
                return hresp.getSuccess();
            }
        } catch (IOException ex) {
            return false;
        } catch (InterruptedException ex) {
            return false;
        }
    }

	@Override
    public boolean validate(String feature, String captchaResponse) {
        if(captchaResponse != null && this.captchaValidate(captchaResponse)) {
            return true;
        }

        log.info("captcha validate feature: {}", feature);
        ValueOperations<String, Integer> operation = this.accessCache.opsForValue();
        Integer times = Integer.valueOf(0);
        if(this.accessCache.hasKey(feature)) {
            times = operation.get(feature);
        }

        if(this.allowNum > times) {
            times += 1;
            operation.set(feature, times, this.retainS, TimeUnit.SECONDS);
            return true;
        } else {
            return false;
        }
    }
}

