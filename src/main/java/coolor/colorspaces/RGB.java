package coolor.colorspaces;

import coolor.type.ColorId;

import java.util.Objects;

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
        StringBuilder builder = new StringBuilder();
        builder.append("R:");
        builder.append(red);
        builder.append("  ");
        builder.append("G:");
        builder.append(green);
        builder.append("  ");
        builder.append("B:");
        builder.append(blue);
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        RGB rgb = (RGB) o;
        return red == rgb.red &&
                green == rgb.green &&
                blue == rgb.blue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue);
    }

    public ColorId spaceName() {
        return ColorId.RGB;
    }
}
