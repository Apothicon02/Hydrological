package com.Apothic0n.Hydrological.api.biome.features.canopies;

import com.Apothic0n.Hydrological.api.biome.features.placement_modifiers.NoiseCoverPlacement;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.Map;

public class DroopingCanopyType extends Canopy {
    public static final MapCodec<DroopingCanopyType> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            (IntProvider.codec(1, 64).fieldOf("height")).forGetter(v -> v.height),
            (IntProvider.codec(1, 22).fieldOf("radius")).forGetter(v -> v.radius),
            (BlockStateProvider.CODEC.fieldOf("leaves")).forGetter(v -> v.leaves)
    ).apply(instance, DroopingCanopyType::new));

    private final IntProvider height;
    private final IntProvider radius;
    private final BlockStateProvider leaves;

    public DroopingCanopyType(IntProvider height, IntProvider radius, BlockStateProvider leaves) {
        this.height = height;
        this.radius = radius;
        this.leaves = leaves;
    }

    @Override
    protected CanopyType<?> type() {
        return CanopyType.DROOPING_CANOPY_TYPE.get();
    }

    private BlockState getLeaves(RandomSource random, BlockPos pos) {
        return this.leaves.getState(random, pos);
    }

    private void addToMap(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random, int minY) {
        if (pos.getY() < minY) {
            map.put(pos.north().east(), getLeaves(random, pos.north().east()));
        } else {
            map.put(pos, getLeaves(random, pos));
        }
    }

    @Override
    public Map<BlockPos, BlockState> generateCanopy(RandomSource random, BlockPos origin, int trunkHeight, BlockPos treeOrigin) {
        Map<BlockPos, BlockState> map = new java.util.HashMap<>(Map.of());
        int height = this.height.sample(random);
        int maxRadius = this.radius.sample(random);
        for (int x = origin.getX()-maxRadius; x <= origin.getX()+maxRadius; x++) {
            for (int z = origin.getZ()-maxRadius; z <= origin.getZ()+maxRadius; z++) {
                int minY = origin.getY();
                int maxY = (origin.getY()+height);
                for (int y = minY; y <= maxY; y++) {
                    int radius = maxRadius;
                    if ((y == minY && height > 2) || (y == maxY-1 && height >= 4)) {
                        radius--;
                    } else if (y == maxY && height >= 5) {
                        radius-=2;
                    }
                    radius*=radius;
                    int xDist = x-origin.getX();
                    int zDist = z-origin.getZ();
                    int dist = xDist*xDist+zDist*zDist;
                    if (dist <= radius) {
                        int droop = 0;
                        if (dist >= radius-2 && y < minY+(height/2)) {
                            droop = (int) (NoiseCoverPlacement.HEIGHT_NOISE.getValue(x, z, false)*3);
                            if (dist >= radius-2) {
                                droop++;
                            }
                        }
                        int xOgDist = x-treeOrigin.getX();
                        int zOgDist = z-treeOrigin.getZ();
                        int originDist = xOgDist*xOgDist+zOgDist*zOgDist;
                        BlockPos pos = new BlockPos(x, y-(originDist/35)-(dist/22), z);
                        for (int i = 0; i <= droop; i++) {
                            if (droop == 2) {
                                addSquare(map, pos.below(i), random, 1, true, minY);
                            } else {
                                addToMap(map, pos.below(i), random, minY);
                            }
                        }
                    }
                }
            }
        }

        return map;
    }

    private void addSquare(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random, int radius, boolean corners, int minY) {
        int minX = pos.getX()-radius;
        int maxX = pos.getX()+radius;
        int minZ = pos.getZ()-radius;
        int maxZ = pos.getZ()+radius;
        for (int x = pos.getX()-radius; x <= maxX; x++) {
            for (int z = pos.getZ()-radius; z <= maxZ; z++) {
                if (!((x == minX || x == maxX) && (z == minZ || z == maxZ)) || corners) {
                    addToMap(map, new BlockPos(x, pos.getY(), z), random, minY);
                }
            }
        }
    }
}
