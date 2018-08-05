package com.org1;

import groovy.util.logging.Slf4j;
import io.restassured.RestAssured;
import io.restassured.config.MatcherConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by sujatha.bando on on 4/8/18.
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.config.name=application, TokenHandlerConfig, UserConfig")
public abstract class BaseIntegrationTest {

    @LocalServerPort
    int port;

    @Before
    public void setup() {
        RestAssured.port = port;
        RestAssured.config = RestAssured.config().matcherConfig(new MatcherConfig(MatcherConfig.ErrorDescriptionType.HAMCREST));
    }
    @Test
    public void verify_health() {
        given().port(port).when().get("/health")
                .then().statusCode(HTTP_OK)
                .extract().body().asString();
    }
}
