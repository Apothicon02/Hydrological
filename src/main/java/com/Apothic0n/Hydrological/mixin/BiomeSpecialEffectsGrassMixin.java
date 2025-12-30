package com.Apothic0n.Hydrological.mixin;

import com.Apothic0n.Hydrological.api.HydrolColorHelper;
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
        cir.setReturnValue(HydrolColorHelper.tintFoliageOrGrass((int) x, 0, (int) z));
    }
}