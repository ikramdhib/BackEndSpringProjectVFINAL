package tn.esprit.pidev.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.entities.Demande;


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





    public void sendEmail(String recipientEmail, Demande demande, String matchingResult) {

        MimeMessage mailMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, "utf-8");

        try {
            helper.setTo(recipientEmail);
            helper.setSubject("Nouvelle demande ajoutée");

            String titre = demande.getTitre();
            String studentName = demande.getStudentName();
            String studentEmail = demande.getStudentEmail();

            // Set the HTML content of the email
            String messageText = "<html><body>" +
                    "<p>Bonjour,</p>" +
                    "<p>Nouvelle demande a été ajoutée avec succès :</p>" +
                    "<p>Nom de la demande : " + titre + "</p>" +
                    "<p>Nom de l'étudiant : " + studentName +"<p>"+
                    "<p> Email de l'étudiant : " + studentEmail + "</p>" +
                    "<p>Résultat du matching : " + matchingResult + "</p>" +  // Ajout du résultat du matching
                    "</body></html>";

            helper.setText(messageText, true);  // Set HTML content to true

            emailSender.send(mailMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
