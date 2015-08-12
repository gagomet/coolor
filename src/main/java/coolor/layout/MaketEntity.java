package coolor.layout;

/**
 * Created by KAKolesnikov on 2015-08-12.
 */
public class MaketEntity implements Comparable<MaketEntity> {

    private double width;
    private double height;
    private int quantity;

    public MaketEntity(double width, double height) {
        this.height = height;
        this.width = width;
        this.quantity = 1;
    }

    public MaketEntity(double width, int quantity, double height) {
        this.width = width;
        this.quantity = quantity;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int compareTo(MaketEntity o) {
        if (this.height > o.height) {
            return 1;
        } else if (this.height < o.height) {
            return -1;
        }
        return 0;
    }
}
