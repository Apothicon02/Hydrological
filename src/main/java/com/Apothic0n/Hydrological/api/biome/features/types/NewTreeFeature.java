package com.Apothic0n.Hydrological.api.biome.features.types;

import com.Apothic0n.Hydrological.api.biome.features.canopies.Canopy;
import com.Apothic0n.Hydrological.api.biome.features.configurations.NewTreeConfiguration;
import com.Apothic0n.Hydrological.api.biome.features.decorations.Decoration;
import com.Apothic0n.Hydrological.api.biome.features.trunks.GeneratedTrunk;
import com.Apothic0n.Hydrological.api.biome.features.trunks.Trunk;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.*;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

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
        int minX = chunk.getMinBlockX()-16;
        int maxX = chunk.getMaxBlockX()+16;
        int minZ = chunk.getMinBlockZ()-16;
        int maxZ = chunk.getMaxBlockZ()+16;
        Map<BlockPos, BlockState> map = construct(random, origin, config);
        boolean enoughSpace = true;
        for (BlockPos pos : map.keySet()) {
            int x = pos.getX();
            int z = pos.getZ();
            if (!config.getIntersect()) {
                if (x >= minX && x <= maxX && z >= minZ && z <= maxZ && !level.getBlockState(pos).canBeReplaced()) {
                    enoughSpace = false;
                    break;
                }
            }
        }
        if (enoughSpace) {
            for (BlockPos pos : map.keySet()) {
                int x = pos.getX();
                int z = pos.getZ();
                if (x >= minX && x <= maxX && z >= minZ && z <= maxZ && level.getBlockState(pos).canBeReplaced()) {
                    level.setBlock(pos, map.get(pos), UPDATE_ALL);
                }
            }
        }
        return enoughSpace;
    }

    public static Map<BlockPos, BlockState> construct(RandomSource random, BlockPos origin, NewTreeConfiguration config) {
        Trunk trunk = config.getTrunk();
        Canopy canopy = config.getCanopy();
        List<Decoration> decorations = config.getDecorations();
        GeneratedTrunk generatedTrunk = trunk.generateTrunk(random, origin);
        Map<BlockPos, BlockState> map = new HashMap<>(generatedTrunk.getMap());
        for (BlockPos branch:generatedTrunk.getCanopies()) {
            Map<BlockPos, BlockState> leaves = canopy.generateCanopy(random, branch);
            for (BlockPos leaf:leaves.keySet()) {
                if (!map.containsKey(leaf)) {
                    map.put(leaf, leaves.get(leaf));
                }
            }
        }
        Map<BlockPos, BlockState> decoratedMap = map;
        for (Decoration decoration : decorations) {
            Map<BlockPos, BlockState> blocks = decoration.generateDecoration(random, map);
            for (BlockPos block : blocks.keySet()) {
                if (!decoratedMap.containsKey(block)) {
                    decoratedMap.put(block, blocks.get(block));
                }
            }
        }
        return decoratedMap;
    }
}
