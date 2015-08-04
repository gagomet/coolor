package coolor;

import javafx.beans.property.*;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Created by KAKolesnikov on 2015-08-03.
 */
public class ImageModel {

    public static final Logger log = Logger.getLogger(ImageModel.class);
    private static float INCH_CM_COEF = 2.54f;

    private StringProperty name;
    private IntegerProperty quantity;
    private ObjectProperty colorspace;
    private FloatProperty width;
    private FloatProperty height;
    private FloatProperty area;
    private FloatProperty totalArea;
    private IntegerProperty densityWidth;
    private IntegerProperty densityHeight;

    public ImageModel(){
    }

    public ImageModel(File file, boolean checkbox) {
        try {
            name = new SimpleStringProperty(file.getName());
            ImageInfo imageInfo = Imaging.getImageInfo(file);
            if (checkbox) {
                quantity = new SimpleIntegerProperty(getFileQuantity(file.getName()));
            } else {
                quantity = new SimpleIntegerProperty(1);
            }
            colorspace = new SimpleObjectProperty<ImageInfo.ColorType>(imageInfo.getColorType());
            width = new SimpleFloatProperty(imageInfo.getPhysicalWidthInch() * INCH_CM_COEF);
            height = new SimpleFloatProperty(imageInfo.getPhysicalHeightInch() * INCH_CM_COEF);
            densityWidth = new SimpleIntegerProperty(imageInfo.getPhysicalWidthDpi());
            densityHeight = new SimpleIntegerProperty(imageInfo.getPhysicalWidthDpi());
            area = new SimpleFloatProperty((width.floatValue() * height.floatValue()) / 10000);
            totalArea = new SimpleFloatProperty(area.floatValue() * quantity.intValue());
        } catch(ImageReadException | IOException e) {
            log.error("Image reading exception" + e);
        }
    }

    private int getFileQuantity(String filename) {
        StringBuilder builder = new StringBuilder();
        if (Character.isDigit(filename.charAt(0))) {
            for (char ch : filename.toCharArray()) {
                if (Character.isDigit(ch)) {
                    builder.append(ch);
                } else {
                    break;
                }
            }
            return Integer.parseInt(builder.toString());
        }
        return 1;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public Object getColorspace() {
        return colorspace.get();
    }

    public ObjectProperty colorspaceProperty() {
        return colorspace;
    }

    public void setColorspace(Object colorspace) {
        this.colorspace.set(colorspace);
    }

    public float getWidth() {
        return width.get();
    }

    public FloatProperty widthProperty() {
        return width;
    }

    public void setWidth(float width) {
        this.width.set(width);
    }

    public float getHeight() {
        return height.get();
    }

    public FloatProperty heightProperty() {
        return height;
    }

    public void setHeight(float height) {
        this.height.set(height);
    }

    public float getArea() {
        return area.get();
    }

    public FloatProperty areaProperty() {
        return area;
    }

    public void setArea(float area) {
        this.area.set(area);
    }

    public float getTotalArea() {
        return totalArea.get();
    }

    public FloatProperty totalAreaProperty() {
        return totalArea;
    }

    public void setTotalArea(float totalArea) {
        this.totalArea.set(totalArea);
    }

    public int getDensityWidth() {
        return densityWidth.get();
    }

    public IntegerProperty densityWidthProperty() {
        return densityWidth;
    }

    public void setDensityWidth(int densityWidth) {
        this.densityWidth.set(densityWidth);
    }

    public int getDensityHeight() {
        return densityHeight.get();
    }

    public IntegerProperty densityHeightProperty() {
        return densityHeight;
    }

    public void setDensityHeight(int densityHeight) {
        this.densityHeight.set(densityHeight);
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .append("name", name)
                .append("quantity", quantity)
                .append("colorspace", colorspace)
                .append("width", width)
                .append("height", height)
                .append("area", area)
                .append("totalArea", totalArea)
                .append("densityWidth", densityWidth)
                .append("densityHeight", densityHeight)
                .toString();
    }
}
