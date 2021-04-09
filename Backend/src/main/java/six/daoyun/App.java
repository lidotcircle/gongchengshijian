package six.daoyun;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
@EnableJpaAuditing
public class App {

    public static void main (String[] args) {
        SpringApplication.run(App.class, args);
    }

}

