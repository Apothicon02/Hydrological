package com.Apothic0n.Hydrological.mixin;

import com.Apothic0n.Hydrological.api.HydrolJsonReader;
import com.Apothic0n.Hydrological.core.objects.CollisionlessLayerBlock;
import com.Apothic0n.Hydrological.core.objects.FragileWallBlock;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.client.renderer.ItemBlockRenderTypes.renderCutout;

@Mixin(ItemBlockRenderTypes.class)
public class ItemBlockRenderTypesMixin {
    @Shadow @Final private static ChunkRenderTypeSet CUTOUT_MIPPED;

    @Shadow @Final private static ChunkRenderTypeSet SOLID;
    @Inject(method = "getRenderLayers(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/neoforged/neoforge/client/ChunkRenderTypeSet;", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getRenderLayers(BlockState state, CallbackInfoReturnable<ChunkRenderTypeSet> ci) {
        if (!HydrolJsonReader.serverSidedOnlyMode) {
            Block block = state.getBlock();
            if (block instanceof CollisionlessLayerBlock || block instanceof FragileWallBlock) {
                ci.setReturnValue(renderCutout ? CUTOUT_MIPPED : SOLID);
            }
        }
    }
}
