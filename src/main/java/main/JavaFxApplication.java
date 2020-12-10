package main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.sql.SQLException;

public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);
        try {
            this.applicationContext = new SpringApplicationBuilder()
                    .sources(main.Main.class)
                    .run(args);
        }catch(Exception e1){
            System.out.println("Spring boot failed to launch, may already be running");
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/mainview.fxml"));
        loader.setController(ViewSwitcher.getInstance());
        Parent rootNode = loader.load();
        Scene scene = new Scene(rootNode);
        stage.setScene(scene);
        stage.setTitle("My Assign1 Demo");
        stage.show();

    }

    @Override
    public void stop() {
        this.applicationContext.close();
    }
}
