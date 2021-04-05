package implementacoes;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import interfaces.ImageProcessor;
import utils.ImageList;
import utils.ScalarNormalizer;
import utils.Util;
import utils.YCbCrColor;

public class EyeMapL implements ImageProcessor {

    @Override
    public ImageList apply(BufferedImage inputBuffImg) {
        
        BufferedImage dilationBuffImg, erosionBuffImg, dilatedEyeMap;
        BufferedImage dilationDivErosionBuffImg = new BufferedImage(inputBuffImg.getWidth(), inputBuffImg.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        int imgWidth = inputBuffImg.getWidth(), imgHeight = inputBuffImg.getHeight();

        YCbCrColor yCbCrColorErosion, yCbCrColorDilation;
        YCbCrColorSpace yCbCrColorSpace = new YCbCrColorSpace();
        
        float dilationDivErosionValue;
        ScalarNormalizer scalarNormalizerBetween0_255 = new ScalarNormalizer(0, 1, 0, 255);
        
        StructuringElement structuringElement = new StructuringElement(3, "CIRCLE");
        GreyScaleDilation greyScaleDilation = new GreyScaleDilation(structuringElement);
        GreyScaleErosion greyScaleErosion = new GreyScaleErosion(structuringElement);

        dilationBuffImg = Util.cloneBufferedImage(inputBuffImg);
        erosionBuffImg = Util.cloneBufferedImage(inputBuffImg);

        for(int iteration = 1; iteration <= 2 ; iteration++)
        {
            dilationBuffImg = greyScaleDilation.apply(dilationBuffImg).get("Dilation");             
            erosionBuffImg = greyScaleErosion.apply(erosionBuffImg).get("Erosion");             
        }

        float[] divNotNormalized = new float[imgHeight*imgWidth];
        float maxDiv = 0;

        for(int y = 0; y < imgHeight; y++)
        {
            for(int x = 0; x < imgWidth; x++)
            {
                yCbCrColorDilation = new YCbCrColor(dilationBuffImg.getRGB(x, y));
                yCbCrColorErosion = new YCbCrColor(erosionBuffImg.getRGB(x, y));

                dilationDivErosionValue = (float)yCbCrColorDilation.getY() / ((float)yCbCrColorErosion.getY() + 1);

                divNotNormalized[x * imgHeight + y] = dilationDivErosionValue;
                maxDiv = Math.max(maxDiv, dilationDivErosionValue);

                int dilationDivErosion = Math.round(scalarNormalizerBetween0_255.run(dilationDivErosionValue));
                dilationDivErosionBuffImg.setRGB(x, y, yCbCrColorSpace.toRGB(dilationDivErosion));
                // System.out.println("Dilation " + yCbCrColorDilation.getY() + " Erosion " + yCbCrColorErosion.getY() + " dilationDivErosion " + dilationDivErosionValue + " " + dilationDivErosion);
            }
        }

        ScalarNormalizer scalarNormalizerDyn = new ScalarNormalizer(0, maxDiv, 0, 255);

        for(int y = 0; y < imgHeight; y++)
        {
            for(int x = 0; x < imgWidth; x++)
            {
                int dilationDivErosion = Math.round(scalarNormalizerDyn.run(divNotNormalized[x * imgHeight + y] ));
                dilationDivErosionBuffImg.setRGB(x, y, yCbCrColorSpace.toRGB(dilationDivErosion));
                // System.out.println(" dilationDivErosion " + dilationDivErosion);
            }
        }

        ImageList processedBuffImgs = new ImageList();
        processedBuffImgs.add("EyeMapL", dilationDivErosionBuffImg);
     
        if ( EyeMapL.isDebug ){
            processedBuffImgs.add("Dilation", dilationBuffImg);
            processedBuffImgs.add("Erosion", erosionBuffImg);
        }

        return processedBuffImgs;
    }
}
