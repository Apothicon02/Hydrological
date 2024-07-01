package com.Apothic0n.Hydrological.api.biome.features.decorations;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public abstract class Decoration {
    public static final Codec<Decoration> CODEC = DecorationType.DECORATION_TYPE_REGISTRY.get().getCodec().dispatch(Decoration::type, DecorationType::codec);

    protected abstract DecorationType<?> type();

    public Map<BlockPos, BlockState> generateDecoration(RandomSource random, Map<BlockPos, BlockState> existing, BlockPos origin, WorldGenLevel level) {
        return new java.util.HashMap<>(Map.of());
    }
}
