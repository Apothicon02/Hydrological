package com.Apothic0n.Hydrological.api.biome.features.types;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public class FireCoralFeature extends Feature<NoneFeatureConfiguration> {
    public FireCoralFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        NoneFeatureConfiguration config = context.config();
        RandomSource random = context.random();
        Map<BlockPos, BlockState> blocks = new java.util.HashMap<>(Map.of());
        int direction = 0;
        int maxHeight = random.nextInt(2, 4);

        for (int height = 0; height <= maxHeight; height++) {
            BlockPos pos = context.origin().above(height);
            if (level.getBlockState(pos).is(Blocks.WATER)) {
                blocks.put(pos, Blocks.FIRE_CORAL_BLOCK.defaultBlockState());
                if (height > 0 && direction <= 3 && random.nextInt(0, 100) > 18) {
                    BlockPos branchPos = pos;
                    for (int branch = 1; branch <= random.nextInt(1, 3); branch++) {
                        if (direction == 0) {
                            branchPos = branchPos.north();
                        } else if (direction == 1) {
                            branchPos = branchPos.east();
                        } else if (direction == 2) {
                            branchPos = branchPos.south();
                        } else if (direction == 3) {
                            branchPos = branchPos.west();
                        }
                        blocks.put(branchPos, Blocks.FIRE_CORAL_BLOCK.defaultBlockState());
                    }
                    for (int branchHeight = 1; branchHeight <= random.nextInt(1, 5); branchHeight++) {
                        BlockPos upBranchPos = branchPos.above(branchHeight);
                        if (level.getBlockState(upBranchPos).is(Blocks.WATER)) {
                            blocks.put(upBranchPos, Blocks.FIRE_CORAL_BLOCK.defaultBlockState());
                        } else {
                            return false;
                        }
                    }
                    direction++;
                }
            } else {
                return false;
            }
        }

        blocks.forEach((BlockPos pos, BlockState state) -> {
            level.setBlock(pos, state, UPDATE_ALL);
            if (context.origin().above(maxHeight).getY() < pos.getY() && level.getBlockState(pos.above()).is(Blocks.WATER)) {
                level.setBlock(pos.above(), Blocks.FIRE_CORAL.defaultBlockState(), UPDATE_ALL);
            }
        });
        int randomDirection = random.nextInt(0, 3);
        BlockPos fanPos = context.origin();
        if (randomDirection == 0) {
            fanPos = fanPos.north();
        } else if (randomDirection == 1) {
            fanPos = fanPos.east();
        } else if (randomDirection == 2) {
            fanPos = fanPos.south();
        } else if (randomDirection == 3) {
            fanPos = fanPos.west();
        }
        if (level.getBlockState(fanPos).is(Blocks.WATER) && level.getBlockState(fanPos.below()).isSolid()) {
            BlockState fan = Blocks.FIRE_CORAL_FAN.defaultBlockState();
            level.setBlock(fanPos, fan, UPDATE_ALL);
        }
        return true;
    }
}
