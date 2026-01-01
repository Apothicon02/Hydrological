package com.Apothic0n.Hydrological.core.events;

import com.Apothic0n.Hydrological.Hydrological;
import com.Apothic0n.Hydrological.api.HydrolJsonReader;
import com.Apothic0n.Hydrological.api.biome.features.placement_modifiers.NoiseCoverPlacement;
import com.Apothic0n.Hydrological.core.objects.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;

@EventBusSubscriber(modid = Hydrological.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void addItemsToTabs(BuildCreativeModeTabContentsEvent event) {
        if (!HydrolJsonReader.serverSidedOnlyMode && event.getTabKey().equals(CreativeModeTabs.NATURAL_BLOCKS)) {
            event.accept(HydrolItems.DRY_GRASS.get(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            List<List<DeferredHolder<Item, Item>>> buildingBlockItems = List.of(HydrolItems.wallItems, HydrolItems.stairItems, HydrolItems.slabItems, HydrolItems.pileItems);
            for (int i = 0; i < buildingBlockItems.size(); i++) {
                List<DeferredHolder<Item, Item>> blockItemTypeList = buildingBlockItems.get(i);
                for (int o = 0; o < blockItemTypeList.size(); o++) {
                    event.accept(blockItemTypeList.get(o).get(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                }
            }
        }
    }

    @SubscribeEvent
    public static void registerSpriteSet(RegisterParticleProvidersEvent event) {
        if (!HydrolJsonReader.serverSidedOnlyMode) {
            event.registerSpriteSet(HydrolParticleTypes.OAK_LEAVES.get(), (spriteSet) -> {
                return (particleType, level, x, y, z, p_277222_, p_277223_, p_277224_) -> {
                    return new OakLeavesParticle(level, x, y, z, spriteSet);
                };
            });
            event.registerSpriteSet(HydrolParticleTypes.DARK_OAK_LEAVES.get(), (spriteSet) -> {
                return (particleType, level, x, y, z, p_277222_, p_277223_, p_277224_) -> {
                    return new DarkOakLeavesParticle(level, x, y, z, spriteSet);
                };
            });
            event.registerSpriteSet(HydrolParticleTypes.BIRCH_LEAVES.get(), (spriteSet) -> {
                return (particleType, level, x, y, z, p_277222_, p_277223_, p_277224_) -> {
                    return new BirchLeavesParticle(level, x, y, z, spriteSet);
                };
            });
            event.registerSpriteSet(HydrolParticleTypes.SPRUCE_LEAVES.get(), (spriteSet) -> {
                return (particleType, level, x, y, z, p_277222_, p_277223_, p_277224_) -> {
                    return new SpruceLeavesParticle(level, x, y, z, spriteSet);
                };
            });
            event.registerSpriteSet(HydrolParticleTypes.JUNGLE_LEAVES.get(), (spriteSet) -> {
                return (particleType, level, x, y, z, p_277222_, p_277223_, p_277224_) -> {
                    return new LeavesParticle(level, x, y, z, spriteSet);
                };
            });
            event.registerSpriteSet(HydrolParticleTypes.ACACIA_LEAVES.get(), (spriteSet) -> {
                return (particleType, level, x, y, z, p_277222_, p_277223_, p_277224_) -> {
                    return new LeavesParticle(level, x, y, z, spriteSet);
                };
            });
            event.registerSpriteSet(HydrolParticleTypes.MANGROVE_LEAVES.get(), (spriteSet) -> {
                return (particleType, level, x, y, z, p_277222_, p_277223_, p_277224_) -> {
                    return new MangroveLeavesParticle(level, x, y, z, spriteSet);
                };
            });
            event.registerSpriteSet(HydrolParticleTypes.AZALEA_LEAVES.get(), (spriteSet) -> {
                return (particleType, level, x, y, z, p_277222_, p_277223_, p_277224_) -> {
                    return new AzaleaLeavesParticle(level, x, y, z, spriteSet);
                };
            });
            event.registerSpriteSet(HydrolParticleTypes.FLOWERING_AZALEA_LEAVES.get(), (spriteSet) -> {
                return (particleType, level, x, y, z, p_277222_, p_277223_, p_277224_) -> {
                    return new AzaleaLeavesParticle(level, x, y, z, spriteSet);
                };
            });
            event.registerSpriteSet(HydrolParticleTypes.FIRE_FLIES.get(), (spriteSet) -> {
                return (particleType, level, x, y, z, p_277222_, p_277223_, p_277224_) -> {
                    return new FireFliesParticle(level, x, y, z, spriteSet);
                };
            });
        }
    }
}