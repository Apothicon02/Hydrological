package com.Apothic0n.Hydrological.api.biome.features;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public class FeatureHelper {
    public static BlockState getBlockState(WorldGenLevel level, BlockPos pos) {
        if (pos.getY() < level.getMaxY() && pos.getY() > level.getMinY()) {
            return level.getBlockState(pos);
        } else {
            return Blocks.VOID_AIR.defaultBlockState();
        }
    }

    public static void setBlock(WorldGenLevel level, BlockPos pos, BlockState state, int update) {
        if (pos.getY() < level.getMaxY() && pos.getY() > level.getMinY()) {
            level.setBlock(pos, state, update);
        }
    }
}
