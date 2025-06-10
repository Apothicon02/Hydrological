package com.Apothic0n.Hydrological.api.biome.features.types;

import com.Apothic0n.Hydrological.api.biome.features.configurations.ReplaceableBlockConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SimpleBlockFeature extends Feature<ReplaceableBlockConfiguration> {
    public SimpleBlockFeature(Codec<ReplaceableBlockConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<ReplaceableBlockConfiguration> context) {
        ReplaceableBlockConfiguration config = context.config();
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        BlockState block = config.toPlace().getState(context.random(), pos);
        BlockState state = level.getBlockState(pos);
        if ((((state.canBeReplaced() && !block.isSolid()) || config.replace()) || state.isAir() || state.is(Blocks.WATER))) {
            if (block.hasProperty(BlockStateProperties.WATERLOGGED) && state.is(Blocks.WATER)) {
                block = block.setValue(BlockStateProperties.WATERLOGGED, true);
            }
            if (block.getBlock() instanceof DoublePlantBlock) {
                if (!level.getBlockState(pos.above()).canBeReplaced()) {
                    return false;
                }

                DoublePlantBlock.placeAt(level, block, pos, 2);
            } else {
                if (pos.getY() < 0) {
                    if (block.is(Blocks.COBBLESTONE)) {
                        block = Blocks.COBBLED_DEEPSLATE.defaultBlockState();
                    } else if (block.is(Blocks.COAL_ORE)) {
                        block = Blocks.DEEPSLATE_COAL_ORE.defaultBlockState();
                    } else if (block.is(Blocks.IRON_ORE)) {
                        block = Blocks.DEEPSLATE_IRON_ORE.defaultBlockState();
                    } else if (block.is(Blocks.GOLD_ORE)) {
                        block = Blocks.DEEPSLATE_GOLD_ORE.defaultBlockState();
                    } else if (block.is(Blocks.LAPIS_ORE)) {
                        block = Blocks.DEEPSLATE_LAPIS_ORE.defaultBlockState();
                    } else if (block.is(Blocks.DIAMOND_ORE)) {
                        block = Blocks.DEEPSLATE_DIAMOND_ORE.defaultBlockState();
                    } else if (block.is(Blocks.REDSTONE_ORE)) {
                        block = Blocks.DEEPSLATE_REDSTONE_ORE.defaultBlockState();
                    } else if (block.is(Blocks.COPPER_ORE)) {
                        block = Blocks.DEEPSLATE_COPPER_ORE.defaultBlockState();
                    }
                }
                level.setBlock(pos, block, 2);
            }
            return true;
        }
        return false;
    }
}
