package coolor.fx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Starter extends Application {
    private Stage primaryStage;
    private Pane rootLayout;

    public Starter(){
//        Map<String, CMYK> preloaded = ParseXml.getOracalsMap(new File(Starter.class.getClassLoader().getResource("oracals.xml").getPath()));
//        for (String s : preloaded.keySet()) {
//            SpotColorModel tempModel = new SpotColorModel(s, preloaded.get(s));
//            predefinedSpots.add(tempModel);
//        }
//        System.out.println("hui");
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");
        initRootLayout();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            File fxmlMain = new File("src/main/java/coolor/fx/mainPane.fxml");
            loader.setLocation(fxmlMain.toURL());
            rootLayout = (Pane)loader.load();
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
