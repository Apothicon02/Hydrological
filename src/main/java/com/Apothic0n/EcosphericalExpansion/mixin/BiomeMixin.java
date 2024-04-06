package com.Apothic0n.EcosphericalExpansion.mixin;

import com.Apothic0n.EcosphericalExpansion.api.EcoDensityFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Biome.class, priority = 69420)
public abstract class BiomeMixin {
    @Shadow
    @Final
    private BiomeSpecialEffects specialEffects;
    @Shadow protected abstract int getGrassColorFromTexture();
    @Shadow @Deprecated protected abstract float getTemperature(BlockPos p_47506_);

    @Unique
    private int biox$getGrassColorFromTexture() {
        if (EcoDensityFunctions.temperature != null) {
            return GrassColor.get(1D, 1D);
        } else {
            return getGrassColorFromTexture();
        }
    }
    @Unique
    public boolean eco$warmEnoughToRain(BlockPos p_198907_) {
        return this.getTemperature(p_198907_) >= -0.8F;
    }

    /**
     * @author Apothicon
     * @reason Prevents it from snowing in warm biomes.
     */
    @Inject(method = "warmEnoughToRain", at = @At("HEAD"), cancellable = true)
    public void warmEnoughToRain(BlockPos blockPos, CallbackInfoReturnable<Boolean> ci) {
        ci.setReturnValue(eco$warmEnoughToRain(blockPos));
    }

    /**
     * @author Apothicon
     * @reason Prevents snow from generating outside snowy biomes.
     */
    @Inject(method = "shouldSnow", at = @At("HEAD"), cancellable = true)
    public void shouldSnow(LevelReader levelReader, BlockPos blockPos, CallbackInfoReturnable<Boolean> ci) {
        if (this.eco$warmEnoughToRain(blockPos)) {
            ci.setReturnValue(false);
        } else if (blockPos.getY() >= levelReader.getMinBuildHeight() && blockPos.getY() < levelReader.getMaxBuildHeight() && levelReader.getBrightness(LightLayer.BLOCK, blockPos) < 10) {
            BlockState blockstate = levelReader.getBlockState(blockPos);
            if ((blockstate.isAir() || blockstate.is(Blocks.SNOW)) && Blocks.SNOW.defaultBlockState().canSurvive(levelReader, blockPos)) {
                ci.setReturnValue(true);
            } else {
                ci.setReturnValue(false);
            }
        } else {
            ci.setReturnValue(false);
        }
    }

    /**
     * @author Apothicon
     * @reason Infinite color blending not dependant on biomes.
     */
    @Overwrite
    public int getGrassColor(double posX, double posZ) {
        int i = this.specialEffects.getGrassColorOverride().orElseGet(this::biox$getGrassColorFromTexture);
        return this.specialEffects.getGrassColorModifier().modifyColor(posX, posZ, i);
    }
}
