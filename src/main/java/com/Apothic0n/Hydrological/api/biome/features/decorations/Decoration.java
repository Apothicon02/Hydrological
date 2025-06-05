package com.Apothic0n.Hydrological.api.biome.features.decorations;

import com.Apothic0n.Hydrological.api.biome.features.FeatureHelper;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public abstract class Decoration {
    public static final MapCodec<Decoration> CODEC = DecorationType.DECORATION_TYPE_REGISTRY.byNameCodec().dispatchMap(Decoration::type, DecorationType::codec);

    protected abstract DecorationType<?> type();

    public Map<BlockPos, BlockState> generateDecoration(RandomSource random, Map<BlockPos, BlockState> existing, BlockPos origin, WorldGenLevel level) {
        return new java.util.HashMap<>(Map.of());
    }
}
