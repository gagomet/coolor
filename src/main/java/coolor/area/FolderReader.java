package coolor.area;

import coolor.ImageModel;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KAKolesnikov on 2015-08-04.
 */
public class FolderReader {

    public static final Logger log = Logger.getLogger(FolderReader.class);


    private static String TIF_EXT = ".tif";
    private static String TIFF_EXT = ".tiff";
    private static String JPG_EXT = ".jpg";
    private static String JPEG_EXT = ".jpeg";


    public List<ImageModel> getListOfImageModels(String pathToFolder){
        List<ImageModel> results = new ArrayList<>();
        for(File file : readImagesFilesInFolder(pathToFolder)){
            results.add(new ImageModel(file));
        }
        return results;
    }

    public List<File> readImagesFilesInFolder(String pathToFolder) {
        List<File> results = new ArrayList<>();
        try {
            Files.walk(Paths.get(pathToFolder)).forEach(filePath -> {
                if (Files.isRegularFile(filePath) && (filePath.toString().endsWith(TIF_EXT) ||
                        filePath.toString().endsWith(TIFF_EXT) || filePath.toString().endsWith(JPG_EXT) || filePath.toString().endsWith(JPEG_EXT))) {
                    log.info(filePath.getFileName().toFile().getName() + " was added to results");
                    results.add(filePath.toFile());
                }
            });
        } catch(IOException e) {
            log.error("IO exception" + e);
        }
        return results;
    }
}
