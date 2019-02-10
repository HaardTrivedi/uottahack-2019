import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.io.*;
import java.awt.*;

public class getColour{
    BufferedImage image;
    int width;
    int height;
   
    public getColour(String img) {
        try {
            File input = new File(file);
            image = ImageIO.read(input);
            width = image.getWidth();
            height = image.getHeight();
            int count = 0;
        
            for(int i=0; i<width; i++) {
                for(int j=0; j<height; j++) {
                    count++;
                    Color colour = new Color(image.getRGB(j, i));
                    System.out.println("P: " + count + "  Red: " + colour.getRed() +"   Green: " + colour.getGreen() + "  Blue: " + colour.getBlue());
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
   
    public static void main(String args[]) throws Exception {
        getColour obj = new getColour();
    }
}
