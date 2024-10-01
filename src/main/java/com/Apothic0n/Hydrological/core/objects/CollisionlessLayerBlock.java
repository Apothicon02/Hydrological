package com.Apothic0n.Hydrological.core.objects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CollisionlessLayerBlock extends SnowLayerBlock implements SimpleWaterloggedBlock {

    public CollisionlessLayerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(BlockStateProperties.LAYERS, 1).setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_56625_, BlockGetter p_56626_, BlockPos p_56627_, CollisionContext p_56628_) {
        return Shapes.empty();
    }

    @Override
    public BlockState updateShape(BlockState p_54440_, Direction p_54441_, BlockState p_54442_, LevelAccessor p_54443_, BlockPos p_54444_, BlockPos p_54445_) {
        if (p_54440_.getValue(BlockStateProperties.WATERLOGGED)) {
            p_54443_.scheduleTick(p_54444_, Fluids.WATER, Fluids.WATER.getTickDelay(p_54443_));
        }

        return !p_54440_.canSurvive(p_54443_, p_54444_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_54440_, p_54441_, p_54442_, p_54443_, p_54444_, p_54445_);
    }

    @Override
    public FluidState getFluidState(BlockState p_221384_) {
        return (Boolean)p_221384_.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_221384_);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_56587_) {
        BlockState $$1 = p_56587_.getLevel().getBlockState(p_56587_.getClickedPos());
        FluidState fluidstate = p_56587_.getLevel().getFluidState(p_56587_.getClickedPos());
        if ($$1.is(this)) {
            int $$2 = $$1.getValue(LAYERS);
            $$1 = $$1.setValue(LAYERS, Math.min(8, $$2 + 1));
        } else {
            $$1 = this.defaultBlockState();
        }
        return $$1.setValue(BlockStateProperties.WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_56613_) {
        p_56613_.add(new Property[]{BlockStateProperties.LAYERS, BlockStateProperties.WATERLOGGED});
    }
}
