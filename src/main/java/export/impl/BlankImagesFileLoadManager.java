package export.impl;

import coolor.generator.BlankImageDto;
import coolor.models.BlankImageModel;
import coolor.models.ImageModel;
import export.XlsCRUD;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by KAKolesnikov on 2015-09-22.
 */
public class BlankImagesFileLoadManager extends AbstractFilesManager {

    public static String DEFAULT_NAME = "ImagesListTemplate";
    private static final Object[] COLUMN_NAMES = {"Code", "Name", "City", "Width", "Height", "Media", "Luverses"};

    @Override
    public File createXlsFile(String absolutePath, List<BlankImageModel> dataToFile) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(DEFAULT_NAME);
        Map<String, Object[]> dataMap = createTemplateMap();
        fillXlsSheetWithStringData(dataMap, sheet, 0);
        File outputFile = new File(absolutePath + "." + XLS_EXTENSION);
        if (outputFile.exists()) {
            log.warn("File already exist!!!");
            return null;
        }
        writeWorkbookToFile(outputFile, workbook);
        return outputFile;
    }

    @Override
    public List<BlankImageModel> readDataFromXlsFile(FileInputStream fis) {
        List<BlankImageDto> dataFromXlsFile = getDataFromXlsFile(fis);
        List<BlankImageModel> result = new ArrayList<>();
        for (BlankImageDto blankImageDto : dataFromXlsFile) {
            result.add(new BlankImageModel(blankImageDto));
        }
        return result;
    }

    @Override
    public boolean updateXlsFile(File existFile, List<BlankImageModel> newData) {
        return false;
    }

    @Override
    public boolean deleteXlsFile(File file) {
        return false;
    }

    @Override
    protected Map<String, Object[]> createDataMap(List<? extends BlankImageModel> dataList, boolean isNewMap) {
        return null;
    }

    private Map<String, Object[]> createTemplateMap() {
        Map<String, Object[]> result = new HashMap<String, Object[]>();
        result.put("0", COLUMN_NAMES);
        return result;
    }

    private List<BlankImageDto> getDataFromXlsFile(FileInputStream fis) {
        List<BlankImageDto> results = new LinkedList<BlankImageDto>();
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(fis);
            HSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); ++i) {
                BlankImageDto tempImageModel = new BlankImageDto();
                HSSFRow tempRow = sheet.getRow(i);
                Object[] data = parseRow(tempRow);
                Double code = (Double) data[0];
                Double luvers = (Double) data[6];
                Double width = (Double) data[3];
                Double height = (Double) data[4];
                tempImageModel.setCode(code.intValue());
                tempImageModel.setName((String) data[1]);
                tempImageModel.setCity((String) data[2]);
                tempImageModel.setWidth(width.floatValue());
                tempImageModel.setHeight(height.floatValue());
                tempImageModel.setMedia((String) data[5]);
                tempImageModel.setLuvers(luvers.intValue());
                results.add(tempImageModel);
            }
        } catch(ClassCastException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error in xls file structure!", ButtonType.OK);
            alert.showAndWait();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return results;
    }
}
