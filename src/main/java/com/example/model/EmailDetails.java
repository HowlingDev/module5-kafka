package com.example.model;

import lombok.*;

@Data
@AllArgsConstructor @NoArgsConstructor
public class EmailDetails {

    private String recipient;

    private String message;

    private String subject;

}
