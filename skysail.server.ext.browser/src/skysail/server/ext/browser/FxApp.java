package skysail.server.ext.browser;

import java.util.concurrent.Executors;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, configurationPid="browser", configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = BrowserConfig.class)
@Slf4j
public class FxApp extends Application implements StageProvider {

    private Stage stage;
    private Scene scene;
	private static BrowserConfig config;
	private boolean started = false;
	
    @Activate
    public synchronized void startBundle(BrowserConfig config) {
    	FxApp.config = config;
        if (config == null || config.title() == null) {
        	return;
        }
        if (started) {
        	return;
        }
        log.info("about to start browser with config: {}", config);
		Executors.defaultThreadFactory().newThread(() -> {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            launch();
        }).start();
		this.started = true;
    }
    
    @Override
    @Deactivate
    public synchronized void stop() throws Exception {
    	this.started = false;
    	FxApp.config = null;
    	stage.close();
    }
    

    @Override
    public void start(Stage primaryStage) throws Exception {
    	if (started) {
    		return;
    	}
        this.stage = primaryStage;

        stage.setTitle(config.title());
		scene = new Scene(new Browser("http://localhost:2020"), config.width(), config.height(), Color.web("#666970"));
        stage.setScene(scene);
        scene.getStylesheets().add("io/skysail/server/ext/browser/BrowserToolbar.css");
        stage.show();
    }

	@Override
    public Stage getStage() {
        return this.stage;
    }

    @Deactivate
    public void stopBundle() {
        Platform.exit();
    }
}
