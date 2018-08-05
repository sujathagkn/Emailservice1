package com.org1.emailservice.builder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.org1.emailservice.model.Email;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sujatha.bandi on 4/8/18.
 */
public class SendGridEmailBuilder {
    SendGridEmail sge = new SendGridEmail();

    public SendGridEmailBuilder email(Email email) {
        Personalization personalization = new Personalization();
        personalization.addTo(new EmailId(email.getTo()));

        for(String cc : email.getCc()) {
            personalization.addCc(new EmailId(cc));
        }

        for(String bcc : email.getBcc()) {
            personalization.addBcc(new EmailId(bcc));
        }
        sge.addPersonalization(personalization);
        sge.setFrom(new EmailId(email.getFrom()));
        sge.setSubject(email.getSubject());
        Content content = new Content();
        content.setType("text/plain");
        content.setValue(email.getMessage() + "\n sent using SendGrid");
        sge.addContent(content);
        return this;
    }

    public SendGridEmail build() {
        return sge;
    }

    @JsonPropertyOrder({"personalizations", "from", "subject", "content"})
    @Getter
    @Setter
    public class SendGridEmail implements Serializable {
        private static final long serialVersionUID = 2542774744370601593L;
        @JsonProperty("personalizations")
        private List<Personalization> personalization = new ArrayList<>();
        private EmailId from;
        private String subject;
        private List<Content> content = new ArrayList<>();

        public void addPersonalization(Personalization personalization) {
            this.personalization.add(personalization);
        }

        public void addContent(Content content) {
            this.content.add(content);
        }
    }

    @Getter
    @Setter
    class Personalization {
        private List<EmailId> to = new ArrayList<>();
        private List<EmailId> cc = new ArrayList<>();
        private List<EmailId> bcc = new ArrayList<>();

        public Personalization() {
        }

        public void addTo(EmailId to) {
            this.to.add(to);
        }

        public void addCc(EmailId cc) {
            this.cc.add(cc);
        }

        public void addBcc(EmailId bcc) {
            this.bcc.add(bcc);
        }
    }

    @Getter
    @Setter
    class EmailId {
        @JsonProperty("email")
        private String email;

        public EmailId(String to) {
            this.email = to;
        }
    }

    @Getter
    @Setter
    class Content {
        private String type;
        private String value;

    }
}
