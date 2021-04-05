package implementacoes;

import java.awt.image.BufferedImage;

import interfaces.ImageProcessor;
import utils.ImageList;
import utils.ScalarNormalizer;
import utils.YCbCrColor;

public class EyeMapC implements ImageProcessor {

    @Override
    public ImageList apply(BufferedImage inputBuffImg) 
    {
        int imgWidth = inputBuffImg.getWidth(), imgHeight = inputBuffImg.getHeight();

        BufferedImage cbSquareBuffImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        BufferedImage crNegativeSquareBuffImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        BufferedImage cbDivCrBuffImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        BufferedImage eyeMapCBuffImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        
        ScalarNormalizer scalarNormalizerBetween0_1 = new ScalarNormalizer(0, 255, 0, 1);
        ScalarNormalizer scalarNormalizerBetween0_255 = new ScalarNormalizer(0, 1, 0, 255);

		YCbCrColorSpace yCbCrColorSpace = new YCbCrColorSpace();
        int lenght = imgHeight*imgWidth;

        float[] cbDivCrNotNormalized = new float[lenght];
        float[] cbSquareNormalized = new float[lenght];
        float[] crNegativeSquareNormalized = new float[lenght];
        
        float maxDiv = 0;

        for(int y = 0; y < imgHeight; y++)
		{
			for(int x = 0; x < imgWidth; x++)
			{
                int position = x * imgHeight + y;

				YCbCrColor yCbCrColor = new YCbCrColor(inputBuffImg.getRGB(x, y));
               
                int cbChannel = yCbCrColor.getCb(), crChannel = yCbCrColor.getCr();
                
                float cbNormalized = scalarNormalizerBetween0_1.run((float)cbChannel);
                float crNormalized = scalarNormalizerBetween0_1.run((float)crChannel);


                cbSquareNormalized[position] = cbNormalized * cbNormalized;
                crNegativeSquareNormalized[position] = (float)(Math.pow(1-crNormalized, 2));

                float cbDivCrValue = (float)cbChannel / ((float)crChannel + 1 );
                
                cbDivCrNotNormalized[position] = cbDivCrValue;
                maxDiv = Math.max(maxDiv, cbDivCrValue);

			}
		}

        ScalarNormalizer scalarNormalizerDynamic = new ScalarNormalizer(0, maxDiv, 0, 1);
        System.out.println("ScalarNormalizerDyn: [0," + maxDiv + "]");
        for(int y = 0; y < imgHeight; y++)
		{
			for(int x = 0; x < imgWidth; x++)
			{
                
                int position = x * imgHeight + y;

                int cb = Math.round(scalarNormalizerBetween0_255.run(cbSquareNormalized[position]));
                cbSquareBuffImg.setRGB(x, y, yCbCrColorSpace.toRGB(cb));
                
                
                int cr = Math.round(scalarNormalizerBetween0_255.run(crNegativeSquareNormalized[position]));
                crNegativeSquareBuffImg.setRGB(x, y, yCbCrColorSpace.toRGB(cr));
                
                // float cbDivCrNormalized = scalarNormalizerDynamic.run(cbDivCrNotNormalized[position]);
                float cbDivCrNormalized = Math.min(1, cbDivCrNotNormalized[position]);
                int cbDivCr = Math.round(scalarNormalizerBetween0_255.run(cbDivCrNormalized));
                cbDivCrBuffImg.setRGB(x, y, yCbCrColorSpace.toRGB(cbDivCr));
                
                
                float eyeMapNormalized = (cbSquareNormalized[position] + crNegativeSquareNormalized[position] + cbDivCrNormalized)/3;
                int eyeMapC = Math.round(scalarNormalizerBetween0_255.run(eyeMapNormalized));

                eyeMapCBuffImg.setRGB(x, y, yCbCrColorSpace.toRGB(eyeMapC));

			}
		}
        
        ImageList processedBuffImgs = new ImageList();

        processedBuffImgs.add("EyeMapC", eyeMapCBuffImg);
        
        if ( EyeMapC.isDebug ){
            processedBuffImgs.add("CbSquare", cbSquareBuffImg);
            processedBuffImgs.add("CrNegativeSquare", crNegativeSquareBuffImg);
            processedBuffImgs.add("CbDivCr", cbDivCrBuffImg);
        }

        return processedBuffImgs;
    }
}