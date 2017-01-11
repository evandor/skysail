package io.skysail.server.app.crm.companies;

import java.util.List;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.FieldRelation;
import io.skysail.server.app.crm.contacts.Contact;
import io.skysail.server.codegen.annotations.GenerateResources;
import io.skysail.server.forms.PostView;
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
    @PostView(tab = "Overview")
    private String companyname;

    @Field
    @PostView(tab = "Overview")
    private String branch;

    @Field
    @PostView(tab = "Comment")
    private String comment;
    
    @FieldRelation(targetEntity = Contact.class)
    @PostView(tab = "Contacts")
    private List<Contact> contacts;

}