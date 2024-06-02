package com.Apothic0n.Hydrological.api.biome.features.trunks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class BendingTrunkType extends Trunk {
    public static final Codec<BendingTrunkType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (IntProvider.codec(1, 64).fieldOf("height")).forGetter(v -> v.height),
            (BlockStateProvider.CODEC.fieldOf("wood")).forGetter(v -> v.wood)
    ).apply(instance, BendingTrunkType::new));

    private final IntProvider height;
    private final BlockStateProvider wood;

    public BendingTrunkType(IntProvider height, BlockStateProvider wood) {
        this.height = height;
        this.wood = wood;
    }

    @Override
    protected TrunkType<?> type() {
        return TrunkType.BENDING_TRNUK_TYPE.get();
    }
}
