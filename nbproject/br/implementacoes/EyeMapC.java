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
        
        BufferedImage cbSquareBuffImg = new BufferedImage(inputBuffImg.getWidth(), inputBuffImg.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage crNegativeSquareBuffImg = new BufferedImage(inputBuffImg.getWidth(), inputBuffImg.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage cbDivCrBuffImg = new BufferedImage(inputBuffImg.getWidth(), inputBuffImg.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage eyeMapCBuffImg = new BufferedImage(inputBuffImg.getWidth(), inputBuffImg.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        ScalarNormalizer scalarNormalizerBetween0_1 = new ScalarNormalizer(0, 255, 0, 1);
        ScalarNormalizer scalarNormalizerBetween0_255 = new ScalarNormalizer(0, 1, 0, 255);

		YCbCrColorSpace yCbCrColorSpace = new YCbCrColorSpace();

        for(int y = 0; y < inputBuffImg.getHeight(); y++)
		{
			for(int x = 0; x < inputBuffImg.getWidth(); x++)
			{
				YCbCrColor yCbCrColor = new YCbCrColor(inputBuffImg.getRGB(x, y));
               
                int cbChannel = yCbCrColor.getCb(), crChannel = yCbCrColor.getCr();
                
                float cbNormalized = scalarNormalizerBetween0_1.run((float)cbChannel);
                float crNormalized = scalarNormalizerBetween0_1.run((float)crChannel);


                float cbSquare = cbNormalized * cbNormalized;
                int cb = Math.round(scalarNormalizerBetween0_255.run(cbSquare));
                
                cbSquareBuffImg.setRGB(x, y, yCbCrColorSpace.toRGB(cb));
                
                float crNegativeSquare = (float)(Math.pow(1-crNormalized, 2));

                int cr = Math.round(scalarNormalizerBetween0_255.run(crNegativeSquare));
                
                crNegativeSquareBuffImg.setRGB(x, y, yCbCrColorSpace.toRGB(cr));

                int cbDivCr = 0;
                float cbDivCrNormalized;
                if ( crChannel != 0 ){
                    cbDivCrNormalized = (float)cbChannel / (float)crChannel;
                    cbDivCr = Math.round(scalarNormalizerBetween0_255.run(cbDivCrNormalized));
                } else {
                    cbDivCr = 255;
                    cbDivCrNormalized = 1;
                }

                cbDivCrBuffImg.setRGB(x, y, yCbCrColorSpace.toRGB(cbDivCr));
                
                
                
                float eyeMapNormalized = (cbSquare + crNegativeSquare + cbDivCrNormalized)/3;
                int eyeMapC = Math.round(scalarNormalizerBetween0_255.run(eyeMapNormalized));

                eyeMapCBuffImg.setRGB(x, y, yCbCrColorSpace.toRGB(eyeMapC));

			}
		}

        ImageList processedBuffImgs = new ImageList();

        processedBuffImgs.add("EyeMapC", eyeMapCBuffImg);
        processedBuffImgs.add("CbSquare", cbSquareBuffImg);
        processedBuffImgs.add("CrNegativeSquare", crNegativeSquareBuffImg);
        processedBuffImgs.add("CbDivCr", cbDivCrBuffImg);

        return processedBuffImgs;
    }
}