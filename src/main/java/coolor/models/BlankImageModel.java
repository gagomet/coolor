package coolor.models;

import coolor.generator.BlankImageDto;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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
    protected IntegerProperty code;
    protected StringProperty name;
    protected StringProperty city;
    protected StringProperty media;
    protected IntegerProperty luvers;

    public BlankImageModel(){

    }

    public BlankImageModel(BlankImageDto dto){
        this.width = new SimpleFloatProperty(dto.getWidth());
        this.height = new SimpleFloatProperty(dto.getHeight());
        this.code = new SimpleIntegerProperty(dto.getCode());
        this.luvers = new SimpleIntegerProperty(dto.getLuvers());
        this.city = new SimpleStringProperty(dto.getCity());
        this.name = new SimpleStringProperty(dto.getName());
        this.media = new SimpleStringProperty(dto.getMedia());
    }

    public int getCode() {
        return code.get();
    }

    public IntegerProperty codeProperty() {
        return code;
    }

    public void setCode(int code) {
        this.code.set(code);
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

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getMedia() {
        return media.get();
    }

    public StringProperty mediaProperty() {
        return media;
    }

    public void setMedia(String media) {
        this.media.set(media);
    }

    public int getLuvers() {
        return luvers.get();
    }

    public IntegerProperty luversProperty() {
        return luvers;
    }

    public void setLuvers(int luvers) {
        this.luvers.set(luvers);
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
