package com.Apothic0n.Hydrological.mixin;

import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ChunkMap.class, priority = 69420)
public abstract class ChunkMapMixin {

    @Inject(method = "save", at = @At("HEAD"), cancellable = true)
    private void save(ChunkAccess chunk, CallbackInfoReturnable<Boolean> ci) {
        ci.setReturnValue(false);
        ci.cancel();
    }
}
