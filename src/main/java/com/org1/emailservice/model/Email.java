package com.org1.emailservice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Email {
    private String from;
    private String to;
    private List<String> cc = new ArrayList<>();
    private List<String> bcc = new ArrayList<>();
    private String subject;
    private String message;

    public Email() {
    }

    @Override
    public String toString() {
        return "Email [from=" + from + ", to=" + to + ", cc=" + cc + ", bcc=" + bcc + ", subject=" + subject
            + ", message=" + message + "]";
    }
}
