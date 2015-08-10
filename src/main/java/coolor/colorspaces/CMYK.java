package coolor.colorspaces;

import coolor.type.ColorId;

/**
 * Created by Padonag on 24.09.2014.
 */
public class CMYK implements Colorspace {
    private double cyan;
    private double magenta;
    private double yellow;
    private double black;

    public CMYK() {

    }

    public CMYK(double cyan, double magenta, double yellow, double black) {
        this.cyan = cyan;
        this.magenta = magenta;
        this.yellow = yellow;
        this.black = black;
    }

    public CMYK getRoundedCmyk(){
        CMYK roundedCmyk = new CMYK();
        roundedCmyk.setCyan(getRoundedChannel(5L, cyan));
        roundedCmyk.setMagenta(getRoundedChannel(5L, magenta));
        roundedCmyk.setYellow(getRoundedChannel(5L, yellow));
        roundedCmyk.setBlack(getRoundedChannel(5L, black));
        return roundedCmyk;
    }

    public double getCyan() {
        return cyan;
    }

    public void setCyan(double cyan) {
        this.cyan = cyan;
    }

    public double getMagenta() {
        return magenta;
    }

    public void setMagenta(double magenta) {
        this.magenta = magenta;
    }

    public double getYellow() {
        return yellow;
    }

    public void setYellow(double yellow) {
        this.yellow = yellow;
    }

    public double getBlack() {
        return black;
    }

    public void setBlack(double black) {
        this.black = black;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("C:");
        builder.append(cyan);
        builder.append("  ");
        builder.append("M:");
        builder.append(magenta);
        builder.append("  ");
        builder.append("Y:");
        builder.append(yellow);
        builder.append("  ");
        builder.append("K:");
        builder.append(black);
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CMYK)) return false;

        CMYK cmyk = (CMYK) o;

        if (Double.compare(cmyk.black, black) != 0) return false;
        if (Double.compare(cmyk.cyan, cyan) != 0) return false;
        if (Double.compare(cmyk.magenta, magenta) != 0) return false;
        if (Double.compare(cmyk.yellow, yellow) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(cyan);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(magenta);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(yellow);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(black);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public ColorId spaceName() {
        return ColorId.CMYK;
    }

    private Long getRoundedChannel(Long roundTo, double channelValue){
        return roundTo*(Math.round(channelValue)/roundTo);
    }
}
