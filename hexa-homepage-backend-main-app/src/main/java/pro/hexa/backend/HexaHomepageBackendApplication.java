package pro.hexa.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HexaHomepageBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(HexaHomepageBackendApplication.class, args);
    }
}
