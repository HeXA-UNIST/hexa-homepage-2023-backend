package pro.hexa.backend.config;

import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@Slf4j
public class EmailSenderConfig {

    public final String EMAIL_SMTP_START_TLS_ENABLE = "mail.smtp.starttls.enable";
    public final String EMAIL_SMTP_AUTH = "mail.smtp.auth";

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private Integer port;

    @Value("${spring.mail.protocol}")
    private String protocol;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.default-encoding}")
    private String defaultEncoding;

    @Value("${spring.mail.smtp.start-tls-enable}")
    private Boolean startTlsEnable;

    @Value("${spring.mail.smtp.auth}")
    private Boolean auth;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setProtocol(protocol);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        javaMailSender.setDefaultEncoding(defaultEncoding);
        Properties javaMailProperties = javaMailSender.getJavaMailProperties();
        javaMailProperties.put(EMAIL_SMTP_START_TLS_ENABLE, startTlsEnable);
        javaMailProperties.put(EMAIL_SMTP_AUTH, auth);
        javaMailSender.setJavaMailProperties(javaMailProperties);
        return javaMailSender;
    }
}
