package com.Apothic0n.Hydrological.api;

import com.Apothic0n.Hydrological.api.biome.features.placement_modifiers.NoiseCoverPlacement;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import org.spongepowered.asm.mixin.Unique;

public class HydrolColorHelper {
    @Unique
    public static final PerlinSimplexNoise BRIGHTNESS_NOISE = new PerlinSimplexNoise(new WorldgenRandom(new LegacyRandomSource(5432L)), ImmutableList.of(0));

    public static int tintFoliageOrGrass(int x, int y, int z, double temperature) {
        double brighten = Mth.clamp(BRIGHTNESS_NOISE.getValue(x * 0.05, z * 0.01, false), -0.25, 0.25)+0.5;
        float darken = (float) (Math.max(-0.45, Math.min(-0.4, temperature) + 0.4) / 2);
        float red = (float) (0.0);
        float green = (float) (0.55) + darken;
        float blue = (float) (0.33) + darken;
        float gray = (float) ((red+green+blue)/(3+brighten));
        float height = (384f/(y+64))*10;
        double saturate = -(Mth.clamp(NoiseCoverPlacement.HEIGHT_NOISE.getValue((x+height) * 0.05, (z+height) * 0.01, false) * 0.4, -0.4, 0.4)+0.5);
        red = (float) Mth.clamp(red + (gray - red) * saturate, 0.1, 1);
        green = (float) Mth.clamp(green + (gray - green) * saturate, 0.1, 1);
        blue = (float) Mth.clamp(blue + (gray - blue) * saturate, 0.1, 1);
        return FastColor.ARGB32.color(1, (int) (red*255), (int) (green*255), (int) (blue*255));
    }
}
