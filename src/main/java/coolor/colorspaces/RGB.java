package coolor.colorspaces;

import coolor.type.ColorId;

import java.util.Objects;

/**
 * Created by Padonag on 24.09.2014.
 */
public class RGB implements Colorspace{

    private double red;
    private double green;
    private double blue;

    public RGB() {

    }

    public RGB(double red, double green, double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public double getRed() {
        return red;
    }

    public void setRed(double red) {
        this.red = red;
    }

    public double getGreen() {
        return green;
    }

    public void setGreen(double green) {
        this.green = green;
    }

    public double getBlue() {
        return blue;
    }

    public void setBlue(double blue) {
        this.blue = blue;
    }

    @Override
    public String toString() {
        return "RGB color = " + red + " " + green + " " + blue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        RGB rgb = (RGB) o;
        return Double.compare(rgb.red, red) == 0 &&
                Double.compare(rgb.green, green) == 0 &&
                Double.compare(rgb.blue, blue) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue);
    }

    public ColorId spaceName() {
        return ColorId.RGB;
    }
}
