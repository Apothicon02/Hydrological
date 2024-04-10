package com.Apothic0n.Hydrological.mixin;

import com.Apothic0n.Hydrological.api.HydrolDensityFunctions;
import com.Apothic0n.Hydrological.api.HydrolColorHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.DensityFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.level.biome.BiomeSpecialEffects$GrassColorModifier$1")
public class BiomeSpecialEffectsGrassMixin {

    /**
     * @author Apothicon
     * @reason Adds grass discoloration.
     */
    @Inject(at = @At("RETURN"), method = "modifyColor", cancellable = true)
    private void modifyColor(double x, double z, int unusedColor, CallbackInfoReturnable<Integer> cir) {
        if (HydrolDensityFunctions.temperature != null && HydrolDensityFunctions.humidity != null) {
            cir.setReturnValue(HydrolColorHelper.tintFoliageOrGrass(Blocks.GRASS_BLOCK.defaultBlockState(), (int) x, 0, (int) z, HydrolDensityFunctions.temperature.compute(new DensityFunction.SinglePointContext((int) x, 0, (int) z)),
                    HydrolDensityFunctions.humidity.compute(new DensityFunction.SinglePointContext((int) x, 0, (int) z)), false));
        }
    }
}