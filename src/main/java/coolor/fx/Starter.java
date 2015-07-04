package coolor.fx;

import coolor.colorspaces.CMYK;
import coolor.parcer.ParseXml;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Starter extends Application {
    private Stage primaryStage;
    private Pane rootLayout;
    private ObservableList<SpotColorModel> predefinedSpots = FXCollections.observableArrayList();

    public Starter(){
        Map<String, CMYK> preloaded = ParseXml.getOracalsMap(new File(Starter.class.getClassLoader().getResource("oracals.xml").getPath()));
        for (String s : preloaded.keySet()) {
            SpotColorModel tempModel = new SpotColorModel(s, preloaded.get(s));
            predefinedSpots.add(tempModel);
        }
        System.out.println("hui");
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
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            loader.setLocation(classloader.getResource("coolor/fx/mainPane.fxml"));
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
}
