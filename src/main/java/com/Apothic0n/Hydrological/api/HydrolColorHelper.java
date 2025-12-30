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

    public static int tintFoliageOrGrass(int x, int y, int z) {
        float brighten = (float)((BRIGHTNESS_NOISE.getValue(x * 0.05, z * 0.01, false)*0.125f)+1f);
        float red = 0.2f*brighten;
        float green = 0.6f*brighten;
        float blue = 0f*brighten;
        return FastColor.ARGB32.colorFromFloat(1.f, red, green, blue);
    }
}
