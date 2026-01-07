package com.Apothic0n.Hydrological.noise;

import com.Apothic0n.Hydrological.Hydrological;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Noises {
    public static Map<Integer, Noise> noiseMap = new HashMap<>(Map.of());
    public static Noise VARIATION_NOISE;
    public static Noise BASE_NOISE;
    public static Noise TEMPERATURE_NOISE;

    public static void init() throws IOException {
        VARIATION_NOISE = create(new Noise(loadImage("variation_noise")));
        BASE_NOISE = create(new Noise(loadImage("base_noise")));
        TEMPERATURE_NOISE = create(new Noise(loadImage("temperature_noise")));
    }

    private static Noise create(Noise type) {
        noiseMap.put(noiseMap.size(), type);
        return type;
    }

    private static BufferedImage loadImage(String name) throws IOException {
        return ImageIO.read(Hydrological.class.getClassLoader().getResourceAsStream("assets/hydrol/textures/noise/"+name+".png"));
    }
}