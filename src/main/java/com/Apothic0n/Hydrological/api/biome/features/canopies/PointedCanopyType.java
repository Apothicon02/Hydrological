package com.Apothic0n.Hydrological.api.biome.features.canopies;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.Map;

public class PointedCanopyType extends Canopy {
    public static final Codec<PointedCanopyType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (BlockStateProvider.CODEC.fieldOf("leaves")).forGetter(v -> v.leaves)
    ).apply(instance, PointedCanopyType::new));

    private final BlockStateProvider leaves;

    public PointedCanopyType(BlockStateProvider leaves) {
        this.leaves = leaves;
    }

    @Override
    protected CanopyType<?> type() {
        return CanopyType.POINTED_CANOPY_TYPE.get();
    }

    private BlockState getLeaves(RandomSource random, BlockPos pos) {
        return this.leaves.getState(random, pos);
    }

    private void addToMap(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random) {
        map.put(pos, getLeaves(random, pos));
    }

    @Override
    public Map<BlockPos, BlockState> generateCanopy(RandomSource random, BlockPos origin, int trunkHeight) {
        Map<BlockPos, BlockState> map = new java.util.HashMap<>(Map.of());

        origin = origin.below(5);
        addSquare(map, origin, random, 1, false);
        origin = origin.above();
        addSquare(map, origin, random, 2, false);
        origin = origin.above();
        addSquare(map, origin, random, 2, false);
        origin = origin.above();
        addSquare(map, origin, random, 1, true);
        addToMap(map, origin.north(2), random);
        addToMap(map, origin.east(2), random);
        addToMap(map, origin.south(2), random);
        addToMap(map, origin.west(2), random);
        origin = origin.above();
        addSquare(map, origin, random, 1, true);
        origin = origin.above();
        addSquare(map, origin, random, 1, false);
        origin = origin.above();
        addToMap(map, origin, random);
        origin = origin.above();
        addToMap(map, origin, random);

        return map;
    }

    private void addSquare(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random, int radius, boolean corners) {
        int minX = pos.getX()-radius;
        int maxX = pos.getX()+radius;
        int minZ = pos.getZ()-radius;
        int maxZ = pos.getZ()+radius;
        for (int x = pos.getX()-radius; x <= maxX; x++) {
            for (int z = pos.getZ()-radius; z <= maxZ; z++) {
                if (!((x == minX || x == maxX) && (z == minZ || z == maxZ)) || corners) {
                    addToMap(map, new BlockPos(x, pos.getY(), z), random);
                }
            }
        }
    }
}
