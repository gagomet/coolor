package export.impl;

import coolor.models.BlankImageModel;
import export.XlsCRUD;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by KAKolesnikov on 2015-09-22.
 */
public abstract class AbstractFilesManager implements XlsCRUD {

    protected static final Logger log = Logger.getLogger(XlsFilesManager.class);
    protected static final String EMPTY_STRING = "";
    protected static final String XLS_EXTENSION = "xls";
    public static String DEFAULT_NAME = "ImagesList";

    @Override
    public File createXlsFile(String absolutePath, List<BlankImageModel> dataToFile) {
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

    protected void fillXlsSheetWithStringData(Map<String, Object[]> dataMap, HSSFSheet sheet, int rowNumber) {
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

    protected abstract Map<String, Object[]> createDataMap(List<? extends BlankImageModel> dataList, boolean isNewMap);

    protected void writeWorkbookToFile(File file, HSSFWorkbook workbook) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
        } catch(IOException e) {
            log.debug("Throwing IO Exception", e);
        }
    }

    protected Object[] parseRow(HSSFRow row) {
        List<Object> result = new LinkedList<Object>();
        short rowSize = row.getLastCellNum();
        for (int i = 0; i < rowSize; ++i) {
            HSSFCell cell = row.getCell(i);
            if (cell.getCellType() == 0) {
                result.add(cell.getNumericCellValue());
            } else {
                result.add(cell.toString());
            }
        }
        return result.toArray();
    }
}
