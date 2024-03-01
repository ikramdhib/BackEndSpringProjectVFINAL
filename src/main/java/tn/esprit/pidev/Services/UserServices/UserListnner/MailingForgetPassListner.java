package tn.esprit.pidev.Services.UserServices.UserListnner;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import tn.esprit.pidev.Services.UserServices.UserServiceImpl;
import org.springframework.mail.javamail.JavaMailSender;
import tn.esprit.pidev.entities.User;

import java.io.UnsupportedEncodingException;

@Component
@Slf4j
@RequiredArgsConstructor
public class MailingForgetPassListner implements ApplicationListener<MailingEventForgetPassword> {

    private final  JavaMailSender javaMailSender;
    private   User user;

    @Override
    public void onApplicationEvent(MailingEventForgetPassword event) {
        user = event.getUser();
    }

    public void sendPasswordResetVerification(String url ,User user) throws MessagingException, UnsupportedEncodingException {
        String subject = "Password Reset Request Verification";
        String senderName = "ESPRIT SERVICE DE STAGE";
        String mailContent = "<p> Hi, "+ user.getFirstName()+ ", </p>"+
                "<p><b>You recently requested to reset your password,</b>"+"" +
                "Please, follow the link below to complete the action.</p>"+
                "<a href=\"" +url+ "\">Reset password</a>"+
                "<p> ESPRIT SERVICE DE STAGE";

        MimeMessage message = javaMailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("dhibikram50@gmail.com",senderName);
        messageHelper.setTo(user.getLogin());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent,true);
        javaMailSender.send(message);
    }


    public void sendVerificationEmail(User user , StringBuilder code) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "User Registration Portal Service";
        String mailContent = "<p> Hi, "+ user.getFirstName()+ ", </p>"+
                "<p>Thank you for your confidence,"+"" +
                "Please, follow the rest of steps  to change your password.</p>"+
                " this is your code : "+"<strong>"+code.toString()+"</strong>"+
                "<p> Thank you <br> Users Portal Service";
        MimeMessage message = javaMailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("dhibikram50@gmail.com", senderName);
        messageHelper.setTo(user.getLogin());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        javaMailSender.send(message);
    }
}
