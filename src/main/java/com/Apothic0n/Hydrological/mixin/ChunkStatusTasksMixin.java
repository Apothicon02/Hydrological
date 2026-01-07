package com.Apothic0n.Hydrological.mixin;

import com.Apothic0n.Hydrological.Hydrological;
import com.Apothic0n.Hydrological.api.HydrolMath;
import net.minecraft.server.level.GenerationChunkHolder;
import net.minecraft.util.StaticCache2D;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatusTasks;
import net.minecraft.world.level.chunk.status.ChunkStep;
import net.minecraft.world.level.chunk.status.WorldGenContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ChunkStatusTasks.class, priority = 69420)
public abstract class ChunkStatusTasksMixin {

    @Inject(method = "initializeLight", at = @At("HEAD"))
    private static void initializeLightHead(WorldGenContext worldGenContext, ChunkStep step, StaticCache2D<GenerationChunkHolder> cache, ChunkAccess chunk, CallbackInfoReturnable<Boolean> ci) {
        Hydrological.lightStart = System.nanoTime();
    }

    @Inject(method = "initializeLight", at = @At("RETURN"))
    private static void initializeLightReturn(WorldGenContext worldGenContext, ChunkStep step, StaticCache2D<GenerationChunkHolder> cache, ChunkAccess chunk, CallbackInfoReturnable<Boolean> ci) {
        Hydrological.lightTimes.addLast(System.nanoTime()-Hydrological.lightStart);
        if (Hydrological.lightTimes.size() > 60) {
            Hydrological.lightTimes.removeFirst();
        }
        Hydrological.timesSincePrintedLight--;
        if (Hydrological.timesSincePrintedLight <= 0) {
            Hydrological.timesSincePrintedLight = 60;
            int lightNS = (int)(1000000000d/HydrolMath.averageLongs(Hydrological.lightTimes));
            int biomesNS = (int)(1000000000d/HydrolMath.averageLongs(Hydrological.biomesTimes));
            int noiseNS = (int)(1000000000d/HydrolMath.averageLongs(Hydrological.noiseTimes));
            int surfaceNS = (int)(1000000000d/HydrolMath.averageLongs(Hydrological.surfaceTimes));
            int featuresNS = (int)(1000000000d/HydrolMath.averageLongs(Hydrological.featuresTimes));
            int totalNS = lightNS+biomesNS+noiseNS+surfaceNS+featuresNS;
            float lightP = 100*((float) lightNS/totalNS);
            float biomesP = 100*((float) biomesNS/totalNS);
            float noiseP = 100*((float) noiseNS/totalNS);
            float surfaceP = 100*((float) surfaceNS/totalNS);
            float featuresP = 100*((float) featuresNS/totalNS);
            System.out.print("Light: "+lightNS+"ns "+lightP+"% | Biomes: "+biomesNS+"ns "+biomesP+"% | Noise: "+noiseNS+"ns "+noiseP+"% | Surface: "+surfaceNS+"ns "+surfaceP+"% | Features: "+featuresNS+"ns "+featuresP+"% \n");
        }
    }

    @Inject(method = "generateBiomes", at = @At("HEAD"))
    private static void generateBiomesHead(WorldGenContext worldGenContext, ChunkStep step, StaticCache2D<GenerationChunkHolder> cache, ChunkAccess chunk, CallbackInfoReturnable<Boolean> ci) {
        Hydrological.biomesStart = System.nanoTime();
    }

    @Inject(method = "generateBiomes", at = @At("RETURN"))
    private static void generateBiomesReturn(WorldGenContext worldGenContext, ChunkStep step, StaticCache2D<GenerationChunkHolder> cache, ChunkAccess chunk, CallbackInfoReturnable<Boolean> ci) {
        Hydrological.biomesTimes.addLast(System.nanoTime()-Hydrological.biomesStart);
        if (Hydrological.biomesTimes.size() > 60) {
            Hydrological.biomesTimes.removeFirst();
        }
    }

    @Inject(method = "generateNoise", at = @At("HEAD"))
    private static void generateNoiseHead(WorldGenContext worldGenContext, ChunkStep step, StaticCache2D<GenerationChunkHolder> cache, ChunkAccess chunk, CallbackInfoReturnable<Boolean> ci) {
        Hydrological.noiseStart = System.nanoTime();
    }

    @Inject(method = "generateNoise", at = @At("RETURN"))
    private static void generateNoiseReturn(WorldGenContext worldGenContext, ChunkStep step, StaticCache2D<GenerationChunkHolder> cache, ChunkAccess chunk, CallbackInfoReturnable<Boolean> ci) {
        Hydrological.noiseTimes.addLast(System.nanoTime()-Hydrological.noiseStart);
        if (Hydrological.noiseTimes.size() > 60) {
            Hydrological.noiseTimes.removeFirst();
        }
    }

    @Inject(method = "generateSurface", at = @At("HEAD"))
    private static void generateSurfaceHead(WorldGenContext worldGenContext, ChunkStep step, StaticCache2D<GenerationChunkHolder> cache, ChunkAccess chunk, CallbackInfoReturnable<Boolean> ci) {
        Hydrological.surfaceStart = System.nanoTime();
    }

    @Inject(method = "generateSurface", at = @At("RETURN"))
    private static void generateSurfaceReturn(WorldGenContext worldGenContext, ChunkStep step, StaticCache2D<GenerationChunkHolder> cache, ChunkAccess chunk, CallbackInfoReturnable<Boolean> ci) {
        Hydrological.surfaceTimes.addLast(System.nanoTime()-Hydrological.surfaceStart);
        if (Hydrological.surfaceTimes.size() > 60) {
            Hydrological.surfaceTimes.removeFirst();
        }
    }

    @Inject(method = "generateFeatures", at = @At("HEAD"))
    private static void generateFeaturesHead(WorldGenContext worldGenContext, ChunkStep step, StaticCache2D<GenerationChunkHolder> cache, ChunkAccess chunk, CallbackInfoReturnable<Boolean> ci) {
        Hydrological.featuresStart = System.nanoTime();
    }

    @Inject(method = "generateFeatures", at = @At("RETURN"))
    private static void generateFeaturesReturn(WorldGenContext worldGenContext, ChunkStep step, StaticCache2D<GenerationChunkHolder> cache, ChunkAccess chunk, CallbackInfoReturnable<Boolean> ci) {
        Hydrological.featuresTimes.addLast(System.nanoTime()-Hydrological.featuresStart);
        if (Hydrological.featuresTimes.size() > 60) {
            Hydrological.featuresTimes.removeFirst();
        }
    }
}
