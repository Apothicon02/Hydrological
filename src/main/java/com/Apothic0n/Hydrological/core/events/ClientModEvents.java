package com.Apothic0n.Hydrological.core.events;

import com.Apothic0n.Hydrological.Hydrological;
import com.Apothic0n.Hydrological.api.HydrolColorHelper;
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
import net.neoforged.fml.common.Mod;
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

    @SubscribeEvent
    public static void onBlockColors(RegisterColorHandlersEvent.Block event) {

        event.register((blockState, blockAndTintGetter, blockPos, tint) -> {
                    if (blockPos != null) {
                        int x = blockPos.getX();
                        int z = blockPos.getZ();
                        int color = -328966;
                        double saturate = Mth.clamp(NoiseCoverPlacement.HEIGHT_NOISE.getValue(x * 0.077, z * 0.09, false) * 0.33, -0.03, 0.03) + 1;
                        double brighten = Mth.clamp(HydrolColorHelper.BRIGHTNESS_NOISE.getValue(x * 0.05, z * 0.01, false) * 0.11, -0.1, 0.1);
                        float red = (float) Mth.clamp(FastColor.ABGR32.red(color), 1, 255) / 255;
                        float green = (float) Mth.clamp(FastColor.ABGR32.green(color), 1, 255) / 255;
                        float blue = (float) Mth.clamp(FastColor.ABGR32.blue(color), 1, 255) / 255;
                        float gray = (float) ((red + green + blue) / (3 + brighten));
                        if (Minecraft.getInstance().level.getBiome(blockPos).toString().contains("himalayan")) {
                            return -9729;
                        }
                        return FastColor.ABGR32.color(FastColor.ABGR32.alpha(color),
                                (int) (Mth.clamp((blue + (gray - blue)) * saturate, 0, 1) * 255),
                                (int) (Mth.clamp((green + (gray - green)) * saturate, 0, 1) * 255),
                                (int) (Mth.clamp((red + (gray - red)) * saturate, 0, 1) * 255));
                    } else {
                        return -328966;
                    }
                },
                Blocks.SAND);
        
        event.register((blockState, blockAndTintGetter, blockPos, tint) -> {
            if (blockPos != null) {
                int x = blockPos.getX();
                int z = blockPos.getZ();
                int color = -328966;
                double saturate = Mth.clamp(NoiseCoverPlacement.HEIGHT_NOISE.getValue(x * 0.077, z * 0.09, false) * 0.33, -0.03, 0.03) + 1;
                double brighten = Mth.clamp(HydrolColorHelper.BRIGHTNESS_NOISE.getValue(x * 0.05, z * 0.01, false) * 0.55, -0.5, 0.5) + 0.75;
                float red = (float) Mth.clamp(FastColor.ABGR32.red(color), 1, 255) / 255;
                float green = (float) Mth.clamp(FastColor.ABGR32.green(color), 1, 255) / 255;
                float blue = (float) Mth.clamp(FastColor.ABGR32.blue(color), 1, 255) / 255;
                float gray = (float) ((red + green + blue) / (3 + brighten));
                return FastColor.ABGR32.color(FastColor.ABGR32.alpha(color),
                        (int) (Mth.clamp((blue + (gray - blue)) * saturate, 0, 1) * 255),
                        (int) (Mth.clamp((green + (gray - green)) * saturate, 0, 1) * 255),
                        (int) (Mth.clamp((red + (gray - red)) * saturate, 0, 1) * 255));
            } else {
                return -328966;
            }
        },
                Blocks.NETHERRACK, Blocks.GRAVEL, Blocks.CLAY, Blocks.TUFF, Blocks.CALCITE,
                Blocks.ANDESITE, Blocks.ANDESITE_SLAB, Blocks.ANDESITE_STAIRS, Blocks.ANDESITE_WALL,
                Blocks.POLISHED_ANDESITE, Blocks.POLISHED_ANDESITE_SLAB, Blocks.POLISHED_ANDESITE_STAIRS,
                Blocks.SMOOTH_BASALT, Blocks.BASALT, Blocks.POLISHED_BASALT,
                Blocks.GRANITE, Blocks.GRANITE_SLAB, Blocks.GRANITE_STAIRS, Blocks.GRANITE_WALL,
                Blocks.POLISHED_GRANITE, Blocks.POLISHED_GRANITE_SLAB, Blocks.POLISHED_GRANITE_STAIRS,
                Blocks.DIORITE, Blocks.DIORITE_SLAB, Blocks.DIORITE_STAIRS,
                Blocks.POLISHED_DIORITE, Blocks.POLISHED_DIORITE_SLAB, Blocks.POLISHED_DIORITE_STAIRS,
                Blocks.COBBLESTONE, Blocks.COBBLESTONE_STAIRS, Blocks.COBBLESTONE_SLAB, Blocks.COBBLESTONE_WALL,
                Blocks.MOSSY_COBBLESTONE, Blocks.MOSSY_COBBLESTONE_STAIRS, Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.MOSSY_COBBLESTONE_WALL,
                Blocks.STONE, Blocks.STONE_STAIRS, Blocks.STONE_SLAB,
                Blocks.STONE_BRICKS, Blocks.STONE_BRICK_STAIRS, Blocks.STONE_BRICK_SLAB, Blocks.STONE_BRICK_WALL, Blocks.CRACKED_STONE_BRICKS, Blocks.INFESTED_CRACKED_STONE_BRICKS,
                Blocks.MOSSY_STONE_BRICKS, Blocks.MOSSY_STONE_BRICK_STAIRS, Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.MOSSY_STONE_BRICK_WALL, Blocks.INFESTED_MOSSY_STONE_BRICKS,
                Blocks.COAL_ORE, Blocks.COPPER_ORE, Blocks.IRON_ORE, Blocks.LAPIS_ORE, Blocks.REDSTONE_ORE, Blocks.GOLD_ORE, Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE,
                Blocks.COBBLED_DEEPSLATE, Blocks.COBBLED_DEEPSLATE_STAIRS, Blocks.COBBLED_DEEPSLATE_SLAB, Blocks.COBBLED_DEEPSLATE_WALL,
                Blocks.DEEPSLATE, Blocks.INFESTED_DEEPSLATE,
                Blocks.DEEPSLATE_BRICKS, Blocks.DEEPSLATE_BRICK_SLAB, Blocks.DEEPSLATE_BRICK_STAIRS, Blocks.DEEPSLATE_BRICK_WALL, Blocks.CRACKED_DEEPSLATE_BRICKS,
                Blocks.DEEPSLATE_TILES, Blocks.DEEPSLATE_TILE_STAIRS, Blocks.DEEPSLATE_TILE_SLAB, Blocks.DEEPSLATE_TILE_WALL, Blocks.CRACKED_DEEPSLATE_TILES,
                Blocks.POLISHED_DEEPSLATE, Blocks.POLISHED_DEEPSLATE_STAIRS, Blocks.POLISHED_DEEPSLATE_SLAB, Blocks.POLISHED_DEEPSLATE_WALL,
                Blocks.DEEPSLATE_COAL_ORE, Blocks.DEEPSLATE_COPPER_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.DEEPSLATE_LAPIS_ORE, Blocks.DEEPSLATE_REDSTONE_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.DEEPSLATE_EMERALD_ORE);

        event.register((blockState, blockAndTintGetter, blockPos, tint) -> {
                    if (blockPos != null) {
                        int x = blockPos.getX();
                        int z = blockPos.getZ();
                        int color = -328966;
                        double saturate = Mth.clamp(NoiseCoverPlacement.HEIGHT_NOISE.getValue(x * 0.077, z * 0.09, false) * 0.33, -0.03, 0.03) + 1;
                        double brighten = Mth.clamp(HydrolColorHelper.BRIGHTNESS_NOISE.getValue(x * 0.05, z * 0.01, false) * 0.11, -0.1, 0.1);
                        float red = (float) Mth.clamp(FastColor.ABGR32.red(color), 1, 255) / 255;
                        float green = (float) Mth.clamp(FastColor.ABGR32.green(color), 1, 255) / 255;
                        float blue = (float) Mth.clamp(FastColor.ABGR32.blue(color), 1, 255) / 255;
                        float gray = (float) ((red + green + blue) / (3 + brighten));
                        return FastColor.ABGR32.color(FastColor.ABGR32.alpha(color),
                                (int) (Mth.clamp((blue + (gray - blue)) * saturate, 0, 1) * 255),
                                (int) (Mth.clamp((green + (gray - green)) * saturate, 0, 1) * 255),
                                (int) (Mth.clamp((red + (gray - red)) * saturate, 0, 1) * 255));
                    } else {
                        return -328966;
                    }
                },
                Blocks.SNOW_BLOCK, Blocks.SNOW, Blocks.POWDER_SNOW);

        event.register((blockState, blockAndTintGetter, blockPos, tint) -> {
                    if (blockPos != null) {
                        int x = blockPos.getX();
                        int z = blockPos.getZ();
                        int color = -328966;
                        double saturate = Mth.clamp(NoiseCoverPlacement.HEIGHT_NOISE.getValue(x * 0.33, z * 0.3, false) * 0.3, -0.6, 0.66) + 0.9;
                        double brighten = Mth.clamp(HydrolColorHelper.BRIGHTNESS_NOISE.getValue(x * 0.05, z * 0.01, false) * 0.3, -0.66, 0.66);
                        float red = (float) Mth.clamp(FastColor.ABGR32.red(color), 1, 255) / 255;
                        float green = (float) Mth.clamp(FastColor.ABGR32.green(color), 1, 255) / 255;
                        float blue = (float) Mth.clamp(FastColor.ABGR32.blue(color), 1, 255) / 255;
                        float gray = (float) ((red + green + blue) / (3 + brighten));
                        return FastColor.ABGR32.color(FastColor.ABGR32.alpha(color),
                                (int) (Mth.clamp((blue + (gray - blue)) * saturate, 0, 1) * 255),
                                (int) (Mth.clamp((green + (gray - green)) * saturate, 0, 1) * 255),
                                (int) (Mth.clamp((red + (gray - red)) * saturate, 0, 1) * 255));
                    } else {
                        return -328966;
                    }
                },
                Blocks.MUD, Blocks.PACKED_MUD, Blocks.MUD_BRICKS, Blocks.MUD_BRICK_STAIRS, Blocks.MUD_BRICK_SLAB, Blocks.MUD_BRICK_WALL,
                Blocks.MANGROVE_ROOTS);
    }
}