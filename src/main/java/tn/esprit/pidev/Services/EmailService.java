package tn.esprit.pidev.Services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private JavaMailSender emailSender;

    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
    public void sendTaskValidatedEmail(String recipientEmail) {
        String subject = "Votre tâche a été validée";
        String body = "Votre tâche a été validée avec succès.";

        sendEmail(recipientEmail, subject, body);
    }

    public void sendTaskRejectedEmail(String recipientEmail, String rejectionReason) {
        String subject = "Votre tâche a été rejetée";
        String body = "Votre tâche a été rejetée pour la raison suivante : " + rejectionReason;

        sendEmail(recipientEmail, subject, body);
    }
}
