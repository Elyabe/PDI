package implementacoes;

import java.awt.image.BufferedImage;

import interfaces.ImageProcessor;
import utils.ImageList;
import utils.YCbCrColor;

public class OtsuBinarizer implements ImageProcessor {

    @Override
    public ImageList apply(BufferedImage inputBuffImg) {
        int imgWidth = inputBuffImg.getWidth(), imgHeight = inputBuffImg.getHeight();

        BufferedImage binarizedEyeMapBuffImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);

        double otsuThreshold = getOtsuThreshold(getGrayScale(inputBuffImg));

        YCbCrColorSpace yCbCrColorSpace = new YCbCrColorSpace();

        for(int y = 0; y < imgHeight; y++)
        {
            for(int x = 0; x < imgWidth; x++)
            {
                int binarizedValue = (new YCbCrColor(inputBuffImg.getRGB(x, y))).getY() > 127 ? 255 : 0;

                binarizedEyeMapBuffImg.setRGB(x, y, yCbCrColorSpace.toRGB(binarizedValue));
            }
        }

        ImageList processedBuffImgs = new ImageList();
        processedBuffImgs.add("BinarizedEyeMap", binarizedEyeMapBuffImg);

        return processedBuffImgs;
    }
    
    public int[] getGrayScale(BufferedImage inputBuffImg)
    {
        int imgWidth = inputBuffImg.getWidth(), imgHeight = inputBuffImg.getHeight();
        int length = imgWidth * imgHeight;

        int[] grayScaleValues =  new int[length];

        for(int y = 0; y < imgHeight; y++)
        {
            for(int x = 0; x < imgWidth; x++)
            {
                int position = x * imgHeight + y;

                grayScaleValues[position] =  (new YCbCrColor(inputBuffImg.getRGB(x, y))).getY();

            }
        }

        return grayScaleValues;
    }

    public double getOtsuThreshold(int[] grayScaleValues) {
    
        int[] n = getHistogram(grayScaleValues);
        double[] p = getProbabilities(n, grayScaleValues.length);
        double [] Wo = getWo(p);
        double W = getW(p);
        double [] W1 = getW1(Wo, W);
        double UT = getUT(p);
        double [] Ut = getUt(p);
        double [] Uo = getUo(Ut, Wo);
        double [] U1 = getU1(UT, Ut, Uo);
        double sigmaSqrT = getSigmaSqrT(UT,p);
        double [] sigmaSqrBt = getSigmaSqrBt(Wo, W1, U1, Uo);
        double [] eta = getEta(sigmaSqrBt,sigmaSqrT);
    
        System.out.println("MaxIndex " + getMaxIndex(eta));
        return getMaxIndex(eta);
    
    }
    
    private int[] getHistogram(int[] grayScaleValues) {
        int[] histogram = new int[256];
    
        for (int index = 0; index < grayScaleValues.length; index++) {
            histogram[grayScaleValues[index]]++;
        }
        return histogram;
    }
    
    private double[] getProbabilities(int[] histogram, int totalPixels) {
    
        double[] probability = new double[histogram.length];
    
        for (int index = 0; index < probability.length; index++) {
            probability[index] = ((double) histogram[index]) / ((double) totalPixels);
        }
    
        return probability;
    }
    
    private double[] getWo(double[] probability) {
    
        double[] Wo = new double[probability.length];
        Wo[0] = probability[0];
    
        for (int index = 1; index < Wo.length; index++) {
            Wo[index] = Wo[index - 1] + probability[index];
        }
    
        return Wo;
    }
    
    private double getW(double[] probability) {
    
        double W = 0;
    
        for (int index = 0; index < probability.length; index++) {
            W += probability[index];
        }
    
        return W;
    }
    
    private double[] getW1(double[] Wo, double W) {
    
        double[] W1 = new double[Wo.length];
    
        for (int index = 0; index < W1.length; index++) {
            W1[index] = W - Wo[index];
        }
    
        return W1;
    }
    
    private double getUT(double[] probability) {
    
        double UT = 0;
    
        for (int index = 0; index < probability.length; index++) {
            UT += (((double) index) * probability[index]);
        }
    
        return UT;
    
    }
    
    private double[] getUt(double[] probability) {
    
        double[] Ut = new double[probability.length];
    
        Ut[0] = 0;
        for (int index = 1; index < probability.length; index++) {
            Ut[index] = Ut[index - 1] + (((double) index) * probability[index]);
        }
    
        return Ut;
    }
    
    private double[] getUo(double[] Ut, double[] Wo) {
    
        double[] Uo = new double[Ut.length];
    
        for (int index = 0; index < Ut.length; index++) {
            if ( Wo[index] != 0 ){
                Uo[index] = Ut[index] / Wo[index];
            } else {
                Uo[index] = 0;
            }
        }
    
        return Uo;
    
    }
    
    private double[] getU1(double UT, double[] Ut, double[] Uo) {
    
        double[] U1 = new double[Ut.length];
    
        for (int index = 0; index < U1.length; index++) {
            U1[index] = (UT - Ut[index]) / (1 - Uo[index]);
        }
    
        return U1;
    
    }
    
    private double getSigmaSqrT(double UT, double[] probability) {
    
        double sigmaSqrT = 0;
    
        for (int index = 0; index < probability.length; index++) {
            sigmaSqrT += (Math.pow((index - UT), 2) * probability[index]);
        }
    
        return sigmaSqrT;
    
    }
    
    private double[] getSigmaSqrBt(double[] Wo, double[] W1, double[] U1, double[] Uo) {
        double sigmaSqrBt[] = new double[Wo.length];
    
        for (int index = 0; index < sigmaSqrBt.length; index++) {
            sigmaSqrBt[index] = Wo[index] * W1[index] * Math.pow((U1[index] - Uo[index]), 2);
        }
    
        return sigmaSqrBt;
    }
    
    private int getMaxIndex(double [] array){
    
        int maxIndex = 0;
        for(int i=0;i<array.length;i++){
            if(array[maxIndex]<array[i]){
            maxIndex=i;
            }
        }
        return maxIndex;
    
    }
    
    private double[] getEta(double[] sigmaSqrBt, double sigmaSqrT) {
        double eta[] = new double[sigmaSqrBt.length];
        for(int index= 0; index<sigmaSqrBt.length;index++){
            eta[index] = sigmaSqrBt[index]/sigmaSqrT;
        }
        return eta;
    }

}
