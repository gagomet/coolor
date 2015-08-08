package coolor.fx;

import coolor.ImageModel;
import coolor.area.FolderReader;
import coolor.colorspaces.CMYK;
import coolor.converter.ColorConverter;
import coolor.dto.CurrencyDTO;
import coolor.parcer.ParseXml;
import coolor.translate.CurrencyTranslator;
import export.XlsCRUD;
import export.impl.XlsFilesManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import org.apache.commons.imaging.ImageInfo;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


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
    private TableColumn<ImageModel, String> width;
    @FXML
    private TableColumn<ImageModel, String> height;
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

//    Color mapper tab
    @FXML
    private ChoiceBox<SpotColor> spotColorsChoiceBox;
    @FXML
    private Rectangle spotColorRectangle;
    @FXML
    private Rectangle roundedSpotColorRectangle;
    @FXML
    private Pane colorsPane;

    private TableColumn deleteButtonColumn;

    private List<ImageModel> imageModelList;
    private CurrencyDTO currencyDTO;
    private CurrencyTranslator currencyTranslator;
    private ObservableList<ImageModel> imageModels;
    private static final String UAH_SUFFIX = " UAH";

    public MainPaneController() {
    }

    @FXML
    private void initialize() {
        pathToFile.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        fileName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        quantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        width.setCellValueFactory(cellData -> cellData.getValue().widthProperty().floatValue() > 0
                                              ? cellData.getValue()
                                                        .widthProperty()
                                                        .asString()
                                              : cellData.getValue().undefinedProperty());
        height.setCellValueFactory(
                cellData -> cellData.getValue().heightProperty().floatValue() > 0
                            ? cellData.getValue()
                                      .heightProperty()
                                      .asString()
                            : cellData.getValue().undefinedProperty());
        area.setCellValueFactory(cellData -> cellData.getValue().areaProperty());
        colorSpace.setCellValueFactory(cellData -> cellData.getValue().colorspaceProperty());
        cost.setCellValueFactory(cellData -> cellData.getValue().costProperty());
        exportToExcel.setDisable(true);
        errorMessage.setVisible(false);
        cost.setVisible(false);
        currencyPane.setVisible(false);
        deleteButtonColumn = new TableColumn("Delete entry");
        deleteButtonColumn.setSortable(false);
    }

    protected void initDeleteButtons() {
        imagesTableView.getColumns().add(deleteButtonColumn);
        deleteButtonColumn.setCellFactory(p -> new ButtonCell(imagesTableView));
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

    protected void initChoiceBoxes(){
        spotColorRectangle.setVisible(false);
        roundedSpotColorRectangle.setVisible(false);
        ObservableList<SpotColor> spotColorsObservableList = FXCollections.observableArrayList();
        ParseXml parseXml = new ParseXml();
        Map<String, CMYK> oracalsMap = parseXml.getOracalsMap(new File(MainPaneController.class.getClassLoader().getResource("oracals.xml").getPath()));
        List<SpotColor> spotColorList = new ArrayList<>();
        Iterator iterator = oracalsMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, CMYK> pair = (Map.Entry<String, CMYK>)iterator.next();
            SpotColor temporaryColor = new SpotColor(pair.getKey(), pair.getValue());
            temporaryColor.setRgb(ColorConverter.getInstance().cmykToRgb(pair.getValue()));
            temporaryColor.setHex(ColorConverter.getInstance().cmykToHex(pair.getValue()));
            spotColorList.add(temporaryColor);
        }
        spotColorsObservableList.addAll(spotColorList);
        spotColorsChoiceBox.setConverter(new SpotColorConverter());
        spotColorsChoiceBox.setItems(spotColorsObservableList);
        spotColorsChoiceBox.getSelectionModel().selectedIndexProperty()
          .addListener((ov, value, new_value) -> {
              //TODO implement logic of choicebox
              SpotColor spotColor = spotColorList.get(new_value.intValue());
              spotColorRectangle.setFill(Color.color(spotColor.getRgb().getRed()/255, spotColor.getRgb().getGreen()/255, spotColor.getRgb().getBlue()/255));
              spotColorRectangle.setVisible(true);
              roundedSpotColorRectangle.setVisible(true);
              System.out.println(spotColor.getCmyk().toString());
          });
    }

    private void scanFolder() {
        FolderReader folderReader = new FolderReader();
        imageModels = FXCollections.observableArrayList();
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
            errorMessage.setVisible(false);
        }
    }

    private String calculateTotalArea() {
        Float totalArea = 0f;
        for (ImageModel imageModel : imageModelList) {
            totalArea += imageModel.getTotalArea();
        }
        return totalArea.toString();
    }

    private class ButtonCell extends TableCell<ImageModel, Boolean> {

        final Button cellButton = new Button("Delete");

        ButtonCell(final TableView tblView) {

            cellButton.setOnAction(t -> {
                int selectdIndex = getTableRow().getIndex();
                //delete the selected item in data
                imagesTableView.getItems().remove(selectdIndex);
                imageModelList.remove(selectdIndex);
                totalArea.setText(calculateTotalArea());
                errorMessage.setVisible(false);
            });
        }

        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            }
        }
    }

    private class SpotColorConverter extends StringConverter<SpotColor> {

        @Override
        public String toString(SpotColor object) {
            return object.toString();
        }

        @Override
        public SpotColor fromString(String string) {
            return null;
        }
    }
}
