package skysail.frontend.smviewer.fx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

@Getter
public class Account {

    private final StringProperty kontonummerProperty;
    private final StringProperty bankleitzahlProperty;

    public Account(String kontonummer, String bankleitzahl) {
        this.kontonummerProperty = new SimpleStringProperty(kontonummer);
        this.bankleitzahlProperty = new SimpleStringProperty(bankleitzahl);
    }

    public Account(io.skysail.server.ext.starmoney.domain.Account a) {
        this.kontonummerProperty = new SimpleStringProperty(a.getKontonummer());
        this.bankleitzahlProperty = new SimpleStringProperty(a.getBankleitzahl());
    }

    public String getKontonummer() {
        return kontonummerProperty.get();
    }

    public String getBankleitzahl() {
        return bankleitzahlProperty.get();
    }

}
