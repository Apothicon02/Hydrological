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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(Hydrological.MODID)
public class Hydrological {
    public static final String MODID = "hydrol";

    public Hydrological(IEventBus eventBus, ModContainer container) throws Exception {
        eventBus.register(this);

        HydrolJsonReader.main();
        TrunkType.register(eventBus);
        CanopyType.register(eventBus);
        DecorationType.register(eventBus);
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

    @SubscribeEvent
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
        });
    }

    private void addLight(ImmutableList<BlockState> blockStates, int light) {
        for (int i = 0; i < blockStates.size(); i++) {
            blockStates.get(i).lightEmission = light;
        }
    }

    @SubscribeEvent
    private void clientSetup(final FMLClientSetupEvent event) {
        if (!HydrolJsonReader.serverSidedOnlyMode) {
            HydrolBlocks.fixBlockRenderLayers();
        }
    }
}