package com.org1.emailservice.controller;

import com.org1.emailservice.model.Email;
import com.org1.emailservice.model.SendStatus;
import com.org1.emailservice.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sujatha.bandi on 4/8/18.
 */
@RestController
@RequestMapping(value = "/api")
public class EmailContoller {
    @Autowired
    EmailSenderService emailService;

    @RequestMapping(value = "/send", method = RequestMethod.POST, consumes = "application/json")
    ResponseEntity<SendStatus> sendEmail(@RequestBody Email email) {
        return emailService.send(email);
    }
}

