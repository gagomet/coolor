package coolor.models;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;

/**
 * Created by KAKolesnikov on 2015-09-21.
 */
public class BlankImageModel {

    protected FloatProperty width;
    protected FloatProperty height;
    protected FloatProperty area;
    protected IntegerProperty densityWidth;
    protected IntegerProperty densityHeight;
    protected ObjectProperty colorspace;

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

    public Object getColorspace() {
        return colorspace.get();
    }

    public ObjectProperty colorspaceProperty() {
        return colorspace;
    }

    public void setColorspace(Object colorspace) {
        this.colorspace.set(colorspace);
    }
}
