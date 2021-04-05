package interfaces;

import java.awt.image.BufferedImage;
import utils.ImageList;

public interface ImageProcessor {
    public static boolean isDebug = false;
    public ImageList apply(BufferedImage inputBuffImg);
}
