package com.Apothic0n.Hydrological.core.events;

import com.Apothic0n.Hydrological.Hydrological;
import com.Apothic0n.Hydrological.core.objects.HydrolBlocks;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.commoble.databuddy.datagen.BlockStateBuilder;
import net.commoble.databuddy.datagen.SimpleModel;
import net.minecraft.core.Direction;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.Apothic0n.Hydrological.core.objects.HydrolBlocks.*;

@EventBusSubscriber(modid = Hydrological.MODID)
public class CommonModEvents {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event) {
        addItemDefinitions(event);

        // models
        SimpleModel.addDataProvider(event, Hydrological.MODID, JsonOps.INSTANCE, Util.make(new HashMap<>(), map ->
        {
            for (int i = 0; i < blocksWithStairsSlabsAndWalls.size(); i++) {
                Block baseBlockBlock = blocksWithStairsSlabsAndWalls.get(i);
                String name = baseBlockBlock.toString();
                String finalName = name.substring(16, name.length() - 1);
                Identifier baseBlock = Identifier.fromNamespaceAndPath("minecraft", "block/" + finalName);
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
                Identifier baseBlock = Identifier.fromNamespaceAndPath("minecraft", "block/" + finalName);
                map = makeWallModels(map, baseBlockBlock, baseBlock);
            }
            for (int i = 0; i < blocksWithFragileWalls.size(); i++) {
                Block baseBlockBlock = blocksWithFragileWalls.get(i);
                String name = baseBlockBlock.toString();
                String finalName = name.substring(16, name.length() - 1);
                Identifier baseBlock = Identifier.fromNamespaceAndPath("minecraft", "block/" + finalName);
                map = makeWallModels(map, baseBlockBlock, baseBlock);
            }
            for (int i = 0; i < blocksWithPiles.size(); i++) {
                Block baseBlockBlock = blocksWithPiles.get(i);
                String name = baseBlockBlock.toString();
                String finalName = name.substring(16, name.length() - 1);
                Identifier baseBlock = Identifier.fromNamespaceAndPath("minecraft", "block/" + finalName);
                map = makePileModels(map, baseBlockBlock, baseBlock);
            }
        }));
        // blockstates
        BlockStateBuilder.addDataProvider(event, Util.make(new HashMap<>(), map -> {
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

    private static void addItemDefinitions(GatherDataEvent.Client event) {
        Map<Identifier, Identifier> itemModels = new HashMap<>();
        itemModels.put(Identifier.fromNamespaceAndPath(Hydrological.MODID, "dry_grass"), Identifier.fromNamespaceAndPath(Hydrological.MODID, "item/dry_grass"));
        addItemDefinitions(itemModels, wallBlocks);
        addItemDefinitions(itemModels, stairBlocks);
        addItemDefinitions(itemModels, slabBlocks);
        addItemDefinitions(itemModels, pileBlocks);
        DataProvider.Factory<DataProvider> factory = output -> new ItemDefinitionProvider(output, itemModels);
        event.getGenerator().addProvider(true, factory);
    }

    private static void addItemDefinitions(Map<Identifier, Identifier> itemModels, List<Map<Block, DeferredHolder<Block, Block>>> blocks) {
        for (Map<Block, DeferredHolder<Block, Block>> blockMap : blocks) {
            for (DeferredHolder<Block, Block> block : blockMap.values()) {
                String path = block.getId().getPath();
                itemModels.put(Identifier.fromNamespaceAndPath(Hydrological.MODID, path), Identifier.fromNamespaceAndPath(Hydrological.MODID, "item/" + path));
            }
        }
    }

    private record ItemDefinitionProvider(PackOutput output, Map<Identifier, Identifier> itemModels) implements DataProvider {
        @Override
        public CompletableFuture<?> run(CachedOutput cache) {
            PackOutput.PathProvider pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");
            List<CompletableFuture<?>> futures = new ArrayList<>();
            for (Map.Entry<Identifier, Identifier> entry : itemModels.entrySet()) {
                JsonObject root = new JsonObject();
                JsonObject model = new JsonObject();
                model.addProperty("type", "minecraft:model");
                model.addProperty("model", entry.getValue().toString());
                root.add("model", model);
                futures.add(DataProvider.saveStable(cache, root, pathProvider.json(entry.getKey())));
            }
            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        }

        @Override
        public String getName() {
            return Hydrological.MODID + " item definitions";
        }
    }

    private static SimpleModel simpleModel(Identifier parent, boolean cutout) {
        return cutout
                ? SimpleModel.create(parent, SimpleModel.RenderTypes.CUTOUT)
                : SimpleModel.createWithoutRenderType(parent);
    }

    private static HashMap makePileModels(HashMap map, Block baseBlockBlock, Identifier baseBlock) {
        Identifier tempPileBlock2 = Identifier.parse("block/failure2");
        Identifier tempPileBlock4 = Identifier.parse("block/failure4");
        Identifier tempPileBlock6 = Identifier.parse("block/failure6");
        Identifier tempPileBlock8 = Identifier.parse("block/failure8");
        Identifier tempPileBlock10 = Identifier.parse("block/failure10");
        Identifier tempPileBlock12 = Identifier.parse("block/failure12");
        Identifier tempPileBlock14 = Identifier.parse("block/failure14");
        Identifier tempPileBlock16 = Identifier.parse("block/failure16");
        Identifier tempPileBlockItem = Identifier.parse("block/failure_block_item");
        for (int o = 0; o < pileBlocks.size(); o++) {
            Map<Block, DeferredHolder<Block, Block>> pileBlockMap = pileBlocks.get(o);
            if (pileBlockMap.containsKey(baseBlockBlock)) {
                tempPileBlock2 = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height2");
                tempPileBlock4 = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height4");
                tempPileBlock6 = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height6");
                tempPileBlock8 = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height8");
                tempPileBlock10 = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height10");
                tempPileBlock12 = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height12");
                tempPileBlock14 = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height14");
                tempPileBlock16 = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height16");
                tempPileBlockItem = Identifier.fromNamespaceAndPath(Hydrological.MODID, "item/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7));
            }
        }
        Identifier pileBlock2 = tempPileBlock2;
        Identifier pileBlock4 = tempPileBlock4;
        Identifier pileBlock6 = tempPileBlock6;
        Identifier pileBlock8 = tempPileBlock8;
        Identifier pileBlock10 = tempPileBlock10;
        Identifier pileBlock12 = tempPileBlock12;
        Identifier pileBlock14 = tempPileBlock14;
        Identifier pileBlock16 = tempPileBlock16;
        Identifier pileBlockItem = tempPileBlockItem;
        map.put(pileBlock2,
                simpleModel(Identifier.fromNamespaceAndPath("hydrol", "block/leaves_height2"), true)
                        .addTexture("texture", baseBlock));
        map.put(pileBlock4,
                simpleModel(Identifier.fromNamespaceAndPath("hydrol", "block/leaves_height4"), true)
                        .addTexture("texture", baseBlock));
        map.put(pileBlock6,
                simpleModel(Identifier.fromNamespaceAndPath("hydrol", "block/leaves_height6"), true)
                        .addTexture("texture", baseBlock));
        map.put(pileBlock8,
                simpleModel(Identifier.fromNamespaceAndPath("hydrol", "block/leaves_height8"), true)
                        .addTexture("texture", baseBlock));
        map.put(pileBlock10,
                simpleModel(Identifier.fromNamespaceAndPath("hydrol", "block/leaves_height10"), true)
                        .addTexture("texture", baseBlock));
        map.put(pileBlock12,
                simpleModel(Identifier.fromNamespaceAndPath("hydrol", "block/leaves_height12"), true)
                        .addTexture("texture", baseBlock));
        map.put(pileBlock14,
                simpleModel(Identifier.fromNamespaceAndPath("hydrol", "block/leaves_height14"), true)
                        .addTexture("texture", baseBlock));
        map.put(pileBlock16,
                simpleModel(Identifier.parse("block/cube_all"), true)
                        .addTexture("all", baseBlock));
        map.put(pileBlockItem,
                SimpleModel.createWithoutRenderType(pileBlock2));
        return map;
    }

    private static HashMap makeWallModels(HashMap map, Block baseBlockBlock, Identifier baseBlock) {
        Identifier tempWallBlock = Identifier.parse("block/failure");
        Identifier tempWallBlockSide = Identifier.parse("block/failure_side");
        Identifier tempWallBlockSideTall = Identifier.parse("block/failure_side_tall");
        Identifier tempWallBlockItem = Identifier.parse("block/failure_block_item");
        for (int o = 0; o < HydrolBlocks.wallBlocks.size(); o++) {
            Map<Block, DeferredHolder<Block, Block>> wallBlockMap = HydrolBlocks.wallBlocks.get(o);
            if (wallBlockMap.containsKey(baseBlockBlock)) {
                tempWallBlock = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + wallBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_post");
                tempWallBlockSide = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + wallBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_side");
                tempWallBlockSideTall = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + wallBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_side_tall");
                tempWallBlockItem = Identifier.fromNamespaceAndPath(Hydrological.MODID, "item/" + wallBlockMap.get(baseBlockBlock).getId().toString().substring(7));
            }
        }
        Identifier wallBlock = tempWallBlock;
        Identifier wallBlockSide = tempWallBlockSide;
        Identifier wallBlockSideTall = tempWallBlockSideTall;
        Identifier wallBlockItem = tempWallBlockItem;
        boolean cutout = blocksWithFragileWalls.contains(baseBlockBlock);
        map.put(wallBlock,
                simpleModel(Identifier.parse("block/template_wall_post"), cutout)
                        .addTexture("wall", baseBlock));
        map.put(wallBlockSide,
                simpleModel(Identifier.parse("block/template_wall_side"), cutout)
                        .addTexture("wall", baseBlock));
        map.put(wallBlockSideTall,
                simpleModel(Identifier.parse("block/template_wall_side_tall"), cutout)
                        .addTexture("wall", baseBlock));
        map.put(wallBlockItem,
                SimpleModel.createWithoutRenderType(Identifier.parse("block/wall_inventory"))
                        .addTexture("wall", baseBlock));
        return map;
    }

    private static HashMap makeStairsModels(HashMap map, Block baseBlockBlock, Identifier baseBlock) {
        Identifier tempStairsBlock = Identifier.parse("block/failure");
        Identifier tempStairsBlockInner = Identifier.parse("block/failure_inner");
        Identifier tempStairsBlockOuter = Identifier.parse("block/failure_outer");
        Identifier tempStairsBlockItem = Identifier.parse("block/failure_block_item");
        for (int o = 0; o < HydrolBlocks.stairBlocks.size(); o++) {
            Map<Block, DeferredHolder<Block, Block>> stairBlockMap = HydrolBlocks.stairBlocks.get(o);
            if (stairBlockMap.containsKey(baseBlockBlock)) {
                tempStairsBlock = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + stairBlockMap.get(baseBlockBlock).getId().toString().substring(7));
                tempStairsBlockInner = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + stairBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_inner");
                tempStairsBlockOuter = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + stairBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_outer");
                tempStairsBlockItem = Identifier.fromNamespaceAndPath(Hydrological.MODID, "item/" + stairBlockMap.get(baseBlockBlock).getId().toString().substring(7));
            }
        }
        Identifier stairsBlock = tempStairsBlock;
        Identifier stairsBlockInner = tempStairsBlockInner;
        Identifier stairsBlockOuter = tempStairsBlockOuter;
        Identifier stairsBlockItem = tempStairsBlockItem;
        map.put(stairsBlock,
                SimpleModel.createWithoutRenderType(Identifier.parse("block/stairs"))
                        .addTexture("bottom", baseBlock)
                        .addTexture("side", baseBlock)
                        .addTexture("top", baseBlock));
        map.put(stairsBlockInner,
                SimpleModel.createWithoutRenderType(Identifier.parse("block/inner_stairs"))
                        .addTexture("bottom", baseBlock)
                        .addTexture("side", baseBlock)
                        .addTexture("top", baseBlock));
        map.put(stairsBlockOuter,
                SimpleModel.createWithoutRenderType(Identifier.parse("block/outer_stairs"))
                        .addTexture("bottom", baseBlock)
                        .addTexture("side", baseBlock)
                        .addTexture("top", baseBlock));
        map.put(stairsBlockItem,
                SimpleModel.createWithoutRenderType(stairsBlock));
        return map;
    }

    private static HashMap makeSlabModels(HashMap map, Block baseBlockBlock, Identifier baseBlock) {
        Identifier tempSlabBlock = Identifier.parse("block/failure");
        Identifier tempSlabBlockTop = Identifier.parse("block/failure_top");
        Identifier tempSlabBlockItem = Identifier.parse("block/failure_block_item");
        for (int o = 0; o < HydrolBlocks.slabBlocks.size(); o++) {
            Map<Block, DeferredHolder<Block, Block>> slabBlockMap = HydrolBlocks.slabBlocks.get(o);
            if (slabBlockMap.containsKey(baseBlockBlock)) {
                tempSlabBlock = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + slabBlockMap.get(baseBlockBlock).getId().toString().substring(7));
                tempSlabBlockTop = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + slabBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_top");
                tempSlabBlockItem = Identifier.fromNamespaceAndPath(Hydrological.MODID, "item/" + slabBlockMap.get(baseBlockBlock).getId().toString().substring(7));
            }
        }
        Identifier slabBlock = tempSlabBlock;
        Identifier slabBlockTop = tempSlabBlockTop;
        Identifier slabBlockItem = tempSlabBlockItem;
        map.put(slabBlock,
                SimpleModel.createWithoutRenderType(Identifier.parse("block/slab"))
                        .addTexture("bottom", baseBlock)
                        .addTexture("side", baseBlock)
                        .addTexture("top", baseBlock));
        map.put(slabBlockTop,
                SimpleModel.createWithoutRenderType(Identifier.parse("block/slab_top"))
                        .addTexture("bottom", baseBlock)
                        .addTexture("side", baseBlock)
                        .addTexture("top", baseBlock));
        map.put(slabBlockItem,
                SimpleModel.createWithoutRenderType(slabBlock));
        return map;
    }

    private static HashMap makePileBlockstates(HashMap map, Block baseBlockBlock) {
        Identifier tempBlockstate = Identifier.parse("block/failure_blockstate");
        Identifier tempPileBlock2 = Identifier.parse("block/failure2");
        Identifier tempPileBlock4 = Identifier.parse("block/failure4");
        Identifier tempPileBlock6 = Identifier.parse("block/failure6");
        Identifier tempPileBlock8 = Identifier.parse("block/failure8");
        Identifier tempPileBlock10 = Identifier.parse("block/failure10");
        Identifier tempPileBlock12 = Identifier.parse("block/failure12");
        Identifier tempPileBlock14 = Identifier.parse("block/failure14");
        Identifier tempPileBlock16 = Identifier.parse("block/failure16");
        for (int o = 0; o < pileBlocks.size(); o++) {
            Map<Block, DeferredHolder<Block, Block>> pileBlockMap = pileBlocks.get(o);
            if (pileBlockMap.containsKey(baseBlockBlock)) {
                tempBlockstate = Identifier.fromNamespaceAndPath(Hydrological.MODID, pileBlockMap.get(baseBlockBlock).getId().toString().substring(7));
                tempPileBlock2 = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height2");
                tempPileBlock4 = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height4");
                tempPileBlock6 = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height6");
                tempPileBlock8 = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height8");
                tempPileBlock10 = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height10");
                tempPileBlock12 = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height12");
                tempPileBlock14 = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height14");
                tempPileBlock16 = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + pileBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_height16");
            }
        }
        Identifier blockstate = tempBlockstate;
        Identifier pileBlock2 = tempPileBlock2;
        Identifier pileBlock4 = tempPileBlock4;
        Identifier pileBlock6 = tempPileBlock6;
        Identifier pileBlock8 = tempPileBlock8;
        Identifier pileBlock10 = tempPileBlock10;
        Identifier pileBlock12 = tempPileBlock12;
        Identifier pileBlock14 = tempPileBlock14;
        Identifier pileBlock16 = tempPileBlock16;
        map.put(blockstate,
                BlockStateBuilder.variants(variants -> variants
                        .addVariant(SnowLayerBlock.LAYERS, 1,
                                BlockStateBuilder.model(pileBlock2))
                        .addVariant(SnowLayerBlock.LAYERS, 2,
                                BlockStateBuilder.model(pileBlock4))
                        .addVariant(SnowLayerBlock.LAYERS, 3,
                                BlockStateBuilder.model(pileBlock6))
                        .addVariant(SnowLayerBlock.LAYERS, 4,
                                BlockStateBuilder.model(pileBlock8))
                        .addVariant(SnowLayerBlock.LAYERS, 5,
                                BlockStateBuilder.model(pileBlock10))
                        .addVariant(SnowLayerBlock.LAYERS, 6,
                                BlockStateBuilder.model(pileBlock12))
                        .addVariant(SnowLayerBlock.LAYERS, 7,
                                BlockStateBuilder.model(pileBlock14))
                        .addVariant(SnowLayerBlock.LAYERS, 8,
                                BlockStateBuilder.model(pileBlock16))));
        return map;
    }

    private static HashMap makeWallBlockstates(HashMap map, Block baseBlockBlock) {
        Identifier tempWallState = Identifier.parse("failure");
        Identifier tempWallBlock = Identifier.parse("block/failure");
        Identifier tempWallBlockSide = Identifier.parse("block/failure_side");
        Identifier tempWallBlockSideTall = Identifier.parse("block/failure_side_tall");
        for (int o = 0; o < HydrolBlocks.wallBlocks.size(); o++) {
            Map<Block, DeferredHolder<Block, Block>> wallBlockMap = HydrolBlocks.wallBlocks.get(o);
            if (wallBlockMap.containsKey(baseBlockBlock)) {
                tempWallState = Identifier.fromNamespaceAndPath(Hydrological.MODID, wallBlockMap.get(baseBlockBlock).getId().toString().substring(7));
                tempWallBlock = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + wallBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_post");
                tempWallBlockSide = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + wallBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_side");
                tempWallBlockSideTall = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + wallBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_side_tall");
            }
        }
        Identifier wallState = tempWallState;
        Identifier wallBlock = tempWallBlock;
        Identifier wallBlockSide = tempWallBlockSide;
        Identifier wallBlockSideTall = tempWallBlockSideTall;
        map.put(wallState,
                BlockStateBuilder.multipart(multipart -> multipart
                        .applyWhen(BlockStateBuilder.model(wallBlock), WallBlock.UP, true)
                        .applyWhen(BlockStateBuilder.model(wallBlockSide), BlockStateProperties.NORTH_WALL, WallSide.LOW)
                        .applyWhen(BlockStateBuilder.model(wallBlockSide, 0, 90, false), BlockStateProperties.EAST_WALL, WallSide.LOW)
                        .applyWhen(BlockStateBuilder.model(wallBlockSide, 0, 180, false), BlockStateProperties.SOUTH_WALL, WallSide.LOW)
                        .applyWhen(BlockStateBuilder.model(wallBlockSide, 0, 270, false), BlockStateProperties.WEST_WALL, WallSide.LOW)
                        .applyWhen(BlockStateBuilder.model(wallBlockSideTall), BlockStateProperties.NORTH_WALL, WallSide.TALL)
                        .applyWhen(BlockStateBuilder.model(wallBlockSideTall, 0, 90, false), BlockStateProperties.EAST_WALL, WallSide.TALL)
                        .applyWhen(BlockStateBuilder.model(wallBlockSideTall, 0, 180, false), BlockStateProperties.SOUTH_WALL, WallSide.TALL)
                        .applyWhen(BlockStateBuilder.model(wallBlockSideTall, 0, 270, false), BlockStateProperties.WEST_WALL, WallSide.TALL)));
        return map;
    }

    private static HashMap makeStairsBlockstates(HashMap map, Block baseBlockBlock) {
        Identifier tempStairState = Identifier.parse("failure");
        Identifier tempStairBlock = Identifier.parse("block/failure");
        Identifier tempStairBlockInner = Identifier.parse("block/failure_inner");
        Identifier tempStairBlockOuter = Identifier.parse("block/failure_outer");
        for (int o = 0; o < HydrolBlocks.stairBlocks.size(); o++) {
            Map<Block, DeferredHolder<Block, Block>> stairBlockMap = HydrolBlocks.stairBlocks.get(o);
            if (stairBlockMap.containsKey(baseBlockBlock)) {
                tempStairState = Identifier.fromNamespaceAndPath(Hydrological.MODID, stairBlockMap.get(baseBlockBlock).getId().toString().substring(7));
                tempStairBlock = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + stairBlockMap.get(baseBlockBlock).getId().toString().substring(7));
                tempStairBlockInner = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + stairBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_inner");
                tempStairBlockOuter = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + stairBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_outer");
            }
        }
        Identifier stairState = tempStairState;
        Identifier stairBlock = tempStairBlock;
        Identifier stairBlockInner = tempStairBlockInner;
        Identifier stairBlockOuter = tempStairBlockOuter;
        BlockStateBuilder.Variants variants = BlockStateBuilder.Variants.builder();
        for (Direction facing : StairBlock.FACING.getPossibleValues()) {
            for (Half half : StairBlock.HALF.getPossibleValues()) {
                for (StairsShape shape : StairBlock.SHAPE.getPossibleValues()) {
                    Identifier model =
                            shape == StairsShape.INNER_LEFT || shape == StairsShape.INNER_RIGHT ? stairBlockInner
                                    : shape == StairsShape.OUTER_LEFT || shape == StairsShape.OUTER_RIGHT ? stairBlockOuter
                                    : stairBlock;
                    int x = half == Half.TOP ? 180 : 0;
                    int y = ((int) facing.toYRot() + 90
                            + (shape == StairsShape.INNER_LEFT || shape == StairsShape.OUTER_LEFT ? 270 : 0)
                            + (half == Half.TOP && shape != StairsShape.STRAIGHT ? 90 : 0))
                            % 360;
                    boolean uvlock = x != 0 || y != 0;
                    variants.addMultiPropertyVariant(propertyValues -> propertyValues
                            .addPropertyValue(StairBlock.FACING, facing)
                            .addPropertyValue(StairBlock.HALF, half)
                            .addPropertyValue(StairBlock.SHAPE, shape),
                            BlockStateBuilder.model(model, x, y, uvlock));
                }
            }
        }
        map.put(stairState, BlockStateBuilder.variants(variants));
        return map;
    }

    private static HashMap makeSlabBlockstates(HashMap map, Block baseBlockBlock) {
        String name = baseBlockBlock.toString();
        Identifier baseBlock = Identifier.fromNamespaceAndPath("minecraft", "block/" + name.substring(16, name.length() - 1));
        Identifier tempSlabState = Identifier.parse("failure");
        Identifier tempSlabBlock = Identifier.parse("block/failure");
        Identifier tempSlabBlockTop = Identifier.parse("block/failure_top");
        for (int o = 0; o < HydrolBlocks.slabBlocks.size(); o++) {
            Map<Block, DeferredHolder<Block, Block>> slabBlockMap = HydrolBlocks.slabBlocks.get(o);
            if (slabBlockMap.containsKey(baseBlockBlock)) {
                tempSlabState = Identifier.fromNamespaceAndPath(Hydrological.MODID, slabBlockMap.get(baseBlockBlock).getId().toString().substring(7));
                tempSlabBlock = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + slabBlockMap.get(baseBlockBlock).getId().toString().substring(7));
                tempSlabBlockTop = Identifier.fromNamespaceAndPath(Hydrological.MODID, "block/" + slabBlockMap.get(baseBlockBlock).getId().toString().substring(7) + "_top");
            }
        }
        Identifier slabState = tempSlabState;
        Identifier slabBlock = tempSlabBlock;
        Identifier slabBlockTop = tempSlabBlockTop;
        Identifier doubleSlabBlock;
        if (name.contains("red_mushroom_block")) {
            doubleSlabBlock = Identifier.fromNamespaceAndPath("hydrol", "block/red_mushroom_block_double_slab");
        } else if (name.contains("brown_mushroom_block")) {
            doubleSlabBlock = Identifier.fromNamespaceAndPath("hydrol", "block/brown_mushroom_block_double_slab");
        } else {
            doubleSlabBlock = baseBlock;
        }
        map.put(slabState,
                BlockStateBuilder.variants(variants -> variants
                        .addVariant(SlabBlock.TYPE, SlabType.BOTTOM,
                                BlockStateBuilder.model(slabBlock))
                        .addVariant(SlabBlock.TYPE, SlabType.DOUBLE,
                                BlockStateBuilder.model(doubleSlabBlock))
                        .addVariant(SlabBlock.TYPE, SlabType.TOP,
                                BlockStateBuilder.model(slabBlockTop))));
        return map;
    }
}
