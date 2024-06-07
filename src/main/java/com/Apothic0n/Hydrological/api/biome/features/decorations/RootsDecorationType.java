package com.Apothic0n.Hydrological.api.biome.features.decorations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
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

    public RootsDecorationType(IntProvider count, BlockStateProvider roots) {
        this.length = count;
        this.roots = roots;
    }

    @Override
    protected DecorationType<?> type() {
        return DecorationType.ROOTS_DECORATION_TYPE.get();
    }

    private boolean addToMap(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random, BlockStateProvider leaves) {
        map.put(pos, leaves.getState(random, pos));
        return true;
    }

    private List<BlockPos> getNeighbors(BlockPos pos) {
        return List.of(pos.north(), pos.east(), pos.south(), pos.west());
    }

    @Override
    public Map<BlockPos, BlockState> generateDecoration(RandomSource random, Map<BlockPos, BlockState> existing, BlockPos origin) {
        Map<BlockPos, BlockState> map = new java.util.HashMap<>(Map.of());
        if (existing.size() > length.getMaxValue()) {
            for (BlockPos pos : getNeighbors(origin)) {
                int length = this.length.sample(random);
                if (length > 0) {
                    for (int y = pos.getY()-1; y <= pos.getY() + length; y++) {
                        addToMap(map, pos.atY(y), random, roots);
                    }
                    if (length >= 3) {
                        BlockPos offPos = getNeighbors(pos).get(random.nextInt(0, 3));
                        addToMap(map, offPos, random, roots);
                        addToMap(map, offPos.below(), random, roots);
                        addToMap(map, offPos.below(2), random, roots);
                    }
                }
            }
        }

        return map;
    }
}
