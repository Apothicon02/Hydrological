package com.Apothic0n.Hydrological.api.biome.features.canopies;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.Map;

public class FirCanopyType extends Canopy {
    public static final MapCodec<FirCanopyType> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            (BlockStateProvider.CODEC.fieldOf("leaves")).forGetter(v -> v.leaves)
    ).apply(instance, FirCanopyType::new));

    private final BlockStateProvider leaves;

    public FirCanopyType(BlockStateProvider leaves) {
        this.leaves = leaves;
    }

    @Override
    protected CanopyType<?> type() {
        return CanopyType.FIR_CANOPY_TYPE.get();
    }

    private BlockState getLeaves(RandomSource random, BlockPos pos) {
        return this.leaves.getState(random, pos);
    }

    private void addToMap(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random) {
        map.put(pos, getLeaves(random, pos));
    }

    @Override
    public Map<BlockPos, BlockState> generateCanopy(RandomSource random, BlockPos origin, int trunkHeight, BlockPos treeOrigin) {
        Map<BlockPos, BlockState> map = new java.util.HashMap<>(Map.of());
        origin = origin.above(2);
        addToMap(map, origin, random);
        origin = origin.below();
        addToMap(map, origin, random);
        origin = origin.below();
        addSquare(map, origin, random, 1, false);
        origin = origin.below();
        addSquare(map, origin, random, 1, true);
        origin = origin.below();
        addSquare(map, origin, random, 1, false);

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
