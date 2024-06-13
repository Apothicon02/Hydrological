package com.Apothic0n.Hydrological.api.biome.features.types;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public class RockFeature extends Feature<SimpleBlockConfiguration> {
    public RockFeature(Codec<SimpleBlockConfiguration> pContext) {
        super(pContext);
    }

    public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> pContext) {
        WorldGenLevel level = pContext.level();
        BlockPos origin = pContext.origin();
        RandomSource random = pContext.random();
        SimpleBlockConfiguration config = pContext.config();
        generateSquare(level, origin, random, config.toPlace());
        int x = random.nextInt(-1, 1);
        int z = random.nextInt(-1, 1);
        if (x == 0) {
            x = -1;
        } else if (z == 0) {
            z = -1;
        }
        origin = origin.offset(x, 0, z);
        generateSquare(level, origin, random, config.toPlace());
        generateSquare(level, origin.above(), random, config.toPlace());
        return true;
    }

    private void generateSquare(WorldGenLevel level, BlockPos origin, RandomSource random, BlockStateProvider toPlace) {
        level.setBlock(origin, toPlace.getState(random, origin), UPDATE_ALL);
        level.setBlock(origin.north(), toPlace.getState(random, origin.north()), UPDATE_ALL);
        level.setBlock(origin.east(), toPlace.getState(random, origin.east()), UPDATE_ALL);
        level.setBlock(origin.north().east(), toPlace.getState(random, origin.north().east()), UPDATE_ALL);
    }
}
