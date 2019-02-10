import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.io.*;
import java.awt.*;
import java.util.Random;

public class getColour{
    BufferedImage image;
    public int width;
    public int height;
   
    public getColour(String img) {
        try {
            File input = new File(img);
            image = ImageIO.read(input);
            width = image.getWidth();
            height = image.getHeight();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
	
	public Color[] getColours() {
		Color[] colours = null;
		try{
			colours = new Color[width * height];
            int count = 0;
        
            for(int i=0; i<width; i++) {
                for(int j=0; j<height; j++) {
                    count++;
                    Color colour = new Color(image.getRGB(i, j));
					colours[j * width + i] = colour;
                    //System.out.println("P: " + count + "  Red: " + colour.getRed() +"   Green: " + colour.getGreen() + "  Blue: " + colour.getBlue());
                }
            }
		} catch(Exception e){
			System.out.println(e);
		}
		return colours;
	}
	
	float getLuminance(Color color){
		int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        return 0.299f*r + 0.587f*g + 0.114f*b;
	}
	
	Point predictLaser(Color[] colors, int width, int height, Color targetColor, int colorThreshold) {
		Random rand = new Random();
		
		int peakLumX = -1;
		int peakLumY = -1;
		float peakLum = -1;
		
		for(int j = 0; j < height; j++) {
			for(int i = 0; i < width; i++) {
				Color color = colors[j * width + i];
				
				int squaredDifferences = (color.getRed() - targetColor.getRed()) * (color.getRed() - targetColor.getRed()) + (color.getGreen() - targetColor.getGreen()) * (color.getGreen() - targetColor.getGreen()) + (color.getBlue() - targetColor.getBlue()) * (color.getBlue() - targetColor.getBlue());
				
				if (squaredDifferences > colorThreshold) {
					continue;
				}
				
				int threshold = 175;
				float luminance = getLuminance(color);
				if (luminance < threshold) {
					continue;
				}
				
				int samplesTaken = 0;
				int samplesAccepted = 0;
				int totalSamples = (2*10+1) * (2*10+1) - (2*5 + 1) * (2 * 5 + 1);
				int desiredSamples = 10;
				
				int totalR = 0;
				int totalG = 0;
				int totalB = 0;
				
				for(int i2 = -10; i2 <= 10; i2++) {
					for(int j2 = -10; j2 <= 10; j2++) {
						if((i2 < -5 || i2 > 5) && (j2 < -5 || j2 > 5)) {
							int r = rand.nextInt(totalSamples - samplesTaken);
							if (r < (desiredSamples - samplesAccepted)) {
								Color c = colors[(j + j2) * width + (i + i2)];
								totalR += c.getRed();
								totalG += c.getGreen();
								totalB += c.getBlue();
								samplesAccepted++;
							}
							samplesTaken++;
						}
					}
				}
				
				totalR /= desiredSamples;
				totalG /= desiredSamples;
				totalB /= desiredSamples;
				
				squaredDifferences = (totalR - targetColor.getRed()) * (totalR - targetColor.getRed()) + (totalG - targetColor.getGreen()) * (totalG - targetColor.getGreen()) + (totalB - targetColor.getBlue()) * (totalB - targetColor.getBlue());
				if (squaredDifferences < colorThreshold * 10) {
					continue;
				}
				
				if(luminance > peakLum) {
					peakLum = luminance;
					peakLumX = i;
					peakLumY = j;
				}
			}
		}
		
		return (peakLumX == -1 && peakLumY == -1) ? null : new Point(peakLumX, peakLumY);
	}
   
    public static void main(String args[]) throws Exception {
        getColour obj = new getColour("test.png");
		Color[] colours = obj.getColours();
		Point p = obj.predictLaser(colours, obj.width, obj.height, new Color(57, 255, 239), 3 * 2*2);
		System.out.println(p);
    }
}
