package com.Apothic0n.Hydrological.api.biome.features.decorations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.Map;

public class GroundDecorationType extends Decoration {
    public static final Codec<GroundDecorationType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (IntProvider.CODEC.fieldOf("offset")).forGetter(v -> v.offset),
            (IntProvider.CODEC.fieldOf("radius")).forGetter(v -> v.radius),
            (BlockStateProvider.CODEC.fieldOf("replace_with")).forGetter(v -> v.replaceWith)
    ).apply(instance, GroundDecorationType::new));

    private final IntProvider offset;
    private final IntProvider radius;
    private final BlockStateProvider replaceWith;

    public GroundDecorationType(IntProvider offset, IntProvider radius, BlockStateProvider replace_with) {
        this.offset = offset;
        this.radius = radius;
        this.replaceWith = replace_with;
    }

    @Override
    protected DecorationType<?> type() {
        return DecorationType.GROUND_DECORATION_TYPE.get();
    }

    private void addToMap(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random) {
        map.put(pos, replaceWith.getState(random, pos));
    }

    @Override
    public Map<BlockPos, BlockState> generateDecoration(RandomSource random, Map<BlockPos, BlockState> existing, BlockPos origin, WorldGenLevel level) {
        Map<BlockPos, BlockState> map = new java.util.HashMap<>(Map.of());
        BlockPos offsetPos = origin.offset(offset.sample(random), -1, offset.sample(random));
        int maxRadius = radius.sample(random);
        for (int x = offsetPos.getX()-maxRadius; x <= offsetPos.getX()+maxRadius; x++) {
            for (int z = offsetPos.getZ()-maxRadius; z <= offsetPos.getZ()+maxRadius; z++) {
                for (int y = offsetPos.getY()+1; y >= offsetPos.getY()-1; y--) {
                    int xDist = x-offsetPos.getX();
                    int zDist = z-offsetPos.getZ();
                    int dist = xDist*xDist + zDist*zDist;
                    BlockPos newPos = new BlockPos(x, y, z);
                    BlockState oldState = level.getBlockState(newPos);
                    if (dist <= (maxRadius*maxRadius)-maxRadius && oldState.is(BlockTags.DIRT) || oldState.is(BlockTags.TERRACOTTA) || oldState.is(BlockTags.BASE_STONE_OVERWORLD)) {
                        addToMap(map, newPos, random);
                    }
                }
            }
        }

        return map;
    }
}
