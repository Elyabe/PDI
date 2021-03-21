package utils;

import java.awt.image.BufferedImage;
import java.awt.Color;

import implementacoes.YCbCrColorSpace;

public class YCbCrSplitter {
    public BufferedImage[] apply(BufferedImage inputImage) {
        
        BufferedImage yComponent= new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage cbComponent = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage crComponent = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), BufferedImage.TYPE_INT_RGB);
	
        YCbCrColorSpace yCbCrColorSpace = new YCbCrColorSpace();

        for(int y = 0; y < inputImage.getHeight(); y++)
		{
			for(int x = 0; x < inputImage.getWidth(); x++)
			{
                YCbCrColor yCbCrColor = new YCbCrColor(inputImage.getRGB(x, y));
               
                int yChannel = yCbCrColor.getY(), cbChannel= yCbCrColor.getCb(), crChannel = yCbCrColor.getCr();
                
                int singleColorComponent;
                
                singleColorComponent = (yChannel<<16)|(yChannel<<8)|yChannel;
                yComponent.setRGB(x, y, singleColorComponent);

                singleColorComponent = (cbChannel<<16)|(cbChannel<<8)|cbChannel;
                cbComponent.setRGB(x, y, singleColorComponent);

                singleColorComponent = (crChannel<<16)|(crChannel<<8)|crChannel;
                crComponent.setRGB(x, y, singleColorComponent);
			}
		}

        return new BufferedImage[]{ yComponent, cbComponent, crComponent };

    }
}
