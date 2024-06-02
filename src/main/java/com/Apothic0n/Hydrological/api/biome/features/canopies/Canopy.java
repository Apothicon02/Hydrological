package com.Apothic0n.Hydrological.api.biome.features.canopies;

import com.mojang.serialization.Codec;

public abstract class Canopy {
    public static final Codec<Canopy> CODEC = CanopyType.CANOPY_TYPE_REGISTRY.get().getCodec().dispatch(Canopy::type, CanopyType::codec);

    protected abstract CanopyType<?> type();
}
