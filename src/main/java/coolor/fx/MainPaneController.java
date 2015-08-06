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
import javafx.util.StringConverter;
import org.apache.commons.imaging.ImageInfo;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
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
    private RadioButton radioUsd;
    @FXML
    private RadioButton radioEuro;
    @FXML
    private MenuBar menuBar;

    private List<ImageModel> imageModelList;
    private CurrencyDTO currencyDTO;
    CurrencyTranslator currencyTranslator;
    private static final String UAH_SUFFIX = " UAH";

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
    }


    protected void initButtonsListeners() {
        scanFolderButton.setOnAction(event -> {
            if (pathToFolder.getText().isEmpty()) {
                errorMessage.setText(bundle.getString("error.set.folder"));
                errorMessage.setVisible(true);
            } else {
                scanFolder();
                exportToExcel.setDisable(false);
            }
        });

        chooseDirectoryButton.setOnAction(event -> {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle(bundle.getString("folder.to.scan.dialog"));
            File defaultDirectory = new File(bundle.getString("initial.folder"));
            chooser.setInitialDirectory(defaultDirectory);
            File selectedDirectory = chooser.showDialog(starter.getPrimaryStage());
            if (selectedDirectory == null) {
                starter.getErrorPopup(bundle.getString("error.choose.folder"));
            } else {
                pathToFolder.setText(selectedDirectory.getAbsolutePath());
            }
            //TODO add handling choice nothing
            errorMessage.setVisible(false);
        });

        exportToExcel.setOnAction(event -> {
            XlsCRUD xlsManager = new XlsFilesManager();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("folder.to.save.dialog");
            fileChooser.setInitialFileName(XlsFilesManager.DEFAULT_NAME);
            fileChooser.setInitialDirectory(new File(bundle.getString("initial.folder")));
            File selectedFile = fileChooser.showSaveDialog(starter.getPrimaryStage());
            xlsManager.createXlsFile(selectedFile.getAbsolutePath(), imagesTableView.getItems());
        });
    }

    protected void initMenus() {
        menuBar.getMenus().get(0).getItems().get(0).setOnAction(event -> {
            starter.getProxyPopup();
        });
        menuBar.getMenus().get(1).getItems().get(0).setOnAction(event -> {

        });
    }

    protected void initCheckboxes() {
        calculateCost.setOnAction(event -> {
            if (calculateCost.isSelected()) {
                currencyTranslator = new CurrencyTranslator(starter.getUserProxy());
                try {
                    currencyTranslator.translateCourses();
                } catch(IOException e) {
                    log.error("IO exception!");
                    starter.getErrorPopup(bundle.getString("error.connection"));
                    calculateCost.setSelected(false);
                }
                usdCurrency.setText(currencyTranslator.getUsdCourse().toString() + UAH_SUFFIX);
                euroCurrency.setText(currencyTranslator.getEuroCourse().toString() + UAH_SUFFIX);
                currencyDTO = new CurrencyDTO();
                cost.setVisible(true);
                currencyPane.setVisible(true);
            }
        });
    }

    protected void initRadioButtons() {
        ToggleGroup currenciesGroup = new ToggleGroup();
        radioUsd.setToggleGroup(currenciesGroup);
        radioEuro.setToggleGroup(currenciesGroup);
        radioUsd.setSelected(true);
    }

    private void scanFolder() {
        FolderReader folderReader = new FolderReader();
        ObservableList<ImageModel> imageModels = FXCollections.observableArrayList();
        if (calculateCost.isSelected()) {
            if (!costSqMeter.getText().isEmpty()) {
                errorMessage.setVisible(false);
                if (radioUsd.isSelected()) {
                    currencyDTO.setCurrency(currencyTranslator.getUsdCourse());
                } else if (radioEuro.isSelected()) {
                    currencyDTO.setCurrency(currencyTranslator.getEuroCourse());
                }
                currencyDTO.setCost(Float.parseFloat(costSqMeter.getText()));
                imageModelList = folderReader.getListOfImageModels(pathToFolder.getText(), handleQuantity.isSelected(),
                        currencyDTO);
                imageModels.addAll(imageModelList);
                imagesTableView.setItems(imageModels);
                totalArea.setText(calculateTotalArea());
            } else {
                errorMessage.setText(bundle.getString("error.set.price"));
                errorMessage.setVisible(true);
            }
        } else {
            imageModelList = folderReader.getListOfImageModels(pathToFolder.getText(), handleQuantity.isSelected());
            imageModels.addAll(imageModelList);
            imagesTableView.setItems(imageModels);
            totalArea.setText(calculateTotalArea());
            errorMessage.setVisible(true);
            errorMessage.setVisible(true);
        }
    }

    private String calculateTotalArea() {
        Float totalArea = 0f;
        for (ImageModel imageModel : imageModelList) {
            totalArea += imageModel.getTotalArea();
        }
        return totalArea.toString();
    }
}
