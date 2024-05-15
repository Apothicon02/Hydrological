package com.Apothic0n.Hydrological.api.biome.features.types;

import com.Apothic0n.Hydrological.api.HydrolDensityFunctions;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import org.joml.SimplexNoise;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public class ArchFeature extends Feature<SimpleBlockConfiguration> {

    public ArchFeature(Codec<SimpleBlockConfiguration> pContext) {
        super(pContext);
    }

    @Override
    public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> pContext) {
        WorldGenLevel worldGenLevel = pContext.level();
        BlockPos origin = pContext.origin();
        RandomSource random = pContext.random();
        BlockState blockState = pContext.config().toPlace().getState(random, origin);
        boolean placedAnything = false;
        for (int x = origin.getX(); x < origin.getX() + 16; ++x) {
            for (int z = origin.getZ(); z < origin.getZ() + 16; ++z) {
                if (HydrolDensityFunctions.isFloatingIslands) {
                    double noise = Math.abs(SimplexNoise.noise(x*0.0007F, z*0.0007F));
                    int y = (int) (42 - (noise * 128));
                    for (int newY = y; newY >= worldGenLevel.getMinBuildHeight(); newY--) {
                        BlockPos blockPos = new BlockPos(x, newY, z);
                        if (worldGenLevel.getBlockState(blockPos).isAir()) {
                            worldGenLevel.setBlock(blockPos, Blocks.WATER.defaultBlockState(), UPDATE_ALL);
                            placedAnything = true;
                        }
                    }
                    y = (int) (8 - (noise * -420));
                    blockState = Blocks.BLUE_ICE.defaultBlockState();
                    double areaNoise = SimplexNoise.noise(x*0.003F, z*0.003F);
                    if (areaNoise > -0.01 && areaNoise < 0.01) {
                        //lower
                        placeCube(worldGenLevel, x, y, z, blockState, random);
                        placedAnything = true;
                    }
                    double secondaryAreaNoise = SimplexNoise.noise(x*0.0015F, z*0.0015F);
                    if (secondaryAreaNoise > -0.01 && secondaryAreaNoise < 0.01) {
                        //secondary
                        placeCube(worldGenLevel, x, y, z, blockState, random);
                        placedAnything = true;
                    }
                } else {
                    double noise = Math.abs(SimplexNoise.noise(x*0.002F, z*0.002F));
                    int y = (int) (32 - (noise * 320));
                    double areaNoise = SimplexNoise.noise(x*0.005F, z*0.005F);
                    if (areaNoise > -0.01 && areaNoise < 0.01) {
                        //caves
                        if (y > worldGenLevel.getMinBuildHeight() + 6) {
                            placeCube(worldGenLevel, x, y, z, blockState, random);
                            placedAnything = true;
                        }
                        //underwater
                        y = (int) (54 - (noise * 196));
                        if (y > -64) {
                            placeCube(worldGenLevel, x, y, z, blockState, random);
                            placedAnything = true;
                        }
                    }
                }
            }
        }
        return placedAnything;
    }

    private void placeCube(WorldGenLevel worldGenLevel, int ogX, int ogY, int ogZ, BlockState blockState, RandomSource random) {
        for (int x = ogX-2; x <= ogX+2; x++) {
            for (int y = ogY-2; y <= ogY+2; y++) {
                for (int z = ogZ - 2; z <= ogZ + 2; z++) {
                    BlockPos blockPos = new BlockPos(x, y, z);
                    BlockState beingReplaced = worldGenLevel.getBlockState(blockPos);
                    if (!beingReplaced.isSolid() || HydrolDensityFunctions.isFloatingIslands) {
                        BlockState placeState = blockState;
                        Holder<Biome> biome = worldGenLevel.getBiome(blockPos);
                        String biomeName = biome.toString();
                        if (!HydrolDensityFunctions.isFloatingIslands) {
                            if (worldGenLevel.getBlockState(blockPos).is(Blocks.LAVA)) {
                                placeState = Blocks.DEEPSLATE_COAL_ORE.defaultBlockState();
                            } else {
                                if (y <= ogY) {
                                    if (y < 6) {
                                        BlockState maybeNewState = getLowerCaveBlock(biome, biomeName, beingReplaced);
                                        if (!maybeNewState.isAir()) {
                                            placeState = maybeNewState;
                                        }
                                    } else if (y >= 62) {
                                        placeState = Blocks.DIORITE.defaultBlockState();
                                    } else {
                                        BlockState maybeNewState = getLowerOceanBlock(biome, biomeName);
                                        if (!maybeNewState.isAir()) {
                                            placeState = maybeNewState;
                                        }
                                    }
                                } else {
                                    if (y < 6) {
                                        BlockState maybeNewState = getCaveBlock(biome, biomeName, beingReplaced);
                                        if (!maybeNewState.isAir()) {
                                            placeState = maybeNewState;
                                        }
                                    } else if (y >= 62) {
                                        placeState = Blocks.CALCITE.defaultBlockState();
                                    } else {
                                        BlockState maybeNewState = getOceanBlock(biome, biomeName);
                                        if (!maybeNewState.isAir()) {
                                            placeState = maybeNewState;
                                        }
                                    }
                                }
                            }
                        }
                        worldGenLevel.setBlock(blockPos, placeState, UPDATE_ALL);
                    }
                }
            }
        }
    }

    private BlockState getLowerOceanBlock(Holder<Biome> biome, String biomeName) {
        if (!biome.is(Biomes.WARM_OCEAN) && !biome.is(Biomes.LUKEWARM_OCEAN) && !biome.is(Biomes.DEEP_LUKEWARM_OCEAN)) {
            if (biome.is(Biomes.OCEAN) || biome.is(Biomes.DEEP_OCEAN)) {
                return Blocks.TUBE_CORAL_BLOCK.defaultBlockState();
            } else if (biomeName.contains("caldera") || biome.is(Biomes.RIVER)) {
                return Blocks.DIORITE.defaultBlockState();
            } else {
                return Blocks.BLUE_ICE.defaultBlockState();
            }
        }
        return Blocks.AIR.defaultBlockState();
    }

    private BlockState getOceanBlock(Holder<Biome> biome, String biomeName) {
        if (!biome.is(Biomes.WARM_OCEAN) && !biome.is(Biomes.LUKEWARM_OCEAN) && !biome.is(Biomes.DEEP_LUKEWARM_OCEAN)) {
            if (biome.is(Biomes.OCEAN) || biome.is(Biomes.DEEP_OCEAN)) {
               return Blocks.PRISMARINE.defaultBlockState();
            } else if (biomeName.contains("caldera") || biome.is(Biomes.RIVER)) {
                return Blocks.CALCITE.defaultBlockState();
            } else {
                return Blocks.ICE.defaultBlockState();
            }
        }
        return Blocks.AIR.defaultBlockState();
    }
    private BlockState getLowerCaveBlock(Holder<Biome> biome, String biomeName, BlockState beingReplaced) {
        if (biomeName.contains("glacial_caves")) {
            return Blocks.BLUE_ICE.defaultBlockState();
        } else if (biomeName.contains("caldera") || biomeName.contains("molten_caves")) {
            return Blocks.DIORITE.defaultBlockState();
        } else if (!beingReplaced.is(Blocks.WATER)) {
            return Blocks.MUDDY_MANGROVE_ROOTS.defaultBlockState();
        } else {
            return getOceanBlock(biome, biomeName);
        }
    }

    private BlockState getCaveBlock(Holder<Biome> biome, String biomeName, BlockState beingReplaced) {
        if (biomeName.contains("glacial_caves")) {
            return Blocks.ICE.defaultBlockState();
        } else if (biomeName.contains("caldera") || biomeName.contains("molten_caves") || biomeName.contains("geodeial_caves")) {
            return Blocks.CALCITE.defaultBlockState();
        } else if (!beingReplaced.is(Blocks.WATER)) {
            return Blocks.MANGROVE_ROOTS.defaultBlockState();
        } else {
            return getOceanBlock(biome, biomeName);
        }
    }
}
