package com.Apothic0n.Hydrological.mixin;

import com.Apothic0n.Hydrological.api.HydrolDensityFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Biome.class, priority = 69420)
public abstract class BiomeMixin {
    
    /**
     * @author Apothicon
     * @reason Allows water to freeze in areas cold enough even in biomes without snow.
     */
    @Inject(method = "shouldFreeze(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Z)Z", at = @At("HEAD"), cancellable = true)
    public void shouldFreeze(LevelReader level, BlockPos blockPos, boolean bool, CallbackInfoReturnable<Boolean> ci) {
        boolean shouldFreeze = false;
        if (blockPos.getX() > 0 && blockPos.getX() < HydrolDensityFunctions.worldSize && blockPos.getZ() > 0 && blockPos.getZ() < HydrolDensityFunctions.worldSize) {
            if (HydrolDensityFunctions.precomputedMaps.get("temperature").get(ChunkPos.asLong(blockPos.getX(), blockPos.getZ())) < -0.8) {
                if (blockPos.getY() >= level.getMinBuildHeight() && blockPos.getY() < level.getMaxBuildHeight() && level.getBrightness(LightLayer.BLOCK, blockPos) < 10) {
                    BlockState blockstate = level.getBlockState(blockPos);
                    FluidState fluidstate = level.getFluidState(blockPos);
                    if (fluidstate.getType() == Fluids.WATER && blockstate.getBlock() instanceof LiquidBlock) {
                        if (!bool) {
                            shouldFreeze = true;
                        } else {
                            boolean flag = level.isWaterAt(blockPos.west()) && level.isWaterAt(blockPos.east()) && level.isWaterAt(blockPos.north()) && level.isWaterAt(blockPos.south());
                            if (!flag) {
                                shouldFreeze = true;
                            }
                        }
                    }
                }
            }
            ci.setReturnValue(shouldFreeze);
        }
    }
}
