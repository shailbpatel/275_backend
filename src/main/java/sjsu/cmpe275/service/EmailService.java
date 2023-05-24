package sjsu.cmpe275.service;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sjsu.cmpe275.entity.User;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;

@Service
public class EmailService {

    private JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(User user) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            mimeMessage.setFrom(new InternetAddress("shailp52@gmail.com"));
            mimeMessage.setSubject("WIORS Account Verification");
            mimeMessage.setText("Dear " + user.getName() + ",\n\nClick on this link to verify your account: \n"
                    + "http://52.23.102.141:8080/verify/" + user.getToken());
        };
        try {
            mailSender.send(messagePreparator);
        } catch (MailException e) {
            System.out.println("Error while sending out email.."+ e.getMessage());
        }
    }
}

