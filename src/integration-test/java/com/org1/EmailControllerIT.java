package com.org1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org1.emailservice.model.Email;
import org.json.JSONException;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by sujatha.bandi on 4/8/18.
 */
public class EmailControllerIT extends BaseIntegrationTest {

    @Test
    public void sendEmail_ERROR() throws JSONException, JsonProcessingException {

        Email email = new Email();
        email.setTo("test@gmail.com");
        email.setCc(new ArrayList<>());
        email.setBcc(new ArrayList<>());
        email.setSubject("Hello test");
        email.setMessage("Hello test");

        ObjectMapper objectMapper = new ObjectMapper();

       given().port(port).accept(MediaType.APPLICATION_JSON_VALUE)
            // auth().basic(user.getBasicUsername(), decrypt(user.getBasicPassword()))
            .body(objectMapper.writeValueAsString(email))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/send")
            .then().statusCode(HTTP_BAD_REQUEST)
            .extract().body().asString();

    }


    @Test
    public void sendEmail_OK() throws JSONException, JsonProcessingException {

        Email email = new Email();
        email.setTo("sujathagkn@gmail.com");
        email.setCc(new ArrayList<>());
        email.setBcc(new ArrayList<>());
        email.setSubject("Hello test");
        email.setMessage("Hello test");

        ObjectMapper objectMapper = new ObjectMapper();

        given().port(port).accept(MediaType.APPLICATION_JSON_VALUE)
            .body(objectMapper.writeValueAsString(email))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/send")
            .then().statusCode(HTTP_OK)
            .extract().body().asString();

    }

}

