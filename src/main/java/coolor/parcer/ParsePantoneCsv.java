package coolor.parcer;

import coolor.colorspaces.Hex;
import coolor.colorspaces.RGB;
import coolor.fx.SpotColor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by KAKolesnikov on 2015-08-11.
 */
public class ParsePantoneCsv {
    public List<SpotColor> getSpotColorsFromCsv(File csvFile){
        List<SpotColor> result = new ArrayList<>();
        try {
            CSVParser parser = CSVParser.parse(csvFile, Charset.defaultCharset(), CSVFormat.EXCEL);
            for (CSVRecord csvRecord : parser) {
                Hex currentHex = new Hex(csvRecord.get(1));
                SpotColor temporarySpotColor  = new SpotColor("Pantone " + csvRecord.get(0), currentHex);
                result.add(temporarySpotColor);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<SpotColor> getSkinPantoneFromCsv(File csvFile){
        List<SpotColor> skinPantones = new ArrayList<>();
        try {
            CSVParser parser = CSVParser.parse(csvFile, Charset.defaultCharset(), CSVFormat.EXCEL);
            for (CSVRecord csvRecord : parser) {
                RGB currentRGB = new RGB(Integer.parseInt(csvRecord.get(1)), Integer.parseInt(csvRecord.get(2)), Integer.parseInt(csvRecord.get(3)));
                SpotColor temporarySpotColor  = new SpotColor("Pantone " + csvRecord.get(0), currentRGB);
                skinPantones.add(temporarySpotColor);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return skinPantones;
    }

    public List<SpotColor> getRalColorsFromCsv(File csvFile){
        List<SpotColor> ralsList = new ArrayList<>();
        try {
            CSVParser parser = CSVParser.parse(csvFile, Charset.defaultCharset(), CSVFormat.EXCEL);
            for (CSVRecord csvRecord : parser) {
                Hex currentHex = new Hex(csvRecord.get(2));
                SpotColor temporarySpotColor  = new SpotColor(csvRecord.get(0), currentHex);
                ralsList.add(temporarySpotColor);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return ralsList;
    }

}
