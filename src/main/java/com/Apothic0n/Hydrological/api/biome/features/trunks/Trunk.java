package com.Apothic0n.Hydrological.api.biome.features.trunks;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;

import java.util.HashSet;
import java.util.Map;

public abstract class Trunk {

    public static final Codec<Trunk> CODEC = TrunkType.TRUNK_TYPE_REGISTRY.get().getCodec().dispatch(Trunk::type, TrunkType::codec);

    protected abstract TrunkType<?> type();

    public GeneratedTrunk generateTrunk(RandomSource random, BlockPos origin) {
        return new GeneratedTrunk(new java.util.HashMap<>(Map.of()), new HashSet<>());
    }
}
