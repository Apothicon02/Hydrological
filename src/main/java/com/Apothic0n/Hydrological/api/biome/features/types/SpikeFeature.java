package com.Apothic0n.Hydrological.api.biome.features.types;

import com.Apothic0n.Hydrological.api.biome.features.FeatureHelper;

import com.Apothic0n.Hydrological.api.biome.features.configurations.SpikeConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public class SpikeFeature extends Feature<SpikeConfiguration> {
    public SpikeFeature(Codec<SpikeConfiguration> pContext) {
        super(pContext);
    }

    public boolean place(FeaturePlaceContext<SpikeConfiguration> pContext) {
        WorldGenLevel level = pContext.level();
        BlockPos blockpos = pContext.origin();
        RandomSource random = pContext.random();
        SpikeConfiguration config = pContext.config();
        BlockStateProvider material = config.getMaterial();
        int mass = config.getMass().sample(random);
        int width = config.getWidth().sample(random);
        int height = config.getHeight().sample(random);
        boolean facingUp = true;
        FeatureHelper.setBlock(level, blockpos, material.getState(random, blockpos), UPDATE_ALL);
        BlockPos blockpos1 = blockpos;
        BlockPos blockpos2 = blockpos;
        //boolean northNegative = false;
        //boolean eastNegative = false;
        //int randomNumber = (int)(random.nextFloat() * 4.0 + 1.0);
        //if (randomNumber >= 4) {
        //    northNegative = true;
        //    eastNegative = true;
        //} else if (randomNumber == 3) {
        //    northNegative = true;
        //} else if (randomNumber == 2) {
        //    eastNegative = true;
        //}

        int xFactor = 1;
        int zFactor = 1;
        //if (northNegative) {
        //    xFactor = -1;
        //}

        //if (eastNegative) {
        //    zFactor = -1;
        //}

        int thickness = 0;
        for (int i = 0; i < mass * 4; ++i) {
            int randomNumber2 = (int)(random.nextFloat() * 4.0 + 1.0);
            if (randomNumber2 >= 4 / height) {
                if (!facingUp) {
                    blockpos1 = new BlockPos(blockpos2.getX() + xFactor, blockpos2.getY() - 1, blockpos2.getZ() + zFactor);
                } else {
                    blockpos1 = new BlockPos(blockpos1.getX() + xFactor, blockpos1.getY() + 1, blockpos1.getZ() + zFactor);
                }
            } else {
                if (!facingUp) {
                    blockpos1 = new BlockPos(blockpos2.getX(), blockpos2.getY() - 1, blockpos2.getZ());
                } else {
                    blockpos1 = new BlockPos(blockpos1.getX(), blockpos1.getY() + 1, blockpos1.getZ());
                }
            }

            int xDistance = blockpos1.getX() - blockpos.getX();
            int zDistance = blockpos1.getZ() - blockpos.getZ();
            if (xDistance >= width || zDistance >= width) {
                i = mass * 5;
            }

            FeatureHelper.setBlock(level, blockpos1, material.getState(random, blockpos1), UPDATE_ALL);
            thickness += 1;
            i += 3;
        }

        thickness = thickness/2;
        boolean toggleThickness = false;
        for (int i = 0; i < mass * 4; ++i) {
            int randomNumber2 = (int)(random.nextFloat() * 4.0 + 1.0);
            if (randomNumber2 >= 4 / height) {
                if (!facingUp) {
                    blockpos2 = new BlockPos(blockpos2.getX() + xFactor, blockpos2.getY() - 1, blockpos2.getZ() + zFactor);
                } else {
                    blockpos2 = new BlockPos(blockpos2.getX() + xFactor, blockpos2.getY() + 1, blockpos2.getZ() + zFactor);
                }
            } else {
                if (!facingUp) {
                    blockpos2 = new BlockPos(blockpos2.getX(), blockpos2.getY() - 1, blockpos2.getZ());
                } else {
                    blockpos2 = new BlockPos(blockpos2.getX(), blockpos2.getY() + 1, blockpos2.getZ());
                }
            }

            int xDistance = blockpos2.getX() - blockpos.getX();
            int zDistance = blockpos2.getZ() - blockpos.getZ();
            if (xDistance >= width || zDistance >= width) {
                i = mass * 5;
            }

            for (int w = 0; w < thickness; w++) {
                for (int l = 0; l < thickness; l++) {
                    if (!(w == l && l == thickness-1)) {
                        FeatureHelper.setBlock(level, blockpos2.north(w).east(l), material.getState(random, blockpos2.north(w).east(l)), UPDATE_ALL);
                        FeatureHelper.setBlock(level, blockpos2.north(w).west(l), material.getState(random, blockpos2.north(w).west(l)), UPDATE_ALL);
                        FeatureHelper.setBlock(level, blockpos2.south(w).east(l), material.getState(random, blockpos2.south(w).east(l)), UPDATE_ALL);
                        FeatureHelper.setBlock(level, blockpos2.south(w).west(l), material.getState(random, blockpos2.south(w).west(l)), UPDATE_ALL);
                        FeatureHelper.setBlock(level, blockpos2.north(l).east(w), material.getState(random, blockpos2.north(l).east(w)), UPDATE_ALL);
                        FeatureHelper.setBlock(level, blockpos2.north(l).west(w), material.getState(random, blockpos2.north(l).west(w)), UPDATE_ALL);
                        FeatureHelper.setBlock(level, blockpos2.south(l).east(w), material.getState(random, blockpos2.south(l).east(w)), UPDATE_ALL);
                        FeatureHelper.setBlock(level, blockpos2.south(l).west(w), material.getState(random, blockpos2.south(l).west(w)), UPDATE_ALL);
                    }
                }
            }

            if (toggleThickness) {
                thickness -= 1;
                toggleThickness = false;
            } else {
                toggleThickness = true;
            }
            i += 3;
        }

        return true;
    }
}