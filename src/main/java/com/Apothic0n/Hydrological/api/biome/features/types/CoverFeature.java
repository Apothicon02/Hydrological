package com.Apothic0n.Hydrological.api.biome.features.types;

import com.Apothic0n.Hydrological.Hydrological;
import com.Apothic0n.Hydrological.api.HydrolJsonReader;
import com.Apothic0n.Hydrological.api.biome.features.configurations.TripleBlockConfiguration;
import com.Apothic0n.Hydrological.core.objects.HydrolBlocks;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
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

public class CoverFeature extends Feature<TripleBlockConfiguration> {
    public CoverFeature(Codec<TripleBlockConfiguration> config) {
        super(config);
    }

    private static final PerlinSimplexNoise HEIGHT_NOISE = new PerlinSimplexNoise(new WorldgenRandom(new LegacyRandomSource(5432L)), ImmutableList.of(-6, 1));

    @Override
    public boolean place(FeaturePlaceContext<TripleBlockConfiguration> pContext) {
        WorldGenLevel worldgenlevel = pContext.level();
        RandomSource random = pContext.random();
        BlockPos origin = pContext.origin();
        BlockState primary = pContext.config().primary().getState(random, origin);
        BlockState secondary = pContext.config().secondary().getState(random, origin);
        BlockState tertiary = pContext.config().tertiary().getState(random, origin);
        TagKey<Block> covering = pContext.config().covering();
        if (primary.isAir()) {
            primary = Blocks.GRASS.defaultBlockState();
        }
        if (secondary.isAir()) {
            secondary = Blocks.TALL_GRASS.defaultBlockState().setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER);
        }
        if (tertiary.isAir()) {
            tertiary = Blocks.TALL_GRASS.defaultBlockState().setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER);
        }
        boolean placedAnything = false;
        for (int x = origin.getX(); x <= origin.getX()+16; x++) {
            for (int z = origin.getZ(); z <= origin.getZ()+16; z++) {
                BlockPos blockPos = new BlockPos(x, worldgenlevel.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z), z);
                BlockState belowState = worldgenlevel.getBlockState(blockPos.below());
                if (worldgenlevel.getBlockState(blockPos).is(Blocks.AIR) && belowState.is(covering)) {
                    if (belowState.is(Blocks.GRASS_BLOCK) || covering != BlockTags.DIRT) {
                        double height = HEIGHT_NOISE.getValue(x, z, false);
                        int chance = random.nextInt(0, 100);
                        if ((height > 0.33 && chance > Math.min(88, (blockPos.getY()*2)-63)) || (blockPos.getY() == 63 && chance >= 75) || chance >= 99) {
                            if (!HydrolJsonReader.serverSidedOnlyMode && tertiary.is(HydrolBlocks.DRY_GRASS.get())) {
                                worldgenlevel.setBlock(blockPos, primary, UPDATE_ALL);
                                worldgenlevel.setBlock(blockPos.above(), secondary, UPDATE_ALL);
                                worldgenlevel.setBlock(blockPos.above(2), tertiary, UPDATE_ALL);
                            } else {
                                worldgenlevel.setBlock(blockPos, secondary, UPDATE_ALL);
                                worldgenlevel.setBlock(blockPos.above(), tertiary, UPDATE_ALL);
                            }
                        } else {
                            if (!HydrolJsonReader.serverSidedOnlyMode && tertiary.is(HydrolBlocks.DRY_GRASS.get())) {
                                worldgenlevel.setBlock(blockPos, secondary, UPDATE_ALL);
                                worldgenlevel.setBlock(blockPos.above(), tertiary, UPDATE_ALL);
                            } else {
                                worldgenlevel.setBlock(blockPos, primary, UPDATE_ALL);
                            }
                        }
                        placedAnything = true;
                    } else if ((random.nextFloat()*4)+1 >= 4) {
                        if (!HydrolJsonReader.serverSidedOnlyMode && tertiary.is(HydrolBlocks.DRY_GRASS.get())) {
                            worldgenlevel.setBlock(blockPos, tertiary, UPDATE_ALL);
                        } else {
                            worldgenlevel.setBlock(blockPos, primary, UPDATE_ALL);
                        }
                        placedAnything = true;
                    }
                }
            }
        }
        return placedAnything;
    }
}
