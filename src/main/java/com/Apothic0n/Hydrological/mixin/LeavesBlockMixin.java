package com.Apothic0n.Hydrological.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin extends Block implements SimpleWaterloggedBlock, net.minecraftforge.common.IForgeShearable {
    @Shadow protected abstract boolean decaying(BlockState p_221386_);

    @Unique
    private static final IntegerProperty hydrological$DISTANCE = IntegerProperty.create("hydrol_distance", 1, 21);

    public LeavesBlockMixin(Properties p_49795_) {
        super(p_49795_);
    }

    @Inject(at = @At("HEAD"), method = "createBlockStateDefinition")
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_54447_, CallbackInfo ci) {
        p_54447_.add(hydrological$DISTANCE);
    }

    @Unique
    private static int hydrological$getThing(BlockState neighbor, int e) {
        int dist = 1;
        if (!neighbor.is(BlockTags.LOGS)) {
            if (neighbor.hasProperty(hydrological$DISTANCE)) {
                dist += neighbor.getValue(hydrological$DISTANCE);
            } else {
                dist = 21;
            }
        }
        return Math.min(e, dist);
    }

    @Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
    private void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (state.hasProperty(hydrological$DISTANCE)) {
            if (this.decaying(state)) {
                dropResources(state, level, pos);
                level.removeBlock(pos, false);
            }
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "updateDistance", cancellable = true)
    private static void updateDistance(BlockState state, LevelAccessor level, BlockPos pos, CallbackInfoReturnable<BlockState> ci) {
        if (state.hasProperty(hydrological$DISTANCE)) {
            ci.setReturnValue(hydrological$getDistance(state, level, pos));
        }
    }

    @Unique
    private static BlockState hydrological$getDistance(BlockState state, LevelAccessor level, BlockPos pos) {
        int e = hydrological$getThing(level.getBlockState(pos.north()), hydrological$getThing(level.getBlockState(pos.east()),
                hydrological$getThing(level.getBlockState(pos.south()), hydrological$getThing(level.getBlockState(pos.west()),
                        hydrological$getThing(level.getBlockState(pos.above()), hydrological$getThing(level.getBlockState(pos.below()), 21))))));

        return state.setValue(LeavesBlock.DISTANCE, Math.max(1, e / 3)).setValue(hydrological$DISTANCE, e);
    }

    @Inject(at = @At("HEAD"), method = "getStateForPlacement", cancellable = true)
    public void getStateForPlacement(BlockPlaceContext context, CallbackInfoReturnable<BlockState> ci) {
        if (this.defaultBlockState().hasProperty(hydrological$DISTANCE)) {
            LevelAccessor level = context.getLevel();
            BlockPos pos = context.getClickedPos();

            ci.setReturnValue(LeavesBlock.updateDistance(this.defaultBlockState().setValue(LeavesBlock.PERSISTENT, Boolean.valueOf(true)).setValue(LeavesBlock.WATERLOGGED, Boolean.valueOf(level.getFluidState(pos).getType() == Fluids.WATER)), level, pos));
        }
    }
}
