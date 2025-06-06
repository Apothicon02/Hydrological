package com.Apothic0n.Hydrological.api.biome.features.types;

import com.Apothic0n.Hydrological.api.biome.features.FeatureHelper;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;

public class BlockColumnFeature extends Feature<BlockColumnConfiguration> {
    public BlockColumnFeature(Codec<BlockColumnConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockColumnConfiguration> p_190791_) {
        WorldGenLevel level = p_190791_.level();
        BlockColumnConfiguration blockcolumnconfiguration = p_190791_.config();
        RandomSource randomsource = p_190791_.random();
        int i = blockcolumnconfiguration.layers().size();
        int[] aint = new int[i];
        int j = 0;

        for(int k = 0; k < i; ++k) {
            aint[k] = blockcolumnconfiguration.layers().get(k).height().sample(randomsource);
            j += aint[k];
        }

        if (j == 0) {
            return false;
        } else {
            BlockPos.MutableBlockPos blockpos$mutableblockpos1 = p_190791_.origin().mutable();
            BlockPos.MutableBlockPos blockpos$mutableblockpos = blockpos$mutableblockpos1.mutable().move(blockcolumnconfiguration.direction());

            for(int l = 0; l < j; ++l) {
                if (!blockcolumnconfiguration.allowedPlacement().test(level, blockpos$mutableblockpos)) {
                    truncate(aint, j, l, blockcolumnconfiguration.prioritizeTip());
                    break;
                }

                blockpos$mutableblockpos.move(blockcolumnconfiguration.direction());
            }

            for(int k1 = 0; k1 < i; ++k1) {
                int i1 = aint[k1];
                if (i1 != 0) {
                    BlockColumnConfiguration.Layer blockcolumnconfiguration$layer = blockcolumnconfiguration.layers().get(k1);

                    for(int j1 = 0; j1 < i1; ++j1) {
                        BlockState block = blockcolumnconfiguration$layer.state().getState(randomsource, blockpos$mutableblockpos1);
                        if (block.hasProperty(BlockStateProperties.WATERLOGGED) && FeatureHelper.getBlockState(level, blockpos$mutableblockpos1).is(Blocks.WATER)) {
                            block = block.setValue(BlockStateProperties.WATERLOGGED, true);
                        }
                        FeatureHelper.setBlock(level, blockpos$mutableblockpos1, block, 2);
                        blockpos$mutableblockpos1.move(blockcolumnconfiguration.direction());
                    }
                }
            }

            return true;
        }
    }

    private static void truncate(int[] p_190793_, int p_190794_, int p_190795_, boolean p_190796_) {
        int i = p_190794_ - p_190795_;
        int j = p_190796_ ? 1 : -1;
        int k = p_190796_ ? 0 : p_190793_.length - 1;
        int l = p_190796_ ? p_190793_.length : -1;

        for(int i1 = k; i1 != l && i > 0; i1 += j) {
            int j1 = p_190793_[i1];
            int k1 = Math.min(j1, i);
            i -= k1;
            p_190793_[i1] -= k1;
        }

    }
}
