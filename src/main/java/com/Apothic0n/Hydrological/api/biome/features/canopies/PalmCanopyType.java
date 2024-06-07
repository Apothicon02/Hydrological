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

    @Override
    public Map<BlockPos, BlockState> generateCanopy(RandomSource random, BlockPos origin, int trunkHeight, BlockPos treeOrigin) {
        Map<BlockPos, BlockState> map = new java.util.HashMap<>(Map.of());

        int x = origin.getX();
        int y = origin.getY();
        int z = origin.getZ();
        
        addToMap(map, new BlockPos(x, y, z), random);
        addToMap(map, new BlockPos(x+1, y, z), random);
        addToMap(map, new BlockPos(x-1, y, z), random);
        addToMap(map, new BlockPos(x, y, z+1), random);
        addToMap(map, new BlockPos(x, y, z-1), random);
        addToMap(map, new BlockPos(x+2, y, z), random);
        addToMap(map, new BlockPos(x-2, y, z), random);
        addToMap(map, new BlockPos(x, y, z+2), random);
        addToMap(map, new BlockPos(x, y, z-2), random);

        y--;

        addToMap(map, new BlockPos(x+1, y, z), random);
        addToMap(map, new BlockPos(x-1, y, z), random);
        addToMap(map, new BlockPos(x, y, z+1), random);
        addToMap(map, new BlockPos(x, y, z-1), random);
        addToMap(map, new BlockPos(x+2, y, z), random);
        addToMap(map, new BlockPos(x-2, y, z), random);
        addToMap(map, new BlockPos(x, y, z+2), random);
        addToMap(map, new BlockPos(x, y, z-2), random);
        addToMap(map, new BlockPos(x+3, y, z), random);
        addToMap(map, new BlockPos(x-3, y, z), random);
        addToMap(map, new BlockPos(x, y, z+3), random);
        addToMap(map, new BlockPos(x, y, z-3), random);
        addToMap(map, new BlockPos(x-1, y, z-1), random);
        addToMap(map, new BlockPos(x-1, y, z+1), random);
        addToMap(map, new BlockPos(x+1, y, z+1), random);
        addToMap(map, new BlockPos(x+1, y, z-1), random);
        addToMap(map, new BlockPos(x-2, y, z-1), random);
        addToMap(map, new BlockPos(x-2, y, z+1), random);
        addToMap(map, new BlockPos(x+2, y, z+1), random);
        addToMap(map, new BlockPos(x+2, y, z-1), random);
        addToMap(map, new BlockPos(x-1, y, z-2), random);
        addToMap(map, new BlockPos(x-1, y, z+2), random);
        addToMap(map, new BlockPos(x+1, y, z+2), random);
        addToMap(map, new BlockPos(x+1, y, z-2), random);

        y--;

        addToMap(map, new BlockPos(x-1, y, z-1), random);
        addToMap(map, new BlockPos(x-1, y, z+1), random);
        addToMap(map, new BlockPos(x+1, y, z+1), random);
        addToMap(map, new BlockPos(x+1, y, z-1), random);
        addToMap(map, new BlockPos(x-2, y, z), random);
        addToMap(map, new BlockPos(x-2, y, z-1), random);
        addToMap(map, new BlockPos(x-2, y, z+1), random);
        addToMap(map, new BlockPos(x+2, y, z), random);
        addToMap(map, new BlockPos(x+2, y, z-1), random);
        addToMap(map, new BlockPos(x+2, y, z+1), random);
        addToMap(map, new BlockPos(x, y, z-2), random);
        addToMap(map, new BlockPos(x-1, y, z-2), random);
        addToMap(map, new BlockPos(x+1, y, z-2), random);
        addToMap(map, new BlockPos(x, y, z+2), random);
        addToMap(map, new BlockPos(x-1, y, z+2), random);
        addToMap(map, new BlockPos(x+1, y, z+2), random);
        addToMap(map, new BlockPos(x-2, y, z-2), random);
        addToMap(map, new BlockPos(x-2, y, z+2), random);
        addToMap(map, new BlockPos(x+2, y, z+2), random);
        addToMap(map, new BlockPos(x+2, y, z-2), random);
        addToMap(map, new BlockPos(x, y, z+3), random);
        addToMap(map, new BlockPos(x, y, z+4), random);
        addToMap(map, new BlockPos(x, y, z-3), random);
        addToMap(map, new BlockPos(x, y, z-4), random);
        addToMap(map, new BlockPos(x+3, y, z), random);
        addToMap(map, new BlockPos(x+4, y, z), random);
        addToMap(map, new BlockPos(x-3, y, z), random);
        addToMap(map, new BlockPos(x-4, y, z), random);

        y--;

        addToMap(map, new BlockPos(x-1, y, z-1), random);
        addToMap(map, new BlockPos(x-1, y, z+1), random);
        addToMap(map, new BlockPos(x+1, y, z+1), random);
        addToMap(map, new BlockPos(x+1, y, z-1), random);
        addToMap(map, new BlockPos(x-2, y, z-2), random);
        addToMap(map, new BlockPos(x-2, y, z+2), random);
        addToMap(map, new BlockPos(x+2, y, z+2), random);
        addToMap(map, new BlockPos(x+2, y, z-2), random);
        addToMap(map, new BlockPos(x, y, z+2), random);
        addToMap(map, new BlockPos(x, y, z+3), random);
        addToMap(map, new BlockPos(x, y, z+4), random);
        addToMap(map, new BlockPos(x, y, z-2), random);
        addToMap(map, new BlockPos(x, y, z-3), random);
        addToMap(map, new BlockPos(x, y, z-4), random);
        addToMap(map, new BlockPos(x+2, y, z), random);
        addToMap(map, new BlockPos(x+3, y, z), random);
        addToMap(map, new BlockPos(x+4, y, z), random);
        addToMap(map, new BlockPos(x-2, y, z), random);
        addToMap(map, new BlockPos(x-3, y, z), random);
        addToMap(map, new BlockPos(x-4, y, z), random);

        y--;

        addToMap(map, new BlockPos(x-1, y, z-1), random);
        addToMap(map, new BlockPos(x-1, y, z+1), random);
        addToMap(map, new BlockPos(x+1, y, z+1), random);
        addToMap(map, new BlockPos(x+1, y, z-1), random);
        addToMap(map, new BlockPos(x-2, y, z-2), random);
        addToMap(map, new BlockPos(x-2, y, z+2), random);
        addToMap(map, new BlockPos(x+2, y, z+2), random);
        addToMap(map, new BlockPos(x+2, y, z-2), random);
        addToMap(map, new BlockPos(x+4, y, z), random);
        addToMap(map, new BlockPos(x-4, y, z), random);
        addToMap(map, new BlockPos(x, y, z+4), random);
        addToMap(map, new BlockPos(x, y, z-4), random);

        y--;

        addToMap(map, new BlockPos(x-2, y, z-2), random);
        addToMap(map, new BlockPos(x-2, y, z+2), random);
        addToMap(map, new BlockPos(x+2, y, z+2), random);
        addToMap(map, new BlockPos(x+2, y, z-2), random);

        return map;
    }
}
