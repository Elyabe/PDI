package utils;

import java.awt.image.BufferedImage;

import java.awt.image.DataBufferInt;

public class Util {
    
    public static boolean coordinateIsValid(int x, int y, int width, int height){
        return  x >= 0 && x < width && y >= 0 && y < height; 
    }

    public static BufferedImage cloneBufferedImage(BufferedImage source){
        BufferedImage bi = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        int[] sourceData = ((DataBufferInt)source.getRaster().getDataBuffer()).getData();
        int[] biData = ((DataBufferInt)bi.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourceData, 0, biData, 0, sourceData.length);
        return bi;
    }
    
    public static double s(int i, int j, float s, float sigma){
        double R = Math.sqrt(i*i + j*j);
        double absSigma = Math.abs(sigma);

        if ( R <= absSigma ){
            return absSigma * ( Math.pow(Math.abs(1 - Math.pow(R/s, 2)), -0.5) - 1);
        } else {
            return -99999999999999.0;
        }
    }

    public static int fixOutLier(int value){
        return (int)Math.max(0, Math.min(value, 255));
    }
}
