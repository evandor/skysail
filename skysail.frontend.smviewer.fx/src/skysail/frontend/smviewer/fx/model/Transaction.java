package skysail.frontend.smviewer.fx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

@Getter
public class Transaction {

    private final StringProperty datumProperty;
    private final StringProperty wertProperty;
    private final StringProperty kategorieProperty;
    private final StringProperty kommentarProperty;
    private final StringProperty saldoProperty;

    public Transaction(String datum, String wert, String kategorie, String kommentar, String saldo) {
        this.datumProperty = new SimpleStringProperty(datum);
        this.wertProperty = new SimpleStringProperty(wert);
        this.kategorieProperty = new SimpleStringProperty(kategorie);
        this.kommentarProperty = new SimpleStringProperty(kommentar);
        this.saldoProperty = new SimpleStringProperty(saldo);
    }

    public String getDatum() {
        return datumProperty.get();
    }

    public String getWert() {
        return wertProperty.get();
    }

    public String getKategorie() {
        return kategorieProperty.get();
    }

    public String getKommentar() {
        return kommentarProperty.get();
    }

    public String getSaldo() {
        return saldoProperty.get();
    }

}
