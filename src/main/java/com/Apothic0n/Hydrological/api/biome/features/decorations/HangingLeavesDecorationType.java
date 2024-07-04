package com.Apothic0n.Hydrological.api.biome.features.decorations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import org.spongepowered.asm.mixin.Mutable;

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

    private void addToMap(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random, BlockStateProvider leaves) {
        map.put(pos, leaves.getState(random, pos));
    }

    @Override
    public Map<BlockPos, BlockState> generateDecoration(RandomSource random, Map<BlockPos, BlockState> existing, BlockPos origin, WorldGenLevel level) {
        Map<BlockPos, BlockState> map = new java.util.HashMap<>(Map.of());
        if (existing.size() > 2) {
            existing.forEach((BlockPos pos, BlockState state) -> {
                int length = this.length.sample(random);
                if (length > 0) {
                    if (state.is(BlockTags.LEAVES) && existing.get(pos.below()) == null && (level.getBlockState(pos.below()).isAir() || level.getBlockState(pos.below()).is(Blocks.WATER))) {
                        for (int y = 1; y <= length; y++) {
                            BlockPos newPos = pos.below(y);
                            if (existing.get(newPos) == null && (level.getBlockState(newPos).isAir() || level.getBlockState(newPos).is(Blocks.WATER))) {
                                addToMap(map, newPos, random, leaves);
                            } else {
                                y = 69420;
                            }
                        }
                    }
                }
            });
        }

        return map;
    }
}
