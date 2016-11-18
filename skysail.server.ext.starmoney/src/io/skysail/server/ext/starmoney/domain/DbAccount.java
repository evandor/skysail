package io.skysail.server.ext.starmoney.domain;

import io.skysail.domain.Nameable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
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
public class DbAccount implements Nameable {

    @Getter
    private String id;

    @Field
    private String name;

    @Field(inputType = InputType.READONLY)
    private String kontonummer;

    @Field(inputType = InputType.READONLY)
    private String bankleitzahl;

    public DbAccount(Account account, String name) {
        this.kontonummer = account.getKontonummer();
        this.bankleitzahl = account.getBankleitzahl();
        this.name = name;
    }


}
