package skysail.frontend.smviewer.fx.view;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import skysail.frontend.smviewer.fx.FxApp;
import skysail.frontend.smviewer.fx.model.Account;

public class TransactionsOverviewController {

    @FXML
    private TableView<Account> accountsTable;
    @FXML
    private TableColumn<Account, String> kontonummerColumn;
    @FXML
    private TableColumn<Account, String> bankleitzahlColumn;

    private FxApp mainApp;

    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        kontonummerColumn.setCellValueFactory(cellData -> cellData.getValue().getKontonummerProperty());
        bankleitzahlColumn.setCellValueFactory(cellData -> cellData.getValue().getBankleitzahlProperty());
    }

    public void setMainApp(FxApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        accountsTable.setItems(mainApp.getAccounts());
    }
}
