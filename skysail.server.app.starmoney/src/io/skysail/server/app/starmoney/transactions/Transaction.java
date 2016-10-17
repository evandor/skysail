package io.skysail.server.app.starmoney.transactions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.server.app.starmoney.AccountsResource;
import io.skysail.server.facets.Facet;
import io.skysail.server.facets.FacetType;
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
    @Facet(type = FacetType.NUMBER, value="0,100,1000,10000")
    private double betrag;

    private String buchungstext;

    private String betragWaehrung;

    @Field
    @Facet(type = FacetType.YEAR)
    private Date buchungstag;

    private String beguenstigterAbsenderBankleitzahl;

    private String beguenstigterAbsenderKontonummer;
    private String beguenstigterAbsenderName;

    @Field
    @ListView(link = AccountsResource.class)
    private String kategorie;

    @Field
    private double saldo;

    private String originalbetrag;
    private String originalbetragWaehrung;
    private String kommentar;


    public Transaction(Map<String, Integer> mapping, List<String> list) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        kontonummer = list.get(mapping.get("kontonummer"));
        bankleitzahl = list.get(mapping.get("bankleitzahl"));
        betrag = Double.parseDouble(list.get(mapping.get("betrag")).replace(",","."));
        //buchungstext = list.get(mapping.get("buchungstext"));
        try {
            buchungstag = sdf.parse(list.get(mapping.get("buchungstag")));
        } catch (ParseException e) {
            log.error(e.getMessage(),e);
        }
        kategorie = list.get(mapping.get("kategorie"));
        saldo = Double.parseDouble(list.get(mapping.get("saldo")).replace(",","."));
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
