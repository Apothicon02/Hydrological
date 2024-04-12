package com.Apothic0n.Hydrological.mixin;

import net.minecraft.world.level.FoliageColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(FoliageColor.class)
public class FoliageColorMixin {

    /**
     * @author Apothicon
     * @reason Makes birch leaves yellow.
     */
    @Inject(method = "getBirchColor", at = @At("HEAD"), cancellable = true)
    private static void getBirchColor(CallbackInfoReturnable<Integer> ci) {
        ci.setReturnValue(Color.YELLOW.getRGB());
    }
}
