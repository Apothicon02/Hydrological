package com.Apothic0n.Hydrological.api.biome.features.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class RockConfiguration implements FeatureConfiguration {
    public static final Codec<RockConfiguration> CODEC = RecordCodecBuilder.create((fields) -> {
        return fields.group(BlockStateProvider.CODEC.fieldOf("to_place").forGetter((v) -> {
            return v.toPlace;
        }), BlockStateProvider.CODEC.fieldOf("filler").forGetter((v) -> {
            return v.filler;
        }), IntProvider.codec(1, 32).fieldOf("radius").forGetter((v) -> {
            return v.radius;
        })).apply(fields, RockConfiguration::new);
    });

    public final BlockStateProvider toPlace;
    public final BlockStateProvider filler;
    private final IntProvider radius;

    public RockConfiguration(BlockStateProvider toPlace, BlockStateProvider filler, IntProvider radius) {
        this.toPlace = toPlace;
        this.filler = filler;
        this.radius = radius;
    }

    public BlockStateProvider getToPlace() {return this.toPlace;}
    public BlockStateProvider getFiller() {return this.filler;}
    public IntProvider getRadius() {return this.radius;}

}
