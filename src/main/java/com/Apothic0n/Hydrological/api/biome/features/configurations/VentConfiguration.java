package com.Apothic0n.Hydrological.api.biome.features.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;

public class VentConfiguration implements FeatureConfiguration {
    public static final Codec<VentConfiguration> CODEC = RecordCodecBuilder.create((fields) -> {
        return fields.group(BlockStateProvider.CODEC.fieldOf("outer").forGetter((v) -> {
            return v.outer;
        }), BlockStateProvider.CODEC.fieldOf("rim").forGetter((v) -> {
            return v.rim;
        }), BlockStateProvider.CODEC.fieldOf("inner").forGetter((v) -> {
            return v.inner;
        }), IntProvider.codec(3, 64).fieldOf("height").forGetter((v) -> {
            return v.height;
        }), IntProvider.codec(3, 16).fieldOf("radius").forGetter((v) -> {
            return v.radius;
        }), IntProvider.codec(-10000, 10000).fieldOf("max_altitude").forGetter((v) -> {
            return v.maxAltitude;
        })).apply(fields, VentConfiguration::new);
    });

    public final BlockStateProvider outer;
    public final BlockStateProvider rim;
    private final BlockStateProvider inner;
    private final IntProvider height;
    private final IntProvider radius;
    private final IntProvider maxAltitude;

    public VentConfiguration(BlockStateProvider outer, BlockStateProvider rim, BlockStateProvider inner, IntProvider height, IntProvider radius, IntProvider maxAltitude) {
        this.outer = outer;
        this.rim = rim;
        this.inner = inner;
        this.height = height;
        this.radius = radius;
        this.maxAltitude = maxAltitude;
    }

    public BlockStateProvider getOuter() {return this.outer;}
    public BlockStateProvider getRim() {return this.rim;}
    public BlockStateProvider getInner() {return this.inner;}
    public IntProvider getHeight() {return this.height;}
    public IntProvider getRadius() {return this.radius;}
    public IntProvider getMaxAltitude() {return this.maxAltitude;}
}
