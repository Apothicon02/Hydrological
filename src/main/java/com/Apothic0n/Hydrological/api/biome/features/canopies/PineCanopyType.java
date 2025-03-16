package com.Apothic0n.Hydrological.api.biome.features.canopies;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.Map;

public class PineCanopyType extends Canopy {
    public static final MapCodec<PineCanopyType> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            (BlockStateProvider.CODEC.fieldOf("leaves")).forGetter(v -> v.leaves)
    ).apply(instance, PineCanopyType::new));

    private final BlockStateProvider leaves;

    public PineCanopyType(BlockStateProvider leaves) {
        this.leaves = leaves;
    }

    @Override
    protected CanopyType<?> type() {
        return CanopyType.PINE_CANOPY_TYPE.get();
    }

    private BlockState getLeaves(RandomSource random, BlockPos pos) {
        return this.leaves.getState(random, pos);
    }

    private void addToMap(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random) {
        map.put(pos, getLeaves(random, pos));
    }

    private Direction getDir(Direction dir, int offset) {
        if (offset == 1) {
            if (dir == Direction.NORTH) {
                return Direction.EAST;
            } else if (dir == Direction.EAST) {
                return Direction.SOUTH;
            } else if (dir == Direction.SOUTH) {
                return Direction.WEST;
            } else if (dir == Direction.WEST) {
                return Direction.NORTH;
            }
        } else if (offset == 2) {
            if (dir == Direction.NORTH) {
                return Direction.SOUTH;
            } else if (dir == Direction.EAST) {
                return Direction.WEST;
            } else if (dir == Direction.SOUTH) {
                return Direction.NORTH;
            } else if (dir == Direction.WEST) {
                return Direction.EAST;
            }
        }
        return dir;
    }

    @Override
    public Map<BlockPos, BlockState> generateCanopy(RandomSource random, BlockPos origin, int trunkHeight, BlockPos treeOrigin) {
        Map<BlockPos, BlockState> map = new java.util.HashMap<>(Map.of());

        addToMap(map, origin, random);
        addToMap(map, origin.above(), random);
        addToMap(map, origin.above(2), random);
        int max = origin.getY();
        int min = max-trunkHeight+2;
        for (int y = max; y >= min; y -= 3) {
            int dirOffset = random.nextInt(0, 2);
            BlockPos pos = origin.atY(y);

            if (y <= min+trunkHeight/3 && trunkHeight > 10) {
                addLargeBranch(map, pos, random, getDir(Direction.NORTH, dirOffset));
                addLargeBranch(map, pos.above(random.nextInt(0, 1)), random, getDir(Direction.EAST, dirOffset));
                addLargeBranch(map, pos.above(random.nextInt(1, 2)), random, getDir(Direction.SOUTH, dirOffset));
                addLargeBranch(map, pos.above(random.nextInt(0, 1)), random, getDir(Direction.WEST, dirOffset));
            } else if (y <= max-trunkHeight/5) {
                addMediumBranch(map, pos, random, getDir(Direction.NORTH, dirOffset));
                addMediumBranch(map, pos.above(random.nextInt(0, 1)), random, getDir(Direction.EAST, dirOffset));
                addMediumBranch(map, pos.above(random.nextInt(1, 2)), random, getDir(Direction.SOUTH, dirOffset));
                addSmallBranch(map, pos.above(random.nextInt(0, 1)), random, getDir(Direction.WEST, dirOffset));
            } else {
                addSmallBranch(map, pos, random, getDir(Direction.NORTH, dirOffset));
                addSmallBranch(map, pos.above(random.nextInt(1, 2)), random, getDir(Direction.SOUTH, dirOffset));
            }
        }

        return map;
    }

    private void addLargeBranch(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random, Direction dir) {
        if (random.nextInt(0, 4) == 0) {
            addMediumBranch(map, pos, random, dir);
        } else {
            int x;
            int y = 0;
            int z;
            if (dir == Direction.NORTH) {
                x = 1;
                z = 1;
            } else if (dir == Direction.EAST) {
                x = 1;
                z = -1;
            } else if (dir == Direction.SOUTH) {
                x = -1;
                z = -1;
            } else {
                x = -1;
                z = 1;
            }
            addToMap(map, pos.offset(x, y, z), random);
            addToMap(map, pos.offset(0, y, z), random);
            addToMap(map, pos.offset(x, y, 0), random);
            addToMap(map, pos.offset(0, y, z * 2), random);
            addToMap(map, pos.offset(x * 2, y, 0), random);
            addToMap(map, pos.offset(x, y, z * 2), random);
            addToMap(map, pos.offset(x * 2, y, z), random);

            y--;

            addToMap(map, pos.offset(x, y, z), random);
            addToMap(map, pos.offset(0, y, z), random);
            addToMap(map, pos.offset(x, y, 0), random);
            addToMap(map, pos.offset(0, y, z * 2), random);
            addToMap(map, pos.offset(x * 2, y, 0), random);
            addToMap(map, pos.offset(x, y, z * 2), random);
            addToMap(map, pos.offset(x * 2, y, z), random);
            addToMap(map, pos.offset(x * 2, y, z * 2), random);
            addToMap(map, pos.offset(x, y, z * 3), random);
            addToMap(map, pos.offset(x * 3, y, z), random);
            addToMap(map, pos.offset(x * 2, y, z * 3), random);
            addToMap(map, pos.offset(x * 3, y, z * 2), random);

            y--;

            addToMap(map, pos.offset(x * 2, y, z * 3), random);
            addToMap(map, pos.offset(x * 3, y, z * 2), random);
            addToMap(map, pos.offset(x * 3, y, z * 3), random);

            y--;

            addToMap(map, pos.offset(x * 3, y, z * 3), random);
        }
    }

    private void addMediumBranch(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random, Direction dir) {
        if (random.nextInt(0, 4) == 0) {
            addLargeBranch(map, pos, random, dir);
        } else {
            int x;
            int y = 0;
            int z;
            if (dir == Direction.NORTH) {
                x = 1;
                z = 1;
            } else if (dir == Direction.EAST) {
                x = 1;
                z = -1;
            } else if (dir == Direction.SOUTH) {
                x = -1;
                z = -1;
            } else {
                x = -1;
                z = 1;
            }
            addToMap(map, pos.offset(x, y, z), random);
            addToMap(map, pos.offset(0, y, z), random);
            addToMap(map, pos.offset(x, y, 0), random);
            addToMap(map, pos.offset(0, y, z * 2), random);
            addToMap(map, pos.offset(x * 2, y, 0), random);
            addToMap(map, pos.offset(x, y, z * 2), random);
            addToMap(map, pos.offset(x * 2, y, z), random);

            y--;

            addToMap(map, pos.offset(x, y, z * 2), random);
            addToMap(map, pos.offset(x * 2, y, z), random);
            addToMap(map, pos.offset(x * 2, y, z * 2), random);

            y--;

            addToMap(map, pos.offset(x * 2, y, z * 2), random);
        }
    }

    private void addSmallBranch(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random, Direction dir) {
        int x;
        int y = 0;
        int z;
        if (dir == Direction.NORTH) {
            x = 1;
            z = 1;
        } else if (dir == Direction.EAST) {
            x = 1;
            z = -1;
        } else if (dir == Direction.SOUTH) {
            x = -1;
            z = -1;
        } else {
            x = -1;
            z = 1;
        }

        addToMap(map, pos.offset(x, y, 0), random);
        addToMap(map, pos.offset(0, y, z), random);
        addToMap(map, pos.offset(x, y, z), random);

        y--;

        addToMap(map, pos.offset(x, y, z), random);
    }
}
