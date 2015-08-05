package export.impl;

import coolor.ImageModel;
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

public class XlsFilesManager implements XlsCRUD {

    private static final Logger log = Logger.getLogger(XlsFilesManager.class);
    private static final String EMPTY_STRING = "";
    private static final String XLS_EXTENSION = "xls";
    public static final String DEFAULT_NAME = "ImagesList";
    private static final Object[] COLUMN_NAMES = {"Path to file", "File Name", "Width", "Height", "Color space", "Quantity", "Position area"};

    @Override
    public File createXlsFile(String absolutePath, List<ImageModel> dataToFile) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(DEFAULT_NAME);
        Map<String, Object[]> dataMap = createDataMap(dataToFile, true);
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
    public List<ImageModel> readCompaniesListFromXlsFile(FileInputStream fis) {
        return getDataFromXlsFile(fis);
    }

    @Override
    public boolean updateXlsFile(File existFile, List<ImageModel> newData) {
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

    private Map<String, Object[]> createDataMap(List<ImageModel> dataList, boolean isNewMap) {
        if (!dataList.isEmpty()) {
            Map<String, Object[]> result = new HashMap<String, Object[]>();
            if (isNewMap) {
                result.put("0", COLUMN_NAMES);
            }
            for (int i = 0; i < dataList.size(); i++) {
                ImageModel tempImageModel = dataList.get(i);
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

    private void fillXlsSheetWithStringData(Map<String, Object[]> dataMap, HSSFSheet sheet, int rowNumber) {
        Set<String> keySet = dataMap.keySet();
        int rowNum = rowNumber;
        for (String key : keySet) {
            Row row = sheet.createRow(rowNum++);
            Object[] rowData = dataMap.get(key);
            int cellNum = 0;
            for (Object tempObject : rowData) {
                Cell cell = row.createCell(cellNum++);
                cell.setCellValue(tempObject.toString());
            }
        }
    }

    private void writeWorkbookToFile(File file, HSSFWorkbook workbook) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
        } catch(IOException e) {
            log.debug("Throwing IO Exception", e);
        }
    }

    private List<ImageModel> getDataFromXlsFile(FileInputStream fis) {
        List<ImageModel> results = new LinkedList<ImageModel>();
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

    private Object[] parseRow(HSSFRow row) {
        List<String> result = new LinkedList<String>();
        short rowSize = row.getLastCellNum();
        for (int i = 0; i < rowSize; ++i) {
            HSSFCell cell = row.getCell(i);
            String value;
            try {
                value = cell.getStringCellValue();
            } catch(NullPointerException e) {
                result.add(EMPTY_STRING);
                continue;
            }
            result.add(cell.getStringCellValue());
            System.out.println("  ");
        }
        return result.toArray();
    }
}
