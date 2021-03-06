package coolor.fx;

import coolor.generator.BlankImageGenerator;
import coolor.models.BlankImageModel;
import coolor.models.ImageModel;
import coolor.area.FolderReader;
import coolor.colorspaces.CMYK;
import coolor.colorspaces.RGB;
import coolor.converter.ColorConverter;
import coolor.dto.CurrencyDTO;
import coolor.parcer.ParsePantoneCsv;
import coolor.parcer.ParseXmlOracals;
import coolor.translate.CurrencyTranslator;
import export.XlsCRUD;
import export.impl.BlankImagesFileLoadManager;
import export.impl.XlsFilesManager;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;


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
    private TableView<BlankImageModel> imagesTableView;
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
    private TableColumn<ImageModel, Number> cost;
    @FXML
    private RadioButton radioUsd;
    @FXML
    private RadioButton radioEuro;
    @FXML
    private MenuBar menuBar;
    //***************************************************************************
    //    Color mapper tab
    @FXML
    private ChoiceBox<SpotColor> spotColorsChoiceBox;
    @FXML
    private ChoiceBox<SpotColor> pantoneCoatedChoicebox;
    @FXML
    private ChoiceBox<SpotColor> pantoneUncoatedChoicebox;
    @FXML
    private ChoiceBox<SpotColor> pantonePastelsNeonsChoicebox;
    @FXML
    private ChoiceBox<SpotColor> pantoneMetallic;
    @FXML
    private ChoiceBox<SpotColor> pantoneColorOfTheYearChoicebox;
    @FXML
    private ChoiceBox<SpotColor> pantoneSkinsChoiceBox;
    @FXML
    private ChoiceBox<SpotColor> ralColorsChoiceBox;
    @FXML
    private Pane colorsPane;
    @FXML
    private Rectangle spotColorRectangle;
    @FXML
    private Rectangle roundedSpotColorRectangle;
    @FXML
    private Label displayedColorName;
    @FXML
    private Label labelCmyk;
    @FXML
    private Label labelRgb;
    @FXML
    private Label roundedCmykLabel;
    @FXML
    private ColorPicker colorPicker;
    //***************************************************************************
    //Layout tab
    @FXML
    private Button startGenerationButton;
    @FXML
    private Button selectXlsToLoadButton;
    @FXML
    private Button aboutButton;
    @FXML
    private Button downloadPatternButton;
    @FXML
    private Button pathToSaveButton;
    @FXML
    private Label folderToSavePath;
    @FXML
    private Label fileToLoadPath;
    @FXML
    private TextArea logs;
    @FXML
    private ProgressBar processing;
    @FXML
    private Label processingDesc;

    private TableColumn deleteButtonColumn;

    private List<ImageModel> imageModelList;
    private CurrencyDTO currencyDTO;
    private CurrencyTranslator currencyTranslator;
    private ObservableList<BlankImageModel> imageModels;
    private static final String UAH_SUFFIX = " UAH";
    private static final double RGB_BYTES = 255d;

    private Task generation;

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
        cost.setCellValueFactory(cellData -> cellData.getValue().costProperty());
        exportToExcel.setDisable(true);
        errorMessage.setVisible(false);
        cost.setVisible(false);
        currencyPane.setVisible(false);
        deleteButtonColumn = new TableColumn("Delete entry");
        deleteButtonColumn.setSortable(false);
        colorsPane.setVisible(false);
        processing.setVisible(false);
        processingDesc.setVisible(false);
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
            } else if (selectedDirectory.exists() && selectedDirectory.canWrite()) {
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

        startGenerationButton.setOnAction(event -> {
            processing.setVisible(true);
            processingDesc.setVisible(true);
            generation = generationTask();
            new Thread(generation).start();
        });

        aboutButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, bundle.getString("information.xls"), ButtonType.OK);
            alert.showAndWait();
        });

        pathToSaveButton.setOnAction(event -> {
            System.out.println("Load to xls button pressed");
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("folder.to.save.dialog");
            directoryChooser.setInitialDirectory(new File(bundle.getString("initial.folder")));
            File selectedFile = directoryChooser.showDialog(starter.getPrimaryStage());
            if (Objects.nonNull(selectedFile) && selectedFile.exists()) {
                folderToSavePath.setText(selectedFile.getAbsolutePath());
            }
        });

        downloadPatternButton.setOnAction(event -> {
            XlsCRUD xlsManager = new BlankImagesFileLoadManager();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("folder.to.save.dialog");
            fileChooser.setInitialFileName(BlankImagesFileLoadManager.DEFAULT_NAME);
            fileChooser.setInitialDirectory(new File(bundle.getString("initial.folder")));
            File selectedFile = fileChooser.showSaveDialog(starter.getPrimaryStage());
            xlsManager.createXlsFile(selectedFile.getAbsolutePath(), imagesTableView.getItems());
            fileToLoadPath.setText(selectedFile.getAbsolutePath());
        });

        selectXlsToLoadButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("folder.to.save.dialog");
            fileChooser.setInitialDirectory(new File(bundle.getString("initial.folder")));
            File selectedFile = fileChooser.showOpenDialog(starter.getPrimaryStage());
            if (Objects.nonNull(selectedFile) && selectedFile.exists()) {
                fileToLoadPath.setText(selectedFile.getAbsolutePath());
            }
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
                } catch (IOException e) {
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

    protected void initChoiceBoxes() {
        ObservableList<SpotColor> oracalsObservableList = FXCollections.observableArrayList();
        ParseXmlOracals parseXmlOracals = new ParseXmlOracals();
        Map<String, CMYK> oracalsMap = parseXmlOracals.getOracalsMap(
                this.getClass().getResourceAsStream("/oracals.xml"));
        List<SpotColor> oracalsColorList = new ArrayList<>();
        Iterator iterator = oracalsMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, CMYK> pair = (Map.Entry<String, CMYK>) iterator.next();
            SpotColor temporaryColor = new SpotColor("Oracal " + pair.getKey(), pair.getValue());
            oracalsColorList.add(temporaryColor);
        }
        oracalsObservableList.addAll(oracalsColorList);
        spotColorsChoiceBox.setConverter(new SpotColorConverter());
        spotColorsChoiceBox.setItems(oracalsObservableList);
        spotColorsChoiceBox.getSelectionModel().selectedIndexProperty()
                .addListener((ov, value, new_value) -> {
                    SpotColor spotColor = oracalsColorList.get(new_value.intValue());
                    handleSpotColorChanges(spotColor);
                });
        initChoiceBoxByCsvFileName(pantoneCoatedChoicebox, "/pantone-coated.csv", null);
        initChoiceBoxByCsvFileName(pantoneUncoatedChoicebox, "/pantone-uncoated.csv", null);
        initChoiceBoxByCsvFileName(pantoneColorOfTheYearChoicebox, "/pantone-color-of-the-year.csv", null);
        initChoiceBoxByCsvFileName(pantoneMetallic, "/pantone-metallic.csv", null);
        initChoiceBoxByCsvFileName(pantonePastelsNeonsChoicebox, "/pantone-pastels-neons.csv", null);

        ParsePantoneCsv parsePantoneCsv = new ParsePantoneCsv();

        List<SpotColor> parsedSkinPantones = parsePantoneCsv.getSkinPantoneFromCsv(
                this.getClass().getResourceAsStream("/pantone-skin.csv"));
        initChoiceBoxByReadyList(pantoneSkinsChoiceBox, parsedSkinPantones);

        List<SpotColor> parsedRals = parsePantoneCsv.getRalColorsFromCsv(
                this.getClass().getResourceAsStream("/ral_standard.csv"));
        initChoiceBoxByReadyList(ralColorsChoiceBox, parsedRals);
    }

    private void initLayoutPane() {

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

    private void handleSpotColorChanges(SpotColor spotColor) {
        double doubleRed = spotColor.getRgb().getRed() / RGB_BYTES;
        double doubleGreen = spotColor.getRgb().getGreen() / RGB_BYTES;
        double doubleBlue = spotColor.getRgb().getBlue() / RGB_BYTES;
        spotColorRectangle.setFill(Color.color(doubleRed, doubleGreen, doubleBlue));
        colorsPane.setVisible(true);
        labelCmyk.setText("CMYK: " + spotColor.getCmyk().toString());
        labelRgb.setText("RGB: " + spotColor.getRgb().toString());
        RGB roundedRgb = ColorConverter.getInstance().cmykToRgb(spotColor.getCmyk().getRoundedCmyk());
        Color roundedColor = Color.color(roundedRgb.getRed() / RGB_BYTES, roundedRgb.getGreen() / RGB_BYTES,
                roundedRgb.getBlue() / RGB_BYTES);
        roundedSpotColorRectangle.setFill(roundedColor);
        roundedCmykLabel.setText("Rounded CMYK: " + spotColor.getCmyk().getRoundedCmyk().toString());
        colorPicker.setValue(roundedColor);
        displayedColorName.setText(spotColor.getName());
        log.info("rounded color: " + spotColor.getCmyk().getRoundedCmyk().toString());
        log.info(spotColor.getCmyk().toString());
    }

    private void initChoiceBoxByCsvFileName(ChoiceBox choiceBox, String csvFileName,
                                            List<SpotColor> spotColorsList) {
        if (spotColorsList == null) {
            ParsePantoneCsv parsePantoneCsv = new ParsePantoneCsv();
            List<SpotColor> parsedPantones = parsePantoneCsv.getSpotColorsFromCsv(
                    this.getClass().getResourceAsStream(csvFileName));
            initChoiceBoxByReadyList(choiceBox, parsedPantones);
        } else {
            initChoiceBoxByReadyList(choiceBox, spotColorsList);
        }
    }

    private void initChoiceBoxByReadyList(ChoiceBox choiceBox, List<SpotColor> spotColorsList) {
        ObservableList<SpotColor> spotColorsObservableList = FXCollections.observableArrayList();
        spotColorsObservableList.addAll(spotColorsList);
        choiceBox.setConverter(new SpotColorConverter());
        choiceBox.setItems(spotColorsObservableList);
        choiceBox.getSelectionModel().selectedIndexProperty()
                .addListener((ov, value, new_value) -> {
                    SpotColor spotColor = spotColorsList.get(new_value.intValue());
                    handleSpotColorChanges(spotColor);
                });
    }

    private void checkPathInLabel(Label label) {
        if ("No specified path".equals(label.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please choose path in " + label.getId(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    private Task generationTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                List<BlankImageModel> models = new ArrayList<>();
                System.out.println("Generation button pressed");
                BlankImagesFileLoadManager manager = new BlankImagesFileLoadManager();
                checkPathInLabel(fileToLoadPath);
                checkPathInLabel(folderToSavePath);
                try {
                    models = manager.readDataFromXlsFile(new FileInputStream(new File(fileToLoadPath.getText())));
                    BlankImageGenerator generator = new BlankImageGenerator();
                    generator.generateBlankImages(folderToSavePath.getText(), models, logs, processing, processingDesc);
                } catch (FileNotFoundException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "File not found!", ButtonType.OK);
                    alert.showAndWait();
                    e.printStackTrace();
                }
                processing.setVisible(false);
                processingDesc.setVisible(false);
                logs.appendText("Processing " + models.size() + " files succesfully finished");
                return true;
            }
        };
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

    protected class SpotColorConverter extends StringConverter<SpotColor> {

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
