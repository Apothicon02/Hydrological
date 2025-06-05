package com.Apothic0n.Hydrological.api.biome.features.types;

import com.Apothic0n.Hydrological.api.biome.features.FeatureHelper;

import com.Apothic0n.Hydrological.api.biome.features.FeatureHelper;
import com.Apothic0n.Hydrological.api.biome.features.canopies.Canopy;
import com.Apothic0n.Hydrological.api.biome.features.configurations.NewTreeConfiguration;
import com.Apothic0n.Hydrological.api.biome.features.decorations.Decoration;
import com.Apothic0n.Hydrological.api.biome.features.decorations.GroundDecorationType;
import com.Apothic0n.Hydrological.api.biome.features.trunks.GeneratedTrunk;
import com.Apothic0n.Hydrological.api.biome.features.trunks.Trunk;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.*;

import static net.minecraft.world.level.block.Block.*;

public class NewTreeFeature extends Feature<NewTreeConfiguration> {
    public NewTreeFeature(Codec<NewTreeConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NewTreeConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        NewTreeConfiguration config = context.config();
        BlockPos origin = context.origin();
        ChunkPos chunk = level.getChunk(origin).getPos();
        List<Decoration> decorations = config.getDecorations();
        int minX = chunk.getMinBlockX()-16;
        int maxX = chunk.getMaxBlockX()+16;
        int minZ = chunk.getMinBlockZ()-16;
        int maxZ = chunk.getMaxBlockZ()+16;
        Map<BlockPos, BlockState> map = construct(random, origin, config);
        boolean enoughSpace = true;
        for (BlockPos pos : map.keySet()) {
            int x = pos.getX();
            int z = pos.getZ();
            if (!config.getIntersect() && pos.getY() > origin.getY()) {
                if (x >= minX && x <= maxX && z >= minZ && z <= maxZ && !FeatureHelper.getBlockState(level, pos).canBeReplaced()) {
                    enoughSpace = false;
                    break;
                }
            }
            if (config.getMustBeFullySubmerged()) {
                if (!FeatureHelper.getBlockState(level, pos).liquid() || !FeatureHelper.getBlockState(level, pos.above()).liquid()) {
                    enoughSpace = false;
                    break;
                }
            }
        }
        if (enoughSpace) {
            for (Decoration decoration : decorations) {
                map.putAll(decoration.generateDecoration(random, map, origin, level));
            }
            map.forEach((BlockPos pos, BlockState state) -> {
                int x = pos.getX();
                int z = pos.getZ();
                if (x >= minX && x <= maxX && z >= minZ && z <= maxZ && (FeatureHelper.getBlockState(level, pos).canBeReplaced() || (pos.getY() <= origin.getY() && state.isSolid() && !state.is(BlockTags.LEAVES)))) {
                    if (state.getBlock() instanceof WallBlock) {
                        state = state.updateShape(Direction.UP, state, level, pos, pos);
                    }
                    if (state.hasProperty(BlockStateProperties.WATERLOGGED) && FeatureHelper.getBlockState(level, pos).is(Blocks.WATER)) {
                        state = state.setValue(BlockStateProperties.WATERLOGGED, true);
                    }
                    BlockState aboveState = FeatureHelper.getBlockState(level, pos.above());
                    boolean cancel = false;
                    if (state.is(Blocks.MYCELIUM) || state.is(Blocks.PODZOL)) {
                        if (aboveState.isSolid()) {
                            if (!aboveState.is(Blocks.ROOTED_DIRT) && !aboveState.is(Blocks.MYCELIUM) && !aboveState.is(Blocks.PODZOL)) {
                                cancel = true;
                            } else {
                                state = Blocks.ROOTED_DIRT.defaultBlockState();
                            }
                        }
                    }
                    if (!cancel) {
                        FeatureHelper.setBlock(level, pos, state, UPDATE_ALL);
                        if ((!state.isFaceSturdy(level, pos, Direction.UP) || state.is(Blocks.PODZOL)) && aboveState.getBlock() instanceof BushBlock) {
                            BlockState filler = Blocks.AIR.defaultBlockState();
                            if (aboveState.hasProperty(BlockStateProperties.WATERLOGGED) && aboveState.getValue(BlockStateProperties.WATERLOGGED)) {
                                filler = Blocks.WATER.defaultBlockState();
                            }
                            FeatureHelper.setBlock(level, pos.above(), filler, UPDATE_ALL);
                            if (aboveState.getBlock() instanceof DoublePlantBlock) {
                                aboveState = FeatureHelper.getBlockState(level, pos.above(2));
                                filler = Blocks.AIR.defaultBlockState();
                                if (aboveState.hasProperty(BlockStateProperties.WATERLOGGED) && aboveState.getValue(BlockStateProperties.WATERLOGGED)) {
                                    filler = Blocks.WATER.defaultBlockState();
                                }
                                FeatureHelper.setBlock(level, pos.above(2), filler, UPDATE_ALL);
                            }
                        }
                    }
                }
            });
        }
        return enoughSpace;
    }

    public static Map<BlockPos, BlockState> construct(RandomSource random, BlockPos origin, NewTreeConfiguration config) {
        Trunk trunk = config.getTrunk();
        Canopy canopy = config.getCanopy();
        GeneratedTrunk generatedTrunk = trunk.generateTrunk(random, origin);
        Map<BlockPos, BlockState> map = new HashMap<>(generatedTrunk.getMap());
        for (BlockPos branch:generatedTrunk.getCanopies()) {
            Map<BlockPos, BlockState> leaves = canopy.generateCanopy(random, branch.above(config.getCanopyOffset().sample(random)), generatedTrunk.getHeight(), origin);
            leaves.forEach(map::putIfAbsent);
        }
        return map;
    }
}
