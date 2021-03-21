package implementacoes;

import java.awt.image.BufferedImage;
import java.awt.Color;

import interfaces.ImageProcessor;
import utils.ImageList;
import utils.YCbCrColor;

public class YCbCrImageConverter implements ImageProcessor {

    @Override
    public ImageList apply(BufferedImage imageInput) {
        BufferedImage yBuffImg = new BufferedImage(imageInput.getWidth(), imageInput.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage CbBuffImg = new BufferedImage(imageInput.getWidth(), imageInput.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage CrBuffImg = new BufferedImage(imageInput.getWidth(), imageInput.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage yCbCrBuffImg = new BufferedImage(imageInput.getWidth(), imageInput.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		YCbCrColorSpace yCbCrColorSpace = new YCbCrColorSpace();

		for(int y = 0; y < imageInput.getHeight(); y++)
		{
			for(int x = 0; x < imageInput.getWidth(); x++)
			{
                Color rgbColor = new Color(imageInput.getRGB(x, y));
                
                YCbCrColor yCbCrColor = yCbCrColorSpace.fromRGB(rgbColor);
               
				yBuffImg.setRGB(x,y, yCbCrColorSpace.toRGB(yCbCrColor.getY()));
				CbBuffImg.setRGB(x,y, yCbCrColorSpace.toRGB(yCbCrColor.getCb()));
				CrBuffImg.setRGB(x,y, yCbCrColorSpace.toRGB(yCbCrColor.getCr()));
				
				yCbCrBuffImg.setRGB(x,y, yCbCrColor.getRGB());


			}
		}

		ImageList processedBuffImgs = new ImageList();
		
		processedBuffImgs.add("YCbCr", yCbCrBuffImg);
		processedBuffImgs.add("Y", yBuffImg);
		processedBuffImgs.add("Cb", CbBuffImg);
		processedBuffImgs.add("Cr", CrBuffImg);

        return processedBuffImgs;
    }
    
}
