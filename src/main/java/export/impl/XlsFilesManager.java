package export.impl;

import coolor.models.BlankImageModel;
import coolor.models.ImageModel;
import export.XlsCRUD;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class XlsFilesManager extends AbstractFilesManager {

    private static final Object[] COLUMN_NAMES = {"Path to file", "File Name", "Width", "Height", "Color space", "Quantity", "Position area"};

    @Override
    public List<BlankImageModel> readDataFromXlsFile(FileInputStream fis) {
        return getDataFromXlsFile(fis);
    }

    @Override
    public boolean updateXlsFile(File existFile, List<BlankImageModel> newData) {
        try {
            FileInputStream fis = new FileInputStream(existFile);
            HSSFWorkbook workbook = new HSSFWorkbook(fis);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Map<String, Object[]> dataMap = createDataMap(newData, false);
            fillXlsSheetWithStringData(dataMap, sheet, sheet.getLastRowNum());
            writeWorkbookToFile(existFile, workbook);
            fis.close();
        } catch(IOException e) {
            log.error("IO error " + e);
        }
        return false;
    }

    @Override
    public boolean deleteXlsFile(File file) {
        if (file.delete()) {
            log.info("File" + file.getName() + "deleted!");
            return true;
        }
        return false;
    }

    protected Map<String, Object[]> createDataMap(List<? extends BlankImageModel> dataList, boolean isNewMap) {
        if (!dataList.isEmpty()) {
            Map<String, Object[]> result = new HashMap<String, Object[]>();
            if (isNewMap) {
                result.put("0", COLUMN_NAMES);
            }
            for (int i = 0; i < dataList.size(); i++) {
                ImageModel tempImageModel = (ImageModel) dataList.get(i);
                List<Object> objectList = new LinkedList<Object>();
                objectList.add(tempImageModel.getPathToFile());
                objectList.add(tempImageModel.getName());
                objectList.add(tempImageModel.getWidth());
                objectList.add(tempImageModel.getHeight());
                objectList.add(tempImageModel.getQuantity());
                objectList.add(tempImageModel.getColorspace());
                objectList.add(tempImageModel.getTotalArea());
                result.put(Integer.toString(i + 2), objectList.toArray());
            }
            return result;
        }
        return Collections.EMPTY_MAP;
    }


    private List<BlankImageModel> getDataFromXlsFile(FileInputStream fis) {
        List<BlankImageModel> results = new LinkedList<BlankImageModel>();
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(fis);
            HSSFSheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); ++i) {
                HSSFRow tempRow = sheet.getRow(i);
                Object[] data = parseRow(tempRow);
                ImageModel tempImageModel = new ImageModel();
                tempImageModel.setPathToFile((String)data[0]);
                tempImageModel.setName((String) data[1]);
                tempImageModel.setWidth((Float) data[2]);
                tempImageModel.setHeight((Float) data[3]);
                tempImageModel.setQuantity((Integer) data[4]);
                tempImageModel.setColorspace(data[5]);
                tempImageModel.setArea((Float) data[6]);
                results.add(tempImageModel);
            }
        } catch(IOException e) {
            log.debug("Throwing IO Exception", e);
        }
        return results;
    }
}
