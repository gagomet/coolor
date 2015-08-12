package coolor.fx;

import coolor.translate.UserProxy;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Starter extends Application {

    private ResourceBundle bundle = ResourceBundle.getBundle("application");

    private Stage primaryStage;
    private Stage popupStage;
    private Stage errorStage;
    private UserProxy userProxy;

    public Starter() {
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
        this.primaryStage.setTitle(bundle.getString("main.frame.name"));
        initRootLayout();
    }

    /**
     * Initializes the root layout.
     */
    protected void initRootLayout() {
        try {
            // Load root layout from fxml file.
            URL location = getClass().getResource(bundle.getString("main.frame.fxml"));
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent rootLayout = (Parent) loader.load(location.openStream());
            MainPaneController mainPaneController = loader.getController();
            mainPaneController.setStarter(this);
            mainPaneController.initButtonsListeners();
            mainPaneController.initMenus();
            mainPaneController.initRadioButtons();
            mainPaneController.initCheckboxes();
            mainPaneController.initDeleteButtons();
            mainPaneController.initChoiceBoxes();
            mainPaneController.initWebView();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    protected void getProxyPopup() {
        try {
            URL location = getClass().getResource(bundle.getString("proxy.settings.fxml"));
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent rootLayout = (Parent) loader.load(location.openStream());
            Scene scene = new Scene(rootLayout);
            ProxySettingsController proxySettingsController = loader.getController();
            proxySettingsController.setStarter(this);
            proxySettingsController.initButtonsListeners();
            popupStage = new Stage();
            popupStage.setTitle(bundle.getString("proxy.settings.name"));
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(scene);
            popupStage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    protected void getErrorPopup(String errorMessage) {
        try {
            URL location = getClass().getResource(bundle.getString("error.fxml"));
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(location);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent rootLayout = (Parent) loader.load(location.openStream());
            Scene scene = new Scene(rootLayout);
            ErrorPopupController errorPopupController = loader.getController();
            errorPopupController.setStarter(this);
            errorPopupController.setErrorMesage(errorMessage);
            errorPopupController.initButtonsListeners();
            errorStage = new Stage();
            errorStage.setTitle(bundle.getString("error.name"));
            errorStage.initModality(Modality.APPLICATION_MODAL);
            errorStage.setScene(scene);
            errorStage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    protected void closeProxySettings(){
        popupStage.close();
    }

    protected void closeErrorPopup(){
        errorStage.close();
    }

    protected void initUserProxy(){
        this.userProxy = new UserProxy();
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

    public UserProxy getUserProxy() {
        return userProxy;
    }

    public void setUserProxy(UserProxy userProxy) {
        this.userProxy = userProxy;
    }
}
