package implementacoes;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import interfaces.ImageProcessor;
import utils.ImageList;
import utils.ScalarNormalizer;
import utils.YCbCrColor;

public class EyeMap implements ImageProcessor {
    private String path;

    public EyeMap(String path){
        this.path = path;
    }
    
    @Override
    public ImageList apply(BufferedImage inputBuffImg) {
      
        YCbCrImageConverter yCbCrImageConverter = new YCbCrImageConverter();
        ImageList yCbCrResults = yCbCrImageConverter.apply(inputBuffImg);
        
        yCbCrResults.save(this.path);

        BufferedImage yCbCrBuffImg = yCbCrResults.get("YCbCr");

        EyeMapC eyeMapC = new EyeMapC();
        ImageList eyeMapCComponents = eyeMapC.apply(yCbCrBuffImg);
        
        eyeMapCComponents.add("EyeMapCEqualized", this.equalize(eyeMapCComponents.get("EyeMapC")));
        eyeMapCComponents.save(this.path);
        
        EyeMapL eyeMapL = new EyeMapL();
        
        ImageList eyeMapLComponents = eyeMapL.apply(yCbCrResults.get("Y"));
        eyeMapLComponents.save(this.path);

        BufferedImage eyeMap = this.andOperator(eyeMapCComponents.get("EyeMapCEqualized"), eyeMapLComponents.get("EyeMapL"));

        ImageList eyeMapComponents = new ImageList();
        eyeMapComponents.add("EyeMap", eyeMap);

        return eyeMapComponents;
    }

    private BufferedImage andOperator(BufferedImage A, BufferedImage B){
        BufferedImage processedBuffImg = new BufferedImage(A.getWidth(), A.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        YCbCrColorSpace yCbCrColorSpace = new YCbCrColorSpace();

        for(int y = 0; y < A.getHeight(); y++)
		{
			for(int x = 0; x < A.getWidth(); x++)
			{
                YCbCrColor yCbcrColorImgA = new YCbCrColor(A.getRGB(x, y));
                YCbCrColor yCbcrColorImgB = new YCbCrColor(B.getRGB(x, y));
                
                ScalarNormalizer scalarNormalizerBetween0_16 = new ScalarNormalizer(0, 255, 0, 16);

                int ANormalizedColor = Math.round(scalarNormalizerBetween0_16.run((float)yCbcrColorImgA.getY()));
                int BNormalizedColor = Math.round(scalarNormalizerBetween0_16.run((float)yCbcrColorImgB.getY()));

                int AAndB = ANormalizedColor * BNormalizedColor;

                if ( AAndB == 256 ){
                    AAndB--;
                }

                processedBuffImg.setRGB(x, y, yCbCrColorSpace.toRGB(AAndB));
            }
        }

        return processedBuffImg;
    }

    private BufferedImage equalize(BufferedImage src){
        BufferedImage nImg = new BufferedImage(src.getWidth(), src.getHeight(),
                             BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster wr = src.getRaster();
        WritableRaster er = nImg.getRaster();
        int totpix= wr.getWidth()*wr.getHeight();
        int[] histogram = new int[256];
    
        for (int x = 0; x < wr.getWidth(); x++) {
            for (int y = 0; y < wr.getHeight(); y++) {
                histogram[wr.getSample(x, y, 0)]++;
            }
        }
    
        int[] chistogram = new int[256];
        chistogram[0] = histogram[0];
        for(int i=1;i<256;i++){
            chistogram[i] = chistogram[i-1] + histogram[i];
        }
    
        float[] arr = new float[256];
        for(int i=0;i<256;i++){
            arr[i] =  (float)((chistogram[i]*255.0)/(float)totpix);
        }
    
        for (int x = 0; x < wr.getWidth(); x++) {
            for (int y = 0; y < wr.getHeight(); y++) {
                int nVal = (int) arr[wr.getSample(x, y, 0)];
                er.setSample(x, y, 0, nVal);
            }
        }
        nImg.setData(er);
        return nImg;
    }
}
