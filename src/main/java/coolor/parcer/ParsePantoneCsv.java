package coolor.parcer;

import com.univocity.parsers.conversions.IntegerConversion;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import coolor.colorspaces.Hex;
import coolor.colorspaces.RGB;
import coolor.fx.SpotColor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by KAKolesnikov on 2015-08-11.
 */
public class ParsePantoneCsv {

    private CsvParserSettings settings;
    private CsvParser parser;

    public ParsePantoneCsv() {
        settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        parser = new CsvParser(settings);

    }

    public List<SpotColor> getSpotColorsFromCsv(InputStream is) {
        List<SpotColor> result = new ArrayList<>();
        try {

            List<String[]> allRows = parser.parseAll(new InputStreamReader(is, "UTF-8"));
            for (String[] row : allRows) {
                Hex currentHex = new Hex(row[1]);
                SpotColor temporarySpotColor = new SpotColor("Pantone " + row[0], currentHex);
                result.add(temporarySpotColor);
            }
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<SpotColor> getSkinPantoneFromCsv(InputStream is) {
        List<SpotColor> skinPantones = new ArrayList<>();
        try {
            List<String[]> allRows = parser.parseAll(new InputStreamReader(is, "UTF-8"));
            for (String[] row : allRows) {
                RGB currentRgb = new RGB(Integer.parseInt(row[1]), Integer.parseInt(row[2]), Integer.parseInt(row[3]));
                SpotColor temporarySpotColor = new SpotColor("Pantone " + row[0], currentRgb);
                skinPantones.add(temporarySpotColor);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return skinPantones;
    }

    public List<SpotColor> getRalColorsFromCsv(InputStream is) {
        List<SpotColor> ralsList = new ArrayList<>();
        try {
            List<String[]> allRows = parser.parseAll(new InputStreamReader(is, "UTF-8"));
            for (String[] row : allRows) {
                Hex currentHex = new Hex(row[2]);
                SpotColor temporarySpotColor = new SpotColor(row[0], currentHex);
                ralsList.add(temporarySpotColor);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return ralsList;
    }
}
