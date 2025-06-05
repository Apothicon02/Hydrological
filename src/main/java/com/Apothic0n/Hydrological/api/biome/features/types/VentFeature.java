package com.Apothic0n.Hydrological.api.biome.features.types;

import com.Apothic0n.Hydrological.api.biome.features.FeatureHelper;

import com.Apothic0n.Hydrological.api.biome.features.configurations.VentConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public class VentFeature extends Feature<VentConfiguration> {
    public VentFeature(Codec<VentConfiguration> pContext) {
        super(pContext);
    }

    public boolean place(FeaturePlaceContext<VentConfiguration> pContext) {
        WorldGenLevel level = pContext.level();
        RandomSource random = pContext.random();
        VentConfiguration config = pContext.config();
        int height = config.getHeight().sample(random);
        int radius = config.getRadius().sample(random);
        int sharpness = height/6;
        int offset = (int) (10-(radius/1.33));
        BlockPos origin = new BlockPos((int) pContext.origin().getCenter().x() + random.nextInt(-offset, offset), pContext.origin().getY() - 2, (int) pContext.origin().getCenter().z() + random.nextInt(-offset, offset));
        boolean placedAnything = false;
        if (pContext.origin().getY()+height < config.getMaxAltitude().sample(random) && FeatureHelper.getBlockState(level, origin.north(radius).below(1)).isSolid() && FeatureHelper.getBlockState(level, origin.east(radius).below(1)).isSolid() &&
                FeatureHelper.getBlockState(level, origin.south(radius).below(1)).isSolid() && FeatureHelper.getBlockState(level, origin.west(radius).below(1)).isSolid()) {
            int xWarp = 0;
            int zWarp = 0;
            for (int y = origin.getY(); y <= origin.getY()+height; y++) {
                BlockState outer = config.getOuter().getState(random, pContext.origin().atY(y));
                BlockState rim = config.getRim().getState(random, pContext.origin().atY(y));
                BlockState inner = config.getInner().getState(random, pContext.origin().atY(y));
                if (random.nextInt(0, 10) > 4+(height/2.5)) {
                    xWarp += 1;
                }
                if (random.nextInt(0, 10) > 4+(height/2.5)) {
                    zWarp += 1;
                }
                for (int x = origin.getX()-radius; x <= origin.getX()+radius; x++) {
                    for (int z = origin.getZ()-radius; z <= origin.getZ()+radius; z++) {
                        int effectiveX = x+xWarp;
                        int effectiveZ = z+zWarp;
                        BlockPos pos = new BlockPos(effectiveX, y, effectiveZ);
                        int currentHeight = Math.abs(origin.getY()-Math.min(y, origin.getY()+(height-(sharpness))))/(sharpness);
                        int distance = (Math.abs(origin.getX()-x)) + (Math.abs(origin.getZ()-z));
                        if (radius-1 >= distance+currentHeight) {
                            if (y < origin.getY()+height) {
                                FeatureHelper.setBlock(level, pos, inner, UPDATE_ALL);
                            }
                        } else if (radius >= distance+currentHeight) {
                            if (y == origin.getY()+height || (y+1 == origin.getY()+height && random.nextGaussian() > -0.4) || (y+2 == origin.getY()+height && random.nextGaussian() > 0.5)) {
                                FeatureHelper.setBlock(level, pos, rim, UPDATE_ALL);
                            } else {
                                FeatureHelper.setBlock(level, pos, outer, UPDATE_ALL);
                            }
                        }
                    }
                }
            }
        }
        return placedAnything;
    }
}
