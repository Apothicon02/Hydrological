package com.Apothic0n.Hydrological.api.biome.features.decorations;

import com.Apothic0n.Hydrological.api.biome.features.FeatureHelper;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class CocoaDecorationType extends Decoration {
    public static final MapCodec<CocoaDecorationType> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            (IntProvider.CODEC.fieldOf("count")).forGetter(v -> v.count)
    ).apply(instance, CocoaDecorationType::new));

    private final IntProvider count;

    public CocoaDecorationType(IntProvider count) {
        this.count = count;
    }

    @Override
    protected DecorationType<?> type() {
        return DecorationType.COCOA_DECORATION_TYPE.get();
    }

    private boolean addToMap(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random) {
        BlockState cocoa = Blocks.COCOA.defaultBlockState().setValue(CocoaBlock.AGE, 2);
        int randomNumber = random.nextInt(0, 10);
        if (randomNumber >= 1 && randomNumber < 4) {
            cocoa.setValue(CocoaBlock.AGE, 1);
        } else {
            cocoa.setValue(CocoaBlock.AGE, 0);
        }
        int randomNumber2 = random.nextInt(1, 4);
        if (randomNumber2 == 1 && !map.containsKey(pos.north())) {
            map.put(pos.north(), cocoa.setValue(CocoaBlock.FACING, Direction.SOUTH));
            return true;
        } else if (randomNumber2 == 2 && !map.containsKey(pos.east())) {
            map.put(pos.east(), cocoa.setValue(CocoaBlock.FACING, Direction.WEST));
            return true;
        } else if (randomNumber2 == 3 && !map.containsKey(pos.south())) {
            map.put(pos.south(), cocoa.setValue(CocoaBlock.FACING, Direction.NORTH));
            return true;
        } else if (randomNumber2 == 4 && !map.containsKey(pos.west())) {
            map.put(pos.west(), cocoa.setValue(CocoaBlock.FACING, Direction.EAST));
            return true;
        }
        return false;
    }

    @Override
    public Map<BlockPos, BlockState> generateDecoration(RandomSource random, Map<BlockPos, BlockState> existing, BlockPos origin, WorldGenLevel level) {
        Map<BlockPos, BlockState> map = new java.util.HashMap<>(Map.of());
        int max = this.count.sample(random);
        if (max > 0 && existing.size() > 2) {
            BlockPos highestLog = null;
            for (BlockPos pos : existing.keySet()) {
                if (existing.get(pos).is(BlockTags.LOGS)) {
                    if (highestLog == null) {
                        highestLog = pos;
                    } else if (pos.getY() > highestLog.getY()) {
                        highestLog = pos;
                    }
                }
            }
            int placed = 0;
            for (int i = 0; i < 32 && placed < max; i++) {
                if (addToMap(map, highestLog.below(3), random)) {
                    placed += 1;
                }
            }
        }

        return map;
    }
}
