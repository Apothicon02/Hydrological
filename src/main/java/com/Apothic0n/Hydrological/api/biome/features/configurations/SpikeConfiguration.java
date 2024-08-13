package com.Apothic0n.Hydrological.api.biome.features.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class SpikeConfiguration implements FeatureConfiguration {
    public static final Codec<SpikeConfiguration> CODEC = RecordCodecBuilder.create((fields) -> {
        return fields.group(BlockStateProvider.CODEC.fieldOf("material").forGetter((v) -> {
            return v.material;
        }), IntProvider.codec(1, 1024).fieldOf("mass").forGetter((v) -> {
            return v.mass;
        }), IntProvider.codec(1, 32).fieldOf("width").forGetter((v) -> {
            return v.width;
        }), IntProvider.codec(1, 128).fieldOf("height").forGetter((v) -> {
            return v.height;
        })).apply(fields, SpikeConfiguration::new);
    });
    private final BlockStateProvider material;
    private final IntProvider mass;
    private final IntProvider width;
    private final IntProvider height;

    public SpikeConfiguration(BlockStateProvider material, IntProvider mass, IntProvider width, IntProvider height) {
        this.material = material;
        this.mass = mass;
        this.width = width;
        this.height = height;
    }

    public BlockStateProvider getMaterial() {return this.material;}
    public IntProvider getMass() {
        return this.mass;
    }
    public IntProvider getWidth() {
        return this.width;
    }
    public IntProvider getHeight() {
        return this.height;
    }
}
