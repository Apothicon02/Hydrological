package com.Apothic0n.Hydrological.api.biome.features.types;

import com.Apothic0n.Hydrological.api.biome.features.FeatureHelper;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class FreezeFeature extends Feature<NoneFeatureConfiguration> {
    public FreezeFeature(Codec<NoneFeatureConfiguration> p_66836_) {
        super(p_66836_);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> p_160368_) {
        WorldGenLevel level = p_160368_.level();
        BlockPos blockpos = p_160368_.origin();
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();

        for(int i = 0; i < 16; ++i) {
            for(int j = 0; j < 16; ++j) {
                int k = blockpos.getX() + i;
                int l = blockpos.getZ() + j;
                int i1 = level.getHeight(Heightmap.Types.MOTION_BLOCKING, k, l);
                blockpos$mutableblockpos.set(k, i1, l);
                blockpos$mutableblockpos1.set(blockpos$mutableblockpos).move(Direction.DOWN, 1);
                Biome biome = level.getBiome(blockpos$mutableblockpos).value();
                if (biome.shouldFreeze(level, blockpos$mutableblockpos1, false)) {
                    FeatureHelper.setBlock(level, blockpos$mutableblockpos1, Blocks.ICE.defaultBlockState(), 2);
                }
            }
        }

        return true;
    }
}