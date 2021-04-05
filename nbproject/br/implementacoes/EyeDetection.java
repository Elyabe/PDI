package implementacoes;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import interfaces.ImageProcessor;
import utils.ImageList;
import utils.ScalarNormalizer;
import utils.Util;
import utils.YCbCrColor;

public class EyeDetection implements ImageProcessor {
    private String path;

    public EyeDetection(String path){
        this.path = path;
    }
    
    @Override
    public ImageList apply(BufferedImage inputBuffImg) {
      
        EyeMap eyeMap = new EyeMap(this.path);
        
        ImageList eyeMapComponents = eyeMap.apply(inputBuffImg);
        
        OtsuBinarizer otsuBinarizer = new OtsuBinarizer();
        ImageList eyeDetectionComponents = otsuBinarizer.apply(eyeMapComponents.get("DilatedEyeMap"));

        if ( EyeDetection.isDebug ){
            eyeDetectionComponents.putAll(eyeMapComponents);
        } 
        
        eyeDetectionComponents.save(this.path);

        return eyeDetectionComponents;
    }

  
}
