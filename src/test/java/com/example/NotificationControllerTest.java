package com.example;

import com.example.controllers.NotificatonController;
import com.example.model.EmailDetails;
import com.example.services.EmailService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificatonController.class)
public class NotificationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    EmailService emailService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Успешная отправка сообщения")
    public void sendEmailTest_success() throws Exception {
        EmailDetails emailDetails = new EmailDetails("user@example.com", "message", "subject");
        when(emailService.sendEmail(emailDetails)).thenReturn("Сообщение успешно отправлено!");

        mockMvc.perform(post("/api/notifications/sendEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailDetails)))
                        .andExpect(status().isOk())
                        .andExpect(content().string("Сообщение успешно отправлено!"));
    }

    @Test
    @DisplayName("Сообщение не отправлено из-за плохого запроса")
    public void sendEmailTest_failBecauseOfBadRequest() throws Exception {
        EmailDetails emailDetails = new EmailDetails("user@example.com", "message", "subject");
        when(emailService.sendEmail(emailDetails)).thenReturn("Сообщение успешно отправлено!");

        mockMvc.perform(post("/api/notifications/sendEmail")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Сообщение не отправлено")
    public void sendEmailTest_fail() throws Exception {
        EmailDetails emailDetails = new EmailDetails("user@example.com", "message", "subject");
        when(emailService.sendEmail(emailDetails)).thenReturn("Произошла ошибка при отправке сообщения.");

        mockMvc.perform(post("/api/notifications/sendEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailDetails)))
                        .andExpect(status().isOk())
                        .andExpect(content().string("Произошла ошибка при отправке сообщения."));
    }
}
