package com.Apothic0n.Hydrological.api.biome.features.canopies;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.Map;

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

    private BlockState getLeaves(RandomSource random, BlockPos pos) {
        return this.leaves.getState(random, pos);
    }

    private void addToMap(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random) {
        map.put(pos, getLeaves(random, pos));
    }

    private void addToMap(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random, int trunkDist, BlockPos origin, BlockPos treeOrigin) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        int xDist = x-origin.getX();
        int zDist = z-origin.getZ();
        int dist = xDist*xDist+zDist*zDist;
        int xTreeDist = x-treeOrigin.getX();
        int zTreeDist = z-treeOrigin.getZ();
        int treeDist = xTreeDist * xTreeDist + zTreeDist * zTreeDist;
        BlockPos offsetPos = new BlockPos(x, y-(treeDist/52)-(dist/52)+(trunkDist/38), z);
        map.put(offsetPos, getLeaves(random, offsetPos));
    }

    private void createFrond(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random, int trunkDist, BlockPos origin, BlockPos treeOrigin, int northSouth, int eastWest) {
        addSquare(map, pos.north(northSouth).east(eastWest), random, 1, true, trunkDist, origin, treeOrigin);
        addSquare(map, pos.north(northSouth).east(eastWest).below(), random, 1, true, trunkDist, origin, treeOrigin);

        for (int i = 3; i <= 5; i++) {
            addSquare(map, pos.north(northSouth * i).east(eastWest * i), random, 0, true, trunkDist, origin, treeOrigin);
            addSquare(map, pos.north(northSouth * i).east(eastWest * i).below(), random, 0, true, trunkDist, origin, treeOrigin);
            addSquare(map, pos.north(northSouth * i).east(eastWest * i).below(2), random, 0, true, trunkDist, origin, treeOrigin);
        }

        addToMap(map, pos.north(northSouth*6).east(eastWest*6).below(), random, trunkDist, origin, treeOrigin);
        addToMap(map, pos.north(northSouth*6).east(eastWest*6).below(2), random, trunkDist, origin, treeOrigin);
        addToMap(map, pos.north(northSouth*6).east(eastWest*6).below(3), random, trunkDist, origin, treeOrigin);
        addToMap(map, pos.north(northSouth*6).east(eastWest*6).below(4), random, trunkDist, origin, treeOrigin);

        addToMap(map, pos.north(northSouth*7).east(eastWest*7).below(2), random, trunkDist, origin, treeOrigin);
        addToMap(map, pos.north(northSouth*7).east(eastWest*7).below(3), random, trunkDist, origin, treeOrigin);
        addToMap(map, pos.north(northSouth*7).east(eastWest*7).below(4), random, trunkDist, origin, treeOrigin);
        addToMap(map, pos.north(northSouth*7).east(eastWest*7).below(5), random, trunkDist, origin, treeOrigin);
        addToMap(map, pos.north(northSouth*7).east(eastWest*7).below(6), random, trunkDist, origin, treeOrigin);
    }

    @Override
    public Map<BlockPos, BlockState> generateCanopy(RandomSource random, BlockPos origin, int trunkHeight, BlockPos treeOrigin) {
        Map<BlockPos, BlockState> map = new java.util.HashMap<>(Map.of());

        if (origin.getY()-treeOrigin.getY() > 12) {
            addSquare(map, origin.below(), random, 1, true);
            addSquare(map, origin, random, 1, true);
            addSquare(map, origin.above(), random, 1, true);
            int xTrunkDist = origin.getX()-treeOrigin.getX();
            int zTrunkDist = origin.getZ()-treeOrigin.getZ();
            int trunkDist = xTrunkDist*xTrunkDist+zTrunkDist*zTrunkDist;
            createFrond(map, origin.below(random.nextInt(0, 2)), random, trunkDist, origin, treeOrigin, 1, 0);
            createFrond(map, origin.below(random.nextInt(0, 2)), random, trunkDist, origin, treeOrigin, 0, 1);
            createFrond(map, origin.below(random.nextInt(0, 2)), random, trunkDist, origin, treeOrigin, -1, 0);
            createFrond(map, origin.below(random.nextInt(0, 2)), random, trunkDist, origin, treeOrigin, 0, -1);
            createFrond(map, origin.below(random.nextInt(0, 2)), random, trunkDist, origin, treeOrigin, 1, 1);
            createFrond(map, origin.below(random.nextInt(0, 2)), random, trunkDist, origin, treeOrigin, -1, 1);
            createFrond(map, origin.below(random.nextInt(0, 2)), random, trunkDist, origin, treeOrigin, -1, -1);
            createFrond(map, origin.below(random.nextInt(0, 2)), random, trunkDist, origin, treeOrigin, 1, -1);
        } else {
            int x = origin.getX();
            int y = origin.getY();
            int z = origin.getZ();

            addToMap(map, new BlockPos(x, y, z), random);
            addToMap(map, new BlockPos(x + 1, y, z), random);
            addToMap(map, new BlockPos(x - 1, y, z), random);
            addToMap(map, new BlockPos(x, y, z + 1), random);
            addToMap(map, new BlockPos(x, y, z - 1), random);
            addToMap(map, new BlockPos(x + 2, y, z), random);
            addToMap(map, new BlockPos(x - 2, y, z), random);
            addToMap(map, new BlockPos(x, y, z + 2), random);
            addToMap(map, new BlockPos(x, y, z - 2), random);

            y--;

            addToMap(map, new BlockPos(x + 1, y, z), random);
            addToMap(map, new BlockPos(x - 1, y, z), random);
            addToMap(map, new BlockPos(x, y, z + 1), random);
            addToMap(map, new BlockPos(x, y, z - 1), random);
            addToMap(map, new BlockPos(x + 2, y, z), random);
            addToMap(map, new BlockPos(x - 2, y, z), random);
            addToMap(map, new BlockPos(x, y, z + 2), random);
            addToMap(map, new BlockPos(x, y, z - 2), random);
            addToMap(map, new BlockPos(x + 3, y, z), random);
            addToMap(map, new BlockPos(x - 3, y, z), random);
            addToMap(map, new BlockPos(x, y, z + 3), random);
            addToMap(map, new BlockPos(x, y, z - 3), random);
            addToMap(map, new BlockPos(x - 1, y, z - 1), random);
            addToMap(map, new BlockPos(x - 1, y, z + 1), random);
            addToMap(map, new BlockPos(x + 1, y, z + 1), random);
            addToMap(map, new BlockPos(x + 1, y, z - 1), random);
            addToMap(map, new BlockPos(x - 2, y, z - 1), random);
            addToMap(map, new BlockPos(x - 2, y, z + 1), random);
            addToMap(map, new BlockPos(x + 2, y, z + 1), random);
            addToMap(map, new BlockPos(x + 2, y, z - 1), random);
            addToMap(map, new BlockPos(x - 1, y, z - 2), random);
            addToMap(map, new BlockPos(x - 1, y, z + 2), random);
            addToMap(map, new BlockPos(x + 1, y, z + 2), random);
            addToMap(map, new BlockPos(x + 1, y, z - 2), random);

            y--;

            addToMap(map, new BlockPos(x - 1, y, z - 1), random);
            addToMap(map, new BlockPos(x - 1, y, z + 1), random);
            addToMap(map, new BlockPos(x + 1, y, z + 1), random);
            addToMap(map, new BlockPos(x + 1, y, z - 1), random);
            addToMap(map, new BlockPos(x - 2, y, z), random);
            addToMap(map, new BlockPos(x - 2, y, z - 1), random);
            addToMap(map, new BlockPos(x - 2, y, z + 1), random);
            addToMap(map, new BlockPos(x + 2, y, z), random);
            addToMap(map, new BlockPos(x + 2, y, z - 1), random);
            addToMap(map, new BlockPos(x + 2, y, z + 1), random);
            addToMap(map, new BlockPos(x, y, z - 2), random);
            addToMap(map, new BlockPos(x - 1, y, z - 2), random);
            addToMap(map, new BlockPos(x + 1, y, z - 2), random);
            addToMap(map, new BlockPos(x, y, z + 2), random);
            addToMap(map, new BlockPos(x - 1, y, z + 2), random);
            addToMap(map, new BlockPos(x + 1, y, z + 2), random);
            addToMap(map, new BlockPos(x - 2, y, z - 2), random);
            addToMap(map, new BlockPos(x - 2, y, z + 2), random);
            addToMap(map, new BlockPos(x + 2, y, z + 2), random);
            addToMap(map, new BlockPos(x + 2, y, z - 2), random);
            addToMap(map, new BlockPos(x, y, z + 3), random);
            addToMap(map, new BlockPos(x, y, z + 4), random);
            addToMap(map, new BlockPos(x, y, z - 3), random);
            addToMap(map, new BlockPos(x, y, z - 4), random);
            addToMap(map, new BlockPos(x + 3, y, z), random);
            addToMap(map, new BlockPos(x + 4, y, z), random);
            addToMap(map, new BlockPos(x - 3, y, z), random);
            addToMap(map, new BlockPos(x - 4, y, z), random);

            y--;

            addToMap(map, new BlockPos(x - 1, y, z - 1), random);
            addToMap(map, new BlockPos(x - 1, y, z + 1), random);
            addToMap(map, new BlockPos(x + 1, y, z + 1), random);
            addToMap(map, new BlockPos(x + 1, y, z - 1), random);
            addToMap(map, new BlockPos(x - 2, y, z - 2), random);
            addToMap(map, new BlockPos(x - 2, y, z + 2), random);
            addToMap(map, new BlockPos(x + 2, y, z + 2), random);
            addToMap(map, new BlockPos(x + 2, y, z - 2), random);
            addToMap(map, new BlockPos(x, y, z + 2), random);
            addToMap(map, new BlockPos(x, y, z + 3), random);
            addToMap(map, new BlockPos(x, y, z + 4), random);
            addToMap(map, new BlockPos(x, y, z - 2), random);
            addToMap(map, new BlockPos(x, y, z - 3), random);
            addToMap(map, new BlockPos(x, y, z - 4), random);
            addToMap(map, new BlockPos(x + 2, y, z), random);
            addToMap(map, new BlockPos(x + 3, y, z), random);
            addToMap(map, new BlockPos(x + 4, y, z), random);
            addToMap(map, new BlockPos(x - 2, y, z), random);
            addToMap(map, new BlockPos(x - 3, y, z), random);
            addToMap(map, new BlockPos(x - 4, y, z), random);

            y--;

            addToMap(map, new BlockPos(x - 1, y, z - 1), random);
            addToMap(map, new BlockPos(x - 1, y, z + 1), random);
            addToMap(map, new BlockPos(x + 1, y, z + 1), random);
            addToMap(map, new BlockPos(x + 1, y, z - 1), random);
            addToMap(map, new BlockPos(x - 2, y, z - 2), random);
            addToMap(map, new BlockPos(x - 2, y, z + 2), random);
            addToMap(map, new BlockPos(x + 2, y, z + 2), random);
            addToMap(map, new BlockPos(x + 2, y, z - 2), random);
            addToMap(map, new BlockPos(x + 4, y, z), random);
            addToMap(map, new BlockPos(x - 4, y, z), random);
            addToMap(map, new BlockPos(x, y, z + 4), random);
            addToMap(map, new BlockPos(x, y, z - 4), random);

            y--;

            addToMap(map, new BlockPos(x - 2, y, z - 2), random);
            addToMap(map, new BlockPos(x - 2, y, z + 2), random);
            addToMap(map, new BlockPos(x + 2, y, z + 2), random);
            addToMap(map, new BlockPos(x + 2, y, z - 2), random);
        }

        return map;
    }

    private void addSquare(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random, int radius, boolean corners, int trunkDist, BlockPos origin, BlockPos treeOrigin) {
        int minX = pos.getX()-radius;
        int maxX = pos.getX()+radius;
        int minZ = pos.getZ()-radius;
        int maxZ = pos.getZ()+radius;
        if (radius == 0) {
            maxX+=1;
            maxZ+=1;
        }
        for (int x = pos.getX()-radius; x <= maxX; x++) {
            for (int z = pos.getZ()-radius; z <= maxZ; z++) {
                if (!((x == minX || x == maxX) && (z == minZ || z == maxZ)) || corners) {
                    addToMap(map, new BlockPos(x, pos.getY(), z), random, trunkDist, origin, treeOrigin);
                }
            }
        }
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
