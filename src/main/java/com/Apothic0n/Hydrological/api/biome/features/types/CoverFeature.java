package com.Apothic0n.Hydrological.api.biome.features.types;

import com.Apothic0n.Hydrological.Hydrological;
import com.Apothic0n.Hydrological.api.HydrolDensityFunctions;
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
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

import static com.Apothic0n.Hydrological.core.objects.HydrolBlocks.wallBlocks;
import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public class CoverFeature extends Feature<TripleBlockConfiguration> {
    public CoverFeature(Codec<TripleBlockConfiguration> config) {
        super(config);
    }

    private static final PerlinSimplexNoise HEIGHT_NOISE = new PerlinSimplexNoise(new WorldgenRandom(new LegacyRandomSource(5432L)), ImmutableList.of(-6, 1));

    @Override
    public boolean place(FeaturePlaceContext<TripleBlockConfiguration> pContext) {
        WorldGenLevel worldGenLevel = pContext.level();
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
        Block azaleaLeaves = Blocks.AZALEA_LEAVES;
        Block floweringAzaleaLeaves = Blocks.FLOWERING_AZALEA_LEAVES;
        if (!HydrolJsonReader.serverSidedOnlyMode) {
            for (int i = 0; i < wallBlocks.size(); i++) {
                Map<Block, RegistryObject<Block>> map = wallBlocks.get(i);
                if (map.containsKey(Blocks.AZALEA_LEAVES)) {
                    azaleaLeaves = map.get(Blocks.AZALEA_LEAVES).get();
                } else if (map.containsKey(Blocks.FLOWERING_AZALEA_LEAVES)) {
                    floweringAzaleaLeaves = map.get(Blocks.FLOWERING_AZALEA_LEAVES).get();
                }
            }
        }
        boolean placedAnything = false;
        for (int x = origin.getX(); x <= origin.getX()+16; x++) {
            for (int z = origin.getZ(); z <= origin.getZ()+16; z++) {
                BlockPos blockPos = new BlockPos(x, worldGenLevel.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z), z);
                BlockPos belowPos = blockPos.below();
                BlockState belowState = worldGenLevel.getBlockState(belowPos);
                if (worldGenLevel.getBlockState(blockPos).is(Blocks.AIR) && belowState.is(covering)) {
                    double temperature = -1;
                    if (HydrolDensityFunctions.temperature != null) {
                        temperature = HydrolDensityFunctions.temperature.compute(new DensityFunction.SinglePointContext(x, 63, z));
                    }
                    if (primary.is(Blocks.GRASS)) {
                        belowState = Blocks.GRASS_BLOCK.defaultBlockState();
                        worldGenLevel.setBlock(belowPos, belowState, UPDATE_ALL);
                    }
                    int length = (int) ((random.nextGaussian()*16)+Mth.clamp(belowPos.getY()-80, -12, 12));
                    for (int y = 1; y <= length; y++) {
                        BlockPos pos = belowPos.below(y);
                        if (worldGenLevel.getBlockState(pos).isAir() && worldGenLevel.getBlockState(pos.below()).isAir() && worldGenLevel.getBlockState(pos.below(2)).isAir()) {
                            BlockState belowBlock = Blocks.AIR.defaultBlockState();
                            if (y == length) {
                                if (temperature > 0.6) {
                                    belowBlock = Blocks.CAVE_VINES.defaultBlockState().setValue(BlockStateProperties.BERRIES, random.nextBoolean());
                                } else if (temperature < -0.8) {
                                    belowBlock = Blocks.ICE.defaultBlockState();
                                } else if (random.nextInt(0, 100) >= 98) {
                                    belowBlock = Blocks.SPORE_BLOSSOM.defaultBlockState();
                                }
                            } else {
                                if (temperature > 0.6) {
                                    belowBlock = Blocks.CAVE_VINES_PLANT.defaultBlockState().setValue(BlockStateProperties.BERRIES, random.nextBoolean());
                                } else if (temperature < -0.8) {
                                    belowBlock = Blocks.ICE.defaultBlockState();
                                } else if (random.nextInt(0, 10) >= 7) {
                                    if (y < length/4 && length > 2) {
                                        belowBlock = Blocks.FLOWERING_AZALEA_LEAVES.defaultBlockState().setValue(BlockStateProperties.PERSISTENT, true);
                                    } else {
                                        belowBlock = floweringAzaleaLeaves.defaultBlockState();
                                    }
                                } else {
                                    if (y < length/4 && length > 2) {
                                        belowBlock = Blocks.AZALEA_LEAVES.defaultBlockState().setValue(BlockStateProperties.PERSISTENT, true);
                                    } else {
                                        belowBlock = azaleaLeaves.defaultBlockState();
                                    }
                                }
                            }
                            if (!belowBlock.is(Blocks.AIR)) {
                                worldGenLevel.setBlock(pos, belowBlock, UPDATE_ALL);
                            }
                        }
                    }
                    if (belowState.is(Blocks.GRASS_BLOCK) || !belowState.is(covering)) {
                        int chance = random.nextInt(0, 100);
                        if ((HEIGHT_NOISE.getValue(x, z, false) > 0.33 && chance > Math.min(88, (blockPos.getY()*2)-63)) || (blockPos.getY() == 63 && chance >= 75) || chance >= 99) {
                            if (!HydrolJsonReader.serverSidedOnlyMode && tertiary.is(HydrolBlocks.DRY_GRASS.get())) {
                                worldGenLevel.setBlock(blockPos, primary, UPDATE_ALL);
                                worldGenLevel.setBlock(blockPos.above(), secondary, UPDATE_ALL);
                                worldGenLevel.setBlock(blockPos.above(2), tertiary, UPDATE_ALL);
                            } else {
                                worldGenLevel.setBlock(blockPos, secondary, UPDATE_ALL);
                                worldGenLevel.setBlock(blockPos.above(), tertiary, UPDATE_ALL);
                            }
                        } else {
                            if (!HydrolJsonReader.serverSidedOnlyMode && tertiary.is(HydrolBlocks.DRY_GRASS.get())) {
                                worldGenLevel.setBlock(blockPos, secondary, UPDATE_ALL);
                                worldGenLevel.setBlock(blockPos.above(), tertiary, UPDATE_ALL);
                            } else {
                                worldGenLevel.setBlock(blockPos, primary, UPDATE_ALL);
                            }
                        }
                        placedAnything = true;
                    } else if ((random.nextFloat()*4)+1 >= 4) {
                        if (!HydrolJsonReader.serverSidedOnlyMode && tertiary.is(HydrolBlocks.DRY_GRASS.get())) {
                            worldGenLevel.setBlock(blockPos, tertiary, UPDATE_ALL);
                        } else {
                            worldGenLevel.setBlock(blockPos, primary, UPDATE_ALL);
                        }
                        placedAnything = true;
                    }
                }
            }
        }
        return placedAnything;
    }
}
