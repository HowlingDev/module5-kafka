package com.example.services;

import com.example.model.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final EmailService emailService;

    @Autowired
    public NotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "actions", groupId = "email-notifications")
    public void listenAccountEvent(String message) {
        String operation = message.split(" ")[0];
        String recipient = message.split(" ")[1];
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setSubject("Account actions");
        emailDetails.setRecipient(recipient);
        if (operation.equals("CREATE")) {
            emailDetails.setMessage("Здравствуйте! Ваш аккаунт был успешно создан.");
            emailService.sendEmail(emailDetails);
        } else if (operation.equals("DELETE")) {
            emailDetails.setMessage("Здравствуйте! Ваш аккаунт был удалён.");
            emailService.sendEmail(emailDetails);
        }
    }
}
