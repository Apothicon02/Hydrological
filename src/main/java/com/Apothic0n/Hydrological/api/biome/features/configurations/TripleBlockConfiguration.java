package com.Apothic0n.Hydrological.api.biome.features.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class TripleBlockConfiguration implements FeatureConfiguration {
    public static final Codec<TripleBlockConfiguration> CODEC = RecordCodecBuilder.create((fields) -> {
        return fields.group(BlockStateProvider.CODEC.fieldOf("primary").forGetter((v) -> {
            return v.primary;
        }), BlockStateProvider.CODEC.fieldOf("secondary").forGetter((v) -> {
            return v.secondary;
        }), BlockStateProvider.CODEC.fieldOf("tertiary").forGetter((v) -> {
            return v.tertiary;
        }), TagKey.hashedCodec(Registries.BLOCK).fieldOf("covering").forGetter((v) -> {
            return v.covering;
        })).apply(fields, TripleBlockConfiguration::new);
    });

    public final BlockStateProvider primary;
    public final BlockStateProvider secondary;
    public final BlockStateProvider tertiary;
    public final TagKey<Block> covering;

    public TripleBlockConfiguration(BlockStateProvider primary, BlockStateProvider secondary, BlockStateProvider tertiary, TagKey<Block> covering) {
        this.primary = primary;
        this.secondary = secondary;
        this.tertiary = tertiary;
        this.covering = covering;
    }

    public BlockStateProvider primary() {
        return this.primary;
    }
    public BlockStateProvider secondary() {
        return this.secondary;
    }
    public BlockStateProvider tertiary() {
        return this.tertiary;
    }
    public TagKey<Block> covering() {
        return this.covering;
    }
}
