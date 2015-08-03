package coolor.type;

/**
 * Created by KAKolesnikov on 2015-07-16.
 */
public enum ColorId {
    CMYK("CMYK"),
    RGB("RGB"),
    HEX("HEX");

    private String id;

    private ColorId(String id){
        this.id = id;
    }
    public String getId() {
        return id;
    }
}
