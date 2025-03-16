package com.Apothic0n.Hydrological.api.biome.features.canopies;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public abstract class Canopy {
    public static final MapCodec<Canopy> CODEC = CanopyType.CANOPY_TYPE_REGISTRY.byNameCodec().dispatchMap(Canopy::type, CanopyType::codec);

    protected abstract CanopyType<?> type();

    public Map<BlockPos, BlockState> generateCanopy(RandomSource random, BlockPos origin, int trunkHeight, BlockPos treeOrigin) {
        return new java.util.HashMap<>(Map.of());
    }
}
