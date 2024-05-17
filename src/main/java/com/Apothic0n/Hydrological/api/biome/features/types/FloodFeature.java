package com.Apothic0n.Hydrological.api.biome.features.types;

import com.Apothic0n.Hydrological.api.biome.features.configurations.FloodConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import static net.minecraft.world.level.block.Block.UPDATE_NONE;

public class FloodFeature extends Feature<FloodConfiguration> {
    public FloodFeature(Codec<FloodConfiguration> pContext) {
        super(pContext);
    }

    public boolean place(FeaturePlaceContext<FloodConfiguration> pContext) {
        WorldGenLevel worldGenLevel = pContext.level();
        ChunkPos chunkOrigin = new ChunkPos(pContext.origin());
        BlockPos origin = new BlockPos(chunkOrigin.getMiddleBlockX(), pContext.origin().getY(), chunkOrigin.getMiddleBlockZ());
        RandomSource random = pContext.random();
        FloodConfiguration config = pContext.config();
        BlockState state = config.material.getState(random, origin);
        if (config.frozen) {
            state = config.frozenMaterial.getState(random, origin);
        }
        Integer elevation = config.getElevation().sample(random);
        if (elevation == -420) {
            elevation = origin.getY();
        }
        for (int x = origin.getX(); x < origin.getX()+16; ++x) {
            for (int z = origin.getZ(); z < origin.getZ()+16; ++z) {
                for (int y = elevation; y > worldGenLevel.getMinBuildHeight() + 1; --y) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (!worldGenLevel.getBlockState(pos).isSolid()) {
                        worldGenLevel.setBlock(pos, state, UPDATE_NONE);
                    }
                }
            }
        }
        return true;
    }
}
