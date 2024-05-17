package com.Apothic0n.Hydrological.api.biome.features.types;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TallSeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

public class CaveSeagrassFeature extends Feature<ProbabilityFeatureConfiguration> {
    public CaveSeagrassFeature(Codec<ProbabilityFeatureConfiguration> pContext) {
        super(pContext);
    }

    public boolean place(FeaturePlaceContext<ProbabilityFeatureConfiguration> pContext) {
        boolean flag = false;
        RandomSource random = pContext.random();
        WorldGenLevel worldGenLevel = pContext.level();
        BlockPos blockpos = pContext.origin();
        ProbabilityFeatureConfiguration probabilityfeatureconfiguration = pContext.config();
        BlockPos blockpos1 = new BlockPos(blockpos.getX(), blockpos.above().getY(), blockpos.getZ());
        if (worldGenLevel.getBlockState(blockpos1).is(Blocks.WATER)) {
            boolean flag1 = random.nextDouble() < (double)probabilityfeatureconfiguration.probability;
            BlockState blockstate = flag1 ? Blocks.TALL_SEAGRASS.defaultBlockState() : Blocks.SEAGRASS.defaultBlockState();
            if (blockstate.canSurvive(worldGenLevel, blockpos1)) {
                if (flag1) {
                    BlockState blockstate1 = blockstate.setValue(TallSeagrassBlock.HALF, DoubleBlockHalf.UPPER);
                    BlockPos blockpos2 = blockpos1.above();
                    if (worldGenLevel.getBlockState(blockpos2).is(Blocks.WATER)) {
                        worldGenLevel.setBlock(blockpos1, blockstate, 2);
                        worldGenLevel.setBlock(blockpos2, blockstate1, 2);
                    }
                } else {
                    worldGenLevel.setBlock(blockpos1, blockstate, 2);
                }

                flag = true;
            }
        }

        return flag;
    }
}
