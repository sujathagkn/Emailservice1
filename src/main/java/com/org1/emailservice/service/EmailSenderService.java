package com.org1.emailservice.service;


import com.org1.emailservice.model.Email;
import org.springframework.http.ResponseEntity;

public interface EmailSenderService {
    ResponseEntity send(Email email);
}
