package com.Apothic0n.Hydrological.api.biome.features.types;

import com.Apothic0n.Hydrological.api.biome.features.FeatureHelper;

import com.Apothic0n.Hydrological.api.biome.features.configurations.CatchingFallConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class CatchingFallFeature extends Feature<CatchingFallConfiguration> {
    public CatchingFallFeature(Codec<CatchingFallConfiguration> pContext) {
        super(pContext);
    }

    public boolean place(FeaturePlaceContext<CatchingFallConfiguration> pContext) {
        CatchingFallConfiguration springconfiguration = pContext.config();
        WorldGenLevel level = pContext.level();
        BlockPos blockpos = pContext.origin();
        if (!springconfiguration.validBlocks.contains(FeatureHelper.getBlockState(level, blockpos.above()).getBlock())) {
            return false;
        } else if (springconfiguration.requiresBlockBelow && !springconfiguration.validBlocks.contains(FeatureHelper.getBlockState(level, blockpos.below()).getBlock())) {
            return false;
        } else {
            BlockState blockstate = FeatureHelper.getBlockState(level, blockpos);
            if (!blockstate.isAir() && !blockstate.is(springconfiguration.state.createLegacyBlock().getBlock()) && !springconfiguration.validBlocks.contains(blockstate.getBlock())) {
                return false;
            } else {
                int i = 0;
                int j = 0;
                if (springconfiguration.validBlocks.contains(FeatureHelper.getBlockState(level, blockpos.west()).getBlock())) {
                    ++j;
                }

                if (springconfiguration.validBlocks.contains(FeatureHelper.getBlockState(level, blockpos.east()).getBlock())) {
                    ++j;
                }

                if (springconfiguration.validBlocks.contains(FeatureHelper.getBlockState(level, blockpos.north()).getBlock())) {
                    ++j;
                }

                if (springconfiguration.validBlocks.contains(FeatureHelper.getBlockState(level, blockpos.south()).getBlock())) {
                    ++j;
                }

                if (springconfiguration.validBlocks.contains(FeatureHelper.getBlockState(level, blockpos.below()).getBlock())) {
                    ++j;
                }

                int k = 0;
                if (level.isEmptyBlock(blockpos.west())) {
                    ++k;
                }

                if (level.isEmptyBlock(blockpos.east())) {
                    ++k;
                }

                if (level.isEmptyBlock(blockpos.north())) {
                    ++k;
                }

                if (level.isEmptyBlock(blockpos.south())) {
                    ++k;
                }

                if (level.isEmptyBlock(blockpos.below())) {
                    ++k;
                }

                if (j >= springconfiguration.rockCount && k >= springconfiguration.holeCount) {
                    BlockPos blockpos1 = blockpos.below();
                    Boolean shouldGenerate = true;
                    if (FeatureHelper.getBlockState(level, blockpos1.below()).isSolid()) {
                        blockpos1 = blockpos1.above();
                        if (level.isEmptyBlock(blockpos1.north())) {
                            blockpos1 = blockpos1.north();
                        } else if (level.isEmptyBlock(blockpos1.east())) {
                            blockpos1 = blockpos1.east();
                        } else if (level.isEmptyBlock(blockpos1.south())) {
                            blockpos1 = blockpos1.south();
                        } else if (level.isEmptyBlock(blockpos1.west())) {
                            blockpos1 = blockpos1.west();
                        }
                    }
                    if (!FeatureHelper.getBlockState(level, blockpos1.below(5)).isSolid()) {
                        for(int l = 0; l < 200; ++l) {
                            BlockState blockBelow = FeatureHelper.getBlockState(level, blockpos1.below());

                            if (springconfiguration.validBlocks.contains(blockBelow.getBlock())) {
                                blockpos1 = blockpos1.below();
                                FeatureHelper.setBlock(level, blockpos1, springconfiguration.state.createLegacyBlock(), 2);
                                level.scheduleTick(blockpos1, springconfiguration.state.getType(), 0);
                                FeatureHelper.setBlock(level, blockpos1.north(), springconfiguration.state.createLegacyBlock(), 2);
                                level.scheduleTick(blockpos1.north(), springconfiguration.state.getType(), 0);
                                FeatureHelper.setBlock(level, blockpos1.east(), springconfiguration.state.createLegacyBlock(), 2);
                                level.scheduleTick(blockpos1.east(), springconfiguration.state.getType(), 0);
                                FeatureHelper.setBlock(level, blockpos1.south(), springconfiguration.state.createLegacyBlock(), 2);
                                level.scheduleTick(blockpos1.south(), springconfiguration.state.getType(), 0);
                                FeatureHelper.setBlock(level, blockpos1.west(), springconfiguration.state.createLegacyBlock(), 2);
                                level.scheduleTick(blockpos1.west(), springconfiguration.state.getType(), 0);

                                FeatureHelper.setBlock(level, blockpos1.south().south(), springconfiguration.basinMaterial.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.south().south().south(), springconfiguration.basinMaterial.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.south().south().above(), springconfiguration.basinMaterial.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.south().south().south().above(), springconfiguration.basinMaterial.defaultBlockState(), 2);

                                FeatureHelper.setBlock(level, blockpos1.south().south().west(), springconfiguration.basinMaterial.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.south().west(), springconfiguration.basinMaterial.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.south().west().west(), springconfiguration.basinMaterial.defaultBlockState(), 2);

                                FeatureHelper.setBlock(level, blockpos1.west().west(), springconfiguration.basinMaterial.defaultBlockState(), 2);

                                FeatureHelper.setBlock(level, blockpos1.west().north(), springconfiguration.basinMaterial.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.west().north().above(), springconfiguration.basinMaterial.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.west().west().north(), springconfiguration.basinMaterial.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.west().north().north(), springconfiguration.basinMaterial.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.west().north().north().above(), springconfiguration.basinMaterial.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.west().north().north().north(), springconfiguration.basinMaterial.defaultBlockState(), 2);

                                FeatureHelper.setBlock(level, blockpos1.north().north(), springconfiguration.basinMaterial.defaultBlockState(), 2);

                                FeatureHelper.setBlock(level, blockpos1.north().north().east(), springconfiguration.basinMaterial.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.north().north().east().east(), springconfiguration.basinMaterial.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.north().east(), springconfiguration.basinMaterial.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.north().east().above(), springconfiguration.basinMaterial.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.north().east().east().above(), springconfiguration.basinMaterial.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.north().east().east(), springconfiguration.basinMaterial.defaultBlockState(), 2);

                                FeatureHelper.setBlock(level, blockpos1.east().east(), springconfiguration.basinMaterial.defaultBlockState(), 2);

                                FeatureHelper.setBlock(level, blockpos1.east().south(), springconfiguration.basinMaterial.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.east().south().above(), springconfiguration.basinMaterial.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.east().east().south(), springconfiguration.basinMaterial.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.east().south().south(), springconfiguration.basinMaterial.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.east().east().south().south(), springconfiguration.basinMaterial.defaultBlockState(), 2);

                                FeatureHelper.setBlock(level, blockpos1.below(), springconfiguration.basinMaterial2.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.below().north(), springconfiguration.basinMaterial.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.below().east(), springconfiguration.basinMaterial.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.below().south(), springconfiguration.basinMaterial.defaultBlockState(), 2);
                                FeatureHelper.setBlock(level, blockpos1.below().west(), springconfiguration.basinMaterial.defaultBlockState(), 2);

                                l = 200;
                            } else if (springconfiguration.invalidBlocks.contains(blockBelow.getBlock())) { //cant generate over these to prevent large lava cast-like formations
                                l = 200;
                                shouldGenerate = false;
                            } else {
                                blockpos1 = blockpos1.below();
                            }
                        }
                        if (shouldGenerate) {
                            FeatureHelper.setBlock(level, blockpos, springconfiguration.state.createLegacyBlock(), 2);
                            level.scheduleTick(blockpos, springconfiguration.state.getType(), 0);
                        }
                    }
                    ++i;
                }
                return i > 0;
            }
        }
    }
}
