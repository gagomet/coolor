package coolor.generator;

import coolor.models.BlankImageModel;
import javafx.scene.control.TextArea;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by KAKolesnikov on 2015-09-22.
 */
public class BlankImageGenerator {

    public void generateBlankImages(String pathToFolder, List<BlankImageModel> imagesInfo, TextArea logs) {
        System.out.println("generator generates");
        for (BlankImageModel blankImageModel : imagesInfo) {
            generateImage(pathToFolder, blankImageModel, logs);
        }
    }

    private void generateImage(String pathToFolder, BlankImageModel imageInfo, TextArea logs) {
        BufferedImage image = new BufferedImage(mmToPx(imageInfo.getWidth()), mmToPx(imageInfo.getHeight()), 1);
        String fullPath = pathToFolder + "\\" + composeFileName(imageInfo) + ".tif";
        File target = new File(fullPath);
        try {
            Imaging.writeImage(image, target, ImageFormats.TIFF, null);
            logs.appendText("File " + target.getAbsolutePath() + " created");
            logs.appendText("\n");
        } catch(ImageWriteException | IOException e) {
            e.printStackTrace();
        }
    }

    private int mmToPx(float mm) {
        Double pixels = (mm * 72) / 25.4;
        return pixels.intValue();
    }

    private String composeFileName(BlankImageModel imageModel) {
        StringBuilder builder = new StringBuilder();
        builder.append(imageModel.getCode());
        builder.append("_");
        builder.append(imageModel.getCity());
        builder.append("_");
        builder.append(imageModel.getName());
        builder.append("_");
        builder.append(imageModel.getWidth());
        builder.append("x");
        builder.append(imageModel.getHeight());
        builder.append("_");
        builder.append(imageModel.getMedia());
        if (imageModel.getLuvers() != 0) {
            builder.append("_luvers_");
            builder.append(imageModel.getLuvers());
        }
        return builder.toString();
    }
}
