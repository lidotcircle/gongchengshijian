package six.daoyun.config;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliyunSMSConfig {
    @Value("${AliyunAccessKeyId:}")
    private String aliyunAccessKeyId;
    @Value("${AliyunAccessKeySecret:}")
    private String aliyunAccessKeySecret;

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AliyunSMSConfig.class);
    @Bean
    public Client createClient() {
        log.info("accessKey id: {}", this.aliyunAccessKeyId);
        log.info("accessKey secret: {}", this.aliyunAccessKeySecret);
        Config config = new Config()
            .setAccessKeyId(this.aliyunAccessKeyId)
            .setAccessKeySecret(this.aliyunAccessKeySecret);
        config.endpoint = "dysmsapi.aliyuncs.com";

        try {
            return new Client(config);
        } catch (Exception ex) {
            return null;
        }
    }
}

