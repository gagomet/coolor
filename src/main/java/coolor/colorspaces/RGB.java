package coolor.colorspaces;

import coolor.type.ColorId;

/**
 * Created by Padonag on 24.09.2014.
 */
public class RGB implements Colorspace{

    private int red;
    private int green;
    private int blue;

    public RGB() {

    }

    public RGB(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    @Override
    public String toString() {
        return "RGB color = " + red + " " + green + " " + blue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RGB)) return false;

        RGB rgb = (RGB) o;

        if (blue != rgb.blue) return false;
        if (green != rgb.green) return false;
        if (red != rgb.red) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = red;
        result = 31 * result + green;
        result = 31 * result + blue;
        return result;
    }

    public ColorId spaceName() {
        return ColorId.RGB;
    }
}
