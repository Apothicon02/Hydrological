package com.Apothic0n.Hydrological.mixin;

import com.Apothic0n.Hydrological.api.HydrolJsonReader;
import com.Apothic0n.Hydrological.core.objects.CollisionlessLayerBlock;
import com.Apothic0n.Hydrological.core.objects.FragileWallBlock;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemBlockRenderTypes.class)
public class ItemBlockRenderTypesMixin {

    @Inject(method = "getRenderLayers", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getRenderLayers(BlockState state, CallbackInfoReturnable<ChunkRenderTypeSet> ci) {
        if (!HydrolJsonReader.serverSidedOnlyMode) {
            Block block = state.getBlock();
            if (block instanceof FragileWallBlock || block instanceof CollisionlessLayerBlock) {
                ci.setReturnValue(ItemBlockRenderTypes.renderCutout ? ChunkRenderTypeSet.of(RenderType.cutoutMipped()) : ChunkRenderTypeSet.of(RenderType.solid()));
            }
        }
    }

    @Inject(method = "getChunkRenderType", at = @At("HEAD"), cancellable = true)
    private static void getChunkRenderType(BlockState state, CallbackInfoReturnable<ChunkRenderTypeSet> ci) {
        if (!HydrolJsonReader.serverSidedOnlyMode) {
            Block block = state.getBlock();
            if (block instanceof FragileWallBlock || block instanceof CollisionlessLayerBlock) {
                ci.setReturnValue(ItemBlockRenderTypes.renderCutout ? ChunkRenderTypeSet.of(RenderType.cutoutMipped()) : ChunkRenderTypeSet.of(RenderType.solid()));
            }
        }
    }
}
