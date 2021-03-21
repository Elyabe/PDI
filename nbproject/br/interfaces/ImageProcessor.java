package interfaces;

import java.awt.image.BufferedImage;
import utils.ImageList;

public interface ImageProcessor {
    public ImageList apply(BufferedImage inputBuffImg);
}
