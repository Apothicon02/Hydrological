package com.Apothic0n.Hydrological.api.biome.features.types;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;

public class SimpleBlockFeature extends Feature<SimpleBlockConfiguration> {
    public SimpleBlockFeature(Codec<SimpleBlockConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> context) {
        SimpleBlockConfiguration config = context.config();
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        BlockState block = config.toPlace().getState(context.random(), pos);
        if (block.canSurvive(level, pos)) {
            if (block.getBlock() instanceof DoublePlantBlock) {
                if (!level.getBlockState(pos.above()).canBeReplaced()) {
                    return false;
                }

                DoublePlantBlock.placeAt(level, block, pos, 2);
            } else {
                level.setBlock(pos, block, 2);
            }
            return true;
        }
        return false;
    }
}
