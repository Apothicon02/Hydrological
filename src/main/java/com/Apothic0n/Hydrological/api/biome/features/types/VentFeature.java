package com.Apothic0n.Hydrological.api.biome.features.types;

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
        WorldGenLevel worldGenLevel = pContext.level();
        RandomSource random = pContext.random();
        VentConfiguration config = pContext.config();
        int height = config.getHeight().sample(random);
        int radius = config.getRadius().sample(random);
        int sharpness = height/5;
        int offset = (int) (10-(radius/1.33));
        BlockPos origin = new BlockPos((int) pContext.origin().getCenter().x() + random.nextInt(-offset, offset), pContext.origin().getY() - 2, (int) pContext.origin().getCenter().z() + random.nextInt(-offset, offset));
        boolean placedAnything = false;
        if (pContext.origin().getY()+height < config.getMaxAltitude().sample(random) && worldGenLevel.getBlockState(origin.north(radius).below(1)).isSolid() && worldGenLevel.getBlockState(origin.east(radius).below(1)).isSolid() &&
                worldGenLevel.getBlockState(origin.south(radius).below(1)).isSolid() && worldGenLevel.getBlockState(origin.west(radius).below(1)).isSolid()) {
            for (int y = origin.getY(); y <= origin.getY()+height; y++) {
                BlockState outer = config.getOuter().getState(random, pContext.origin().atY(y));
                BlockState inner = config.getInner().getState(random, pContext.origin().atY(y));
                for (int x = origin.getX()-radius; x <= origin.getX()+radius; x++) {
                    for (int z = origin.getZ()-radius; z <= origin.getZ()+radius; z++) {
                        BlockPos pos = new BlockPos(x, y, z);
                        int currentHeight = Math.abs(origin.getY()-Math.min(y, origin.getY()+(height-(sharpness))))/(sharpness);
                        int distance = (Math.abs(origin.getX()-x)) + (Math.abs(origin.getZ()-z));
                        if (radius-1 >= distance+currentHeight) {
                            if (y < origin.getY()+height) {
                                worldGenLevel.setBlock(pos, inner, UPDATE_ALL);
                            }
                        } else if (radius >= distance+currentHeight) {
                            worldGenLevel.setBlock(pos, outer, UPDATE_ALL);
                        }
                    }
                }
            }
        }
        return placedAnything;
    }
}
