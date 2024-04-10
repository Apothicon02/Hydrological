package com.Apothic0n.Hydrological.mixin;

import com.Apothic0n.Hydrological.api.HydrolColorHelper;
import com.Apothic0n.Hydrological.api.HydrolDensityFunctions;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeColors.class)
public abstract class BiomeColorsMixin {

    /**
     * @author Apothicon
     * @reason Custom foliage tinting
     */
    @Inject(method = "getAverageFoliageColor", at = @At("HEAD"), cancellable = true)
    private static void getAverageFoliageColor(BlockAndTintGetter blockAndTintGetter, BlockPos blockPos, CallbackInfoReturnable<Integer> ci) {
        if (HydrolDensityFunctions.temperature != null && HydrolDensityFunctions.humidity != null) {
            int x = blockPos.getX();
            int y = blockPos.getY();
            int z = blockPos.getZ();
            ci.setReturnValue(HydrolColorHelper.tintFoliageOrGrass(blockAndTintGetter.getBlockState(blockPos), x, y, z, HydrolDensityFunctions.temperature.compute(new DensityFunction.SinglePointContext(x, y, z)),
                    HydrolDensityFunctions.humidity.compute(new DensityFunction.SinglePointContext(x, y, z)), true));
        }
    }
}
