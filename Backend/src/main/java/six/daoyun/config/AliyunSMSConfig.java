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

    @Bean
    public Client createClient() {
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

