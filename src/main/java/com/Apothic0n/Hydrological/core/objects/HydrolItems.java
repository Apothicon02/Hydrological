package com.Apothic0n.Hydrological.core.objects;

import com.Apothic0n.Hydrological.Hydrological;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.Apothic0n.Hydrological.core.objects.HydrolBlocks.*;

public final class HydrolItems extends Items {
    private HydrolItems() {}

    private static ResourceKey<Item> itemKey(String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Hydrological.MODID, name));
    }

    private static Item.Properties itemProps(String name) {
        return new Item.Properties().setId(itemKey(name));
    }

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Hydrological.MODID);

    public static final DeferredHolder<Item, Item> DRY_GRASS = ITEMS.register("dry_grass", () ->
            new BlockItem(HydrolBlocks.DRY_GRASS.get(), itemProps("dry_grass")));

    public static final List<DeferredHolder<Item, Item>> wallItems = new ArrayList<>(List.of());
    public static final List<DeferredHolder<Item, Item>> stairItems = new ArrayList<>(List.of());
    public static final List<DeferredHolder<Item, Item>> slabItems = new ArrayList<>(List.of());
    public static final List<DeferredHolder<Item, Item>> pileItems = new ArrayList<>(List.of());

    public static void generateStairsSlabsWalls() {
        for (int i = 0; i < blocksWithStairsSlabsAndWalls.size(); i++) {
            Block baseBlock = blocksWithStairsSlabsAndWalls.get(i);
            wallItems.add(createWallItems(baseBlock));
            stairItems.add(createStairItems(baseBlock));
            slabItems.add(createSlabItems(baseBlock));
        }
        for (int i = 0; i < blocksWithWalls.size(); i++) {
            Block baseBlock = blocksWithWalls.get(i);
            wallItems.add(createWallItems(baseBlock));
        }
        for (int i = 0; i < blocksWithFragileWalls.size(); i++) {
            Block baseBlock = blocksWithFragileWalls.get(i);
            wallItems.add(createWallItems(baseBlock));
        }
        for (int i = 0; i < blocksWithPiles.size(); i++) {
            Block baseBlock = blocksWithPiles.get(i);
            pileItems.add(createPileItems(baseBlock));
        }
    }

    public static DeferredHolder<Item, Item> createPileItems(Block baseBlock) {
        DeferredHolder<Block, Block> block = getBlock(baseBlock, pileBlocks);
        String itemName = block.getId().toString().substring(7);
        return ITEMS.register(itemName, () ->
                new BlockItem(block.get(), itemProps(itemName))
        );
    }

    public static DeferredHolder<Item, Item> createWallItems(Block baseBlock) {
        DeferredHolder<Block, Block> block = getBlock(baseBlock, wallBlocks);
        String itemName = block.getId().toString().substring(7);
        return ITEMS.register(itemName, () ->
                new BlockItem(block.get(), itemProps(itemName))
        );
    }

    public static DeferredHolder<Item, Item> createStairItems(Block baseBlock) {
        DeferredHolder<Block, Block> block = getBlock(baseBlock, stairBlocks);
        String itemName = block.getId().toString().substring(7);
        return ITEMS.register(itemName, () ->
                new BlockItem(block.get(), itemProps(itemName))
        );
    }

    public static DeferredHolder<Item, Item> createSlabItems(Block baseBlock) {
        DeferredHolder<Block, Block> block = getBlock(baseBlock, slabBlocks);
        String itemName = block.getId().toString().substring(7);
        return ITEMS.register(itemName, () ->
                new BlockItem(block.get(), itemProps(itemName))
        );
    }

    public static DeferredHolder<Block, Block> getBlock(Block block, List<Map<Block, DeferredHolder<Block, Block>>> blockList) {
        for (int i = 0; i < blockList.size(); i++) {
            if (blockList.get(i).containsKey(block)) {
                return blockList.get(i).get(block);
            }
        }
        return HydrolBlocks.DRY_GRASS; //this means it messed up
    }
}