package io.skysail.server.app.starmoney;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.server.forms.ListView;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@Slf4j
public class Transaction implements Identifiable {

    private final static String DATE_FORMAT = "dd.MM.yyyy";

    private String id;

    @Field
    private String starMoneyId;

    @Field
    private String kontonummer;
    @Field
    private String bankleitzahl;
    @Field
    private String betrag;

    private String buchungstext;

    private String betragWaehrung;

    @Field
    private Date buchungstag;

    private String beguenstigterAbsenderBankleitzahl;

    private String beguenstigterAbsenderKontonummer;
    private String beguenstigterAbsenderName;

    @Field
    @ListView(link = AccountsResource.class)
    private String kategorie;

    @Field
    private String saldo;

    private String originalbetrag;
    private String originalbetragWaehrung;
    private String kommentar;


    public Transaction(Map<String, Integer> mapping, List<String> list) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        kontonummer = list.get(mapping.get("kontonummer"));
        bankleitzahl = list.get(mapping.get("bankleitzahl"));
        betrag = list.get(mapping.get("betrag"));
        //buchungstext = list.get(mapping.get("buchungstext"));
        try {
            buchungstag = sdf.parse(list.get(mapping.get("buchungstag")));
        } catch (ParseException e) {
            log.error(e.getMessage(),e);
        }
        kategorie = list.get(mapping.get("kategorie"));
        saldo = list.get(mapping.get("saldo"));
        starMoneyId = list.get(mapping.get("starMoneyId"));
        //id = this.toString();
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(kontonummer != null ? kontonummer.hashCode() : "null").append("-")
                .append(bankleitzahl != null ? bankleitzahl.hashCode() : "null").append("-")
                .append(buchungstag != null ? buchungstag.hashCode() : "null").append("-")
                .append(originalbetrag)
                .toString();
    }
}
