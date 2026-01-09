package com.Apothic0n.Hydrological.mixin;

import com.Apothic0n.Hydrological.api.HydrolDensityFunctions;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.joml.SimplexNoise;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.Apothic0n.Hydrological.api.HydrolDensityFunctions.floatingIslandsSeaOffset;

@Mixin(NoiseBasedChunkGenerator.class)
public abstract class NoiseBasedChunkGeneratorMixin {

    @Shadow protected abstract NoiseChunk createNoiseChunk(ChunkAccess p_224257_, StructureManager p_224258_, Blender p_224259_, RandomState p_224260_);

    @Shadow @Final private Holder<NoiseGeneratorSettings> settings;

    @Shadow protected abstract BlockState debugPreliminarySurfaceLevel(NoiseChunk p_198232_, int p_198233_, int p_198234_, int p_198235_, BlockState p_198236_);

    /**
     * @author Apothicon
     * @reason Prevent flooded caves, and adds lava / water aquifers to them.
     */
    @Inject(method = "doFill", at = @At("HEAD"), cancellable = true)
    private void doFill(Blender blender, StructureManager structureManager, RandomState randomState, ChunkAccess chunkAccess, int p_224289_, int p_224290_,  CallbackInfoReturnable<ChunkAccess> ci) {
        NoiseChunk $$6 = chunkAccess.getOrCreateNoiseChunk((p_224255_) -> {
            return this.createNoiseChunk(p_224255_, structureManager, blender, randomState);
        });
        Heightmap $$7 = chunkAccess.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        Heightmap $$8 = chunkAccess.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
        ChunkPos $$9 = chunkAccess.getPos();
        int $$10 = $$9.getMinBlockX();
        int $$11 = $$9.getMinBlockZ();
        Aquifer $$12 = $$6.aquifer();
        $$6.initializeForFirstCellX();
        BlockPos.MutableBlockPos $$13 = new BlockPos.MutableBlockPos();
        int $$14 = $$6.cellWidth;
        int $$15 = $$6.cellHeight;
        int $$16 = 16 / $$14;
        int $$17 = 16 / $$14;

        for(int $$18 = 0; $$18 < $$16; ++$$18) {
            $$6.advanceCellX($$18);

            for(int $$19 = 0; $$19 < $$17; ++$$19) {
                int $$20 = chunkAccess.getSectionsCount() - 1;
                LevelChunkSection $$21 = chunkAccess.getSection($$20);

                for(int $$22 = p_224290_ - 1; $$22 >= 0; --$$22) {
                    $$6.selectCellYZ($$22, $$19);

                    for(int $$23 = $$15 - 1; $$23 >= 0; --$$23) {
                        int $$24 = (p_224289_ + $$22) * $$15 + $$23;
                        int $$25 = $$24 & 15;
                        int $$26 = chunkAccess.getSectionIndex($$24);
                        if ($$20 != $$26) {
                            $$20 = $$26;
                            $$21 = chunkAccess.getSection($$26);
                        }

                        double $$27 = (double)$$23 / (double)$$15;
                        $$6.updateForY($$24, $$27);

                        for(int $$28 = 0; $$28 < $$14; ++$$28) {
                            int $$29 = $$10 + $$18 * $$14 + $$28;
                            int $$30 = $$29 & 15;
                            double $$31 = (double)$$28 / (double)$$14;
                            $$6.updateForX($$29, $$31);

                            for(int $$32 = 0; $$32 < $$14; ++$$32) {
                                int $$33 = $$11 + $$19 * $$14 + $$32;
                                int $$34 = $$33 & 15;
                                double $$35 = (double)$$32 / (double)$$14;
                                $$6.updateForZ($$33, $$35);
                                BlockState state = $$6.getInterpolatedState();
                                if (state == null) {
                                    state = ((NoiseGeneratorSettings)this.settings.value()).defaultBlock();
                                }

                                state = this.debugPreliminarySurfaceLevel($$6, $$29, $$24, $$33, state);
                                if (HydrolDensityFunctions.generateAquifers) {
                                    if (HydrolDensityFunctions.isFloatingIslands && $$24 > 0) {
                                        if (state.isAir()) {
                                            int newY = (42 + (floatingIslandsSeaOffset / 2)) - (int) (Math.abs(SimplexNoise.noise($$29 * 0.0007F, $$33 * 0.0007F)) * 128);
                                            if (newY > $$24) {
                                                state = Blocks.WATER.defaultBlockState();
                                            }
                                        }
                                    } else {
                                        if (state != Blocks.AIR.defaultBlockState() && !SharedConstants.debugVoidTerrain(chunkAccess.getPos())) {
                                            if (state == Blocks.WATER.defaultBlockState() || state == Blocks.LAVA.defaultBlockState()) {
                                                int newY = 16;
                                                for (int currentY = 240; currentY > 16; currentY = currentY - 4) {
                                                    if (chunkAccess.getBlockState(new BlockPos($$29, currentY, $$33)).isSolid()) {
                                                        newY = currentY;
                                                        currentY = 0;
                                                    }
                                                }
                                                if (newY - 20 > $$24) {
                                                    state = Blocks.CAVE_AIR.defaultBlockState();
                                                }
                                                if (newY <= 48 && $$24 < -55) {
                                                    state = Blocks.WATER.defaultBlockState();
                                                }
                                            }
                                        }
                                        if (state.isAir() && $$24 < -55 && chunkAccess.getBlockState(new BlockPos($$29, chunkAccess.getMinBuildHeight(), $$33)).isSolid()) {
                                            state = Blocks.LAVA.defaultBlockState();
                                        }
                                    }
                                }
                                $$21.setBlockState($$30, $$25, $$34, state, false);
                                $$7.update($$30, $$24, $$34, state);
                                $$8.update($$30, $$24, $$34, state);
                                if ($$12.shouldScheduleFluidUpdate() && !state.getFluidState().isEmpty()) {
                                    $$13.set($$29, $$24, $$33);
                                    chunkAccess.markPosForPostprocessing($$13);
                                }
                            }
                        }
                    }
                }
            }

            $$6.swapSlices();
        }

        $$6.stopInterpolation();
        ci.setReturnValue(chunkAccess);
    }
}
