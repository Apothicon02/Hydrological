package com.Apothic0n.Hydrological.mixin;

import com.Apothic0n.Hydrological.api.HydrolJsonReader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.world.level.block.PowderSnowBlock.canEntityWalkOnPowderSnow;

@Mixin(SnowLayerBlock.class)
public class SnowLayerBlockMixin extends Block {
    public SnowLayerBlockMixin(Properties p_49795_) {
        super(p_49795_);
    }

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    private void getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> ci) {
        if (!HydrolJsonReader.serverSidedOnlyMode && HydrolJsonReader.removeCollisionFromSnowLayers) {
            if (context instanceof EntityCollisionContext entitycollisioncontext) {
                Entity entity = entitycollisioncontext.getEntity();
                if (entity != null && !canEntityWalkOnPowderSnow(entity)) {
                    ci.setReturnValue(Shapes.empty());
                }
            }
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!HydrolJsonReader.serverSidedOnlyMode && state.is(Blocks.SNOW) && HydrolJsonReader.removeCollisionFromSnowLayers) {
            double eye = entity.getEyeY();
            double snow = pos.getY() + (state.getValue(SnowLayerBlock.LAYERS).doubleValue() / 10) + 0.2;
            if (eye < snow) {
                if (!(entity instanceof LivingEntity) || level.getBlockState(entity.getOnPos()).is(this)) {
                    entity.makeStuckInBlock(state, new Vec3((double) 0.9F, 1.5D, (double) 0.9F));
                    if (level.isClientSide) {
                        RandomSource randomsource = level.getRandom();
                        boolean flag = entity.xOld != entity.getX() || entity.zOld != entity.getZ();
                        if (flag && randomsource.nextBoolean()) {
                            level.addParticle(ParticleTypes.SNOWFLAKE, entity.getX(), (double) (pos.getY() + 1), entity.getZ(), (double) (Mth.randomBetween(randomsource, -1.0F, 1.0F) * 0.083333336F), (double) 0.05F, (double) (Mth.randomBetween(randomsource, -1.0F, 1.0F) * 0.083333336F));
                        }
                    }
                }

                entity.setIsInPowderSnow(true);
                if (!level.isClientSide) {
                    if (entity.isOnFire() && (level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) || entity instanceof Player) && entity.mayInteract(level, pos)) {
                        level.destroyBlock(pos, false);
                    }

                    entity.setSharedFlagOnFire(false);
                }
            }
        }
    }
}
