package com.Apothic0n.Hydrological.api.biome.features.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record ReplaceableBlockConfiguration(boolean replace, BlockStateProvider toPlace) implements FeatureConfiguration {
    public static final Codec<ReplaceableBlockConfiguration> CODEC = RecordCodecBuilder.create((p_191331_) -> {
        return p_191331_.group(Codec.BOOL.fieldOf("replace").forGetter((p_161168_) -> {
            return p_161168_.replace;
        }), BlockStateProvider.CODEC.fieldOf("to_place").forGetter((p_161168_) -> {
            return p_161168_.toPlace;
        })).apply(p_191331_, ReplaceableBlockConfiguration::new);
    });

    public ReplaceableBlockConfiguration(boolean replace, BlockStateProvider toPlace) {
        this.replace = replace;
        this.toPlace = toPlace;
    }

    public BlockStateProvider toPlace() {
        return this.toPlace;
    }
}