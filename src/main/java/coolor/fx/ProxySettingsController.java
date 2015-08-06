package coolor.fx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Created by KAKolesnikov on 2015-08-05.
 */
public class ProxySettingsController extends AbstractController {
    private Starter starter;

    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField proxyHost;
    @FXML
    private TextField proxyPort;

    @Override
    public Starter getStarter() {
        return starter;
    }

    @Override
    public void setStarter(Starter starter) {
        this.starter = starter;
    }

    public void initButtonsListeners(){
        submitButton.setOnAction(event -> {
            starter.initUserProxy();
            starter.getUserProxy().setProxyHost(proxyHost.getText());
            starter.getUserProxy().setProxyPort(Integer.parseInt(proxyPort.getText()));
            starter.closeProxySettings();
        });

        cancelButton.setOnAction(event -> {
            starter.closeProxySettings();
        });
    }
}
