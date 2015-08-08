package coolor.fx;

import coolor.colorspaces.CMYK;
import coolor.colorspaces.Colorspace;
import coolor.colorspaces.Hex;
import coolor.colorspaces.RGB;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by KAKolesnikov on 2015-07-16.
 */
public class SpotColor {

    private String name;
    private CMYK cmyk;
    private RGB rgb;
    private Hex hex;

    public SpotColor(String name) {
        this.name = name;
    }

    public SpotColor(String name, Colorspace colorspace) {
        this.name = name;
        this.cmyk = (CMYK)colorspace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CMYK getCmyk() {
        return cmyk;
    }

    public void setCmyk(CMYK cmyk) {
        this.cmyk = cmyk;
    }

    public RGB getRgb() {
        return rgb;
    }

    public void setRgb(RGB rgb) {
        this.rgb = rgb;
    }

    public Hex getHex() {
        return hex;
    }

    public void setHex(Hex hex) {
        this.hex = hex;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Oracal  ");
        builder.append(this.getName());
        return builder.toString();
    }
}
