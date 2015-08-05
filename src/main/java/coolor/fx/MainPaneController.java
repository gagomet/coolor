package coolor.fx;

import coolor.ImageModel;
import coolor.area.FolderReader;
import coolor.dto.CurrencyDTO;
import coolor.translate.CurrencyTranslator;
import export.XlsCRUD;
import export.impl.XlsFilesManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import org.apache.commons.imaging.ImageInfo;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.List;


/**
 * Created by KAKolesnikov on 2015-08-04.
 */
public class MainPaneController extends AbstractController {

    @FXML
    private Pane currencyPane;
    @FXML
    private TextField costSqMeter;
    @FXML
    private TextField pathToFolder;
    @FXML
    private Button scanFolderButton;
    @FXML
    private Button chooseDirectoryButton;
    @FXML
    private Button exportToExcel;
    @FXML
    private CheckBox handleQuantity;
    @FXML
    private CheckBox calculateCost;
    @FXML
    private Label totalArea;
    @FXML
    private Label errorMessage;
    @FXML
    private Label usdCurrency;
    @FXML
    private Label euroCurrency;
    @FXML
    private TableView<ImageModel> imagesTableView;
    @FXML
    private TableColumn<ImageModel, String> pathToFile;
    @FXML
    private TableColumn<ImageModel, String> fileName;
    @FXML
    private TableColumn<ImageModel, Number> quantity;
    @FXML
    private TableColumn<ImageModel, Number> width;
    @FXML
    private TableColumn<ImageModel, Number> height;
    @FXML
    private TableColumn<ImageModel, Number> area;
    @FXML
    private TableColumn<ImageModel, ImageInfo.ColorType> colorSpace;
    @FXML
    private TableColumn<ImageModel, Number> cost;
    @FXML
    private ComboBox<String> currencies;
    @FXML
    private MenuBar menuBar;

    private List<ImageModel> imageModelList;
    private CurrencyDTO currencyDTO;
    private static final String USD = "USD";
    private static final String EURO = "EURO";

    public MainPaneController() {
    }

    @FXML
    private void initialize() {
        pathToFile.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        fileName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        quantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        width.setCellValueFactory(cellData -> cellData.getValue().widthProperty());
        height.setCellValueFactory(cellData -> cellData.getValue().heightProperty());
        area.setCellValueFactory(cellData -> cellData.getValue().areaProperty());
        colorSpace.setCellValueFactory(cellData -> cellData.getValue().colorspaceProperty());
        cost.setCellValueFactory(cellData -> cellData.getValue().costProperty());
        exportToExcel.setDisable(true);
        errorMessage.setVisible(false);
        cost.setVisible(false);
        currencyPane.setVisible(false);
        currencies = new ComboBox<String>(FXCollections.observableArrayList("USD", "EUR"));
    }


    protected void initButtonsListeners() {
        scanFolderButton.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (pathToFolder.getText().isEmpty()) {
                    errorMessage.setText(bundle.getString("error.set.folder"));
                    errorMessage.setVisible(true);
                } else {
                    scanFolder();
                    exportToExcel.setDisable(false);
                }
            }
        });
        chooseDirectoryButton.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle(bundle.getString("folder.to.scan.dialog"));
                File defaultDirectory = new File(bundle.getString("initial.folder"));
                chooser.setInitialDirectory(defaultDirectory);
                File selectedDirectory = chooser.showDialog(starter.getPrimaryStage());
                pathToFolder.setText(selectedDirectory.getAbsolutePath());
                errorMessage.setVisible(false);
            }
        });
        exportToExcel.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                XlsCRUD xlsManager = new XlsFilesManager();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("folder.to.save.dialog");
                fileChooser.setInitialFileName(XlsFilesManager.DEFAULT_NAME);
                fileChooser.setInitialDirectory(new File(bundle.getString("initial.folder")));
                File selectedFile = fileChooser.showSaveDialog(starter.getPrimaryStage());
                xlsManager.createXlsFile(selectedFile.getAbsolutePath(), imagesTableView.getItems());
            }
        });
    }

    protected void initMenus() {
        menuBar.getMenus().get(0).getItems().get(0).addEventHandler(ActionEvent.ACTION,
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        starter.getProxyPopup();
                    }
                });
    }

    protected void initCheckboxes() {
        calculateCost.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (calculateCost.isSelected()) {
                    CurrencyTranslator currencyTranslator = new CurrencyTranslator(starter.getUserProxy());
                    currencyTranslator.translateCourses();
                    usdCurrency.setText(currencyTranslator.getUsdCourse().toString());
                    euroCurrency.setText(currencyTranslator.getEuroCourse().toString());
                    currencyDTO = new CurrencyDTO();
                    //TODO resolve different currencies
                    cost.setVisible(true);
                    currencyPane.setVisible(true);
                }
            }
        });
    }

    protected void initComboBoxes() {
//        currencies.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                switch (currencies.getValue()) {
//                    case USD:
//                        currencyDTO.setCurrency(Float.parseFloat(usdCurrency.getText()));
//                        break;
//                    case EURO:
//                        currencyDTO.setCurrency(Float.parseFloat(euroCurrency.getText()));
//                        break;
//                }
//            }
//        });
    }

    private void scanFolder() {
        FolderReader folderReader = new FolderReader();
        ObservableList<ImageModel> imageModels = FXCollections.observableArrayList();
        if (calculateCost.isSelected()) {
            if (!costSqMeter.getText().isEmpty()) {
                errorMessage.setVisible(false);
                currencyDTO.setCost(Float.parseFloat(costSqMeter.getText()));
                imageModelList = folderReader.getListOfImageModels(pathToFolder.getText(), handleQuantity.isSelected(),
                        currencyDTO);
            } else {
                errorMessage.setText(bundle.getString("error.set.price"));
                errorMessage.setVisible(true);
            }
        } else {
            imageModelList = folderReader.getListOfImageModels(pathToFolder.getText(), handleQuantity.isSelected());
        }
        imageModels.addAll(imageModelList);
        imagesTableView.setItems(imageModels);
        totalArea.setText(calculateTotalArea());
    }

    private String calculateTotalArea() {
        Float totalArea = 0f;
        for (ImageModel imageModel : imageModelList) {
            totalArea += imageModel.getTotalArea();
        }
        return totalArea.toString();
    }
}
