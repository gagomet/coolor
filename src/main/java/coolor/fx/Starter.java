package coolor.fx;

import coolor.ImageModel;
import coolor.area.FolderReader;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Starter extends Application {
    private Stage primaryStage;

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
        this.primaryStage.setTitle("Area counter application");
        initRootLayout();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            URL location = getClass().getResource("/mainPane.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent rootLayout = (Parent) loader.load(location.openStream());
            MainPaneController mainPaneController = loader.getController();
            mainPaneController.setStarter(this);
            mainPaneController.initButtonsListeners();
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
