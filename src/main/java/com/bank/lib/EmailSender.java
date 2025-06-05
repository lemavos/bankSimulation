package com.bank.lib;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailSender {
    public static void sendValEmail(String toValEmail, String validationCode) {
        Dotenv dotenv = Dotenv.load();

        final String fromEmail = dotenv.get("EMAIL_USER");
        final String password = dotenv.get("EMAIL_PASS");
        final String toEmail = toValEmail;
        final String smtpHost = dotenv.get("SMTP_HOST");
        final String smtpPort = dotenv.get("SMTP_PORT");

        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Validation Code for Your Registration");
            message.setText("Is your validation code: " + validationCode + "\n\n" +
                            "Please use this code to complete your registration.");

            Transport.send(message);
            System.out.println("Email sent successfully to " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
