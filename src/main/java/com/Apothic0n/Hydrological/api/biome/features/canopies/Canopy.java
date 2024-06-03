package com.Apothic0n.Hydrological.api.biome.features.canopies;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public abstract class Canopy {
    public static final Codec<Canopy> CODEC = CanopyType.CANOPY_TYPE_REGISTRY.get().getCodec().dispatch(Canopy::type, CanopyType::codec);

    protected abstract CanopyType<?> type();

    public Map<BlockPos, BlockState> generateCanopy(RandomSource random, BlockPos origin) {
        return new java.util.HashMap<>(Map.of());
    }
}
