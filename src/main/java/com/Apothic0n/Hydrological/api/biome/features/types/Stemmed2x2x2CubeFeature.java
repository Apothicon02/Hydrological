package com.Apothic0n.Hydrological.api.biome.features.types;

import com.Apothic0n.Hydrological.api.biome.features.FeatureHelper;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;

public class Stemmed2x2x2CubeFeature extends Feature<SimpleBlockConfiguration> {
    public Stemmed2x2x2CubeFeature(Codec<SimpleBlockConfiguration> pContext) {
        super(pContext);
    }

    public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> pContext) {
        WorldGenLevel level = pContext.level();
        BlockPos blockpos = pContext.origin();
        RandomSource random = pContext.random();
        BlockState material = pContext.config().toPlace().getState(random, blockpos);
        if (level.isEmptyBlock(blockpos.below())) {
            return false;
        } else {
            if (!FeatureHelper.getBlockState(level, blockpos.offset(0, 0, 0)).canBeReplaced() || !FeatureHelper.getBlockState(level, blockpos.offset(1, 0, 0)).canBeReplaced() ||
                    !FeatureHelper.getBlockState(level, blockpos.offset(0, 0, 1)).canBeReplaced() || !FeatureHelper.getBlockState(level, blockpos.offset(1, 0, 1)).canBeReplaced() ||
                    !FeatureHelper.getBlockState(level, blockpos.offset(0, 1, 0)).canBeReplaced() || !FeatureHelper.getBlockState(level, blockpos.offset(1, 1, 0)).canBeReplaced() ||
                    !FeatureHelper.getBlockState(level, blockpos.offset(0, 1, 1)).canBeReplaced() || !FeatureHelper.getBlockState(level, blockpos.offset(1, 1, 1)).canBeReplaced()) {
                return false;
            }
            FeatureHelper.setBlock(level, blockpos.offset(0, 0, 0), material, 2);
            FeatureHelper.setBlock(level, blockpos.offset(1, 0, 0), material, 2);
            FeatureHelper.setBlock(level, blockpos.offset(0, 0, 1), material, 2);
            FeatureHelper.setBlock(level, blockpos.offset(1, 0, 1), material, 2);
            FeatureHelper.setBlock(level, blockpos.offset(0, 1, 0), material, 2);
            FeatureHelper.setBlock(level, blockpos.offset(1, 1, 0), material, 2);
            FeatureHelper.setBlock(level, blockpos.offset(0, 1, 1), material, 2);
            FeatureHelper.setBlock(level, blockpos.offset(1, 1, 1), material, 2);
            int randomNumber = (int)(Math.random()*(2-1+1)+1);
            if (randomNumber < 2) {
                if (FeatureHelper.getBlockState(level, blockpos.offset(0, 2, 0)).canBeReplaced()) {
                    FeatureHelper.setBlock(level, blockpos.offset(0, 2, 0), Blocks.MOSS_CARPET.defaultBlockState(), 2);
                }
            } else {
                if (FeatureHelper.getBlockState(level, blockpos.offset(1, 2, 0)).canBeReplaced()) {
                    FeatureHelper.setBlock(level, blockpos.offset(1, 2, 0), Blocks.MOSS_CARPET.defaultBlockState(), 2);
                }
            }
            return true;
        }
    }
}
