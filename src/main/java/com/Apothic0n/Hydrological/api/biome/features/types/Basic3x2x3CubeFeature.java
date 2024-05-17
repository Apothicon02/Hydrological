package com.Apothic0n.Hydrological.api.biome.features.types;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;

public class Basic3x2x3CubeFeature extends Feature<SimpleBlockConfiguration> {
    public Basic3x2x3CubeFeature(Codec<SimpleBlockConfiguration> pContext) {
        super(pContext);
    }

    public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> pContext) {
        WorldGenLevel worldGenLevel = pContext.level();
        BlockPos blockpos = pContext.origin();
        RandomSource random = pContext.random();
        BlockState material = pContext.config().toPlace().getState(random, blockpos);
        if (blockpos.getY() > 55 && worldGenLevel.getBiome(blockpos).is(BiomeTags.IS_OCEAN)) {
            return false;
        }
        if (worldGenLevel.isEmptyBlock(blockpos.offset(-1, -1, 0)) || worldGenLevel.isEmptyBlock(blockpos.offset(1, -1, 0)) ||
                worldGenLevel.isEmptyBlock(blockpos.offset(0, -1, -1)) || worldGenLevel.isEmptyBlock(blockpos.offset(0, -1, 1))) {
            return false;
        } else {
            worldGenLevel.setBlock(blockpos.offset(0, 0, 0), material, 2);
            worldGenLevel.setBlock(blockpos.offset(1, 0, 0), material, 2);
            worldGenLevel.setBlock(blockpos.offset(0, 0, 1), material, 2);
            worldGenLevel.setBlock(blockpos.offset(-1, 0, 0), material, 2);
            worldGenLevel.setBlock(blockpos.offset(0, 0, -1), material, 2);
            worldGenLevel.setBlock(blockpos.offset(-1, 0, -1), material, 2);
            worldGenLevel.setBlock(blockpos.offset(1, 0, 1), material, 2);
            worldGenLevel.setBlock(blockpos.offset(1, 0, -1), material, 2);
            worldGenLevel.setBlock(blockpos.offset(-1, 0, 1), material, 2);

            worldGenLevel.setBlock(blockpos.offset(0, 1, 0), material, 2);
            worldGenLevel.setBlock(blockpos.offset(1, 1, 0), material, 2);
            worldGenLevel.setBlock(blockpos.offset(0, 1, 1), material, 2);
            worldGenLevel.setBlock(blockpos.offset(-1, 1, 0), material, 2);
            worldGenLevel.setBlock(blockpos.offset(0, 1, -1), material, 2);
            worldGenLevel.setBlock(blockpos.offset(-1, 1, -1), material, 2);
            worldGenLevel.setBlock(blockpos.offset(1, 1, 1), material, 2);
            worldGenLevel.setBlock(blockpos.offset(1, 1, -1), material, 2);
            worldGenLevel.setBlock(blockpos.offset(-1, 1, 1), material, 2);

            if ((int)(random.nextFloat()*(3)+1) < 2) {
                blockpos = blockpos.above(2);
                worldGenLevel.setBlock(blockpos.offset(0, 0, 0), material, 2);
                worldGenLevel.setBlock(blockpos.offset(1, 0, 0), material, 2);
                worldGenLevel.setBlock(blockpos.offset(0, 0, 1), material, 2);
                worldGenLevel.setBlock(blockpos.offset(-1, 0, 0), material, 2);
                worldGenLevel.setBlock(blockpos.offset(0, 0, -1), material, 2);
                worldGenLevel.setBlock(blockpos.offset(-1, 0, -1), material, 2);
                worldGenLevel.setBlock(blockpos.offset(1, 0, 1), material, 2);
                worldGenLevel.setBlock(blockpos.offset(1, 0, -1), material, 2);
                worldGenLevel.setBlock(blockpos.offset(-1, 0, 1), material, 2);

                worldGenLevel.setBlock(blockpos.offset(0, 1, 0), material, 2);
                worldGenLevel.setBlock(blockpos.offset(1, 1, 0), material, 2);
                worldGenLevel.setBlock(blockpos.offset(0, 1, 1), material, 2);
                worldGenLevel.setBlock(blockpos.offset(-1, 1, 0), material, 2);
                worldGenLevel.setBlock(blockpos.offset(0, 1, -1), material, 2);
                worldGenLevel.setBlock(blockpos.offset(-1, 1, -1), material, 2);
                worldGenLevel.setBlock(blockpos.offset(1, 1, 1), material, 2);
                worldGenLevel.setBlock(blockpos.offset(1, 1, -1), material, 2);
                worldGenLevel.setBlock(blockpos.offset(-1, 1, 1), material, 2);
                if ((int)(random.nextFloat()*(3)+1) < 2) {
                    blockpos = blockpos.above(2);
                    worldGenLevel.setBlock(blockpos.offset(0, 0, 0), material, 2);
                    worldGenLevel.setBlock(blockpos.offset(1, 0, 0), material, 2);
                    worldGenLevel.setBlock(blockpos.offset(0, 0, 1), material, 2);
                    worldGenLevel.setBlock(blockpos.offset(-1, 0, 0), material, 2);
                    worldGenLevel.setBlock(blockpos.offset(0, 0, -1), material, 2);
                    worldGenLevel.setBlock(blockpos.offset(-1, 0, -1), material, 2);
                    worldGenLevel.setBlock(blockpos.offset(1, 0, 1), material, 2);
                    worldGenLevel.setBlock(blockpos.offset(1, 0, -1), material, 2);
                    worldGenLevel.setBlock(blockpos.offset(-1, 0, 1), material, 2);

                    worldGenLevel.setBlock(blockpos.offset(0, 1, 0), material, 2);
                    worldGenLevel.setBlock(blockpos.offset(1, 1, 0), material, 2);
                    worldGenLevel.setBlock(blockpos.offset(0, 1, 1), material, 2);
                    worldGenLevel.setBlock(blockpos.offset(-1, 1, 0), material, 2);
                    worldGenLevel.setBlock(blockpos.offset(0, 1, -1), material, 2);
                    worldGenLevel.setBlock(blockpos.offset(-1, 1, -1), material, 2);
                    worldGenLevel.setBlock(blockpos.offset(1, 1, 1), material, 2);
                    worldGenLevel.setBlock(blockpos.offset(1, 1, -1), material, 2);
                    worldGenLevel.setBlock(blockpos.offset(-1, 1, 1), material, 2);
                    if ((int)(random.nextFloat()*(3)+1) < 2) {
                        blockpos = blockpos.above(2);
                        worldGenLevel.setBlock(blockpos.offset(0, 0, 0), material, 2);
                        worldGenLevel.setBlock(blockpos.offset(1, 0, 0), material, 2);
                        worldGenLevel.setBlock(blockpos.offset(0, 0, 1), material, 2);
                        worldGenLevel.setBlock(blockpos.offset(-1, 0, 0), material, 2);
                        worldGenLevel.setBlock(blockpos.offset(0, 0, -1), material, 2);
                        worldGenLevel.setBlock(blockpos.offset(-1, 0, -1), material, 2);
                        worldGenLevel.setBlock(blockpos.offset(1, 0, 1), material, 2);
                        worldGenLevel.setBlock(blockpos.offset(1, 0, -1), material, 2);
                        worldGenLevel.setBlock(blockpos.offset(-1, 0, 1), material, 2);

                        worldGenLevel.setBlock(blockpos.offset(0, 1, 0), material, 2);
                        worldGenLevel.setBlock(blockpos.offset(1, 1, 0), material, 2);
                        worldGenLevel.setBlock(blockpos.offset(0, 1, 1), material, 2);
                        worldGenLevel.setBlock(blockpos.offset(-1, 1, 0), material, 2);
                        worldGenLevel.setBlock(blockpos.offset(0, 1, -1), material, 2);
                        worldGenLevel.setBlock(blockpos.offset(-1, 1, -1), material, 2);
                        worldGenLevel.setBlock(blockpos.offset(1, 1, 1), material, 2);
                        worldGenLevel.setBlock(blockpos.offset(1, 1, -1), material, 2);
                        worldGenLevel.setBlock(blockpos.offset(-1, 1, 1), material, 2);
                        if ((int)(random.nextFloat()*(4)+1) < 2) {
                            blockpos = blockpos.above(2);
                            worldGenLevel.setBlock(blockpos.offset(0, 0, 0), material, 2);
                            worldGenLevel.setBlock(blockpos.offset(1, 0, 0), material, 2);
                            worldGenLevel.setBlock(blockpos.offset(0, 0, 1), material, 2);
                            worldGenLevel.setBlock(blockpos.offset(-1, 0, 0), material, 2);
                            worldGenLevel.setBlock(blockpos.offset(0, 0, -1), material, 2);
                            worldGenLevel.setBlock(blockpos.offset(-1, 0, -1), material, 2);
                            worldGenLevel.setBlock(blockpos.offset(1, 0, 1), material, 2);
                            worldGenLevel.setBlock(blockpos.offset(1, 0, -1), material, 2);
                            worldGenLevel.setBlock(blockpos.offset(-1, 0, 1), material, 2);

                            worldGenLevel.setBlock(blockpos.offset(0, 1, 0), material, 2);
                            worldGenLevel.setBlock(blockpos.offset(1, 1, 0), material, 2);
                            worldGenLevel.setBlock(blockpos.offset(0, 1, 1), material, 2);
                            worldGenLevel.setBlock(blockpos.offset(-1, 1, 0), material, 2);
                            worldGenLevel.setBlock(blockpos.offset(0, 1, -1), material, 2);
                            worldGenLevel.setBlock(blockpos.offset(-1, 1, -1), material, 2);
                            worldGenLevel.setBlock(blockpos.offset(1, 1, 1), material, 2);
                            worldGenLevel.setBlock(blockpos.offset(1, 1, -1), material, 2);
                            worldGenLevel.setBlock(blockpos.offset(-1, 1, 1), material, 2);
                            if ((int)(random.nextFloat()*(4)+1) < 2) {
                                blockpos = blockpos.above(2);
                                worldGenLevel.setBlock(blockpos.offset(0, 0, 0), material, 2);
                                worldGenLevel.setBlock(blockpos.offset(1, 0, 0), material, 2);
                                worldGenLevel.setBlock(blockpos.offset(0, 0, 1), material, 2);
                                worldGenLevel.setBlock(blockpos.offset(-1, 0, 0), material, 2);
                                worldGenLevel.setBlock(blockpos.offset(0, 0, -1), material, 2);
                                worldGenLevel.setBlock(blockpos.offset(-1, 0, -1), material, 2);
                                worldGenLevel.setBlock(blockpos.offset(1, 0, 1), material, 2);
                                worldGenLevel.setBlock(blockpos.offset(1, 0, -1), material, 2);
                                worldGenLevel.setBlock(blockpos.offset(-1, 0, 1), material, 2);

                                worldGenLevel.setBlock(blockpos.offset(0, 1, 0), material, 2);
                                worldGenLevel.setBlock(blockpos.offset(1, 1, 0), material, 2);
                                worldGenLevel.setBlock(blockpos.offset(0, 1, 1), material, 2);
                                worldGenLevel.setBlock(blockpos.offset(-1, 1, 0), material, 2);
                                worldGenLevel.setBlock(blockpos.offset(0, 1, -1), material, 2);
                                worldGenLevel.setBlock(blockpos.offset(-1, 1, -1), material, 2);
                                worldGenLevel.setBlock(blockpos.offset(1, 1, 1), material, 2);
                                worldGenLevel.setBlock(blockpos.offset(1, 1, -1), material, 2);
                                worldGenLevel.setBlock(blockpos.offset(-1, 1, 1), material, 2);
                            }
                        }
                    }
                }
            }
            return true;
        }
    }
}
