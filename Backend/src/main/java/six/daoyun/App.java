package six.daoyun;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import six.daoyun.controller.MessageCode;

import org.springframework.boot.SpringApplication;

@SpringBootApplication
@EnableJpaAuditing
public class App {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(App.class);
    public static void main (String[] args) {
        SpringApplication.run(App.class, args);

        // TODO
        MessageCode m = new MessageCode();
        log.info("{}", m);
    }

}

