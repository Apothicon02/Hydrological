package com.Apothic0n.Hydrological.core.objects;

import com.Apothic0n.Hydrological.Hydrological;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.minecraft.world.level.block.Blocks.*;

public final class HydrolBlocks {
    private HydrolBlocks() {}

    private static ResourceKey<Block> blockKey(String name) {
        return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Hydrological.MODID, name));
    }

    private static BlockBehaviour.Properties blockProps(String name, BlockBehaviour.Properties props) {
        return props.setId(blockKey(name));
    }

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, Hydrological.MODID);

    public static final DeferredHolder<Block, Block> AMETHYST_VINES = BLOCKS.register("amethyst_vines", () ->
            new AmethystVinesBlock(blockProps("amethyst_vines", BlockBehaviour.Properties.ofFullCopy(AMETHYST_CLUSTER)
                    .randomTicks().strength(0.2F).noCollision().sound(SoundType.MEDIUM_AMETHYST_BUD))));
    public static final DeferredHolder<Block, Block> AMETHYST_VINES_PLANT = BLOCKS.register("amethyst_vines_plant", () ->
            new AmethystVinesBlock(blockProps("amethyst_vines_plant", BlockBehaviour.Properties.ofFullCopy(AMETHYST_CLUSTER)
                    .noCollision().strength(0.2F).sound(SoundType.LARGE_AMETHYST_BUD))));
    public static final DeferredHolder<Block, Block> DRY_GRASS = BLOCKS.register("dry_grass", () ->
            new DryGrassBlock(blockProps("dry_grass", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW)
                    .replaceable().noCollision().instabreak().offsetType(BlockBehaviour.OffsetType.XZ).sound(SoundType.CROP).pushReaction(PushReaction.DESTROY))));

    public static List<Block> blocksWithStairsSlabsAndWalls = List.of(
            RED_MUSHROOM_BLOCK, BROWN_MUSHROOM_BLOCK
    );

    public static List<Block> blocksWithWalls = List.of(
            MUSHROOM_STEM, OAK_WOOD, DARK_OAK_WOOD, BIRCH_WOOD, SPRUCE_WOOD, JUNGLE_WOOD, ACACIA_WOOD, MANGROVE_WOOD, CHERRY_WOOD
    );

    public static List<Block> blocksWithFragileWalls = List.of(
            OAK_LEAVES, DARK_OAK_LEAVES, BIRCH_LEAVES, SPRUCE_LEAVES, JUNGLE_LEAVES, ACACIA_LEAVES, MANGROVE_LEAVES, CHERRY_LEAVES, AZALEA_LEAVES, FLOWERING_AZALEA_LEAVES
    );

    public static List<Block> blocksWithPiles = List.of(
            OAK_LEAVES, DARK_OAK_LEAVES, BIRCH_LEAVES, SPRUCE_LEAVES, JUNGLE_LEAVES, ACACIA_LEAVES, MANGROVE_LEAVES, CHERRY_LEAVES, AZALEA_LEAVES, FLOWERING_AZALEA_LEAVES
    );


    public static final List<Map<Block, DeferredHolder<Block, Block>>> wallBlocks = new ArrayList<>(List.of());
    public static final List<Map<Block, DeferredHolder<Block, Block>>> stairBlocks = new ArrayList<>(List.of());
    public static final List<Map<Block, DeferredHolder<Block, Block>>> slabBlocks = new ArrayList<>(List.of());
    public static final List<Map<Block, DeferredHolder<Block, Block>>> pileBlocks = new ArrayList<>(List.of());

    public static void generateStairsSlabsWalls() {
        for (int i = 0; i < blocksWithStairsSlabsAndWalls.size(); i++) {
            Block baseBlock = blocksWithStairsSlabsAndWalls.get(i);
            int bright = 0;
            if (baseBlock.defaultBlockState().is(RED_MUSHROOM_BLOCK)) {
                bright = 13;
            }
            wallBlocks.add(createWallBlocks(baseBlock, bright));
            stairBlocks.add(createStairBlocks(baseBlock, bright));
            slabBlocks.add(createSlabBlocks(baseBlock, bright));
        }
        for (int i = 0; i < blocksWithWalls.size(); i++) {
            Block baseBlock = blocksWithWalls.get(i);
            wallBlocks.add(createWallBlocks(baseBlock, 0));
        }
        for (int i = 0; i < blocksWithFragileWalls.size(); i++) {
            Block baseBlock = blocksWithFragileWalls.get(i);
            wallBlocks.add(createFragileWallBlocks(baseBlock, 0));
        }
        for (int i = 0; i < blocksWithPiles.size(); i++) {
            Block baseBlock = blocksWithPiles.get(i);
            pileBlocks.add(createPileBlocks(baseBlock, 0));
        }
    }

    public static Map<Block, DeferredHolder<Block, Block>> createPileBlocks(Block baseBlock, int brightness) {
        String name = baseBlock.toString();
        String blockName = name.substring(16, name.length() - 1) + "_pile";
        return Map.of(
                baseBlock, BLOCKS.register(blockName, () ->
                        new CollisionlessLayerBlock(blockProps(blockName, BlockBehaviour.Properties.ofFullCopy(baseBlock).forceSolidOff().noCollision().noOcclusion().replaceable().instabreak().lightLevel((something) -> brightness))))
        );
    }

    public static Map<Block, DeferredHolder<Block, Block>> createFragileWallBlocks(Block baseBlock, int brightness) {
        String name = baseBlock.toString();
        String blockName = name.substring(16, name.length() - 1) + "_wall";
        return Map.of(
                baseBlock, BLOCKS.register(blockName, () ->
                        new FragileWallBlock(blockProps(blockName, BlockBehaviour.Properties.ofFullCopy(baseBlock).forceSolidOff().noCollision().noOcclusion().replaceable().instabreak().lightLevel((something) -> brightness))))
        );
    }

    public static Map<Block, DeferredHolder<Block, Block>> createWallBlocks(Block baseBlock, int brightness) {
        String name = baseBlock.toString();
        String blockName = name.substring(16, name.length() - 1) + "_wall";
        return Map.of(
                baseBlock, BLOCKS.register(blockName, () ->
                        new WallBlock(blockProps(blockName, BlockBehaviour.Properties.ofFullCopy(baseBlock).lightLevel((something) -> brightness))))
        );
    }

    public static Map<Block, DeferredHolder<Block, Block>> createStairBlocks(Block baseBlock, int brightness) {
        String name = baseBlock.toString();
        String blockName = name.substring(16, name.length() - 1) + "_stairs";
        return Map.of(
                baseBlock, BLOCKS.register(blockName, () ->
                        new StairBlock(baseBlock.defaultBlockState(), blockProps(blockName, BlockBehaviour.Properties.ofFullCopy(baseBlock).lightLevel((something) -> brightness))))
        );
    }

    public static Map<Block, DeferredHolder<Block, Block>> createSlabBlocks(Block baseBlock, int brightness) {
        String name = baseBlock.toString();
        String blockName = name.substring(16, name.length() - 1) + "_slab";
        return Map.of(
                baseBlock, BLOCKS.register(blockName, () ->
                        new SlabBlock(blockProps(blockName, BlockBehaviour.Properties.ofFullCopy(baseBlock).lightLevel((something) -> brightness))))
        );
    }
}
