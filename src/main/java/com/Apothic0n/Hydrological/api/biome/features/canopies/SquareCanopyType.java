package com.Apothic0n.Hydrological.api.biome.features.canopies;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.Map;

public class SquareCanopyType extends Canopy {
    public static final Codec<SquareCanopyType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (IntProvider.codec(1, 64).fieldOf("height")).forGetter(v -> v.height),
            (IntProvider.codec(1, 22).fieldOf("radius")).forGetter(v -> v.radius),
            (BlockStateProvider.CODEC.fieldOf("leaves")).forGetter(v -> v.leaves)
    ).apply(instance, SquareCanopyType::new));

    private final IntProvider height;
    private final IntProvider radius;
    private final BlockStateProvider leaves;

    public SquareCanopyType(IntProvider height, IntProvider radius, BlockStateProvider leaves) {
        this.height = height;
        this.radius = radius;
        this.leaves = leaves;
    }

    @Override
    protected CanopyType<?> type() {
        return CanopyType.SQUARE_CANOPY_TYPE.get();
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
        int height = this.height.sample(random);
        int maxRadius = this.radius.sample(random);
        origin = origin.below(height-1);
        for (int i = 0; i <= height; i++) {
            int radius = maxRadius;
            if ((i == 0 && height > 2) || ((i == height && height < 5)) || (i == height-1 && height >= 5)) {
                radius--;
            } else if (height >= 5 && i == height) {
                radius -= 2;
            }
            addSquare(map, origin.above(i), random, radius, false);
        }

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
