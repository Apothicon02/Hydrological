package com.Apothic0n.Hydrological.mixin;

import com.Apothic0n.Hydrological.api.HydrolDensityFunctions;
import com.Apothic0n.Hydrological.api.HydrolJsonReader;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.material.WaterFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WaterFluid.class)
public class WaterFluidMixin {
    @Inject(method = "canConvertToSource", at = @At("HEAD"), cancellable = true)
    public void canConvertToSource(ServerLevel level, CallbackInfoReturnable<Boolean> ci) {
        if (HydrolJsonReader.wavyOcean && HydrolDensityFunctions.changeWaterBehavior && level.dimension().equals(ServerLevel.OVERWORLD)) {
            ci.setReturnValue(false);
        }
    }
}
