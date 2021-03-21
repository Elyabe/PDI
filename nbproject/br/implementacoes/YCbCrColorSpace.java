package implementacoes;

import java.awt.Color;
import java.awt.color.ColorSpace;

import utils.YCbCrColor;

public class YCbCrColorSpace extends ColorSpace {

    public YCbCrColorSpace() {
        super(TYPE_YCbCr, 3);
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public float[] fromCIEXYZ(float[] colorvalue) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public float[] fromRGB(float[] rgbvalue) {
        float r = rgbvalue[0];
        float g = rgbvalue[1];
        float b = rgbvalue[2];

        float[] yCbCr = new float[3];
        float y, cb, cr;

        y  = (float)(0.299*r + 0.587*g + 0.114*b);
        cb = (float)(-0.168736*r - 0.331364*g + 0.5*b) + 128;
        cr = (float)(0.5*r - 0.418688*g - 0.081312*b) + 128;
    
        // y  = 16 + (float)(65.481*r + 128.553*g + 24.966*b);
        // cb = 128 + (float)(-37.797*r - 74.203*g + 112*b);
        // cr = 128 + (float)(112*r - 93.786*g - 18.214*b);

        yCbCr[1] = cb;
        yCbCr[2] = cr;
        yCbCr[0] = y;

        return yCbCr; 
    }

    public int[] fromRGB(int[] rgbvalue) {
        float convertion[] = this.fromRGB(new float[]{rgbvalue[0], rgbvalue[1], rgbvalue[2]}); 
        
        return new int[]{(int)convertion[0],(int)convertion[1], (int)convertion[2]};
    }

    public YCbCrColor fromRGB(Color color)
    {
        float[] rgb = new float[]{(float)color.getRed(), (float)color.getGreen(), (float)color.getBlue()};

        float[] yCbCr = this.fromRGB(rgb);
        
        YCbCrColor convertedColor = new YCbCrColor((int)yCbCr[0],(int)yCbCr[1],(int)yCbCr[2]);

        return convertedColor; 
    }

    @Override
    public float[] toCIEXYZ(float[] colorvalue) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public float[] toRGB(float[] colorvalue) {
        float y = colorvalue[0];
        float cb = colorvalue[1];
        float cr = colorvalue[2];

        float[] rgb = new float[3];
        float r, g, b;

        // r  = (float)(y + 1.402*cr)*255;
        // g = (float)(y - 0.344136*cb - 0.714136*cr) * 255;
        // b = (float)(y + 1.772*cb)* 255;
        
        // r  = (float)((298.082/256.0)*y + 408.583/256.0*cr- 222.921);
        // g = (float)((298.082/256.0)*y  - 100.291/256*cb - 208.120/256*cr + 135.576);
        // b = (float)((298.082/256.0)*y  + 516.412/256*cb - 276.836);
        
        r  = (float)(y + 1.402*(cr-128));
        g = (float)(y - 0.344136*(cb-128) - 0.714136*(cr-128));
        b = (float)(y + 1.772*(cb-128));
        

        // rgb[0] = Math.max(0, Math.min(255, r));
        // rgb[1] = Math.max(0, Math.min(255, g));
        // rgb[2] = Math.max(0, Math.min(255, b));

        rgb[0] = r;
        rgb[1] = g;
        rgb[2] = b;
        
        return rgb;
    }

    public int toRGB(int value){
        return ((value<<16)|(value<<8)|value);
    }

}
