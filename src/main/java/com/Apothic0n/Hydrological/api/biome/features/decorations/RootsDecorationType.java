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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.List;
import java.util.Map;

public class RootsDecorationType extends Decoration {
    public static final Codec<RootsDecorationType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (IntProvider.CODEC.fieldOf("length")).forGetter(v -> v.length),
            (BlockStateProvider.CODEC.fieldOf("roots")).forGetter(v -> v.roots)
    ).apply(instance, RootsDecorationType::new));

    private final IntProvider length;
    private final BlockStateProvider roots;

    public RootsDecorationType(IntProvider length, BlockStateProvider roots) {
        this.length = length;
        this.roots = roots;
    }

    @Override
    protected DecorationType<?> type() {
        return DecorationType.ROOTS_DECORATION_TYPE.get();
    }

    private boolean addToMap(Map<BlockPos, BlockState> existing, Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random, BlockStateProvider roots, WorldGenLevel level) {
        if (!existing.containsKey(pos) && level.getBlockState(pos).canBeReplaced()) {
            map.put(pos, roots.getState(random, pos));
        }
        return true;
    }

    private List<BlockPos> getNeighbors(BlockPos pos) {
        return List.of(pos.north(), pos.east(), pos.south(), pos.west());
    }

    @Override
    public Map<BlockPos, BlockState> generateDecoration(RandomSource random, Map<BlockPos, BlockState> existing, BlockPos origin, WorldGenLevel level) {
        Map<BlockPos, BlockState> map = new java.util.HashMap<>(Map.of());
        if (existing.size() > length.getMaxValue()) {
            for (BlockPos pos : getNeighbors(origin)) {
                int length = this.length.sample(random);
                if (length > 0) {
                    for (int y = pos.getY()-1; y <= pos.getY() + length; y++) {
                        addToMap(existing, map, pos.atY(y), random, roots, level);
                    }
                    if (length >= 3) {
                        BlockPos offPos = getNeighbors(pos).get(random.nextInt(0, 3));
                        addToMap(existing, map, offPos, random, roots, level);
                        addToMap(existing, map, offPos.below(), random, roots, level);
                        addToMap(existing, map, offPos.below(2), random, roots, level);
                    }
                }
            }
        }

        return map;
    }
}
