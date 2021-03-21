package implementacoes;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import interfaces.ImageProcessor;
import utils.ImageList;
import utils.ScalarNormalizer;
import utils.YCbCrColor;

public class EyeMapL implements ImageProcessor {

    @Override
    public ImageList apply(BufferedImage inputBuffImg) {
        StructuringElement structuringElement = new StructuringElement(3, "CIRCLE");
        
        BufferedImage dilationBuffImg = new BufferedImage(inputBuffImg.getWidth(), inputBuffImg.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage erosionBuffImg = new BufferedImage(inputBuffImg.getWidth(), inputBuffImg.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage dilationDivErosionBuffImg = new BufferedImage(inputBuffImg.getWidth(), inputBuffImg.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        int structElemSize = structuringElement.getSize() / 2;

        System.out.println(structuringElement);
        
        int[][] kernel = structuringElement.getKernel();
        int i, j, imgWidth = inputBuffImg.getWidth(), imgHeight = inputBuffImg.getHeight(), maxValue, minValue;

        YCbCrColor yCbCrColorErosion, yCbCrColorDilation;
        YCbCrColorSpace yCbCrColorSpace = new YCbCrColorSpace();

        float dilationDivErosionValue;
        ScalarNormalizer scalarNormalizerBetween0_255 = new ScalarNormalizer(0, 1, 0, 255);

        BufferedImage inputDilationBuffImg, inputErosionBuffImg;
        
        inputDilationBuffImg = clone(inputBuffImg);
        inputErosionBuffImg = clone(inputBuffImg);

        for(int iteration = 1; iteration <= 1 ; iteration++)
        {
            for(int y = 0; y < imgHeight; y++)
            {
                for(int x = 0; x < imgWidth; x++)
                {
                    maxValue = 0;
                    minValue = 255;
                    for(int deltaX = -structElemSize; deltaX <= structElemSize; deltaX++ ){
                        for(int deltaY = -structElemSize; deltaY <= structElemSize; deltaY++ ){
                            i = x + deltaX;
                            j = y + deltaY;

                            
                            if ( coordinateIsValid(i, j, imgWidth, imgHeight) ){
                                // if ( kernel[deltaX+structElemSize][deltaY+structElemSize] == 1 ){
                                //     yCbCrColorDilation = new YCbCrColor(inputDilationBuffImg.getRGB(i, j));
                                //     yCbCrColorErosion = new YCbCrColor(inputErosionBuffImg.getRGB(i, j));
                                //     maxValue = Math.max(maxValue, yCbCrColorDilation.getY());
                                //     minValue = Math.min(minValue, yCbCrColorErosion.getY());
                                // }
                                yCbCrColorDilation = new YCbCrColor(inputBuffImg.getRGB(i, j));
                                
                                double sApplied = yCbCrColorDilation.getY() + this.s(deltaX + structElemSize, deltaY + structElemSize, (float)3, (float)3.0);
                                
                                // System.out.println(sApplied);

                                maxValue = this.fixOutLier((int)Math.max(maxValue, sApplied));
                                minValue = this.fixOutLier((int)Math.min(minValue, sApplied));
                            }
                        }
                    }


                    dilationBuffImg.setRGB(x, y, yCbCrColorSpace.toRGB(maxValue));
                    erosionBuffImg.setRGB(x, y, yCbCrColorSpace.toRGB(minValue));
                    
                    dilationDivErosionValue = (float)maxValue / (minValue + 1);

                    int dilationDivErosion = Math.round(scalarNormalizerBetween0_255.run(dilationDivErosionValue));
                    dilationDivErosionBuffImg.setRGB(x, y, yCbCrColorSpace.toRGB(dilationDivErosion));
                }
            }

            inputDilationBuffImg = clone(dilationBuffImg);
            inputErosionBuffImg = clone(erosionBuffImg);
    }

        ImageList processedBuffImgs = new ImageList();
        processedBuffImgs.add("Dilation", dilationBuffImg);
        processedBuffImgs.add("Erosion", erosionBuffImg);
        processedBuffImgs.add("EyeMapL", dilationDivErosionBuffImg);

        return processedBuffImgs;
    }

    public boolean coordinateIsValid(int x, int y, int width, int height){
        return  x >= 0 && x < width && y >= 0 && y < height; 
    }

    public static BufferedImage clone(BufferedImage source){
        BufferedImage bi = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        int[] sourceData = ((DataBufferInt)source.getRaster().getDataBuffer()).getData();
        int[] biData = ((DataBufferInt)bi.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourceData, 0, biData, 0, sourceData.length);
        return bi;
    }
    
    private double s(int i, int j, float s, float sigma){
        double R = Math.sqrt(i*i + j*j);
        double absSigma = Math.abs(sigma);

        if ( R <= absSigma ){
            return absSigma * ( Math.pow(Math.abs(1 - Math.pow(R/s, 2)), -0.5) - 1);
        } else {
            return -99999999999999.0;
        }
    }

    private int fixOutLier(int value){
        return (int)Math.max(0, Math.min(value, 255));
    }
}
