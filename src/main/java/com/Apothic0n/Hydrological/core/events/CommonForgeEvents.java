package com.Apothic0n.Hydrological.core.events;

import com.Apothic0n.Hydrological.Hydrological;
import com.Apothic0n.Hydrological.api.HydrolDensityFunctions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.level.SaplingGrowTreeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

@Mod.EventBusSubscriber(modid = Hydrological.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {

    @SubscribeEvent
    public static void saplingGrowTree(SaplingGrowTreeEvent event) {
        LevelAccessor level = event.getLevel();
        BlockPos origin = event.getPos();
        BlockState sapling = level.getBlockState(origin);
        BlockState below = level.getBlockState(origin.below());
        if (!below.is(Blocks.DIRT) && !below.is(Blocks.GRASS_BLOCK)) {
            if (sapling.is(Blocks.OAK_SAPLING)) {
                if (below.is(Blocks.PODZOL)) {
                    event.setFeature(ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("hydrol", "trees/oak")));
                } else if (below.is(Blocks.COARSE_DIRT)) {
                    event.setFeature(ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("hydrol", "trees/willow")));
                }
            } else if (sapling.is(Blocks.DARK_OAK_SAPLING)) {
                if (below.is(Blocks.PODZOL)) {
                    event.setFeature(ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("hydrol", "trees/dark_oak")));
                } else if (below.is(Blocks.COARSE_DIRT)) {
                    event.setFeature(ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("hydrol", "trees/dark_willow")));
                }
            } else if (sapling.is(Blocks.BIRCH_SAPLING)) {
                if (below.is(Blocks.COARSE_DIRT)) {
                    event.setFeature(ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("hydrol", "trees/birch_fir")));
                } else if (below.is(Blocks.SAND) || below.is(Blocks.GRAVEL)) {
                    event.setFeature(ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("hydrol", "trees/birch_spruce")));
                }
            } else if (sapling.is(Blocks.SPRUCE_SAPLING)) {
                if (below.is(Blocks.PODZOL)) {
                    event.setFeature(ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("hydrol", "trees/redwood")));
                } else if (below.is(Blocks.COARSE_DIRT)) {
                    event.setFeature(ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("hydrol", "trees/fir")));
                } else if (below.is(BlockTags.SNOW)) {
                    event.setFeature(ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("hydrol", "trees/pine")));
                } else if (below.is(Blocks.SAND) || below.is(Blocks.GRAVEL)) {
                    event.setFeature(ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("hydrol", "trees/spruce")));
                }
            } else if (sapling.is(Blocks.JUNGLE_SAPLING)) {
                if (below.is(Blocks.PODZOL)) {
                    event.setFeature(ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("hydrol", "trees/jungle")));
                } else if (below.is(BlockTags.SAND)) {
                    event.setFeature(ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("hydrol", "trees/palm")));
                }
            } else if (sapling.is(Blocks.CHERRY_SAPLING)) {
                if (below.is(Blocks.PODZOL)) {
                    event.setFeature(ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("hydrol", "trees/cherry")));
                }
            } else if (sapling.is(Blocks.MANGROVE_PROPAGULE)) {
                if (below.is(Blocks.MANGROVE_ROOTS) || below.is(Blocks.MUDDY_MANGROVE_ROOTS)) {
                    event.setFeature(ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("hydrol", "trees/mangrove")));
                }
            } else if (sapling.is(Blocks.RED_MUSHROOM)) {
                if (below.is(Blocks.MYCELIUM)) {
                    event.setFeature(ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("hydrol", "trees/red_mushroom")));
                }
            } else if (sapling.is(Blocks.BROWN_MUSHROOM)) {
                if (below.is(Blocks.MYCELIUM)) {
                    event.setFeature(ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation("hydrol", "trees/brown_mushroom")));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onCreateSpawnPosition(LevelEvent.CreateSpawnPosition event) {
        if (event.getLevel() instanceof ServerLevel level && !level.isClientSide()) {
            if (HydrolDensityFunctions.isFloatingIslands) {
                BlockPos pos = new BlockPos(event.getSettings().getXSpawn(), 256, event.getSettings().getZSpawn());
                boolean overVoid = true;
                for (int i = level.getMinBuildHeight() - 1; i < level.getMaxBuildHeight(); i++) {
                    if (!level.getBlockState(new BlockPos(pos.getX(), i, pos.getZ())).isAir()) {
                        overVoid = false;
                    }
                }
                if (overVoid) {
                    pos = new BlockPos(pos.getX(), 64, pos.getZ());
                    level.setBlock(pos.below(5), Blocks.CAVE_VINES.defaultBlockState().setValue(BlockStateProperties.BERRIES, true), UPDATE_ALL);
                    generateSquare(level, pos.below(4), Blocks.STONE.defaultBlockState());
                    generateSquare(level, pos.below(3), Blocks.STONE.defaultBlockState());
                    generateSquare(level, pos.below(2), Blocks.DIRT.defaultBlockState());
                    generateSquare(level, pos.below(), Blocks.GRASS_BLOCK.defaultBlockState());
                    generateSquare(level, pos.north(3).below(3), Blocks.STONE.defaultBlockState());
                    generateSquare(level, pos.north(3).below(2), Blocks.DIRT.defaultBlockState());
                    generateSquare(level, pos.north(3).below(), Blocks.GRASS_BLOCK.defaultBlockState());
                    generateSquare(level, pos.east(3).below(3), Blocks.STONE.defaultBlockState());
                    generateSquare(level, pos.east(3).below(2), Blocks.DIRT.defaultBlockState());
                    generateSquare(level, pos.east(3).below(), Blocks.GRASS_BLOCK.defaultBlockState());
                    generateSquare(level, pos.south(3).below(3), Blocks.STONE.defaultBlockState());
                    generateSquare(level, pos.south(3).below(2), Blocks.DIRT.defaultBlockState());
                    generateSquare(level, pos.south(3).below(), Blocks.GRASS_BLOCK.defaultBlockState());
                    generateSquare(level, pos.west(3).below(3), Blocks.STONE.defaultBlockState());
                    generateSquare(level, pos.west(3).below(2), Blocks.DIRT.defaultBlockState());
                    generateSquare(level, pos.west(3).below(), Blocks.GRASS_BLOCK.defaultBlockState());
                    generateSquare(level, pos.north(3).east(3).below(2), Blocks.DIRT.defaultBlockState());
                    generateSquare(level, pos.north(3).east(3).below(), Blocks.GRASS_BLOCK.defaultBlockState());
                    generateSquare(level, pos.north(3).west(3).below(2), Blocks.DIRT.defaultBlockState());
                    generateSquare(level, pos.north(3).west(3).below(), Blocks.GRASS_BLOCK.defaultBlockState());
                    generateSquare(level, pos.south(3).east(3).below(2), Blocks.DIRT.defaultBlockState());
                    generateSquare(level, pos.south(3).east(3).below(), Blocks.GRASS_BLOCK.defaultBlockState());
                    generateSquare(level, pos.south(3).west(3).below(2), Blocks.DIRT.defaultBlockState());
                    generateSquare(level, pos.south(3).west(3).below(), Blocks.GRASS_BLOCK.defaultBlockState());
                    level.setBlock(pos.north(2).east(3), Blocks.OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.Z), UPDATE_ALL);
                    level.setBlock(pos.north(3).east(3), Blocks.OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.Z), UPDATE_ALL);
                    level.setBlock(pos.north(3).east(3).above(), Blocks.MOSS_CARPET.defaultBlockState(), UPDATE_ALL);
                    level.setBlock(pos.north(4).east(3), Blocks.OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.Z), UPDATE_ALL);
                    level.setBlock(pos.north(5).east(3), Blocks.OAK_LOG.defaultBlockState().setValue(BlockStateProperties.AXIS, Direction.Axis.Z), UPDATE_ALL);
                    level.setBlock(pos.east(3).south(3), Blocks.OAK_SAPLING.defaultBlockState(), UPDATE_ALL);
                    level.setBlock(pos.south(3).west(3), Blocks.OAK_SAPLING.defaultBlockState(), UPDATE_ALL);
                    level.setBlock(pos.west(3).north(3), Blocks.OAK_SAPLING.defaultBlockState(), UPDATE_ALL);
                    level.setBlock(pos.north(1).east().below(), Blocks.WATER.defaultBlockState(), UPDATE_ALL);
                    level.setBlock(pos.north(1).east().south().below(2), Blocks.AIR.defaultBlockState(), UPDATE_ALL);
                    level.setBlock(pos.north(1).east().west().below(2), Blocks.AIR.defaultBlockState(), UPDATE_ALL);
                    level.setBlock(pos.south(1).west().below(), Blocks.LAVA.defaultBlockState(), UPDATE_ALL);
                    level.setBlock(pos.below(), Blocks.COBBLESTONE.defaultBlockState(), UPDATE_ALL);
                    level.setBlock(pos.north(1).west().below(), Blocks.COBBLESTONE.defaultBlockState(), UPDATE_ALL);
                    level.setBlock(pos.south(1).east().below(), Blocks.COBBLESTONE.defaultBlockState(), UPDATE_ALL);
                    level.setBlock(pos.north(1).below(), Blocks.AIR.defaultBlockState(), UPDATE_ALL);
                    level.setBlock(pos.east(1).below(), Blocks.AIR.defaultBlockState(), UPDATE_ALL);
                    level.setBlock(pos.south(1).below(), Blocks.AIR.defaultBlockState(), UPDATE_ALL);
                    level.setBlock(pos.west(1).below(), Blocks.AIR.defaultBlockState(), UPDATE_ALL);
                }
            } else if (level.dimension().equals(Level.OVERWORLD) && level.dimensionType().hasCeiling()) {
                BlockPos pos = new BlockPos(event.getSettings().getXSpawn(), level.getMaxBuildHeight()-64, event.getSettings().getZSpawn());
                generateSquare(level, pos.below(2), Blocks.OAK_WOOD.defaultBlockState());
                generateSquare(level, pos.below(), Blocks.OAK_WOOD.defaultBlockState());
                generateSquare(level, pos, Blocks.AIR.defaultBlockState());
                generateSquare(level, pos.above(), Blocks.AIR.defaultBlockState());
                level.setBlock(pos, Blocks.TORCH.defaultBlockState(), UPDATE_ALL);
            } else if (level.dimension().equals(Level.OVERWORLD)) {
                BlockPos pos = new BlockPos(event.getSettings().getXSpawn(), 63, event.getSettings().getZSpawn());
                if (level.getBiome(pos).is(BiomeTags.IS_OCEAN)) {
                    ChestBoat chestBoat = new ChestBoat(level, pos.getX(), pos.getY(), pos.getZ());
                    chestBoat.setVariant(Boat.Type.BAMBOO);
                    chestBoat.setChestVehicleItem(0, new ItemStack(Items.BREAD, 5));
                    chestBoat.setChestVehicleItem(11, new ItemStack(Items.BOW, 1));
                    chestBoat.setChestVehicleItem(2, new ItemStack(Items.ARROW, 23));
                    level.addFreshEntity(chestBoat);
                    chestBoat = new ChestBoat(level, pos.getX()+3, pos.getY(), pos.getZ());
                    chestBoat.setVariant(Boat.Type.BAMBOO);
                    chestBoat.setChestVehicleItem(20, new ItemStack(Items.BREAD, 8));
                    chestBoat.setChestVehicleItem(14, new ItemStack(Items.STONE_AXE, 1));
                    level.addFreshEntity(chestBoat);
                    chestBoat = new ChestBoat(level, pos.getX()+1, pos.getY(), pos.getZ()+3);
                    chestBoat.setVariant(Boat.Type.BAMBOO);
                    chestBoat.setChestVehicleItem(10, new ItemStack(Items.BREAD, 2));
                    chestBoat.setChestVehicleItem(24, new ItemStack(Items.IRON_SWORD, 1));
                    level.addFreshEntity(chestBoat);
                    Boat boat = new Boat(level, pos.getX()-5, pos.getY(), pos.getZ()+2);
                    boat.setVariant(Boat.Type.BAMBOO);
                    level.addFreshEntity(boat);
                }
            }
        }
    }

    @SubscribeEvent
    public static void entityLoaded(EntityLoadEvent event) {
        Level level = event.level;
        Entity entity = event.entity;
        BlockPos pos = entity.blockPosition();
        if (entity instanceof Player && level.dimension().equals(Level.OVERWORLD) && level.dimensionType().hasCeiling() && pos.getY() == level.getMaxBuildHeight() && level.getBlockState(pos.below(64)).is(Blocks.TORCH)) {
            entity.teleportRelative(0, -64, 0);
        }
    }

    private static void generateSquare(Level level, BlockPos pos, BlockState state) {
        level.setBlock(pos, state, UPDATE_ALL);
        level.setBlock(pos.north(), state, UPDATE_ALL);
        level.setBlock(pos.east(), state, UPDATE_ALL);
        level.setBlock(pos.south(), state, UPDATE_ALL);
        level.setBlock(pos.west(), state, UPDATE_ALL);
        level.setBlock(pos.north().east(), state, UPDATE_ALL);
        level.setBlock(pos.south().east(), state, UPDATE_ALL);
        level.setBlock(pos.north().west(), state, UPDATE_ALL);
        level.setBlock(pos.south().west(), state, UPDATE_ALL);
    }
}
