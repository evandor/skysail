package io.skysail.server.ext.starmoney.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.server.forms.ListView;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@Slf4j
public class Transaction implements Entity {

    private final static String DATE_FORMAT = "dd.MM.yyyy";

    private String id;

    @Field
    @ListView(hide = true)
    private String starMoneyId;

    @Field
    @ListView(hide = true)
    private String kontonummer;

    @Field
    @ListView(hide = true)
    private String bankleitzahl;

    @Field
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date buchungstag;

    @Field
    private String buchungstext;

    @Field
    private double betrag;

    private String betragWaehrung;

    private String beguenstigterAbsenderBankleitzahl;

    private String beguenstigterAbsenderKontonummer;
    private String beguenstigterAbsenderName;

    @Field
   // @ListView(link = AccountsResource.class)
    private String kategorie;

    @Field
    private String verwendungszweck;

    @Field
    private double saldo;

    private String originalbetrag;
    private String originalbetragWaehrung;
    private String kommentar;

    private Calendar calendar = new GregorianCalendar();

    @Field
    @ListView(hide = true)
    private Month month;

    @Field
    @ListView(hide = true)
    private int year;

    public Transaction(Map<String, Integer> mapping, List<String> list) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        kontonummer = list.get(mapping.get("kontonummer"));
        bankleitzahl = list.get(mapping.get("bankleitzahl"));
        betrag = Double.parseDouble(list.get(mapping.get("betrag")).replace(",","."));
        try {
            buchungstag = sdf.parse(list.get(mapping.get("buchungstag")));
            LocalDate bookingDay = buchungstag.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            month = bookingDay.getMonth();
            year = bookingDay.getYear();
        } catch (ParseException e) {
            buchungstag = new Date(0);
            log.error(e.getMessage(),e);
        }
        buchungstext = list.get(mapping.get("buchungstext"));
        verwendungszweck = getVerwendungszweck(list,mapping);
        kategorie = list.get(mapping.get("kategorie"));
        saldo = Double.parseDouble(list.get(mapping.get("saldo")).replace(",","."));
        starMoneyId = list.get(mapping.get("starMoneyId"));
        id = starMoneyId;
    }

    private String getVerwendungszweck(List<String> list, Map<String, Integer> mapping) {
        return new StringBuilder()
                .append(list.get(mapping.get("verwendungszweckzeile1"))).append("\n")
                .append(list.get(mapping.get("verwendungszweckzeile2"))).append("\n")
                .append(list.get(mapping.get("verwendungszweckzeile3"))).append("\n")
                .append(list.get(mapping.get("verwendungszweckzeile4")))
                .toString();
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(kontonummer != null ? kontonummer.hashCode() : "null").append("-")
                .append(bankleitzahl != null ? bankleitzahl.hashCode() : "null").append("-")
                .append(buchungstag != null ? buchungstag.hashCode() : "null").append("-")
                .append(kategorie).append("-")
                .append(originalbetrag)
                .toString();
    }
}
