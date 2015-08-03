package coolor.fx;

import coolor.colorspaces.CMYK;
import coolor.colorspaces.Colorspace;
import coolor.colorspaces.Hex;
import coolor.colorspaces.RGB;

/**
 * Created by KAKolesnikov on 2015-07-16.
 */
public class SpotColor {
    private String name;
    private CMYK cmyk;
    private RGB rgb;
    private Hex hex;

    public SpotColor (String name){
        this.name = name;
    }

    public SpotColor(String name, Colorspace colorspace){

    }
}
