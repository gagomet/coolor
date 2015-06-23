package coolor.fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Starter extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Coolor");
        Button button = new Button("Button");
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(button);
        stage.setScene(new Scene(stackPane, 640, 480));
        stage.show();
    }
}
