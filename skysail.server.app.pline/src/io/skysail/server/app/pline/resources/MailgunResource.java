package io.skysail.server.app.pline.resources;

import org.restlet.data.ChallengeScheme;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import io.skysail.server.restlet.resources.PostEntityServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailgunResource extends PostEntityServerResource<Mail> {

    @Override
    public Mail createEntityTemplate() {
        return new Mail();
    }

    @Override
    public void addEntity(Mail entity) {       
        ClientResource cr = new ClientResource("https://api.mailgun.net/v3/sandboxc553a4d53f224995804921bd11891114.mailgun.org/messages");
        cr.setChallengeResponse(ChallengeScheme.HTTP_BASIC, "api", "key-d00353c2873a61a41be8eb91f9bfcaa5");

        Form form = new Form();
        form.add("from", "mica.graef@gmail.com");
        form.add("to", "app@pline.one");
        form.add("subject", "form submitted from pline.one");
        
        StringBuilder text = new StringBuilder();
        text.append("Name:    ").append(entity.getName()).append("\n");
        text.append("Mail:    ").append(entity.getEmail()).append("\n");
        text.append("Message: ").append(entity.getMessage()).append("\n");
        
        form.add("text", text.toString());

        try {
        	Representation post = cr.post(form, MediaType.APPLICATION_WWW_FORM);
        	log.info(post.toString());
        } catch (Exception e) {
        	log.error(e.getMessage(), e);
        }
    }

}
