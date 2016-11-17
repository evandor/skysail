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

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import io.skysail.server.forms.ListView;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@Slf4j
public class DbTransaction implements Identifiable {

    private final static String DATE_FORMAT = "dd.MM.yyyy";

    private String id;

    @Field
    private String starMoneyId;

    @Field
    private String kontonummer;

    @Field
    private String bankleitzahl;

    @Field
   // @Facet(type = FacetType.NUMBER, value="0,100,1000,10000")
    private double betrag;

    private String buchungstext;

    private String betragWaehrung;

    @Field
   // @Facet(type = FacetType.YEAR)
    private Date buchungstag;

    private String beguenstigterAbsenderBankleitzahl;

    private String beguenstigterAbsenderKontonummer;
    private String beguenstigterAbsenderName;

    @Field
   // @ListView(link = AccountsResource.class)
    private String kategorie;

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

    public DbTransaction(Map<String, Integer> mapping, List<String> list) {
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
        kategorie = list.get(mapping.get("kategorie"));
        saldo = Double.parseDouble(list.get(mapping.get("saldo")).replace(",","."));
        starMoneyId = list.get(mapping.get("starMoneyId"));
        id = starMoneyId;
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
