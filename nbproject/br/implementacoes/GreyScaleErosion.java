package implementacoes;

import java.awt.image.BufferedImage;

import interfaces.ImageProcessor;

import utils.ImageList;
import utils.YCbCrColor;

import utils.Util;

public class GreyScaleErosion implements ImageProcessor {

    private StructuringElement  structuringElement;

    public GreyScaleErosion( StructuringElement structuringElement ){
        this.structuringElement = structuringElement;
    }

    @Override
    public ImageList apply(BufferedImage inputBuffImg) {
        // StructuringElement structuringElement = new StructuringElement(3, "CIRCLE");
        
        BufferedImage erosionBuffImg = new BufferedImage(inputBuffImg.getWidth(), inputBuffImg.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        int structElemSize = this.structuringElement.getSize() / 2;

        // System.out.println(structuringElement);
        
        int[][] kernel = structuringElement.getKernel();
        int i, j, imgWidth = inputBuffImg.getWidth(), imgHeight = inputBuffImg.getHeight(), maxValue, minValue;

        YCbCrColor yCbCrColor;
        YCbCrColorSpace yCbCrColorSpace = new YCbCrColorSpace();

        for(int y = 0; y < imgHeight; y++)
        {
            for(int x = 0; x < imgWidth; x++)
            {
                minValue = 255;
                for(int deltaX = -structElemSize; deltaX <= structElemSize; deltaX++ ){
                    for(int deltaY = -structElemSize; deltaY <= structElemSize; deltaY++ ){
                        i = x + deltaX;
                        j = y + deltaY;

                        
                        if ( Util.coordinateIsValid(i, j, imgWidth, imgHeight) ){
                            // if ( kernel[deltaX+structElemSize][deltaY+structElemSize] == 1 ){
                            //     yCbCrColor = new YCbCrColor(inputBuffImg.getRGB(i, j));
                            //     minValue = Math.min(minValue, yCbCrColor.getY());
                            // }
                            yCbCrColor = new YCbCrColor(inputBuffImg.getRGB(i, j));
                            
                            double sApplied = yCbCrColor.getY() + Util.s(deltaX + structElemSize, deltaY + structElemSize, (float)3, (float)3.0);
                            
                            minValue = Util.fixOutLier((int)Math.min(minValue, sApplied));
                        }
                    }
                }

                erosionBuffImg.setRGB(x, y, yCbCrColorSpace.toRGB(minValue));
            }
        }

        ImageList processedBuffImgs = new ImageList();
        processedBuffImgs.add("Erosion", erosionBuffImg);

        return processedBuffImgs;
    }
}
