package com.Apothic0n.Hydrological.api;

import com.Apothic0n.Hydrological.Hydrological;
import com.google.common.base.Suppliers;
import com.google.common.collect.Sets;
import com.mojang.serialization.MapCodec;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.joml.SimplexNoise;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static com.Apothic0n.Hydrological.api.HydrolDensityFunctions.floatingIslandsSeaOffset;


public final class HydrolChunkGenerators {
    public static final DeferredRegister<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATORS = DeferredRegister.create(Registries.CHUNK_GENERATOR, Hydrological.MODID);
    public static final DeferredHolder<MapCodec<? extends ChunkGenerator>, ?> NOISE_GENERATOR = CHUNK_GENERATORS.register("noise", HydrolChunkGenerators.HydrolChunkGenerator.NEW_CODEC::codec);

    public static void register(IEventBus eventBus) {
        CHUNK_GENERATORS.register(eventBus);
    }

    static <O> KeyDispatchDataCodec<O> makeCodec(MapCodec<O> codec) {
        return KeyDispatchDataCodec.of(codec);
    }

    public static class HydrolChunkGenerator extends NoiseBasedChunkGenerator {
        public static final KeyDispatchDataCodec<NoiseBasedChunkGenerator> NEW_CODEC = HydrolChunkGenerators.makeCodec(CODEC);
        public final Holder<NoiseGeneratorSettings> settings;
        private final Supplier<Aquifer.FluidPicker> globalFluidPicker;

        public HydrolChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings) {
            super(biomeSource, settings);
            this.settings = settings;
            this.globalFluidPicker = Suppliers.memoize(() -> createFluidPicker(settings.value()));
        }

        private static Aquifer.FluidPicker createFluidPicker(NoiseGeneratorSettings settings) {
            Aquifer.FluidStatus aquifer$fluidstatus = new Aquifer.FluidStatus(-54, Blocks.LAVA.defaultBlockState());
            int i = settings.seaLevel();
            Aquifer.FluidStatus aquifer$fluidstatus1 = new Aquifer.FluidStatus(i, settings.defaultFluid());
            Aquifer.FluidStatus aquifer$fluidstatus2 = new Aquifer.FluidStatus(DimensionType.MIN_Y * 2, Blocks.AIR.defaultBlockState());
            return (p_224274_, p_224275_, p_224276_) -> p_224275_ < Math.min(-54, i) ? aquifer$fluidstatus : aquifer$fluidstatus1;
        }

        private NoiseChunk createNoiseChunk(ChunkAccess chunk, StructureManager structureManager, Blender blender, RandomState random) {
            return NoiseChunk.forChunk(
                    chunk,
                    random,
                    Beardifier.forStructuresInChunk(structureManager, chunk.getPos()),
                    this.settings.value(),
                    this.globalFluidPicker.get(),
                    blender
            );
        }

        @Override
        public @NotNull CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
            NoiseSettings noisesettings = this.settings.value().noiseSettings().clampToHeightAccessor(chunk.getHeightAccessorForGeneration());
            int i = noisesettings.minY();
            int j = Mth.floorDiv(i, noisesettings.getCellHeight());
            int k = Mth.floorDiv(noisesettings.height(), noisesettings.getCellHeight());
            return k <= 0 ? CompletableFuture.completedFuture(chunk) : CompletableFuture.supplyAsync(Util.wrapThreadWithTaskName("wgen_fill_noise", () -> {
//                int l = chunk.getSectionIndex(k * noisesettings.getCellHeight() - 1 + i);
//                int i1 = chunk.getSectionIndex(i);
//                Set<LevelChunkSection> set = Sets.newHashSet();
//
//                for (int j1 = l; j1 >= i1; j1--) {
//                    LevelChunkSection levelchunksection = chunk.getSection(j1);
//                    levelchunksection.acquire();
//                    set.add(levelchunksection);
//                }
//
//                ChunkAccess chunkaccess;
//                try {
//                    chunkaccess = this.doFill(blender, structureManager, randomState, chunk, j, k);
//                } finally {
//                    for (LevelChunkSection levelchunksection1 : set) {
//                        levelchunksection1.release();
//                    }
//                }
//                return chunkAccess;

                return chunk;
            }), Util.backgroundExecutor());
        }

        private ChunkAccess doFill(Blender blender, StructureManager structureManager, RandomState random, ChunkAccess chunk, int minCellY, int cellCountY) {
            NoiseChunk noisechunk = chunk.getOrCreateNoiseChunk(p_224255_ -> this.createNoiseChunk(p_224255_, structureManager, blender, random));
            Heightmap heightmap = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
            Heightmap heightmap1 = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
            ChunkPos chunkpos = chunk.getPos();
            int i = chunkpos.getMinBlockX();
            int j = chunkpos.getMinBlockZ();
            Aquifer aquifer = noisechunk.aquifer();
            noisechunk.initializeForFirstCellX();
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            int k = noisechunk.cellWidth;
            int l = noisechunk.cellHeight;
            int i1 = 16 / k;
            int j1 = 16 / k;

            for (int k1 = 0; k1 < i1; k1++) {
                noisechunk.advanceCellX(k1);

                for (int l1 = 0; l1 < j1; l1++) {
                    int i2 = chunk.getSectionsCount() - 1;
                    LevelChunkSection levelchunksection = chunk.getSection(i2);

                    for (int j2 = cellCountY - 1; j2 >= 0; j2--) {
                        noisechunk.selectCellYZ(j2, l1);

                        for (int k2 = l - 1; k2 >= 0; k2--) {
                            int l2 = (minCellY + j2) * l + k2;
                            int i3 = l2 & 15;
                            int j3 = chunk.getSectionIndex(l2);
                            if (i2 != j3) {
                                i2 = j3;
                                levelchunksection = chunk.getSection(j3);
                            }

                            double d0 = (double) k2 / (double) l;
                            noisechunk.updateForY(l2, d0);

                            for (int k3 = 0; k3 < k; k3++) {
                                int l3 = i + k1 * k + k3;
                                int i4 = l3 & 15;
                                double d1 = (double) k3 / (double) k;
                                noisechunk.updateForX(l3, d1);

                                for (int j4 = 0; j4 < k; j4++) {
                                    int k4 = j + l1 * k + j4;
                                    int l4 = k4 & 15;
                                    double d2 = (double) j4 / (double) k;
                                    noisechunk.updateForZ(k4, d2);
                                    BlockState blockstate = noisechunk.getInterpolatedState();
                                    if (blockstate == null) {
                                        blockstate = this.settings.value().defaultBlock();
                                    }

                                    if (HydrolDensityFunctions.generateAquifers) {
                                        if (HydrolDensityFunctions.isFloatingIslands && l2 > 0) {
                                            if (blockstate.isAir()) {
                                                int newY = (42 + (floatingIslandsSeaOffset / 2)) - (int) (Math.abs(SimplexNoise.noise(l3 * 0.0007F, k4 * 0.0007F)) * 128);
                                                if (newY > l2) {
                                                    blockstate = Blocks.WATER.defaultBlockState();
                                                }
                                            }
                                        } else {
                                            if (blockstate != Blocks.AIR.defaultBlockState() && !SharedConstants.debugVoidTerrain(chunk.getPos())) {
                                                if (blockstate == Blocks.WATER.defaultBlockState() || blockstate == Blocks.LAVA.defaultBlockState()) {
                                                    int newY = 16;
                                                    for (int currentY = 240; currentY > 16; currentY = currentY - 4) {
                                                        if (chunk.getBlockState(new BlockPos(l3, currentY, k4)).isSolid()) {
                                                            newY = currentY;
                                                            currentY = 0;
                                                        }
                                                    }
                                                    if (newY - 20 > l2) {
                                                        blockstate = Blocks.CAVE_AIR.defaultBlockState();
                                                    }
                                                    if (newY <= 48 && l2 < -55) {
                                                        blockstate = Blocks.WATER.defaultBlockState();
                                                    }
                                                }
                                            }
                                            if (blockstate.isAir() && l2 < -55 && chunk.getBlockState(new BlockPos(l3, chunk.getMinBuildHeight(), k4)).isSolid()) {
                                                blockstate = Blocks.LAVA.defaultBlockState();
                                            }
                                        }
                                    }
                                    levelchunksection.setBlockState(i4, i3, l4, blockstate, false);
                                    heightmap.update(i4, l2, l4, blockstate);
                                    heightmap1.update(i4, l2, l4, blockstate);
                                    if (aquifer.shouldScheduleFluidUpdate() && !blockstate.getFluidState().isEmpty()) {
                                        blockpos$mutableblockpos.set(l3, l2, k4);
                                        chunk.markPosForPostprocessing(blockpos$mutableblockpos);
                                    }
                                }
                            }
                        }
                    }
                }

                noisechunk.swapSlices();
            }

            noisechunk.stopInterpolation();
            return chunk;
        }
    }
}
