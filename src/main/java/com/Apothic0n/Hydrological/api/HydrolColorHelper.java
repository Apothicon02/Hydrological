package com.Apothic0n.Hydrological.api;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import org.spongepowered.asm.mixin.Unique;

public class HydrolColorHelper {
    @Unique
    private static final PerlinSimplexNoise SATURATION_NOISE = new PerlinSimplexNoise(new WorldgenRandom(new LegacyRandomSource(2345L)), ImmutableList.of(0));
    @Unique
    private static final PerlinSimplexNoise BRIGHTNESS_NOISE = new PerlinSimplexNoise(new WorldgenRandom(new LegacyRandomSource(5432L)), ImmutableList.of(0));

    public static int tintFoliageOrGrass(BlockState blockState, int x, int y, int z, double temperature, double humidity, boolean isFoliage) {
        double brighten = Mth.clamp(BRIGHTNESS_NOISE.getValue(x * 0.05, z * 0.01, false), -0.25, 0.25)+0.5;
        float red = (float) (0.0);
        float green = (float) (0.55);
        float blue = (float) (0.33);
        String blockName = blockState.getBlock().getDescriptionId();
        if (blockName.contains("dark")) {
            green -= 0.15F;
            blue += 0.07F;
        } else if (blockName.contains("mangrove")) {
            green -= 0.08F;
            blue -= 0.07F;
        } else if (blockName.contains("jungle")) {
            red += 0.3F;
            green += 0.125F;
            blue -= 0.035F;
        }
        float gray = (float) ((red+green+blue)/(3+brighten));
        float height = (384f/(y+64))*10;
        double saturate = -(Mth.clamp(SATURATION_NOISE.getValue((x+height) * 0.05, (z+height) * 0.01, false) * 0.4, -0.4, 0.4)+0.5);
        red = (float) Mth.clamp(red + (gray - red) * saturate, 0.1, 1);
        green = (float) Mth.clamp(green + (gray - green) * saturate, 0.1, 1);
        blue = (float) Mth.clamp(blue + (gray - blue) * saturate, 0.1, 1);
        return FastColor.ARGB32.color(1, (int) (red*255), (int) (green*255), (int) (blue*255));
    }
}
