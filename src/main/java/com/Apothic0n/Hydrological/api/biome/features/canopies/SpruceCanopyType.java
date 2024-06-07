package com.Apothic0n.Hydrological.api.biome.features.canopies;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.Map;

public class SpruceCanopyType extends Canopy {
    public static final Codec<SpruceCanopyType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (BlockStateProvider.CODEC.fieldOf("leaves")).forGetter(v -> v.leaves)
    ).apply(instance, SpruceCanopyType::new));

    private final BlockStateProvider leaves;

    public SpruceCanopyType(BlockStateProvider leaves) {
        this.leaves = leaves;
    }

    @Override
    protected CanopyType<?> type() {
        return CanopyType.SPRUCE_CANOPY_TYPE.get();
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
        float repeations = trunkHeight/8F;
        origin = origin.below((int) (trunkHeight-(repeations*2))+2);
        addSquare(map, origin, random, 1, true);
        for (float i = repeations; i > 0; i--) {
            origin = origin.above();
            addSquare(map, origin, random, 2, false);
        }

        for (float i = repeations; i > 0; i--) {
            origin = origin.above();
            addSquare(map, origin, random, 2, false);
        }

        for (float i = repeations; i > 0; i--) {
            origin = origin.above();
            addSquare(map, origin, random, 1, true);
            addPlus(map, origin, random, 2);
        }

        for (float i = repeations; i > 0; i--) {
            origin = origin.above();
            addSquare(map, origin, random, 1, true);
            addToMap(map, origin.north(2), random);
            addToMap(map, origin.east(2), random);
        }

        for (float i = repeations; i > 0; i--) {
            origin = origin.above();
            addSquare(map, origin, random, 1, false);
            addToMap(map, origin.north(2), random);
            addToMap(map, origin.east(2), random);
            addToMap(map, origin.north().east(), random);
            addToMap(map, origin.north().west(), random);
            addToMap(map, origin.south().east(), random);
        }

        for (float i = repeations; i > 0; i--) {
            origin = origin.above();
            addSquare(map, origin, random, 1, false);
            addToMap(map, origin.north().east(), random);
        }

        for (float i = repeations; i > 0; i--) {
            origin = origin.above();
            addToMap(map, origin, random);
            addToMap(map, origin.north(), random);
            addToMap(map, origin.east(), random);
        }

        for (float i = repeations; i > 0; i--) {
            origin = origin.above();
            addToMap(map, origin, random);
        }

        for (float i = repeations; i > 0; i--) {
            origin = origin.above();
            addToMap(map, origin, random);
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

    private void addPlus(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random, int radius) {
        addToMap(map, pos.north(radius), random);
        addToMap(map, pos.east(radius), random);
        addToMap(map, pos.south(radius), random);
        addToMap(map, pos.west(radius), random);
    }
}
