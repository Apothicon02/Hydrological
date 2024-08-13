package com.Apothic0n.Hydrological.api.biome.features.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;

public class AnvilRockConfiguration implements FeatureConfiguration {
    public static final Codec<AnvilRockConfiguration> CODEC = RecordCodecBuilder.create((fields) -> {
        return fields.group(RuleBasedBlockStateProvider.CODEC.fieldOf("material").forGetter(AnvilRockConfiguration::getMaterial
        ), IntProvider.codec(1, 3).fieldOf("radius").forGetter((v) -> {
            return v.radius;
        }), IntProvider.codec(3, 64).fieldOf("height").forGetter((v) -> {
            return v.height;
        }), IntProvider.codec(0, 16).fieldOf("stretch").forGetter((v) -> {
            return v.stretch;
        }), Codec.BOOL.fieldOf("forced").forGetter((v) -> {
            return v.forced;
        })).apply(fields, AnvilRockConfiguration::new);
    });

    public final RuleBasedBlockStateProvider material;
    private final IntProvider radius;
    private final IntProvider height;
    private final IntProvider stretch;
    private final Boolean forced;

    public AnvilRockConfiguration(RuleBasedBlockStateProvider material, IntProvider radius, IntProvider height, IntProvider stretch, Boolean forced) {
        this.material = material;
        this.radius = radius;
        this.height = height;
        this.stretch = stretch;
        this.forced = forced;
    }

    public RuleBasedBlockStateProvider getMaterial() {return this.material;}
    public IntProvider getRadius() {return this.radius;}
    public IntProvider getHeight() {return this.height;}
    public IntProvider getStretch() {return this.stretch;}
    public Boolean getForced() {return this.forced;}
}
