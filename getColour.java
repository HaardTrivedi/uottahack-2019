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
	
	private int getRed(int color) {
		return (color >> 16) & 0xFF;
	}
	
	private int getGreen(int color) {
		return (color >> 8) & 0xFF;
	}
	
	private int getBlue(int color) {
		return color & 0xFF;
	}
	
	public int[] getColours() {
		int[] rgbData = null;
		int colorRed;
		int colorGreen;
		int colorBlue;
		try{
			rgbData = new int[width * height];
			image.getRGB(0,0, width, height, rgbData, 0, width); 
		} catch(Exception e){
			System.out.println(e);
		}
		return rgbData;
	}
	
	float getLuminance(int color){
		int r = getRed(color);
        int g = getGreen(color);
        int b = getBlue(color);
        return 0.299f*r + 0.587f*g + 0.114f*b;
	}
	
	Point predictLaser(int[] colors, int width, int height, int targetColor, int colorThreshold) {
		Random rand = new Random();
		
		int peakLumX = -1;
		int peakLumY = -1;
		float peakLum = -1;
		
		for(int j = 0; j < height; j+=1) {
			for(int i = 0; i < width; i+=1) {
				int color = colors[j * width + i];
				
				int squaredDifferences = (getRed(color) - getRed(targetColor)) * (getRed(color) - getRed(targetColor)) + (getGreen(color) - getGreen(targetColor)) * (getGreen(color) - getGreen(targetColor)) + (getBlue(color) - getBlue(targetColor)) * (getBlue(color) - getBlue(targetColor));
				
				if (squaredDifferences > colorThreshold) {
					continue;
				}
				
				int threshold = 150;
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
								int c = colors[(j + j2) * width + (i + i2)];
								totalR += getRed(c);
								totalG += getGreen(c);
								totalB += getBlue(c);
								samplesAccepted++;
							}
							samplesTaken++;
						}
					}
				}
				
				totalR /= desiredSamples;
				totalG /= desiredSamples;
				totalB /= desiredSamples;
				
				squaredDifferences = (totalR - getRed(targetColor)) * (totalR - getRed(targetColor)) + (totalG - getGreen(targetColor)) * (totalG - getGreen(targetColor)) + (totalB - getBlue(targetColor)) * (totalB - getBlue(targetColor));
				if (squaredDifferences < colorThreshold * 1.5) {
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
		long startTime = System.currentTimeMillis();
        getColour obj = new getColour("test (1).png");
		System.out.println(System.currentTimeMillis() - startTime);
		startTime = System.currentTimeMillis();
		int[] colours = obj.getColours();
		System.out.println(System.currentTimeMillis() - startTime);
		startTime = System.currentTimeMillis();
		Point p = obj.predictLaser(colours, obj.width, obj.height, (57 << 16) | (255 << 8) | 239, 3 * 4*4);
		System.out.println(System.currentTimeMillis() - startTime);
		System.out.println(p);
    }
}
