package io.skysail.server.ext.starmoney.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.skysail.domain.Nameable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import io.skysail.domain.html.Relation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode(of = "id")
@ToString(of = {"id","name","kontonummer", "bankleitzahl"})
@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="id")
public class Account implements Nameable {

    @Getter
    private String id;

    @Field
    @Getter
    private String name;

    @Field(inputType = InputType.READONLY)
    @Getter
    private String kontonummer;

    @Field(inputType = InputType.READONLY)
    @Getter
    private String bankleitzahl;

    @Field(inputType = InputType.READONLY)
    private Integer count;

    @Relation
    @JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
    private List<Transaction> transactions = new ArrayList<>();

    public Account(String kontonummer, String bankleitzahl) {
        this.kontonummer = kontonummer;
        this.bankleitzahl = bankleitzahl;
        name = kontonummer;
    }

    public Integer getCount() {
        return transactions.size();
    }


}
