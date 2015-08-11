package coolor.fx;

import coolor.colorspaces.CMYK;
import coolor.colorspaces.Colorspace;
import coolor.colorspaces.Hex;
import coolor.colorspaces.RGB;
import coolor.converter.ColorConverter;
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
        if (colorspace instanceof CMYK) {
            this.cmyk = (CMYK) colorspace;
            this.rgb = ColorConverter.getInstance().cmykToRgb(cmyk);
            this.hex = ColorConverter.getInstance().rgbToHex(rgb);
        } else if (colorspace instanceof RGB){
            this.rgb = (RGB) colorspace;
            this.cmyk = ColorConverter.getInstance().rgbToCmyk(rgb);
            this.hex = ColorConverter.getInstance().rgbToHex(rgb);
        } else if (colorspace instanceof Hex){
            this.hex = (Hex) colorspace;
            this.rgb = ColorConverter.getInstance().hexToRgb(hex);
            this.cmyk = ColorConverter.getInstance().rgbToCmyk(rgb);
        }
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
        return this.getName();
    }
}
