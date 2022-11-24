package com.jbmo60927.utilz;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.jbmo60927.App;

public class LoadSave {
    private LoadSave(){}

    public static final String LEVEL_ONE_DATA = "res/level_one_data.png";
    
    /**
     * load all image for the animations
     * @return 
     */
	public static BufferedImage getSpriteAtlas(String fileName) {
		BufferedImage img = null;
        final InputStream is = LoadSave.class.getResourceAsStream("/"+fileName); //path to images
		try {
			img = ImageIO.read(is); //read image
		} catch (final IOException e) {
			e.printStackTrace(); //exception
		} finally {
			try {
				is.close(); //close the input
			} catch (final IOException e) {
				e.printStackTrace(); //exception
			}
		}
        return img;
	}

    public static int[][] getLevelData() {
        int[][] lvlData = new int[App.TILE_IN_HEIGHT][App.TILE_IN_WIDTH];
        BufferedImage img = getSpriteAtlas(LoadSave.LEVEL_ONE_DATA);

        if (img != null)
            for (int j = 0; j < img.getHeight(); j++)
                for (int i = 0; i < img.getWidth(); i++) {
                    Color color = new Color(img.getRGB(i, j));
                    int value = color.getRed();
                    if (value >= 48)
                        value = 0;
                    lvlData[j][i] = value;
                }
        return lvlData;
    }
}
