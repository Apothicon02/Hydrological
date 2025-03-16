package com.Apothic0n.Hydrological.mixin;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SaplingBlock.class)
public abstract class SaplingBlockMixin extends BushBlock {
    public SaplingBlockMixin(Properties p_51021_) {
        super(p_51021_);
    }

    @Override
    public abstract MapCodec<? extends BushBlock> codec();

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter getter, BlockPos pos) {
        return state.is(BlockTags.DIRT) || state.is(Blocks.FARMLAND) || state.is(BlockTags.SAND) || state.is(Blocks.GRAVEL) || state.is(BlockTags.SNOW);
    }
}
