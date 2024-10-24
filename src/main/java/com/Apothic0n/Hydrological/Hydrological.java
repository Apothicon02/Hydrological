package com.Apothic0n.Hydrological;

import com.Apothic0n.Hydrological.api.HydrolDensityFunctions;
import com.Apothic0n.Hydrological.api.HydrolJsonReader;
import com.Apothic0n.Hydrological.api.biome.HydrolSurfaceRules;
import com.Apothic0n.Hydrological.api.biome.features.HydrolFeatureRegistry;
import com.Apothic0n.Hydrological.api.biome.features.canopies.CanopyType;
import com.Apothic0n.Hydrological.api.biome.features.decorations.DecorationType;
import com.Apothic0n.Hydrological.api.biome.features.placement_modifiers.HydrolPlacementModifierTypes;
import com.Apothic0n.Hydrological.api.biome.features.trunks.TrunkType;
import com.Apothic0n.Hydrological.core.objects.HydrolBlocks;
import com.Apothic0n.Hydrological.core.objects.HydrolItems;
import com.Apothic0n.Hydrological.core.objects.HydrolParticleTypes;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2FloatOpenHashMap;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Mod(Hydrological.MODID)
public class Hydrological {
    public static final String MODID = "hydrol";

    public Hydrological() throws Exception {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        HydrolJsonReader.main();
        TrunkType.register(eventBus);
        CanopyType.register(eventBus);
        DecorationType.register(eventBus);
        HydrolDensityFunctions.init();
        HydrolDensityFunctions.register(eventBus);
        HydrolSurfaceRules.register(eventBus);
        if (!HydrolJsonReader.serverSidedOnlyMode) {
            HydrolParticleTypes.PARTICLE_TYPES.register(eventBus);
            HydrolBlocks.BLOCKS.register(eventBus);
            HydrolBlocks.generateStairsSlabsWalls();
            HydrolItems.ITEMS.register(eventBus);
            HydrolItems.generateStairsSlabsWalls();
        }
        HydrolPlacementModifierTypes.register(eventBus);
        HydrolFeatureRegistry.register(eventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            if (!HydrolJsonReader.serverSidedOnlyMode && HydrolJsonReader.addLightEmissionToVanillaBlocks) {
                addLight(Blocks.SUNFLOWER.getStateDefinition().getPossibleStates(), 2);
                addLight(Blocks.SPORE_BLOSSOM.getStateDefinition().getPossibleStates(), 4);
                addLight(Blocks.BLUE_ICE.getStateDefinition().getPossibleStates(), 7);
                addLight(Blocks.COCOA.getStateDefinition().getPossibleStates(), 7);
                addLight(Blocks.TALL_GRASS.getStateDefinition().getPossibleStates(), 8);
                addLight(Blocks.TALL_SEAGRASS.getStateDefinition().getPossibleStates(), 8);
                addLight(Blocks.TORCHFLOWER.getStateDefinition().getPossibleStates(), 9);
                addLight(Blocks.TORCHFLOWER_CROP.getStateDefinition().getPossibleStates(), 9);
                addLight(Blocks.RED_MUSHROOM.getStateDefinition().getPossibleStates(), 9);
                addLight(Blocks.RED_MUSHROOM_BLOCK.getStateDefinition().getPossibleStates(), 13);
                addLight(Blocks.MAGMA_BLOCK.getStateDefinition().getPossibleStates(), 13);
            }

            //for (int seed = 0; seed <= 9; seed++) {
                //generateNoiseImage("surface_variation"+seed, 10000, 1, seed, new NormalNoise.NoiseParameters(-6, 1));
                //generateNoiseImage("erosion"+seed, 10000, 0.66f, seed, new NormalNoise.NoiseParameters(-8, 1));
                //generateNoiseImage("continentalness"+seed, 10000, 1, seed, new NormalNoise.NoiseParameters(-11, 1, 0, 0, 0, -0.1));
                //generateNoiseImage("temperature"+seed, 10000, false, seed, new NormalNoise.NoiseParameters(-11, 1.5, 0, 1, 0, 0, 0, 3, 0, -1, 0, 0, 0, 2));
            //}
        });
    }

    public static Map<Long, Float> generateNoiseMap(int resolution, float xzScale, int seed, NormalNoise.NoiseParameters noiseParameters) {
        NormalNoise noise = NormalNoise.create(RandomSource.create(seed), noiseParameters);
        HashMap<Long, Float> map = new HashMap<>();
        for (int x = 0; x < resolution; x++) {
            for (int z = 0; z < resolution; z++) {
                map.put(ChunkPos.asLong(x, z), (float) noise.getValue(x*xzScale, 0, z*xzScale));
            }
        }
        return map;
    }

    private void generateNoiseImage(String name, int resolution, float xzScale, int seed, NormalNoise.NoiseParameters noiseParameters) {
        NormalNoise noise = NormalNoise.create(RandomSource.create(seed), noiseParameters);
        BufferedImage image = new BufferedImage(resolution, resolution, BufferedImage.TYPE_BYTE_GRAY);
        for (int x = 0; x < resolution; x++) {
            for (int z = 0; z < resolution; z++) {
                int value = (int) Math.min(255, Math.max(0, (noise.getValue(x*xzScale, 0, z*xzScale)*100)+127));
                image.setRGB(x, z, new Color(value, value, value).getRGB());
            }
        }
        try {
            ImageIO.write(image, "png", new File(FMLPaths.MODSDIR.get()+"/"+name+".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addLight(ImmutableList<BlockState> blockStates, int light) {
        for (int i = 0; i < blockStates.size(); i++) {
            blockStates.get(i).lightEmission = light;
        }
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        if (!HydrolJsonReader.serverSidedOnlyMode) {
            HydrolBlocks.fixBlockRenderLayers();
        }
    }
}