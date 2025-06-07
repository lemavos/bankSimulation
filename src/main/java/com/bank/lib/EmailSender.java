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
        final String smtpHost = dotenv.get("SMTP_HOST");
        final String smtpPort = dotenv.get("SMTP_PORT");

        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toValEmail));
            message.setSubject("Your Validation Code");

            String html = "<html>" +
                    "<body style=\"font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 20px;\">" +
                    "<div style=\"max-width: 500px; margin: auto; background-color: white; padding: 30px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">" +
                    "<h2 style=\"color: #333;\">Hey 👋</h2>" +
                    "<p>Here is your validation code:</p>" +
                    "<h1 style=\"color: #007bff;\">" + validationCode + "</h1>" +
                    "<p>Use this code to complete your registration.</p>" +
                    "<p style=\"font-size: 12px; color: #888;\">If you didn't request this, just ignore it.</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            message.setContent(html, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Email sent successfully to " + toValEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
