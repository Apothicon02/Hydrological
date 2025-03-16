package com.Apothic0n.Hydrological.api.biome.features.trunks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class StraightDeadBranchedTrunkType extends Trunk {
    public static final MapCodec<StraightDeadBranchedTrunkType> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            (IntProvider.codec(1, 64).fieldOf("height")).forGetter(v -> v.height),
            (BlockStateProvider.CODEC.fieldOf("wood")).forGetter(v -> v.wood)
    ).apply(instance, StraightDeadBranchedTrunkType::new));

    private final IntProvider height;
    private final BlockStateProvider wood;

    public StraightDeadBranchedTrunkType(IntProvider height, BlockStateProvider wood) {
        this.height = height;
        this.wood = wood;
    }

    @Override
    protected TrunkType<?> type() {
        return TrunkType.STRAIGHT_DEAD_BRANCHED_TRNUK_TYPE.get();
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
            } else if (i > 2 && maxHeight > i+1) {
                int randomNumber = (int) (Math.random() * (30) + 1);
                if (randomNumber < 2) {
                    placeBranch(map, random, pos.north(), Direction.Axis.Z);
                } else if (randomNumber < 3) {
                    placeBranch(map, random, pos.east(), Direction.Axis.X);
                } else if (randomNumber < 4) {
                    placeBranch(map, random, pos.south(), Direction.Axis.Z);
                } else if (randomNumber < 5) {
                    placeBranch(map, random, pos.west(), Direction.Axis.X);
                }
            }
        }

        return new GeneratedTrunk(map, canopies, maxHeight);
    }

    private void placeBranch(Map<BlockPos, BlockState> map, RandomSource random, BlockPos pos, Direction.Axis axis) {
        if (!map.containsKey(pos) && !map.containsKey(pos.below())) {
            map.put(pos, getWood(random, pos).setValue(RotatedPillarBlock.AXIS, axis));
        }
    }
}
