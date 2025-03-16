package com.Apothic0n.Hydrological.core.events;

import com.Apothic0n.Hydrological.Hydrological;
import com.Apothic0n.Hydrological.core.objects.HydrolBlocks;
import com.mojang.serialization.JsonOps;
import net.commoble.databuddy.datagen.BlockStateFile;
import net.commoble.databuddy.datagen.SimpleModel;
import net.minecraft.Util;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.Apothic0n.Hydrological.core.objects.HydrolBlocks.*;

@EventBusSubscriber(modid = Hydrological.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CommonModEvents {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        // models
        SimpleModel.addDataProvider(event, Hydrological.MODID, JsonOps.INSTANCE, Util.make(new HashMap<>(), map ->
        {
            for (int i = 0; i < blocksWithStairsSlabsAndWalls.size(); i++) {
                Block baseBlockBlock = blocksWithStairsSlabsAndWalls.get(i);
                String name = baseBlockBlock.toString();
                String finalName = name.substring(16, name.length() - 1);
                ResourceLocation baseBlock = ResourceLocation.fromNamespaceAndPath("minecraft", "block/" + finalName);
                map = makeWallModels(map, baseBlockBlock, baseBlock);
                map = makeStairsModels(map, baseBlockBlock, baseBlock);
                map = makeSlabModels(map, baseBlockBlock, baseBlock);
            }
            for (int i = 0; i < blocksWithWalls.size(); i++) {
                Block baseBlockBlock = blocksWithWalls.get(i);
                String name = baseBlockBlock.toString();
                String finalName = name.substring(16, name.length() - 1);
                if (finalName.contains("wood")) {
                    finalName = finalName.substring(0, finalName.length() - 4) + "log";
                }
                ResourceLocation baseBlock = ResourceLocation.fromNamespaceAndPath("minecraft", "block/" + finalName);
                map = makeWallModels(map, baseBlockBlock, baseBlock);
            }
            for (int i = 0; i < blocksWithFragileWalls.size(); i++) {
                Block baseBlockBlock = blocksWithFragileWalls.get(i);
                String name = baseBlockBlock.toString();
                String finalName = name.substring(16, name.length() - 1);
                ResourceLocation baseBlock = ResourceLocation.fromNamespaceAndPath("minecraft", "block/" + finalName);
                map = makeWallModels(map, baseBlockBlock, baseBlock);
            }
            for (int i = 0; i < blocksWithPiles.size(); i++) {
                Block baseBlockBlock = blocksWithPiles.get(i);
                String name = baseBlockBlock.toString();
                String finalName = name.substring(16, name.length() - 1);
                ResourceLocation baseBlock = ResourceLocation.fromNamespaceAndPath("minecraft", "block/" + finalName);
                map = makePileModels(map, baseBlockBlock, baseBlock);
            }
        }));
        // blockstates
        BlockStateFile.addDataProvider(event, Hydrological.MODID, JsonOps.INSTANCE, Util.make(new HashMap<>(), map -> {
            for (int i = 0; i < blocksWithStairsSlabsAndWalls.size(); i++) {
                Block baseBlockBlock = blocksWithStairsSlabsAndWalls.get(i);
                map = makeWallBlockstates(map, baseBlockBlock);
                map = makeStairsBlockstates(map, baseBlockBlock);
                map = makeSlabBlockstates(map, baseBlockBlock);
            }
            for (int i = 0; i < blocksWithWalls.size(); i++) {
                Block baseBlockBlock = blocksWithWalls.get(i);
                map = makeWallBlockstates(map, baseBlockBlock);
            }
            for (int i = 0; i < blocksWithFragileWalls.size(); i++) {
                Block baseBlockBlock = blocksWithFragileWalls.get(i);
                map = makeWallBlockstates(map, baseBlockBlock);
            }
            for (int i = 0; i < blocksWithPiles.size(); i++) {
                Block baseBlockBlock = blocksWithPiles.get(i);
                map = makePileBlockstates(map, baseBlockBlock);
            }
        }));
    }

    private static HashMap makePileModels(HashMap map, Block baseBlockBlock, ResourceLocation baseBlock) {
        ResourceLocation tempPileBlock2 = ResourceLocation.parse("block/failure2");
        ResourceLocation tempPileBlock4 = ResourceLocation.parse("block/failure4");
        ResourceLocation tempPileBlock6 = ResourceLocation.parse("block/failure6");
        ResourceLocation tempPileBlock8 = ResourceLocation.parse("block/failure8");
        ResourceLocation tempPileBlock10 = ResourceLocation.parse("block/failure10");
        ResourceLocation tempPileBlock12 = ResourceLocation.parse("block/failure12");
        ResourceLocation tempPileBlock14 = ResourceLocation.parse("block/failure14");
        ResourceLocation tempPileBlock16 = ResourceLocation.parse("block/failure16");
        ResourceLocation tempPileBlockItem = ResourceLocation.parse("block/failure_block_item");
        for (int o = 0; o < pileBlocks.size(); o++) {
            Map<Block, DeferredHolder<Block, Block>> pileBlockMap = pileBlocks.get(o);
            if (pileBlockMap.containsKey(baseBlockBlock)) {
                tempPileBlock2 = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height2");
                tempPileBlock4 = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height4");
                tempPileBlock6 = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height6");
                tempPileBlock8 = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height8");
                tempPileBlock10 = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height10");
                tempPileBlock12 = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height12");
                tempPileBlock14 = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height14");
                tempPileBlock16 = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height16");
                tempPileBlockItem = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "item/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7));
            }
        }
        ResourceLocation pileBlock2 = tempPileBlock2;
        ResourceLocation pileBlock4 = tempPileBlock4;
        ResourceLocation pileBlock6 = tempPileBlock6;
        ResourceLocation pileBlock8 = tempPileBlock8;
        ResourceLocation pileBlock10 = tempPileBlock10;
        ResourceLocation pileBlock12 = tempPileBlock12;
        ResourceLocation pileBlock14 = tempPileBlock14;
        ResourceLocation pileBlock16 = tempPileBlock16;
        ResourceLocation pileBlockItem = tempPileBlockItem;
        map.put(pileBlock2,
                SimpleModel.createWithoutRenderType(ResourceLocation.fromNamespaceAndPath("hydrol", "block/leaves_height2"))
                        .addTexture("texture", baseBlock));
        map.put(pileBlock4,
                SimpleModel.createWithoutRenderType(ResourceLocation.fromNamespaceAndPath("hydrol", "block/leaves_height4"))
                        .addTexture("texture", baseBlock));
        map.put(pileBlock6,
                SimpleModel.createWithoutRenderType(ResourceLocation.fromNamespaceAndPath("hydrol", "block/leaves_height6"))
                        .addTexture("texture", baseBlock));
        map.put(pileBlock8,
                SimpleModel.createWithoutRenderType(ResourceLocation.fromNamespaceAndPath("hydrol", "block/leaves_height8"))
                        .addTexture("texture", baseBlock));
        map.put(pileBlock10,
                SimpleModel.createWithoutRenderType(ResourceLocation.fromNamespaceAndPath("hydrol", "block/leaves_height10"))
                        .addTexture("texture", baseBlock));
        map.put(pileBlock12,
                SimpleModel.createWithoutRenderType(ResourceLocation.fromNamespaceAndPath("hydrol", "block/leaves_height12"))
                        .addTexture("texture", baseBlock));
        map.put(pileBlock14,
                SimpleModel.createWithoutRenderType(ResourceLocation.fromNamespaceAndPath("hydrol", "block/leaves_height14"))
                        .addTexture("texture", baseBlock));
        map.put(pileBlock16,
                SimpleModel.createWithoutRenderType(ResourceLocation.parse("block/cube_all"))
                        .addTexture("all", baseBlock));
        map.put(pileBlockItem,
                SimpleModel.createWithoutRenderType(pileBlock2));
        return map;
    }

    private static HashMap makeWallModels(HashMap map, Block baseBlockBlock, ResourceLocation baseBlock) {
        ResourceLocation tempWallBlock = ResourceLocation.parse("block/failure");
        ResourceLocation tempWallBlockSide = ResourceLocation.parse("block/failure_side");
        ResourceLocation tempWallBlockSideTall = ResourceLocation.parse("block/failure_side_tall");
        ResourceLocation tempWallBlockItem = ResourceLocation.parse("block/failure_block_item");
        for (int o = 0; o < HydrolBlocks.wallBlocks.size(); o++) {
            Map<Block, DeferredHolder<Block, Block>> wallBlockMap = HydrolBlocks.wallBlocks.get(o);
            if (wallBlockMap.containsKey(baseBlockBlock)) {
                tempWallBlock = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + wallBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_post");
                tempWallBlockSide = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + wallBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_side");
                tempWallBlockSideTall = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + wallBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_side_tall");
                tempWallBlockItem = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "item/" + wallBlockMap.get(baseBlockBlock).getId().toString().substring(7));
            }
        }
        ResourceLocation wallBlock = tempWallBlock;
        ResourceLocation wallBlockSide = tempWallBlockSide;
        ResourceLocation wallBlockSideTall = tempWallBlockSideTall;
        ResourceLocation wallBlockItem = tempWallBlockItem;
        map.put(wallBlock,
                SimpleModel.createWithoutRenderType(ResourceLocation.parse("block/template_wall_post"))
                        .addTexture("wall", baseBlock));
        map.put(wallBlockSide,
                SimpleModel.createWithoutRenderType(ResourceLocation.parse("block/template_wall_side"))
                        .addTexture("wall", baseBlock));
        map.put(wallBlockSideTall,
                SimpleModel.createWithoutRenderType(ResourceLocation.parse("block/template_wall_side_tall"))
                        .addTexture("wall", baseBlock));
        map.put(wallBlockItem,
                SimpleModel.createWithoutRenderType(ResourceLocation.parse("block/wall_inventory"))
                        .addTexture("wall", baseBlock));
        return map;
    }

    private static HashMap makeStairsModels(HashMap map, Block baseBlockBlock, ResourceLocation baseBlock) {
        ResourceLocation tempStairsBlock = ResourceLocation.parse("block/failure");
        ResourceLocation tempStairsBlockInner = ResourceLocation.parse("block/failure_inner");
        ResourceLocation tempStairsBlockOuter = ResourceLocation.parse("block/failure_outer");
        ResourceLocation tempStairsBlockItem = ResourceLocation.parse("block/failure_block_item");
        for (int o = 0; o < HydrolBlocks.stairBlocks.size(); o++) {
            Map<Block, DeferredHolder<Block, Block>> stairBlockMap = HydrolBlocks.stairBlocks.get(o);
            if (stairBlockMap.containsKey(baseBlockBlock)) {
                tempStairsBlock = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + stairBlockMap.get(baseBlockBlock).getId().toString().substring(7));
                tempStairsBlockInner = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + stairBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_inner");
                tempStairsBlockOuter = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + stairBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_outer");
                tempStairsBlockItem = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "item/" + stairBlockMap.get(baseBlockBlock).getId().toString().substring(7));
            }
        }
        ResourceLocation stairsBlock = tempStairsBlock;
        ResourceLocation stairsBlockInner = tempStairsBlockInner;
        ResourceLocation stairsBlockOuter = tempStairsBlockOuter;
        ResourceLocation stairsBlockItem = tempStairsBlockItem;
        map.put(stairsBlock,
                SimpleModel.createWithoutRenderType(ResourceLocation.parse("block/stairs"))
                        .addTexture("bottom", baseBlock)
                        .addTexture("side", baseBlock)
                        .addTexture("top", baseBlock));
        map.put(stairsBlockInner,
                SimpleModel.createWithoutRenderType(ResourceLocation.parse("block/inner_stairs"))
                        .addTexture("bottom", baseBlock)
                        .addTexture("side", baseBlock)
                        .addTexture("top", baseBlock));
        map.put(stairsBlockOuter,
                SimpleModel.createWithoutRenderType(ResourceLocation.parse("block/outer_stairs"))
                        .addTexture("bottom", baseBlock)
                        .addTexture("side", baseBlock)
                        .addTexture("top", baseBlock));
        map.put(stairsBlockItem,
                SimpleModel.createWithoutRenderType(stairsBlock));
        return map;
    }

    private static HashMap makeSlabModels(HashMap map, Block baseBlockBlock, ResourceLocation baseBlock) {
        ResourceLocation tempSlabBlock = ResourceLocation.parse("block/failure");
        ResourceLocation tempSlabBlockTop = ResourceLocation.parse("block/failure_top");
        ResourceLocation tempSlabBlockItem = ResourceLocation.parse("block/failure_block_item");
        for (int o = 0; o < HydrolBlocks.slabBlocks.size(); o++) {
            Map<Block, DeferredHolder<Block, Block>> slabBlockMap = HydrolBlocks.slabBlocks.get(o);
            if (slabBlockMap.containsKey(baseBlockBlock)) {
                tempSlabBlock = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + slabBlockMap.get(baseBlockBlock).getId().toString().substring(7));
                tempSlabBlockTop = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + slabBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_top");
                tempSlabBlockItem = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "item/" + slabBlockMap.get(baseBlockBlock).getId().toString().substring(7));
            }
        }
        ResourceLocation slabBlock = tempSlabBlock;
        ResourceLocation slabBlockTop = tempSlabBlockTop;
        ResourceLocation slabBlockItem = tempSlabBlockItem;
        map.put(slabBlock,
                SimpleModel.createWithoutRenderType(ResourceLocation.parse("block/slab"))
                        .addTexture("bottom", baseBlock)
                        .addTexture("side", baseBlock)
                        .addTexture("top", baseBlock));
        map.put(slabBlockTop,
                SimpleModel.createWithoutRenderType(ResourceLocation.parse("block/slab_top"))
                        .addTexture("bottom", baseBlock)
                        .addTexture("side", baseBlock)
                        .addTexture("top", baseBlock));
        map.put(slabBlockItem,
                SimpleModel.createWithoutRenderType(slabBlock));
        return map;
    }

    private static HashMap makePileBlockstates(HashMap map, Block baseBlockBlock) {
        ResourceLocation tempBlockstate = ResourceLocation.parse("block/failure_blockstate");
        ResourceLocation tempPileBlock2 = ResourceLocation.parse("block/failure2");
        ResourceLocation tempPileBlock4 = ResourceLocation.parse("block/failure4");
        ResourceLocation tempPileBlock6 = ResourceLocation.parse("block/failure6");
        ResourceLocation tempPileBlock8 = ResourceLocation.parse("block/failure8");
        ResourceLocation tempPileBlock10 = ResourceLocation.parse("block/failure10");
        ResourceLocation tempPileBlock12 = ResourceLocation.parse("block/failure12");
        ResourceLocation tempPileBlock14 = ResourceLocation.parse("block/failure14");
        ResourceLocation tempPileBlock16 = ResourceLocation.parse("block/failure16");
        for (int o = 0; o < pileBlocks.size(); o++) {
            Map<Block, DeferredHolder<Block, Block>> pileBlockMap = pileBlocks.get(o);
            if (pileBlockMap.containsKey(baseBlockBlock)) {
                tempBlockstate = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, pileBlockMap.get(baseBlockBlock).getId().toString().substring(7));
                tempPileBlock2 = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height2");
                tempPileBlock4 = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height4");
                tempPileBlock6 = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height6");
                tempPileBlock8 = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height8");
                tempPileBlock10 = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height10");
                tempPileBlock12 = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height12");
                tempPileBlock14 = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height14");
                tempPileBlock16 = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height16");
            }
        }
        ResourceLocation blockstate = tempBlockstate;
        ResourceLocation pileBlock2 = tempPileBlock2;
        ResourceLocation pileBlock4 = tempPileBlock4;
        ResourceLocation pileBlock6 = tempPileBlock6;
        ResourceLocation pileBlock8 = tempPileBlock8;
        ResourceLocation pileBlock10 = tempPileBlock10;
        ResourceLocation pileBlock12 = tempPileBlock12;
        ResourceLocation pileBlock14 = tempPileBlock14;
        ResourceLocation pileBlock16 = tempPileBlock16;
        map.put(blockstate,
                BlockStateFile.variants(BlockStateFile.Variants.builder()
                        .addVariant(
                                BlockStateFile.PropertyValue.create(SnowLayerBlock.LAYERS, 1),
                                BlockStateFile.Model.create(pileBlock2))
                        .addVariant(
                                BlockStateFile.PropertyValue.create(SnowLayerBlock.LAYERS, 2),
                                BlockStateFile.Model.create(pileBlock4))
                        .addVariant(
                                BlockStateFile.PropertyValue.create(SnowLayerBlock.LAYERS, 3),
                                BlockStateFile.Model.create(pileBlock6))
                        .addVariant(
                                BlockStateFile.PropertyValue.create(SnowLayerBlock.LAYERS, 4),
                                BlockStateFile.Model.create(pileBlock8))
                        .addVariant(
                                BlockStateFile.PropertyValue.create(SnowLayerBlock.LAYERS, 5),
                                BlockStateFile.Model.create(pileBlock10))
                        .addVariant(
                                BlockStateFile.PropertyValue.create(SnowLayerBlock.LAYERS, 6),
                                BlockStateFile.Model.create(pileBlock12))
                        .addVariant(
                                BlockStateFile.PropertyValue.create(SnowLayerBlock.LAYERS, 7),
                                BlockStateFile.Model.create(pileBlock14))
                        .addVariant(
                                BlockStateFile.PropertyValue.create(SnowLayerBlock.LAYERS, 8),
                                BlockStateFile.Model.create(pileBlock16))));
        return map;
    }

    private static HashMap makeWallBlockstates(HashMap map, Block baseBlockBlock) {
        ResourceLocation tempWallState = ResourceLocation.parse("failure");
        ResourceLocation tempWallBlock = ResourceLocation.parse("block/failure");
        ResourceLocation tempWallBlockSide = ResourceLocation.parse("block/failure_side");
        ResourceLocation tempWallBlockSideTall = ResourceLocation.parse("block/failure_side_tall");
        for (int o = 0; o < HydrolBlocks.wallBlocks.size(); o++) {
            Map<Block, DeferredHolder<Block, Block>> wallBlockMap = HydrolBlocks.wallBlocks.get(o);
            if (wallBlockMap.containsKey(baseBlockBlock)) {
                tempWallState = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, wallBlockMap.get(baseBlockBlock).getId().toString().substring(7));
                tempWallBlock = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + wallBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_post");
                tempWallBlockSide = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + wallBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_side");
                tempWallBlockSideTall = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + wallBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_side_tall");
            }
        }
        ResourceLocation wallState = tempWallState;
        ResourceLocation wallBlock = tempWallBlock;
        ResourceLocation wallBlockSide = tempWallBlockSide;
        ResourceLocation wallBlockSideTall = tempWallBlockSideTall;
        map.put(wallState,
                BlockStateFile.multipart(BlockStateFile.Multipart.builder()
                        .addWhenApply(BlockStateFile.WhenApply.when(
                                BlockStateFile.Case.create(WallBlock.UP, true),
                                BlockStateFile.Model.create(wallBlock)
                        )).addWhenApply(BlockStateFile.WhenApply.when(
                                BlockStateFile.Case.create(BlockStateProperties.NORTH_WALL, WallSide.LOW),
                                BlockStateFile.Model.create(wallBlockSide)
                        )).addWhenApply(BlockStateFile.WhenApply.when(
                                BlockStateFile.Case.create(BlockStateProperties.EAST_WALL, WallSide.LOW),
                                BlockStateFile.Model.create(wallBlockSide, BlockModelRotation.X0_Y90)
                        )).addWhenApply(BlockStateFile.WhenApply.when(
                                BlockStateFile.Case.create(BlockStateProperties.SOUTH_WALL, WallSide.LOW),
                                BlockStateFile.Model.create(wallBlockSide, BlockModelRotation.X0_Y180)
                        )).addWhenApply(BlockStateFile.WhenApply.when(
                                BlockStateFile.Case.create(BlockStateProperties.WEST_WALL, WallSide.LOW),
                                BlockStateFile.Model.create(wallBlockSide, BlockModelRotation.X0_Y270)
                        )).addWhenApply(BlockStateFile.WhenApply.when(
                                BlockStateFile.Case.create(BlockStateProperties.NORTH_WALL, WallSide.TALL),
                                BlockStateFile.Model.create(wallBlockSideTall)
                        )).addWhenApply(BlockStateFile.WhenApply.when(
                                BlockStateFile.Case.create(BlockStateProperties.EAST_WALL, WallSide.TALL),
                                BlockStateFile.Model.create(wallBlockSideTall, BlockModelRotation.X0_Y90)
                        )).addWhenApply(BlockStateFile.WhenApply.when(
                                BlockStateFile.Case.create(BlockStateProperties.SOUTH_WALL, WallSide.TALL),
                                BlockStateFile.Model.create(wallBlockSideTall, BlockModelRotation.X0_Y180)
                        )).addWhenApply(BlockStateFile.WhenApply.when(
                                BlockStateFile.Case.create(BlockStateProperties.WEST_WALL, WallSide.TALL),
                                BlockStateFile.Model.create(wallBlockSideTall, BlockModelRotation.X0_Y270)
                        ))));
        return map;
    }

    private static HashMap makeStairsBlockstates(HashMap map, Block baseBlockBlock) {
        ResourceLocation tempStairState = ResourceLocation.parse("failure");
        ResourceLocation tempStairBlock = ResourceLocation.parse("block/failure");
        ResourceLocation tempStairBlockInner = ResourceLocation.parse("block/failure_inner");
        ResourceLocation tempStairBlockOuter = ResourceLocation.parse("block/failure_outer");
        for (int o = 0; o < HydrolBlocks.stairBlocks.size(); o++) {
            Map<Block, DeferredHolder<Block, Block>> stairBlockMap = HydrolBlocks.stairBlocks.get(o);
            if (stairBlockMap.containsKey(baseBlockBlock)) {
                tempStairState = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, stairBlockMap.get(baseBlockBlock).getId().toString().substring(7));
                tempStairBlock = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + stairBlockMap.get(baseBlockBlock).getId().toString().substring(7));
                tempStairBlockInner = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + stairBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_inner");
                tempStairBlockOuter = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + stairBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_outer");
            }
        }
        ResourceLocation stairState = tempStairState;
        ResourceLocation stairBlock = tempStairBlock;
        ResourceLocation stairBlockInner = tempStairBlockInner;
        ResourceLocation stairBlockOuter = tempStairBlockOuter;
        BlockStateFile.Variants variants = BlockStateFile.Variants.builder();
        for (Direction facing : StairBlock.FACING.getPossibleValues()) {
            for (Half half : StairBlock.HALF.getPossibleValues()) {
                for (StairsShape shape : StairBlock.SHAPE.getPossibleValues()) {
                    ResourceLocation model =
                            shape == StairsShape.INNER_LEFT || shape == StairsShape.INNER_RIGHT ? stairBlockInner
                                    : shape == StairsShape.OUTER_LEFT || shape == StairsShape.OUTER_RIGHT ? stairBlockOuter
                                    : stairBlock;
                    int x = half == Half.TOP ? 180 : 0;
                    int y = ((int) facing.toYRot() + 90
                            + (shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT ? 270 : 0)
                            + (half == Half.TOP && shape != StairsShape.STRAIGHT ? 90 : 0))
                            % 360;
                    boolean uvlock = x != 0 || y != 0;
                    variants.addVariant(List.of(BlockStateFile.PropertyValue.create(StairBlock.FACING, facing), BlockStateFile.PropertyValue.create(StairBlock.HALF, half), BlockStateFile.PropertyValue.create(StairBlock.SHAPE, shape)),
                            BlockStateFile.Model.create(model, BlockModelRotation.by(x, y), uvlock, 1));
                }
            }
        }
        map.put(stairState, BlockStateFile.variants(variants));
        return map;
    }

    private static HashMap makeSlabBlockstates(HashMap map, Block baseBlockBlock) {
        String name = baseBlockBlock.toString();
        ResourceLocation baseBlock = ResourceLocation.fromNamespaceAndPath("minecraft", "block/" + name.substring(16, name.length() - 1));
        ResourceLocation tempSlabState = ResourceLocation.parse("failure");
        ResourceLocation tempSlabBlock = ResourceLocation.parse("block/failure");
        ResourceLocation tempSlabBlockTop = ResourceLocation.parse("block/failure_top");
        for (int o = 0; o < HydrolBlocks.slabBlocks.size(); o++) {
            Map<Block, DeferredHolder<Block, Block>> slabBlockMap = HydrolBlocks.slabBlocks.get(o);
            if (slabBlockMap.containsKey(baseBlockBlock)) {
                tempSlabState = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, slabBlockMap.get(baseBlockBlock).getId().toString().substring(7));
                tempSlabBlock = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + slabBlockMap.get(baseBlockBlock).getId().toString().substring(7));
                tempSlabBlockTop = ResourceLocation.fromNamespaceAndPath(Hydrological.MODID, "block/" + slabBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_top");
            }
        }
        ResourceLocation slabState = tempSlabState;
        ResourceLocation slabBlock = tempSlabBlock;
        ResourceLocation slabBlockTop = tempSlabBlockTop;
        if (name.contains("red_mushroom_block")) {
            baseBlock = ResourceLocation.fromNamespaceAndPath("hydrol", "block/red_mushroom_block_double_slab");
        } else if (name.contains("brown_mushroom_block")) {
            baseBlock = ResourceLocation.fromNamespaceAndPath("hydrol", "block/brown_mushroom_block_double_slab");
        }
        map.put(slabState,
                BlockStateFile.variants(BlockStateFile.Variants.builder()
                        .addVariant(
                                BlockStateFile.PropertyValue.create(SlabBlock.TYPE, SlabType.BOTTOM),
                                BlockStateFile.Model.create(slabBlock))
                        .addVariant(
                                BlockStateFile.PropertyValue.create(SlabBlock.TYPE, SlabType.DOUBLE),
                                BlockStateFile.Model.create(baseBlock))
                        .addVariant(
                                BlockStateFile.PropertyValue.create(SlabBlock.TYPE, SlabType.TOP),
                                BlockStateFile.Model.create(slabBlockTop))));
        return map;
    }
}
