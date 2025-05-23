package com.Apothic0n.Hydrological.mixin;

import com.Apothic0n.Hydrological.api.HydrolJsonReader;
import com.Apothic0n.Hydrological.core.objects.HydrolParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TallGrassBlock.class)
public abstract class TallGrassBlockMixin extends BushBlock {

    public TallGrassBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        super.animateTick(blockState, level, blockPos, randomSource);
        if (!HydrolJsonReader.serverSidedOnlyMode) {
            if (randomSource.nextInt(33) == 0 && level.getDayTime() > 12750 && level.getDayTime() < 23500 && HydrolJsonReader.fireflies) {
                level.addAlwaysVisibleParticle((ParticleOptions) HydrolParticleTypes.FIRE_FLIES.get(), true, blockPos.getX(), blockPos.getY() + randomSource.nextDouble() * 4, blockPos.getZ(), 0, 0, 0);
            }
        }
    }
}
