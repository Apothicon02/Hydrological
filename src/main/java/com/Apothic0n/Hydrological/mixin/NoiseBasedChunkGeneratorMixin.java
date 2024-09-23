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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.Apothic0n.Hydrological.api.HydrolMath.unprogressBetweenInts;

@Mixin(NoiseBasedChunkGenerator.class)
public abstract class NoiseBasedChunkGeneratorMixin {

    @Shadow protected abstract NoiseChunk createNoiseChunk(ChunkAccess p_224257_, StructureManager p_224258_, Blender p_224259_, RandomState p_224260_);

    @Shadow @Final private Holder<NoiseGeneratorSettings> settings;

    @Shadow protected abstract BlockState debugPreliminarySurfaceLevel(NoiseChunk p_198232_, int p_198233_, int p_198234_, int p_198235_, BlockState p_198236_);

    /**
     * @author Apothic0n
     * @reason Removes lava aquifers.
     */
    @Inject(method = "createFluidPicker", at = @At("HEAD"), cancellable = true)
    private static void createFluidPicker(NoiseGeneratorSettings noiseGeneratorSettings, CallbackInfoReturnable<Aquifer.FluidPicker> ci) {
        int y = -128;
        Aquifer.FluidStatus aquifer$fluidstatus = new Aquifer.FluidStatus(y, Blocks.LAVA.defaultBlockState());
        int sea = noiseGeneratorSettings.seaLevel();
        Aquifer.FluidStatus aquifer$fluidstatus1 = new Aquifer.FluidStatus(sea, noiseGeneratorSettings.defaultFluid());
        ci.setReturnValue((p_224274_, p_224275_, p_224276_) -> {
            return p_224275_ < Math.min(y, sea) ? aquifer$fluidstatus : aquifer$fluidstatus1;
        });
    }

    /**
     * @author Apothicon
     * @reason Prevent flooded caves, and adds lava rivers to them.
     */
    @Overwrite
    private ChunkAccess doFill(Blender p_224285_, StructureManager p_224286_, RandomState p_224287_, ChunkAccess p_224288_, int p_224289_, int p_224290_) {
        NoiseChunk $$6 = p_224288_.getOrCreateNoiseChunk((p_224255_) -> {
            return this.createNoiseChunk(p_224255_, p_224286_, p_224285_, p_224287_);
        });
        Heightmap $$7 = p_224288_.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        Heightmap $$8 = p_224288_.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
        ChunkPos $$9 = p_224288_.getPos();
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
                int $$20 = p_224288_.getSectionsCount() - 1;
                LevelChunkSection $$21 = p_224288_.getSection($$20);

                for(int $$22 = p_224290_ - 1; $$22 >= 0; --$$22) {
                    $$6.selectCellYZ($$22, $$19);

                    for(int $$23 = $$15 - 1; $$23 >= 0; --$$23) {
                        int $$24 = (p_224289_ + $$22) * $$15 + $$23;
                        int $$25 = $$24 & 15;
                        int $$26 = p_224288_.getSectionIndex($$24);
                        if ($$20 != $$26) {
                            $$20 = $$26;
                            $$21 = p_224288_.getSection($$26);
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
                                BlockState $$36 = $$6.getInterpolatedState();
                                if ($$36 == null) {
                                    $$36 = ((NoiseGeneratorSettings)this.settings.value()).defaultBlock();
                                }

                                $$36 = this.debugPreliminarySurfaceLevel($$6, $$29, $$24, $$33, $$36);
                                if ($$36 != Blocks.AIR.defaultBlockState() && !SharedConstants.debugVoidTerrain(p_224288_.getPos())) {
                                    if ($$36 == Blocks.WATER.defaultBlockState()) {
                                        int newY = unprogressBetweenInts(16, 239, HydrolDensityFunctions.heightmap.get(ChunkPos.asLong($$29/4, $$33/4)));
                                        if (newY-20 > $$24) {
                                            $$36 = Blocks.CAVE_AIR.defaultBlockState();
                                        }
                                    }
                                    if (!$$36.isSolid() && $$24 < -55) {
                                        $$36 = Blocks.LAVA.defaultBlockState();
                                    }
                                    $$21.setBlockState($$30, $$25, $$34, $$36, false);
                                    $$7.update($$30, $$24, $$34, $$36);
                                    $$8.update($$30, $$24, $$34, $$36);
                                    if ($$12.shouldScheduleFluidUpdate() && !$$36.getFluidState().isEmpty()) {
                                        $$13.set($$29, $$24, $$33);
                                        p_224288_.markPosForPostprocessing($$13);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            $$6.swapSlices();
        }

        $$6.stopInterpolation();
        return p_224288_;
    }
}
