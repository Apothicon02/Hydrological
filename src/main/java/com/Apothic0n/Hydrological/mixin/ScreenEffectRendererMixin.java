package com.Apothic0n.Hydrological.mixin;

import com.Apothic0n.Hydrological.api.HydrolJsonReader;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {
    @Inject(method = "getOverlayBlock", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getOverlayBlock(Player player, CallbackInfoReturnable<Pair<BlockState, BlockPos>> ci) {
        if (!HydrolJsonReader.serverSidedOnlyMode && HydrolJsonReader.removeCollisionFromSnowLayers) {
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for (int i = 0; i < 8; ++i) {
                double eye = player.getEyeY();
                double d0 = player.getX() + (double) (((float) ((i >> 0) % 2) - 0.5F) * player.getBbWidth() * 0.8F);
                double d1 = eye + (double) (((float) ((i >> 1) % 2) - 0.5F) * 0.1F);
                double d2 = player.getZ() + (double) (((float) ((i >> 2) % 2) - 0.5F) * player.getBbWidth() * 0.8F);
                blockpos$mutableblockpos.set(d0, d1, d2);
                BlockState state = player.level().getBlockState(blockpos$mutableblockpos);
                if (state.is(Blocks.SNOW)) {
                    double snow = blockpos$mutableblockpos.getY() + (state.getValue(SnowLayerBlock.LAYERS).doubleValue() / 10) + 0.2;
                    if (eye < snow) {
                        ci.setReturnValue(org.apache.commons.lang3.tuple.Pair.of(state, blockpos$mutableblockpos.immutable()));
                    }
                }
            }
        }
    }
}
