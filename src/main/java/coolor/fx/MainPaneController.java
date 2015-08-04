package coolor.fx;

import coolor.ImageModel;
import coolor.area.FolderReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import org.apache.commons.imaging.ImageInfo;

import java.io.File;
import java.util.List;


/**
 * Created by KAKolesnikov on 2015-08-04.
 */
public class MainPaneController {

    private Starter starter;

    @FXML
    private TextField pathToFolder;
    @FXML
    private Button scanFolderButton;
    @FXML
    private Button chooseDirectoryButton;
    @FXML
    private CheckBox handleQuantity;
    @FXML
    private Label totalArea;
    @FXML
    private TableView<ImageModel> imagesTableView;
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

    List<ImageModel> imageModelList;

    public MainPaneController() {
    }

    @FXML
    private void initialize(){
        fileName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        quantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        width.setCellValueFactory(cellData -> cellData.getValue().widthProperty());
        height.setCellValueFactory(cellData -> cellData.getValue().heightProperty());
        area.setCellValueFactory(cellData -> cellData.getValue().areaProperty());
        colorSpace.setCellValueFactory(cellData -> cellData.getValue().colorspaceProperty());
    }

    public void setStarter(Starter starter) {
        this.starter = starter;
    }


    protected void initButtonsListeners(){
        scanFolderButton.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                scanFolder();
            }
        });
        chooseDirectoryButton.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser chooser = new DirectoryChooser();
                chooser.setTitle("Choose directory to scan");
                File defaultDirectory = new File("c:/");
                chooser.setInitialDirectory(defaultDirectory);
                File selectedDirectory = chooser.showDialog(starter.getPrimaryStage());
                pathToFolder.setText(selectedDirectory.getAbsolutePath());
            }
        });
    }

    private void scanFolder(){
        FolderReader folderReader = new FolderReader();
        ObservableList<ImageModel> imageModels = FXCollections.observableArrayList();
         imageModelList = folderReader.getListOfImageModels(pathToFolder.getText(), handleQuantity.isSelected());
        imageModels.addAll(imageModelList);
        imagesTableView.setItems(imageModels);
        totalArea.setText(calculateTotalArea());
    }
    private String calculateTotalArea(){
        Float totalArea = 0f;
        for(ImageModel imageModel: imageModelList){
            totalArea += imageModel.getTotalArea();
        }
        return totalArea.toString();
    }
}
