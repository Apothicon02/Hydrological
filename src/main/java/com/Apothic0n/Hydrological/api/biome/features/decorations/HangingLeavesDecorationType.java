package com.Apothic0n.Hydrological.api.biome.features.decorations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.Map;

public class HangingLeavesDecorationType extends Decoration {
    public static final Codec<HangingLeavesDecorationType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (IntProvider.CODEC.fieldOf("length")).forGetter(v -> v.length),
            (BlockStateProvider.CODEC.fieldOf("leaves")).forGetter(v -> v.leaves)
    ).apply(instance, HangingLeavesDecorationType::new));

    private final IntProvider length;
    private final BlockStateProvider leaves;

    public HangingLeavesDecorationType(IntProvider count, BlockStateProvider leaves) {
        this.length = count;
        this.leaves = leaves;
    }

    @Override
    protected DecorationType<?> type() {
        return DecorationType.HANGING_LEAVES_DECORATION_TYPE.get();
    }

    private boolean addToMap(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random, BlockStateProvider leaves) {
        map.put(pos, leaves.getState(random, pos));
        return true;
    }

    @Override
    public Map<BlockPos, BlockState> generateDecoration(RandomSource random, Map<BlockPos, BlockState> existing, BlockPos origin) {
        Map<BlockPos, BlockState> map = new java.util.HashMap<>(Map.of());
        if (existing.size() > 2) {
            for (BlockPos pos : existing.keySet()) {
                if (existing.get(pos).is(BlockTags.LEAVES) && existing.get(pos.below()) == null) {
                    int length = this.length.sample(random);
                    if (length > 0) {
                        for (int y = pos.getY() - 1; y >= pos.getY() - length; y--) {
                            addToMap(map, pos.atY(y), random, leaves);
                        }
                    }
                }
            }
        }

        return map;
    }
}
