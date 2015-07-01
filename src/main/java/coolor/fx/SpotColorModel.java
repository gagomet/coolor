package coolor.fx;

import coolor.colorspaces.CMYK;

/**
 * Created by KAKolesnikov on 2015-07-01.
 */
public class SpotColorModel {
    private String spotName;
    private CMYK cmykFormula;

    public SpotColorModel(){
        this.spotName = null;
        this.cmykFormula = null;
    }

    public SpotColorModel(CMYK cmyk){
        cmykFormula = cmyk;
        spotName = "Custom spot color";
    }

    public SpotColorModel(String spotName, CMYK cmyk){
        this.spotName = spotName;
        cmykFormula = cmyk;
    }

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public CMYK getCmykFormula() {
        return cmykFormula;
    }

    public void setCmykFormula(CMYK cmykFormula) {
        this.cmykFormula = cmykFormula;
    }
}
