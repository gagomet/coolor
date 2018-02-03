package coolor.generator;

import coolor.models.BlankImageModel;
import coolor.type.AtomicFloat;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class BlankImageGenerator {

    public void generateBlankImages(String pathToFolder, List<BlankImageModel> imagesInfo, TextArea logs, ProgressBar progressBar, Label processingLabel) {
        System.out.println("generator generates");
        float progressStep = 1f/imagesInfo.size();
        AtomicFloat atomicProgress = new AtomicFloat();

        imagesInfo.parallelStream().forEach(info -> {
            generateImage(pathToFolder, info, logs);
            float preProgress = atomicProgress.get();
            atomicProgress.set(preProgress + progressStep);
        });

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
