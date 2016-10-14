package io.skysail.server.app.starmoney;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.skysail.domain.Nameable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.domain.html.Relation;
import io.skysail.server.app.starmoney.transactions.Transaction;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(of = "id")
@ToString
@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class Account implements Nameable {

    private String id;

    @Field
    private String name;

    @Field(inputType = InputType.READONLY)
    private String kontonummer;

    @Field(inputType = InputType.READONLY)
    private String bankleitzahl;

    @Relation
    @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
    private List<Transaction> transactions = new ArrayList<>();

    public Account(String kontonummer, String bankleitzahl) {
        this.kontonummer = kontonummer;
        this.bankleitzahl = bankleitzahl;
        name = kontonummer;
    }

}
