package coolor.layout;

import coolor.models.ImageModel;

import java.util.List;

/**
 * Created by KAKolesnikov on 2015-08-19.
 */
public class MediaCanvas {
    private float width;
    private float height;
    private List<ImageModel> images;

    public MediaCanvas(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public MediaCanvas(float width) {
        this.width = width;
        this.height = 50000;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public List<ImageModel> getImages() {
        return images;
    }

    public void setImages(List<ImageModel> images) {
        this.images = images;
    }
}
