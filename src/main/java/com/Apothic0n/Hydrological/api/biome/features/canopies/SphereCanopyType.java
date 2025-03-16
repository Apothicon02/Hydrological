package com.Apothic0n.Hydrological.api.biome.features.canopies;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.HashMap;
import java.util.Map;

public class SphereCanopyType extends Canopy {
    public static final MapCodec<SphereCanopyType> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            (IntProvider.codec(1, 64).fieldOf("height")).forGetter(v -> v.height),
            (IntProvider.codec(1, 22).fieldOf("radius")).forGetter(v -> v.radius),
            (BlockStateProvider.CODEC.fieldOf("leaves")).forGetter(v -> v.leaves)
    ).apply(instance, SphereCanopyType::new));

    private final IntProvider height;
    private final IntProvider radius;
    private final BlockStateProvider leaves;

    public SphereCanopyType(IntProvider height, IntProvider radius, BlockStateProvider leaves) {
        this.height = height;
        this.radius = radius;
        this.leaves = leaves;
    }

    @Override
    protected CanopyType<?> type() {
        return CanopyType.SPHERE_CANOPY_TYPE.get();
    }

    private BlockState getLeaves(RandomSource random, BlockPos pos) {
        return this.leaves.getState(random, pos);
    }

    private void addToMap(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random) {
        map.put(pos, getLeaves(random, pos));
    }

    @Override
    public Map<BlockPos, BlockState> generateCanopy(RandomSource random, BlockPos origin, int trunkHeight, BlockPos treeOrigin) {
        Map<BlockPos, BlockState> map = new HashMap<>();
        int maxHeight = this.height.sample(random);
        int maxRadius = this.radius.sample(random);
        BlockPos pos = origin.below(maxHeight-1);
        for (int x = pos.getX()-maxRadius; x <= pos.getX()+maxRadius; x++) {
            for (int z = pos.getZ()-maxRadius; z <= pos.getZ()+maxRadius; z++) {
                for (int y = pos.getY()-maxHeight; y <= pos.getY()+(maxHeight*2); y++) {
                    int xDist = x-origin.getX();
                    int yDist = y-origin.getY();
                    int zDist = z-origin.getZ();
                    int dist = xDist*xDist+zDist*zDist+yDist*yDist;
                    if (dist <= maxRadius*3) {
                        addToMap(map, new BlockPos(x, y, z), random);
                    }
                }
            }
        }

        return map;
    }
}
