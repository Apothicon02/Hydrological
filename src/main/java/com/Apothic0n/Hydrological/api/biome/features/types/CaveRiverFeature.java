package com.Apothic0n.Hydrological.api.biome.features.types;

import com.Apothic0n.Hydrological.api.HydrolJsonReader;
import com.Apothic0n.Hydrological.core.objects.HydrolBlocks;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;

public class CaveRiverFeature  extends Feature<NoneFeatureConfiguration> {
    public CaveRiverFeature(Codec<NoneFeatureConfiguration> pContext) {
        super(pContext);
    }

    public static final PerlinSimplexNoise NOODLE_NOISE = new PerlinSimplexNoise(new WorldgenRandom(new LegacyRandomSource(2345L)), ImmutableList.of(-7, 1, 1, -1));

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
        WorldGenLevel worldGenLevel = pContext.level();
        ChunkPos chunkOrigin = new ChunkPos(pContext.origin());
        BlockPos origin = new BlockPos(chunkOrigin.getMiddleBlockX(), -56, chunkOrigin.getMiddleBlockZ());
        RandomSource random = pContext.random();

        if (worldGenLevel.getBlockState(new BlockPos(origin.getX(), worldGenLevel.getMinBuildHeight(), origin.getZ())).isSolid()) {
            for (int x = origin.getX(); x < origin.getX() + 16; ++x) {
                for (int z = origin.getZ() - 16; z < origin.getZ() + 16; ++z) {
                    double noise = NOODLE_NOISE.getValue(x, z, true);
                    if (noise > 0 && noise < 0.01) {
                        replaceFromPos(worldGenLevel, new BlockPos((int) (x + ((Math.random() * 5) - 2)), worldGenLevel.getMinBuildHeight() + 9, (int) (z + ((Math.random() * 5) - 2))), 5, 10, 5, Blocks.WATER.defaultBlockState(), Blocks.DRIPSTONE_BLOCK.defaultBlockState());
                    }
                }
            }
        }

        return true;
    }
    public void replaceFromPos(WorldGenLevel worldGenLevel, BlockPos blockpos, int i, int j, int k, BlockState liquid, BlockState barrier) {
        int l = Math.max(i, Math.max(j, k));
        Block block = liquid.getBlock();

        for(BlockPos blockpos1 : BlockPos.withinManhattan(blockpos, i, j, k)) {
            if (blockpos1.distManhattan(blockpos) > l) {
                break;
            }
            if (worldGenLevel.getBlockState(blockpos1).is(BlockTags.OVERWORLD_CARVER_REPLACEABLES) && blockpos1.getY() > worldGenLevel.getMinBuildHeight()) {
                if (blockpos1.getY() >= blockpos.getY()) {
                    worldGenLevel.setBlock(blockpos1, Blocks.CAVE_AIR.defaultBlockState(), 2);
                } else {
                    worldGenLevel.setBlock(blockpos1, liquid, 2);
                    if (!worldGenLevel.getBlockState(blockpos1.below()).isSolid() && !worldGenLevel.getBlockState(blockpos1.below()).is(block) && !(worldGenLevel.getBlockState(blockpos1.below()).getBlock() instanceof SimpleWaterloggedBlock) &&
                            !(worldGenLevel.getBlockState(blockpos1.below()).getBlock() instanceof LiquidBlockContainer)) {
                        worldGenLevel.setBlock(blockpos1.below(), barrier, 2);
                    }
                    if (!worldGenLevel.getBlockState(blockpos1.north()).isSolid() && !worldGenLevel.getBlockState(blockpos1.north()).is(block) && !(worldGenLevel.getBlockState(blockpos1.north()).getBlock() instanceof SimpleWaterloggedBlock) &&
                            !(worldGenLevel.getBlockState(blockpos1.north()).getBlock() instanceof LiquidBlockContainer)) {
                        worldGenLevel.setBlock(blockpos1.north(), barrier, 2);
                    }
                    if (!worldGenLevel.getBlockState(blockpos1.east()).isSolid() && !worldGenLevel.getBlockState(blockpos1.east()).is(block) && !(worldGenLevel.getBlockState(blockpos1.east()).getBlock() instanceof SimpleWaterloggedBlock) &&
                            !(worldGenLevel.getBlockState(blockpos1.east()).getBlock() instanceof LiquidBlockContainer)) {
                        worldGenLevel.setBlock(blockpos1.east(), barrier, 2);
                    }
                    if (!worldGenLevel.getBlockState(blockpos1.south()).isSolid() && !worldGenLevel.getBlockState(blockpos1.south()).is(block) && !(worldGenLevel.getBlockState(blockpos1.south()).getBlock() instanceof SimpleWaterloggedBlock) &&
                            !(worldGenLevel.getBlockState(blockpos1.south()).getBlock() instanceof LiquidBlockContainer)) {
                        worldGenLevel.setBlock(blockpos1.south(), barrier, 2);
                    }
                    if (!worldGenLevel.getBlockState(blockpos1.west()).isSolid() && !worldGenLevel.getBlockState(blockpos1.west()).is(block) && !(worldGenLevel.getBlockState(blockpos1.west()).getBlock() instanceof SimpleWaterloggedBlock) &&
                            !(worldGenLevel.getBlockState(blockpos1.west()).getBlock() instanceof LiquidBlockContainer)) {
                        worldGenLevel.setBlock(blockpos1.west(), barrier, 2);
                    }
                }
                BlockState aboveBlock = worldGenLevel.getBlockState(blockpos1.above());
                if (aboveBlock.getBlock() instanceof FallingBlock || (!aboveBlock.isSolid() && !aboveBlock.is(block) && !aboveBlock.isAir())) {
                    worldGenLevel.setBlock(blockpos1.above(), Blocks.CAVE_AIR.defaultBlockState(), 2);
                    aboveBlock = worldGenLevel.getBlockState(blockpos1.above(2));
                    if (aboveBlock.getBlock() instanceof FallingBlock || (!aboveBlock.isSolid() && !aboveBlock.is(block) && !aboveBlock.isAir())) {
                        worldGenLevel.setBlock(blockpos1.above(2), Blocks.CAVE_AIR.defaultBlockState(), 2);
                    }
                }
            }
            if (!HydrolJsonReader.serverSidedOnlyMode && liquid.is(Blocks.WATER) && blockpos1.getY() == blockpos.getY() && worldGenLevel.getBlockState(blockpos1).isAir()) {
                worldGenLevel.setBlock(blockpos1, HydrolBlocks.AQUATIC_LICHEN.get().defaultBlockState(), 2);
            }
        }
    }
}