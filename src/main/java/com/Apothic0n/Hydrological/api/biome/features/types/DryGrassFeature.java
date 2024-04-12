package com.Apothic0n.Hydrological.api.biome.features.types;

import com.Apothic0n.Hydrological.api.HydrolJsonReader;
import com.Apothic0n.Hydrological.core.objects.HydrolBlocks;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public class DryGrassFeature extends Feature<NoneFeatureConfiguration> {
    public DryGrassFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    private static final PerlinSimplexNoise HEIGHT_NOISE = new PerlinSimplexNoise(new WorldgenRandom(new LegacyRandomSource(5432L)), ImmutableList.of(-6, 1));

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
        WorldGenLevel worldgenlevel = pContext.level();
        RandomSource random = pContext.random();
        BlockPos origin = pContext.origin();
        BlockState bottomState = Blocks.TALL_GRASS.defaultBlockState().setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER);
        BlockState topState = Blocks.TALL_GRASS.defaultBlockState().setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER);
        if (!HydrolJsonReader.serverSidedOnlyMode) {
            bottomState = HydrolBlocks.DRY_GRASS.get().defaultBlockState().setValue(BlockStateProperties.HALF, Half.BOTTOM);
            topState = HydrolBlocks.DRY_GRASS.get().defaultBlockState();
        }
        boolean placedAnything = false;
        for (int x = origin.getX(); x <= origin.getX()+16; x++) {
            for (int z = origin.getZ(); z <= origin.getZ()+16; z++) {
                BlockPos blockPos = new BlockPos(x, worldgenlevel.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z), z);
                BlockState belowState = worldgenlevel.getBlockState(blockPos.below());
                Holder<Biome> biome = worldgenlevel.getBiome(blockPos);
                if ((biome.is(BiomeTags.IS_SAVANNA) || biome.toString().contains("orchard")) && worldgenlevel.getBlockState(blockPos).is(Blocks.AIR) && belowState.is(BlockTags.DIRT)) {
                    if (belowState.is(Blocks.GRASS_BLOCK)) {
                        double height = HEIGHT_NOISE.getValue(x, z, false);
                        if (height > 0.33) {
                            if (!HydrolJsonReader.serverSidedOnlyMode) {
                                worldgenlevel.setBlock(blockPos, bottomState, UPDATE_ALL);
                                worldgenlevel.setBlock(blockPos.above(), bottomState, UPDATE_ALL);
                                worldgenlevel.setBlock(blockPos.above(2), topState, UPDATE_ALL);
                            } else {
                                worldgenlevel.setBlock(blockPos, bottomState, UPDATE_ALL);
                                worldgenlevel.setBlock(blockPos.above(), topState, UPDATE_ALL);
                            }
                        } else {
                            if (!HydrolJsonReader.serverSidedOnlyMode) {
                                worldgenlevel.setBlock(blockPos, bottomState, UPDATE_ALL);
                                worldgenlevel.setBlock(blockPos.above(), topState, UPDATE_ALL);
                            } else {
                                worldgenlevel.setBlock(blockPos, Blocks.GRASS.defaultBlockState(), UPDATE_ALL);
                            }
                        }
                        placedAnything = true;
                    } else if ((random.nextFloat()*4)+1 >= 2) {
                        if (!HydrolJsonReader.serverSidedOnlyMode) {
                            worldgenlevel.setBlock(blockPos, topState, UPDATE_ALL);
                        } else {
                            worldgenlevel.setBlock(blockPos, Blocks.GRASS.defaultBlockState(), UPDATE_ALL);
                        }
                        placedAnything = true;
                    }
                }
            }
        }
        return placedAnything;
    }
}
