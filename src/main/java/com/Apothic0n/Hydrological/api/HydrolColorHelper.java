package com.Apothic0n.Hydrological.api;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.FastColor;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import org.spongepowered.asm.mixin.Unique;

public class HydrolColorHelper {
    @Unique
    public static final PerlinSimplexNoise BRIGHTNESS_NOISE = new PerlinSimplexNoise(new WorldgenRandom(new LegacyRandomSource(5432L)), ImmutableList.of(0));

    public static int tintFoliageOrGrass(int x, int y, int z) {
        float brighten = (float)((BRIGHTNESS_NOISE.getValue(x * 0.05, z * 0.01, false)*0.125f)+1f);
        float red = 0.2f*brighten;
        float green = 0.6f*brighten;
        float blue = 0f*brighten;
        return FastColor.ARGB32.color((int) 1.f, (int) (red*255), (int) (green*255), (int) (blue*255));
    }
}
