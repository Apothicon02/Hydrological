package com.Apothic0n.Hydrological.api.biome.features.trunks;

import com.mojang.serialization.Codec;

public abstract class Trunk {

    public static final Codec<Trunk> CODEC = TrunkType.TRUNK_TYPE_REGISTRY.get().getCodec().dispatch(Trunk::type, TrunkType::codec);

    protected abstract TrunkType<?> type();
}
