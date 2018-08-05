package com.org1.emailservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.org1.emailservice.builder.SendGridEmailBuilder;
import com.org1.emailservice.model.Email;
import com.org1.emailservice.model.SendStatus;
import com.org1.emailservice.model.Status;
import com.org1.emailservice.qualifier.MailGun;
import com.org1.emailservice.qualifier.SendGrid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class EmailSenderServiceImpl implements EmailSenderService {
    private final Logger logger = LoggerFactory.getLogger(EmailSenderServiceImpl.class);
    ObjectMapper mapper = new ObjectMapper();
    @Value("${mailgun.from}")
    private String from;
    @Value("${mailgun.endpointuri}")
    private String mailGunEndPointURI;
    @Value("${sendGrid.endpointuri}")
    private String sendGridEndPointURI;
    @Autowired
    @MailGun
    private RestTemplate gunMailRestTemplate;
    @Autowired
    @SendGrid
    private RestTemplate sendGridRestTemplate;

    public ResponseEntity fallbackSend(Email email) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            SendGridEmailBuilder.SendGridEmail sendGridEmail = new SendGridEmailBuilder().email(email).build();
            String payload = mapper.writeValueAsString(sendGridEmail);
            logger.info(payload);
            ResponseEntity<String> response = sendGridRestTemplate.postForEntity(sendGridEndPointURI,
                new HttpEntity<String>(payload, headers), String.class);
            return createResponse(response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity<SendStatus>(new SendStatus(Status.EMAIL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @HystrixCommand(fallbackMethod = "fallbackSend")
    public ResponseEntity send(Email email) {
        try {
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            HttpHeaders headers = new HttpHeaders();
            map.add("from", from);
            map.add("to", email.getTo());
            map.add("cc", email.getCc());
			map.add("bcc", email.getBcc());
            map.add("subject", email.getSubject());
            map.add("text", email.getMessage() + "\n sent using MailGun");
            ResponseEntity<String> response = gunMailRestTemplate.exchange(mailGunEndPointURI, HttpMethod.POST, new HttpEntity<>(map, headers),
                String.class);
            return createResponse(response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private ResponseEntity createResponse(ResponseEntity<String> response) {
        if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED) {
            return new ResponseEntity(new SendStatus(Status.EMAIL_SEND_SUCCESS), response.getStatusCode());
        } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return new ResponseEntity(new SendStatus(Status.EMAIL_SEND_FAILURE), response.getStatusCode());
        }

        return new ResponseEntity(new SendStatus(Status.EMAIL_SERVER_ERROR), response.getStatusCode());
    }
}
