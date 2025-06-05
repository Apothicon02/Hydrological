package com.Apothic0n.Hydrological.api.biome.features.types;

import com.Apothic0n.Hydrological.api.biome.features.FeatureHelper;
import com.Apothic0n.Hydrological.api.biome.features.configurations.RockConfiguration;
import com.Apothic0n.Hydrological.api.biome.features.placement_modifiers.NoiseCoverPlacement;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public class RockFeature extends Feature<RockConfiguration> {
    public RockFeature(Codec<RockConfiguration> pContext) {
        super(pContext);
    }

    public boolean place(FeaturePlaceContext<RockConfiguration> pContext) {
        WorldGenLevel level = pContext.level();
        BlockPos origin = pContext.origin().below();
        RandomSource random = pContext.random();
        RockConfiguration config = pContext.config();
        BlockStateProvider toPlace = config.getToPlace();
        BlockStateProvider filler = config.getFiller();

        FeatureHelper.setBlock(level, origin, toPlace.getState(random, origin), UPDATE_ALL);
        int maxRadius = config.getRadius().sample(random);;
        for (int x = origin.getX() - maxRadius; x <= origin.getX() + maxRadius; x++) {
            for (int z = origin.getZ() - maxRadius; z <= origin.getZ() + maxRadius; z++) {
                int minY = origin.getY() - maxRadius;
                int maxY = origin.getY() + maxRadius;
                for (int y = minY; y <= maxY; y++) {
                    int radius = maxRadius*maxRadius;
                    int xDist = x-origin.getX();
                    int yDist = y-origin.getY();
                    int zDist = z-origin.getZ();
                    int dist = xDist*xDist+yDist*yDist+zDist*zDist;
                    if (dist <= radius && y < maxY) {
                        int droop = Mth.abs((int) (NoiseCoverPlacement.HEIGHT_NOISE.getValue(x, z, false) * 5));
                        BlockPos pos = new BlockPos(x, y-droop, z);
                        BlockState state = toPlace.getState(random, pos);
                        int chance = random.nextInt(0, 4);
                        if (y > origin.getY()+chance) {
                            state = filler.getState(random, pos);
                        }
                        FeatureHelper.setBlock(level, pos, state, UPDATE_ALL);
                    }
                }
            }
        }

        return true;
    }
}
