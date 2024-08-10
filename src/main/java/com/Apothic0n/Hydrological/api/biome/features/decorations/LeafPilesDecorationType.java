package com.Apothic0n.Hydrological.api.biome.features.decorations;

import com.Apothic0n.Hydrological.api.biome.features.placement_modifiers.NoiseCoverPlacement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.Map;

import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public class LeafPilesDecorationType extends Decoration {
    public static final Codec<LeafPilesDecorationType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (IntProvider.CODEC.fieldOf("length")).forGetter(v -> v.length),
            (BlockStateProvider.CODEC.fieldOf("leaves")).forGetter(v -> v.leaves)
    ).apply(instance, LeafPilesDecorationType::new));

    private final IntProvider length;
    private final BlockStateProvider leaves;

    public LeafPilesDecorationType(IntProvider count, BlockStateProvider leaves) {
        this.length = count;
        this.leaves = leaves;
    }

    @Override
    protected DecorationType<?> type() {
        return DecorationType.LEAF_PILES_DECORATION_TYPE.get();
    }

    private boolean addToMap(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random, BlockStateProvider leaves) {
        map.put(pos, leaves.getState(random, pos).setValue(SnowLayerBlock.LAYERS, Mth.abs((int) (NoiseCoverPlacement.HEIGHT_NOISE.getValue(pos.getX(), pos.getZ(), false) * 4)) + random.nextInt(1, 4)));
        return true;
    }

    @Override
    public Map<BlockPos, BlockState> generateDecoration(RandomSource random, Map<BlockPos, BlockState> existing, BlockPos origin, WorldGenLevel level) {
        Map<BlockPos, BlockState> map = new java.util.HashMap<>(Map.of());
        if (existing.size() > 2) {
            for (BlockPos pos : existing.keySet()) {
                if (existing.get(pos).is(BlockTags.LEAVES) && existing.get(pos.below()) == null) {
                    int length = this.length.sample(random);
                    if (length > 0) {
                        for (int y = pos.getY() - 1; y >= pos.getY() - length; y--) {
                            BlockPos newPos = pos.atY(y);
                            if (existing.get(newPos) == null && (level.getBlockState(newPos).isAir() || level.getBlockState(newPos).canBeReplaced()) && level.getBlockState(newPos.below()).isSolid() && level.getBlockState(newPos.above()).isAir()) {
                                addToMap(map, newPos, random, leaves);
                            }
                        }
                    }
                }
            }
        }

        return map;
    }
}
