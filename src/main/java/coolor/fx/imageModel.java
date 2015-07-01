package coolor.fx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.awt.image.BufferedImage;

/**
 * Created by KAKolesnikov on 2015-07-01.
 */
public class ImageModel {
    private final ObjectProperty<BufferedImage> image;

    public ImageModel(){
        this.image = null;
    }

    public ImageModel(BufferedImage image){
        this.image = new SimpleObjectProperty<BufferedImage>(image);
    }

    public BufferedImage getImage() {
        return image.get();
    }

    public ObjectProperty<BufferedImage> imageProperty() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image.set(image);
    }
}
