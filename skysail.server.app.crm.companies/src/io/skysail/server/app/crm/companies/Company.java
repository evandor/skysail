package io.skysail.server.app.crm.companies;

import java.util.List;

import javax.persistence.Id;

import org.restlet.ext.apispark.internal.model.Contact;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.server.codegen.annotations.GenerateResources;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@GenerateResources(application = "io.skysail.server.app.crm.companies.CompaniesApplication")
public class Company implements Identifiable {

    @Id
    private String id;

    @Field
    private String name;
    
    @Field
    private List<Contact> contacts;

}