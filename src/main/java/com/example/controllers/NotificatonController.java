package com.example.controllers;

import com.example.model.EmailDetails;
import com.example.services.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificatonController {

    private final EmailService emailService;

    @Autowired
    public NotificatonController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/sendEmail")
        public String sendEmail(@RequestBody EmailDetails emailDetails) {
            return emailService.sendEmail(emailDetails);
    }
}
