package com.Apothic0n.Hydrological.api.biome.features.types;

import com.Apothic0n.Hydrological.api.biome.features.FeatureHelper;

import com.Apothic0n.Hydrological.api.biome.features.configurations.AnvilRockConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class AnvilRockFeature extends Feature<AnvilRockConfiguration> {
    public AnvilRockFeature(Codec<AnvilRockConfiguration> pContext) {
        super(pContext);
    }

    public boolean place(FeaturePlaceContext<AnvilRockConfiguration> pContext) {
        WorldGenLevel level = pContext.level();
        BlockPos blockpos = new BlockPos((int) pContext.origin().getCenter().x(), pContext.origin().getY(), (int) pContext.origin().getCenter().z());
        RandomSource random = pContext.random();
        AnvilRockConfiguration config = pContext.config();
        Integer radius = config.getRadius().sample(random);
        Integer height = config.getHeight().sample(random);
        Integer stretch = config.getStretch().sample(random);
        Integer maxHeight = height-1;
        Boolean forced = config.getForced();
        if (!forced && !FeatureHelper.getBlockState(level, blockpos.below()).isSolid()) {
            return false;
        } else {
            int randomNumber = (int)(Math.random()*(4));
            for (int s = 0; s <= stretch; ++s) {
                for (int h = 0; h < height; ++h) {
                    BlockPos pos;
                    if (randomNumber == 3) {
                        pos = blockpos.offset(-((h/4)*s), 0, -((h/6)));
                    } else if (randomNumber == 2) {
                        pos = blockpos.offset(-((h/6)), 0, -((h/4)*s));
                    } else if (randomNumber == 1) {
                        pos = blockpos.offset(((h/4)*s), 0, ((h/6)));
                    } else {
                        pos = blockpos.offset(((h/6)), 0, ((h/4)*s));
                    }
                    boolean obese = maxHeight >= 5 && h == 2 || maxHeight >= 5 && h == height - 2;
                    boolean narrow = maxHeight >= 5 && h > 3 && h < height-3;
                    boolean anorexic = maxHeight >= 9 && h > 5 && h < height-5;
                    //Radius 1
                    if (narrow) {
                        genNarrowRadius1(pContext, level, pos, h);
                    } else if (anorexic) {
                        genAnorexicRadius1(pContext, level, pos, h);
                    } else if (obese) {
                        genObeseRadius1(pContext, level, pos, h);
                    } else {
                        genRadius1(pContext, level, pos, h);
                    }
                    //Radius 2
                    if (radius >= 2 || h == 1 || h == maxHeight) {
                        if (narrow) {
                            genNarrowRadius2(pContext, level, pos, h);
                        } else if (!anorexic) {
                            if (obese) {
                                genObeseRadius2(pContext, level, pos, h);
                            } else {
                                genRadius2(pContext, level, pos, h);
                            }
                        }
                    }
                    //Radius 3
                    if (radius >= 3) {
                        if (anorexic) {
                            genNarrowRadius2(pContext, level, pos, h);
                        } else if (narrow) {
                            genRadius2(pContext, level, pos, h);
                        } else if (obese) {
                            genObeseRadius3(pContext, level, pos, h);
                        } else {
                            genRadius3(pContext, level, pos, h);
                        }
                        if (h == 1 || h == maxHeight) {
                            genRadius4(pContext, level, pos, h);
                        }
                    } else if (radius == 2 && h == 1) {
                        genRadius3(pContext, level, pos, h);
                    } else if (radius == 2 && h == maxHeight) {
                        genRadius3(pContext, level, pos, h);
                    }
                }
            }
            return true;
        }
    }

    private void genAnorexicRadius1(FeaturePlaceContext<AnvilRockConfiguration> pContext, WorldGenLevel level, BlockPos blockpos, int h) {
        h--;
        FeatureHelper.setBlock(level, blockpos.offset(0, h, 0), getState(pContext, blockpos.offset(0, h, 0)), 2);
    }

    private void genNarrowRadius1(FeaturePlaceContext<AnvilRockConfiguration> pContext, WorldGenLevel level, BlockPos blockpos, int h) {
        h--;
        FeatureHelper.setBlock(level, blockpos.offset(0, h, 0), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(1, h, 0), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(0, h, 1), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, 0), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(0, h, -1), getState(pContext, blockpos.offset(0, h, 0)), 2);
    }

    private void genRadius1(FeaturePlaceContext<AnvilRockConfiguration> pContext, WorldGenLevel level, BlockPos blockpos, int h) {
        h--;
        FeatureHelper.setBlock(level, blockpos.offset(0, h, 0), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(1, h, 0), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(0, h, 1), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, 0), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(0, h, -1), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(1, h, 1), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, -1), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, 1), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(1, h, -1), getState(pContext, blockpos.offset(0, h, 0)), 2);
    }

    private void genObeseRadius1(FeaturePlaceContext<AnvilRockConfiguration> pContext, WorldGenLevel level, BlockPos blockpos, int h) {
        h--;
        FeatureHelper.setBlock(level, blockpos.offset(0, h, 0), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(1, h, 0), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(0, h, 1), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, 0), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(0, h, -1), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(1, h, 1), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, -1), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, 1), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(1, h, -1), getState(pContext, blockpos.offset(0, h, 0)), 2);

        FeatureHelper.setBlock(level, blockpos.offset(2, h, 0), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(0, h, 2), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-2, h, 0), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(0, h, -2), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(2, h, 1), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(1, h, 2), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-2, h, 1), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(1, h, -2), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(2, h, -1), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, 2), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-2, h, -1), getState(pContext, blockpos.offset(0, h, 0)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, -2), getState(pContext, blockpos.offset(0, h, 0)), 2);
    }

    private void genNarrowRadius2(FeaturePlaceContext<AnvilRockConfiguration> pContext, WorldGenLevel level, BlockPos blockpos, int h) {
        h--;
        FeatureHelper.setBlock(level, blockpos.offset(0, h, 2), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(2, h, 0), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(0, h, -2), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-2, h, 0), getState(pContext, blockpos.offset(1, h, 1)), 2);
    }

    private void genRadius2(FeaturePlaceContext<AnvilRockConfiguration> pContext, WorldGenLevel level, BlockPos blockpos, int h) {
        h--;
        FeatureHelper.setBlock(level, blockpos.offset(0, h, 2), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(2, h, 0), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(0, h, -2), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-2, h, 0), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(1, h, 2), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(2, h, 1), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(1, h, -2), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-2, h, 1), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, 2), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(2, h, -1), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, -2), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-2, h, -1), getState(pContext, blockpos.offset(1, h, 1)), 2);
    }

    private void genObeseRadius2(FeaturePlaceContext<AnvilRockConfiguration> pContext, WorldGenLevel level, BlockPos blockpos, int h) {
        h--;
        FeatureHelper.setBlock(level, blockpos.offset(0, h, 2), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(2, h, 0), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(0, h, -2), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-2, h, 0), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(1, h, 2), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(2, h, 1), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(1, h, -2), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-2, h, 1), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, 2), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(2, h, -1), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, -2), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-2, h, -1), getState(pContext, blockpos.offset(1, h, 1)), 2);

        FeatureHelper.setBlock(level, blockpos.offset(0, h, 3), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(3, h, 0), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(0, h, -3), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h, 0), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(1, h, 3), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(3, h, 1), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(1, h, -3), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h, 1), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, 3), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(3, h, -1), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, -3), getState(pContext, blockpos.offset(1, h, 1)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h, -1), getState(pContext, blockpos.offset(1, h, 1)), 2);
    }

    private void genRadius3(FeaturePlaceContext<AnvilRockConfiguration> pContext, WorldGenLevel level, BlockPos blockpos, int h) {
        h--;
        FeatureHelper.setBlock(level, blockpos.offset(2, h, 2), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(2, h, -2), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-2, h, 2), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-2, h, -2), getState(pContext, blockpos.offset(2, h, 2)), 2);

        FeatureHelper.setBlock(level, blockpos.offset(0, h, 3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(3, h, 0), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(0, h, -3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h, 0), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(1, h, 3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(3, h, 1), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(1, h, -3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h, 1), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, 3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(3, h, -1), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, -3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h, -1), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(2, h, 3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(3, h, 2), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(2, h, -3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h, 2), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-2, h, 3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(3, h, -2), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-2, h, -3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h, -2), getState(pContext, blockpos.offset(2, h, 2)), 2);
    }

    private void genObeseRadius3(FeaturePlaceContext<AnvilRockConfiguration> pContext, WorldGenLevel level, BlockPos blockpos, int h) {
        h--;
        FeatureHelper.setBlock(level, blockpos.offset(2, h, 2), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(2, h, -2), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-2, h, 2), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-2, h, -2), getState(pContext, blockpos.offset(2, h, 2)), 2);

        FeatureHelper.setBlock(level, blockpos.offset(0, h, 3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(3, h, 0), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(0, h, -3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h, 0), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(1, h, 3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(3, h, 1), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(1, h, -3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h, 1), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, 3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(3, h, -1), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, -3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h, -1), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(2, h, 3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(3, h, 2), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(2, h, -3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h, 2), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-2, h, 3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(3, h, -2), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-2, h, -3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h, -2), getState(pContext, blockpos.offset(2, h, 2)), 2);

        FeatureHelper.setBlock(level, blockpos.offset(3, h+1, 3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(3, h+1, -3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h+1, 3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h+1, -3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(3, h-1, 3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(3, h-1, -3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h-1, 3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h-1, -3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(3, h, 3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(3, h, -3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h, 3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h, -3), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(0, h, 4), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(4, h, 0), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(0, h, -4), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-4, h, 0), getState(pContext, blockpos.offset(2, h, 2)), 2);

        FeatureHelper.setBlock(level, blockpos.offset(1, h, 4), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(4, h, 1), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(1, h, -4), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-4, h, 1), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, 4), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(4, h, -1), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, -4), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-4, h, -1), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(2, h, 4), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(4, h, 2), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(2, h, -4), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-4, h, 2), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-2, h, 4), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(4, h, -2), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-2, h, -4), getState(pContext, blockpos.offset(2, h, 2)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-4, h, -2), getState(pContext, blockpos.offset(2, h, 2)), 2);
    }

    private void genRadius4(FeaturePlaceContext<AnvilRockConfiguration> pContext, WorldGenLevel level, BlockPos blockpos, int h) {
        h--;
        FeatureHelper.setBlock(level, blockpos.offset(3, h, 3), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(3, h, -3), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h, 3), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h, -3), getState(pContext, blockpos.offset(3, h, 3)), 2);

        FeatureHelper.setBlock(level, blockpos.offset(0, h, 4), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(4, h, 0), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(0, h, -4), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-4, h, 0), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(1, h, 4), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(4, h, 1), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(1, h, -4), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-4, h, 1), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, 4), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(4, h, -1), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-1, h, -4), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-4, h, -1), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(2, h, 4), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(4, h, 2), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(2, h, -4), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-4, h, 2), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-2, h, 4), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(4, h, -2), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-2, h, -4), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-4, h, -2), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(3, h, 4), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(4, h, 3), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(3, h, -4), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-4, h, 3), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h, 4), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(4, h, -3), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-3, h, -4), getState(pContext, blockpos.offset(3, h, 3)), 2);
        FeatureHelper.setBlock(level, blockpos.offset(-4, h, -3), getState(pContext, blockpos.offset(3, h, 3)), 2);
    }

    private BlockState getState(FeaturePlaceContext<AnvilRockConfiguration> pContext, BlockPos pos) {
        return pContext.config().material.getState(pContext.level(), pContext.random(), pos);
    }
}
