package skysail.frontend.smviewer.fx;

import java.io.IOException;
import java.util.concurrent.Executors;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import io.skysail.server.ext.starmoney.api.StarMoneyApi;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;
import skysail.frontend.smviewer.fx.model.Account;
import skysail.frontend.smviewer.fx.view.TransactionsOverviewController;

@Component
public class FxApp extends Application { //implements StageProvider {

    private Stage stage;
    private Stage primaryStage;
    private BorderPane rootLayout;

    private static volatile StarMoneyApi starMoneyApi;

    @Getter
    private ObservableList<Account> accounts = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
    public void setStarMoneyApi(StarMoneyApi api) {
        if (api != null) {
            FxApp.starMoneyApi = api;
        }
    }

    public void unsetStarMoneyApi(StarMoneyApi api) {
    }

    @Activate
    public synchronized void startBundle() {
        Executors.defaultThreadFactory().newThread(() -> {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            launch();
        }).start();
    }

    @Override
    @Deactivate
    public synchronized void stop() throws Exception {
        if (stage != null) {
            stage.close();
        }
        //Platform.exit();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Skysail StarMoney Reporting");
       // starMoneyApi.getAccounts().stream().forEach(a -> accounts.add(new Account(a)));
        initRootLayout();
        showPersonOverview();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(FxApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPersonOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(FxApp.class.getResource("view/TransactionsOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();

            rootLayout.setCenter(personOverview);

            TransactionsOverviewController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
