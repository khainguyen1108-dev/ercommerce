package storemanagement.example.group_15.infrastructure.helper;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class MailHelper {

    private static Logger logger = LoggerFactory.getLogger(MailHelper.class);
    private static JavaMailSender mailHelper;
    @Value("${spring.mail.host}")
    public String host;
    @Value("${spring.mail.port}")
    public int port;
    @Value("${spring.mail.username}")
    public String username;
    @Value("${spring.mail.password}")
    public String password;

    @PostConstruct()
    public void init() {
        mailHelper = this.getJavaMailSender();
    }

    private JavaMailSender getJavaMailSender() {
        logger.info("Initializing JavaMailSender with host: {}, port: {}, username: {}", host, port, username);
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }

    public void sendMail(String to, String subject, String body, boolean isHtml) throws MessagingException {
        logger.info("Sending email to: {}, subject: {}", to, subject);
        MimeMessage mimeMessage = mailHelper.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setFrom(username);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, isHtml);
        mailHelper.send(mimeMessage);
        logger.info("Email sent successfully to: {}", to);
    }
}