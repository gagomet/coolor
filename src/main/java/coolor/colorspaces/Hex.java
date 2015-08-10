package coolor.colorspaces;

import coolor.type.ColorId;

/**
 * Created by Padonag on 24.09.2014.
 */
public class Hex implements Colorspace {
    private static final String EMPTY_STRING = "";

    private String hexRed;
    private String hexGreen;
    private String hexBlue;

    public Hex() {

    }

    public String getHexRed() {
        return hexRed;
    }

    public void setHexRed(String hexRed) {
        this.hexRed = hexRed;
    }

    public String getHexGreen() {
        return hexGreen;
    }

    public void setHexGreen(String hexGreen) {
        this.hexGreen = hexGreen;
    }

    public String getHexBlue() {
        return hexBlue;
    }

    public void setHexBlue(String hexBlue) {
        this.hexBlue = hexBlue;
    }

    @Override
    public String toString() {
        return "Hex color = #" + hexRed.substring(2) + hexGreen.substring(2) + hexBlue.substring(2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hex)) return false;

        Hex hex = (Hex) o;

        if (!hexBlue.equals(hex.hexBlue)) return false;
        if (!hexGreen.equals(hex.hexGreen)) return false;
        if (!hexRed.equals(hex.hexRed)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = hexRed.hashCode();
        result = 31 * result + hexGreen.hashCode();
        result = 31 * result + hexBlue.hashCode();
        return result;
    }

    public ColorId spaceName() {
        return ColorId.HEX;
    }
}
