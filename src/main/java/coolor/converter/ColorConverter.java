package coolor.converter;


import coolor.colorspaces.CMYK;
import coolor.colorspaces.Hex;
import coolor.colorspaces.RGB;

import java.awt.Color;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Padonag on 24.09.2014.
 */
public class ColorConverter {

    private static final double MAX_RGB = 255d;
    private static final int MAX_CMYK = 100;
    private static final int HEX = 16;
    private static final char[] HEX_CHARACTERS
            = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    private ColorConverter() {

    }

    private static class Holder {

        private static final ColorConverter INSTANCE = new ColorConverter();
    }

    public static ColorConverter getInstance() {
        return Holder.INSTANCE;
    }

    public CMYK rgbToCmyk(RGB rgb) {
        double redCoef = rgb.getRed() / MAX_RGB;
        double greenCoef = rgb.getGreen() / MAX_RGB;
        double blueCoef = rgb.getBlue() / MAX_RGB;

        double K = 1 - maxDouble(redCoef, greenCoef, blueCoef);
        double C = (1 - redCoef - K) / (1 - K);
        double M = (1 - greenCoef - K) / (1 - K);
        double Y = (1 - blueCoef - K) / (1 - K);

        return new CMYK(Math.round(C * MAX_CMYK * 100) / 100, Math.round(M * MAX_CMYK * 100) / 100,
                Math.round(Y * MAX_CMYK * 100) / 100, Math.round(K * MAX_CMYK * 100) / 100);
    }

    public RGB cmykToRgb(CMYK cmyk) {

        double cyanCoef = cmyk.getCyan() / MAX_CMYK;
        double magentaCoef = cmyk.getMagenta() / MAX_CMYK;
        double yellowCoef = cmyk.getYellow() / MAX_CMYK;
        double blackCoef = cmyk.getBlack() / MAX_CMYK;

        int R = (int) (MAX_RGB * (1 - cyanCoef) * (1 - blackCoef));
        int G = (int) (MAX_RGB * (1 - magentaCoef) * (1 - blackCoef));
        int B = (int) (MAX_RGB * (1 - yellowCoef) * (1 - blackCoef));

        return new RGB(R, G, B);
    }

    public Hex rgbToHex(RGB rgb) {
        String hexR = checkToSingleNumber(Double.toHexString(rgb.getRed()));
        String hexG = checkToSingleNumber(Double.toHexString(rgb.getGreen()));
        String hexB = checkToSingleNumber(Double.toHexString(rgb.getBlue()));
        Hex hex = new Hex();
        hex.setHexRed(hexR);
        hex.setHexGreen(hexG);
        hex.setHexBlue(hexB);
        return hex;
    }

    public String rgbToHexString(RGB rgb) {
        Color color = new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
        return "Hex color is: #" + Integer.toHexString(color.getRGB()).substring(2);
    }

    public RGB hexToRgb(Hex hex) {
        int R = Integer.parseInt(hex.getHexRed(), HEX);
        int G = Integer.parseInt(hex.getHexGreen(), HEX);
        int B = Integer.parseInt(hex.getHexBlue(), HEX);

        return new RGB(R, G, B);
    }

    public CMYK hexToCmyk(Hex hex) {
        RGB tempRgb = hexToRgb(hex);
        return rgbToCmyk(tempRgb);
    }

    public Hex cmykToHex(CMYK cmyk) {
        RGB tempRgb = cmykToRgb(cmyk);
        return rgbToHex(tempRgb);
    }

    private double maxDouble(double... nums) {
        double max = Double.NEGATIVE_INFINITY;
        for (double current : nums) {
            if (current > max) {
                max = current;
            }
        }
        return max;
    }

    private String checkToSingleNumber(String input) {
        for (char i : HEX_CHARACTERS) {
            if (input.length() == 1 && input.charAt(0) == i) {
                return 0 + input;
            }
        }
        return input;
    }
}
