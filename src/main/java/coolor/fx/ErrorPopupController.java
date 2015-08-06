package coolor.fx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


/**
 * Created by KAKolesnikov on 2015-08-06.
 */
public class ErrorPopupController extends AbstractController {

    @FXML
    private Button okButton;
    @FXML
    private Label errorMessageLabel;

    private String errorMesage;

    public ErrorPopupController(){

    }


    protected void initButtonsListeners(){
        okButton.setOnAction(event -> {
            starter.closeErrorPopup();
        });
        errorMessageLabel.setText(errorMesage);
    }

    public String getErrorMesage() {
        return errorMesage;
    }

    public void setErrorMesage(String errorMesage) {
        this.errorMesage = errorMesage;
    }
}
