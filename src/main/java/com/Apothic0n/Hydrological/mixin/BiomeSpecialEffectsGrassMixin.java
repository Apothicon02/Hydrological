package com.Apothic0n.Hydrological.mixin;

import com.Apothic0n.Hydrological.api.HydrolDensityFunctions;
import com.Apothic0n.Hydrological.api.HydrolColorHelper;
import net.minecraft.world.level.ChunkPos;
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
        if (HydrolDensityFunctions.precomputedMaps.get("temperature") != null && x > 0 && x < HydrolDensityFunctions.worldSize && z > 0 && z < HydrolDensityFunctions.worldSize) {
            cir.setReturnValue(HydrolColorHelper.tintFoliageOrGrass((int) x, 0, (int) z, HydrolDensityFunctions.precomputedMaps.get("temperature").get(ChunkPos.asLong((int) x, (int) z))));
        }
    }
}