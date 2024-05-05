package com.Apothic0n.Hydrological.mixin;

import com.Apothic0n.Hydrological.api.HydrolDensityFunctions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.WaterFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WaterFluid.class)
public class WaterFluidMixin {
    @Inject(method = "canConvertToSource", at = @At("HEAD"), cancellable = true)
    public void canConvertToSource(Level level, CallbackInfoReturnable<Boolean> ci) {
        if (HydrolDensityFunctions.isFloatingIslands == true && level.dimension().equals(Level.OVERWORLD)) {
            ci.setReturnValue(false);
        }
    }
}
