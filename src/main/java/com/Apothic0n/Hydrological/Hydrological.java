package com.Apothic0n.Hydrological;

import com.Apothic0n.Hydrological.api.HydrolDensityFunctions;
import com.Apothic0n.Hydrological.api.HydrolJsonReader;
import com.Apothic0n.Hydrological.api.biome.HydrolSurfaceRules;
import com.Apothic0n.Hydrological.api.biome.features.HydrolFeatureRegistry;
import com.Apothic0n.Hydrological.api.biome.features.canopies.CanopyType;
import com.Apothic0n.Hydrological.api.biome.features.decorations.DecorationType;
import com.Apothic0n.Hydrological.api.biome.features.decorators.HydrolTreeDecoratorType;
import com.Apothic0n.Hydrological.api.biome.features.foliage_placers.HydrolFoliagePlacerType;
import com.Apothic0n.Hydrological.api.biome.features.placement_modifiers.HydrolPlacementModifierTypes;
import com.Apothic0n.Hydrological.api.biome.features.trunk_placers.HydrolTrunkPlacerType;
import com.Apothic0n.Hydrological.api.biome.features.trunks.TrunkType;
import com.Apothic0n.Hydrological.core.objects.HydrolBlocks;
import com.Apothic0n.Hydrological.core.objects.HydrolItems;
import com.Apothic0n.Hydrological.core.objects.HydrolParticleTypes;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
        HydrolTrunkPlacerType.register(eventBus);
        HydrolFoliagePlacerType.register(eventBus);
        HydrolTreeDecoratorType.register(eventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            addLight(Blocks.SUNFLOWER.getStateDefinition().getPossibleStates(), 2);
            addLight(Blocks.SPORE_BLOSSOM.getStateDefinition().getPossibleStates(), 4);
            addLight(Blocks.BLUE_ICE.getStateDefinition().getPossibleStates(), 7);
            addLight(Blocks.COCOA.getStateDefinition().getPossibleStates(), 7);
            addLight(Blocks.TALL_GRASS.getStateDefinition().getPossibleStates(), 8);
            addLight(Blocks.TORCHFLOWER.getStateDefinition().getPossibleStates(), 9);
            addLight(Blocks.TORCHFLOWER_CROP.getStateDefinition().getPossibleStates(), 9);
            addLight(Blocks.RED_MUSHROOM.getStateDefinition().getPossibleStates(), 9);
            addLight(Blocks.RED_MUSHROOM_BLOCK.getStateDefinition().getPossibleStates(), 13);
            addLight(Blocks.MAGMA_BLOCK.getStateDefinition().getPossibleStates(), 13);
        });
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