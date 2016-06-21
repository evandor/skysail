package io.skysail.server.app.demo;

import org.restlet.data.ChallengeScheme;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;

import io.skysail.server.restlet.resources.PostEntityServerResource;

public class MailgunResource extends PostEntityServerResource<Mail> {

    @Override
    public Mail createEntityTemplate() {
        return new Mail();
    }

    @Override
    public void addEntity(Mail entity) {
        ClientResource cr = new ClientResource("https://api.mailgun.net/v3/sandboxc553a4d53f224995804921bd11891114.mailgun.org/messages");
        //cr.setAttribute(name, value);
        cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "api", "key-d00353c2873a61a41be8eb91f9bfcaa5");

        Form form = new Form();
        form.add("from", "evandor@gmail.com");
        form.add("to", "mira.v.graef@gmail.com");
       // form.add("to", "evandor@gmail.com");
        form.add("subject", "Saying...");
        form.add("text", "...hi. xxx!!!");

        cr.post(form, MediaType.APPLICATION_WWW_FORM);
    }

}
