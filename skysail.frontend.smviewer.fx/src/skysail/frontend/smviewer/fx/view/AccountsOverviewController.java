package skysail.frontend.smviewer.fx.view;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import skysail.frontend.smviewer.fx.FxApp;
import skysail.frontend.smviewer.fx.model.Account;

public class AccountsOverviewController {

    @FXML
    private TableView<Account> accountsTable;
    @FXML
    private TableColumn<Account, String> kontonummerColumn;
    @FXML
    private TableColumn<Account, String> bankleitzahlColumn;

    private FxApp mainApp;

    @FXML
    private void initialize() {
        kontonummerColumn.setCellValueFactory(cellData -> cellData.getValue().getKontonummerProperty());
        bankleitzahlColumn.setCellValueFactory(cellData -> cellData.getValue().getBankleitzahlProperty());
    }

    public void setMainApp(FxApp mainApp) {
        this.mainApp = mainApp;
        accountsTable.setItems(mainApp.getAccounts());
    }

    private void showPersonDetails(Account account) {
        if (account != null) {
            // Fill the labels with info from the person object.
//            firstNameLabel.setText(person.getFirstName());
//            lastNameLabel.setText(person.getLastName());
//            streetLabel.setText(person.getStreet());
//            postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
//            cityLabel.setText(person.getCity());

            // TODO: We need a way to convert the birthday into a String!
            // birthdayLabel.setText(...);
        } else {
            // Person is null, remove all the text.
//            firstNameLabel.setText("");
//            lastNameLabel.setText("");
//            streetLabel.setText("");
//            postalCodeLabel.setText("");
//            cityLabel.setText("");
//            birthdayLabel.setText("");
        }
    }
}
