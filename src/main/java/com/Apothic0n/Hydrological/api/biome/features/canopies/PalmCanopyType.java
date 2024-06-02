package com.Apothic0n.Hydrological.api.biome.features.canopies;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class PalmCanopyType extends Canopy {
    public static final Codec<PalmCanopyType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (BlockStateProvider.CODEC.fieldOf("leaves")).forGetter(v -> v.leaves)
    ).apply(instance, PalmCanopyType::new));

    private final BlockStateProvider leaves;

    public PalmCanopyType(BlockStateProvider height) {
        this.leaves = height;
    }

    @Override
    protected CanopyType<?> type() {
        return CanopyType.PALM_CANOPY_TYPE.get();
    }
}
