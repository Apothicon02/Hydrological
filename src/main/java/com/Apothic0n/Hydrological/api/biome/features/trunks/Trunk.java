package com.Apothic0n.Hydrological.api.biome.features.trunks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;

import java.util.HashSet;
import java.util.Map;

public abstract class Trunk {

    public static final MapCodec<Trunk> CODEC = TrunkType.TRUNK_TYPE_REGISTRY.byNameCodec().dispatchMap(Trunk::type, TrunkType::codec);

    protected abstract TrunkType<?> type();

    public GeneratedTrunk generateTrunk(RandomSource random, BlockPos origin) {
        return new GeneratedTrunk(new java.util.HashMap<>(Map.of()), new HashSet<>(), 0);
    }
}
