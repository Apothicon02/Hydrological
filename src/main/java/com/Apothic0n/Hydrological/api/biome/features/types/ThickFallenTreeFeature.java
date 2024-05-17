package com.Apothic0n.Hydrological.api.biome.features.types;

import com.Apothic0n.Hydrological.api.biome.features.configurations.FallenTreeConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class ThickFallenTreeFeature extends Feature<FallenTreeConfiguration> {
    public ThickFallenTreeFeature(Codec<FallenTreeConfiguration> pContext) {
        super(pContext);
    }

    public boolean place(FeaturePlaceContext<FallenTreeConfiguration> pContext) {
        WorldGenLevel worldGenLevel = pContext.level();
        BlockPos blockpos = pContext.origin().offset(0, 1, 0);
        RandomSource random = pContext.random();
        FallenTreeConfiguration config = pContext.config();
        Block material = config.material.getBlock();
        Integer length = config.getLength().sample(random);
        if (worldGenLevel.isEmptyBlock(blockpos.below())) {
            return false;
        } else {
            int randomNumber = (int)(Math.random()*(2-1+1)+1);
            Direction.Axis axis = Direction.Axis.X;
            if (randomNumber < 2) {
                axis = Direction.Axis.Z;
                for (int l = 0; l < length; ++l) {
                    if (worldGenLevel.getBlockState(blockpos.offset(0, 1, l)).is(BlockTags.LOGS) || worldGenLevel.getBlockState(blockpos.offset(0, 0, l)).is(BlockTags.LOGS) ||
                            worldGenLevel.getBlockState(blockpos.offset(1, 0, l)).is(BlockTags.LOGS) || worldGenLevel.getBlockState(blockpos.offset(1, 1, l)).is(BlockTags.LOGS)) {
                        return false;
                    }
                    worldGenLevel.setBlock(blockpos.offset(0, 0, l), material.defaultBlockState().setValue(BlockStateProperties.AXIS, axis), 2);
                    worldGenLevel.setBlock(blockpos.offset(1, 0, l), material.defaultBlockState().setValue(BlockStateProperties.AXIS, axis), 2);
                    worldGenLevel.setBlock(blockpos.offset(0, 1, l), material.defaultBlockState().setValue(BlockStateProperties.AXIS, axis), 2);
                    worldGenLevel.setBlock(blockpos.offset(1, 1, l), material.defaultBlockState().setValue(BlockStateProperties.AXIS, axis), 2);
                    worldGenLevel.setBlock(blockpos.offset(0, -1, l), Blocks.PODZOL.defaultBlockState(), 2);
                    worldGenLevel.setBlock(blockpos.offset(1, -1, l), Blocks.PODZOL.defaultBlockState(), 2);
                    worldGenLevel.setBlock(blockpos.offset(0, -1, l), Blocks.PODZOL.defaultBlockState(), 2);
                    worldGenLevel.setBlock(blockpos.offset(1, -1, l), Blocks.PODZOL.defaultBlockState(), 2);
                }
            } else {
                for (int l = 0; l < length; ++l) {
                    if (worldGenLevel.getBlockState(blockpos.offset(l, 1, 0)).is(BlockTags.LOGS) || worldGenLevel.getBlockState(blockpos.offset(l, 0, 0)).is(BlockTags.LOGS) ||
                            worldGenLevel.getBlockState(blockpos.offset(l, 0, 1)).is(BlockTags.LOGS) || worldGenLevel.getBlockState(blockpos.offset(l, 1, 1)).is(BlockTags.LOGS)) {
                        return false;
                    }
                    worldGenLevel.setBlock(blockpos.offset(l, 0, 0), material.defaultBlockState().setValue(BlockStateProperties.AXIS, axis), 2);
                    worldGenLevel.setBlock(blockpos.offset(l, 0, 1), material.defaultBlockState().setValue(BlockStateProperties.AXIS, axis), 2);
                    worldGenLevel.setBlock(blockpos.offset(l, 1, 0), material.defaultBlockState().setValue(BlockStateProperties.AXIS, axis), 2);
                    worldGenLevel.setBlock(blockpos.offset(l, 1, 1), material.defaultBlockState().setValue(BlockStateProperties.AXIS, axis), 2);
                    worldGenLevel.setBlock(blockpos.offset(l, -1, 0), Blocks.PODZOL.defaultBlockState(), 2);
                    worldGenLevel.setBlock(blockpos.offset(l, -1, 1), Blocks.PODZOL.defaultBlockState(), 2);
                    worldGenLevel.setBlock(blockpos.offset(l, -1, 0), Blocks.PODZOL.defaultBlockState(), 2);
                    worldGenLevel.setBlock(blockpos.offset(l, -1, 1), Blocks.PODZOL.defaultBlockState(), 2);
                }
            }
            return true;
        }
    }
}
