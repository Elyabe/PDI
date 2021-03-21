package utils;

import java.util.HashMap;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ImageList {
    private HashMap<String, BufferedImage> imageList;
    
    public ImageList(){
        this.imageList = new HashMap<String, BufferedImage>();
    }

    public void add(String key, BufferedImage image){
        this.imageList.put(key, image);
    }

    public BufferedImage get(String key){
       return this.imageList.get(key);
    }

    public void save(String path){
        this.imageList.entrySet().forEach(entry -> {
            String output = path + "_" + entry.getKey() + ".png";
            File outputFile = new File(output);
            try {
                ImageIO.write(entry.getValue(), "png", outputFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
