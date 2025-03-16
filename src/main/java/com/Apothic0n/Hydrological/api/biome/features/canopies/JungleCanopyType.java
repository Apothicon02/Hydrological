package com.Apothic0n.Hydrological.api.biome.features.canopies;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.Map;

public class JungleCanopyType extends Canopy {
    public static final MapCodec<JungleCanopyType> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            (IntProvider.codec(1, 64).fieldOf("height")).forGetter(v -> v.height),
            (IntProvider.codec(1, 22).fieldOf("radius")).forGetter(v -> v.radius),
            (BlockStateProvider.CODEC.fieldOf("leaves")).forGetter(v -> v.leaves)
    ).apply(instance, JungleCanopyType::new));

    private final IntProvider height;
    private final IntProvider radius;
    private final BlockStateProvider leaves;

    public JungleCanopyType(IntProvider height, IntProvider radius, BlockStateProvider leaves) {
        this.height = height;
        this.radius = radius;
        this.leaves = leaves;
    }

    @Override
    protected CanopyType<?> type() {
        return CanopyType.JUNGLE_CANOPY_TYPE.get();
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
        int radius = this.radius.sample(random);
        addSquare(map, origin.above(), random, radius-1, false);
        for (int i = 0; i < height; i++) {
            addSquare(map, origin.below(i), random, radius, false);
        }
        BlockPos pos = origin.below(height);
        int minX = pos.getX()-radius;
        int maxX = pos.getX()+radius;
        int minZ = pos.getZ()-radius;
        int maxZ = pos.getZ()+radius;
        for (int x = pos.getX()-radius; x <= maxX; x++) {
            for (int z = pos.getZ() - radius; z <= maxZ; z++) {
                if (!((x == minX || x == maxX) && (z == minZ || z == maxZ)) && x != origin.getX() && z != origin.getZ() && (x == minX || x == maxX || z == minZ || z == maxZ)) {
                    addToMap(map, new BlockPos(x, pos.getY(), z), random);
                    if (random.nextInt(1, 10) < 4) {
                        addToMap(map, new BlockPos(x, pos.getY()-1, z), random);
                    }
                }
            }
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
