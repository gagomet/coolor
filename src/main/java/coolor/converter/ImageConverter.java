package coolor.converter;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import org.apache.log4j.Logger;


import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by KAKolesnikov on 2015-06-24.
 */
public class ImageConverter {

    public static final Logger log = Logger.getLogger(ImageConverter.class);

    public static BufferedImage readCMYKImage(String pathToFile) throws IOException {
        Iterator readers = ImageIO.getImageReadersByFormatName("JPEG");
        ImageReader reader = null;
        while (readers.hasNext()) {
            reader = (ImageReader) readers.next();
            if (reader.canReadRaster()) {
                break;
            }
        }
        //Stream the image file (the original CMYK image)
        ImageInputStream input = ImageIO.createImageInputStream(new File(pathToFile));
        reader.setInput(input);

        //Read the image raster
        Raster raster = reader.readRaster(0, null);

        //Create a new RGB image
        BufferedImage bufferedImage = new BufferedImage(raster.getWidth(), raster.getHeight(),
                BufferedImage.TYPE_4BYTE_ABGR);

        //Fill the new image with the old raster
        bufferedImage.getRaster().setRect(raster);
        return bufferedImage;
    }

    public static void main(String[] args) {
        BufferedImage img = readRgbOrCmykImage("C:\\sample\\example_tiff_cmyk.jpg");
        System.out.println(img.getColorModel().getColorSpace().toString());
    }

    public static BufferedImage readRgbOrCmykImage(String pathTofile) {
        BufferedImage cmykImage = null;
        try {
            cmykImage = ImageIO.read(new File("C:\\sample\\example_cmyk.jpg"));
            System.out.println("hui");
        } catch(IIOException ex) {
            log.error(ex.getMessage() + " maybe image in CMYK colorspace");
            log.info("Trying to read with Raster as CMYK");
            try {
                cmykImage = readCMYKImage("C:\\sample\\example_cmyk.jpg");
                System.out.println(cmykImage.getColorModel().getColorSpace().toString());
            } catch(IOException e1) {
                log.error(e1);
            }
        } catch(IOException e) {
            log.error(e);
        }
        return cmykImage;
    }
}
