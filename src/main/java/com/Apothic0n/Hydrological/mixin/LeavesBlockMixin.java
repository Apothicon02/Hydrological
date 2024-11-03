package com.Apothic0n.Hydrological.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin extends Block implements SimpleWaterloggedBlock, net.minecraftforge.common.IForgeShearable {
    private static final IntegerProperty DISTANCE = IntegerProperty.create("distance", 1, 14);

    public LeavesBlockMixin(Properties p_49795_) {
        super(p_49795_);
    }

    @Inject(at = @At("HEAD"), method = "isRandomlyTicking", cancellable = true)
    public void isRandomlyTicking(BlockState p_54449_, CallbackInfoReturnable<Boolean> ci) {
        ci.setReturnValue(p_54449_.getValue(DISTANCE) == 14 && !p_54449_.getValue(LeavesBlock.PERSISTENT));
    }

    @Inject(at = @At("HEAD"), method = "decaying", cancellable = true)
    protected void decaying(BlockState p_221386_, CallbackInfoReturnable<Boolean> ci) {
        ci.setReturnValue(!p_221386_.getValue(LeavesBlock.PERSISTENT) && p_221386_.getValue(DISTANCE) == 14);
    }

    @Inject(at = @At("HEAD"), method = "getOptionalDistanceAt", cancellable = true)
    private static void getOptionalDistanceAt(BlockState p_277868_, CallbackInfoReturnable<OptionalInt> ci) {
        if (p_277868_.is(BlockTags.LOGS)) {
            ci.setReturnValue(OptionalInt.of(0));
        } else {
            ci.setReturnValue(p_277868_.hasProperty(DISTANCE) ? OptionalInt.of(p_277868_.getValue(DISTANCE)) : OptionalInt.empty());
        }
    }

    @Inject(at = @At("HEAD"), method = "updateDistance", cancellable = true)
    private static void updateDistance(BlockState p_54436_, LevelAccessor p_54437_, BlockPos p_54438_, CallbackInfoReturnable<BlockState> ci) {
        int i = 14;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(Direction direction : Direction.values()) {
            blockpos$mutableblockpos.setWithOffset(p_54438_, direction);
            i = Math.min(i, LeavesBlock.getDistanceAt(p_54437_.getBlockState(blockpos$mutableblockpos)) + 1);
            if (i == 1) {
                break;
            }
        }

        ci.setReturnValue(p_54436_.setValue(DISTANCE, Integer.valueOf(i)));
    }

    @Inject(at = @At("HEAD"), method = "createBlockStateDefinition", cancellable = true)
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_54447_, CallbackInfo ci) {
        p_54447_.add(DISTANCE, LeavesBlock.PERSISTENT, LeavesBlock.WATERLOGGED);
        ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "getStateForPlacement", cancellable = true)
    public void getStateForPlacement(BlockPlaceContext p_54424_, CallbackInfoReturnable<BlockState> ci) {
        FluidState fluidstate = p_54424_.getLevel().getFluidState(p_54424_.getClickedPos());
        BlockState blockstate = this.defaultBlockState().setValue(LeavesBlock.PERSISTENT, Boolean.valueOf(true)).setValue(LeavesBlock.WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
        ci.setReturnValue(LeavesBlock.updateDistance(blockstate, p_54424_.getLevel(), p_54424_.getClickedPos()));
    }
}
