package com.Apothic0n.Hydrological.api.biome.features.trunks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StraightTrunkType extends Trunk {
    public static final Codec<StraightTrunkType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (IntProvider.codec(1, 64).fieldOf("height")).forGetter(v -> v.height),
            (BlockStateProvider.CODEC.fieldOf("wood")).forGetter(v -> v.wood)
    ).apply(instance, StraightTrunkType::new));

    private final IntProvider height;
    private final BlockStateProvider wood;

    public StraightTrunkType(IntProvider height, BlockStateProvider wood) {
        this.height = height;
        this.wood = wood;
    }

    @Override
    protected TrunkType<?> type() {
        return TrunkType.STRAIGHT_TRNUK_TYPE.get();
    }

    private BlockState getWood(RandomSource random, BlockPos pos) {
        return this.wood.getState(random, pos);
    }

    @Override
    public GeneratedTrunk generateTrunk(RandomSource random, BlockPos origin) {
        Map<BlockPos, BlockState> map = new java.util.HashMap<>(Map.of());
        Set<BlockPos> canopies = new HashSet<>();
        int maxHeight = this.height.sample(random);

        for (int i = 0; i <= maxHeight; i++) {
            BlockPos pos = origin.above(i);
            map.put(pos, getWood(random, pos));
            if (i == maxHeight) {
                canopies.add(pos.above());
            }
        }

        return new GeneratedTrunk(map, canopies, maxHeight);
    }
}
