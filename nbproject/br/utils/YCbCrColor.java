package utils;

import java.awt.Color;

public class YCbCrColor extends Color{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    public YCbCrColor(int i, int j, int k) {
        super(i,j,k);
    }

    public YCbCrColor(int rgb) {
        super(rgb);
    }

    public int getY(){
        return this.getRed();
    }

    public int getCb(){
        return this.getGreen();
    }

    public int getCr(){
        return this.getBlue();
    }
}
