package com.Apothic0n.Hydrological.api.biome.features.types;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public class CliffCarvingFeature extends Feature<NoneFeatureConfiguration> {
    public CliffCarvingFeature(Codec<NoneFeatureConfiguration> pContext) {
        super(pContext);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
        WorldGenLevel worldGenLevel = pContext.level();
        BlockPos blockPos = pContext.origin();

        for (int y = 0; y >= -3; y--) {
            BlockPos pos = blockPos.above(y);
            BlockState block = worldGenLevel.getBlockState(pos);
            if (block.is(Blocks.DIORITE) || block.is(Blocks.DEEPSLATE) || block.is(Blocks.SMOOTH_SANDSTONE)) {
                worldGenLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), UPDATE_ALL);
            } else {
                return false;
            }
        }

        return true;
    }
}
