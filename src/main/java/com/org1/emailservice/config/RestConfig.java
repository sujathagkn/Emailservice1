package com.org1.emailservice.config;

import com.org1.emailservice.qualifier.MailGun;
import com.org1.emailservice.qualifier.SendGrid;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
class RestConfig {
    @Bean
    @MailGun
    public RestTemplate mailGunRestTemplate(@Value("${mailgun.key}") String key) {
        HttpHost host = new HttpHost("");
        final ClientHttpRequestFactory requestFactory = new HttpSecurityConfig(host);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor("api", key));
        return restTemplate;
    }

    @Bean
    @SendGrid
    public RestTemplate sendGridRestTemplate(@Value("${sendGrid.key}") String key) {
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                throws IOException {
                request.getHeaders().set("Authorization", key);
                return execution.execute(request, body);
            }
        });
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}
