package com.Apothic0n.Hydrological.api.biome.features.trunks;

import com.Apothic0n.Hydrological.api.HydrolMath;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ThickTrunkType extends Trunk {
    public static final MapCodec<ThickTrunkType> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            (Codec.BOOL.fieldOf("single_canopy")).forGetter(v -> v.singleCanopy),
            (IntProvider.codec(1, 64).fieldOf("height")).forGetter(v -> v.height),
            (IntProvider.codec(1, 10).fieldOf("branch_chance")).forGetter(v -> v.branchChance),
            (BlockStateProvider.CODEC.fieldOf("wood")).forGetter(v -> v.wood)
    ).apply(instance, ThickTrunkType::new));

    private final boolean singleCanopy;
    private final IntProvider height;
    private final IntProvider branchChance;
    private final BlockStateProvider wood;

    public ThickTrunkType(boolean singleCanopy, IntProvider height, IntProvider branchChance, BlockStateProvider wood) {
        this.singleCanopy = singleCanopy;
        this.height = height;
        this.branchChance = branchChance;
        this.wood = wood;
    }

    @Override
    protected TrunkType<?> type() {
        return TrunkType.THICK_TRUNK_TYPE.get();
    }

    private BlockState getWood(RandomSource random, BlockPos pos) {
        return this.wood.getState(random, pos);
    }

    @Override
    public GeneratedTrunk generateTrunk(RandomSource random, BlockPos origin) {
        Map<BlockPos, BlockState> map = new java.util.HashMap<>(Map.of());
        Set<BlockPos> canopies = new HashSet<>();
        int maxHeight = this.height.sample(random);
        int branchChance = this.branchChance.sample(random);
        int baseRadius = 1;
        origin = origin.below();
        BlockPos.MutableBlockPos pos = origin.mutable();

        pos.move(0, -3, 0);
        int trunkOffset = (baseRadius*3)+3;
        int min = (int) (maxHeight*0.2) + trunkOffset;
        int max = (int) (maxHeight*0.27) + trunkOffset;
        makeColumn(map, pos.north(baseRadius+1), random, min, max);
        makeColumn(map, pos.east(baseRadius+1), random, min, max);
        makeColumn(map, pos.south(baseRadius+1), random, min, max);
        makeColumn(map, pos.west(baseRadius+1), random, min, max);
        min = (int) (maxHeight*0.08) + trunkOffset;
        max = (int) (maxHeight*0.13) + trunkOffset;
        makeColumn(map, pos.north(baseRadius+1).east(), random, min, max);
        makeColumn(map, pos.north(baseRadius+1).west(), random, min, max);
        makeColumn(map, pos.east(baseRadius+1).north(), random, min, max);
        makeColumn(map, pos.east(baseRadius+1).south(), random, min, max);
        makeColumn(map, pos.south(baseRadius+1).east(), random, min, max);
        makeColumn(map, pos.south(baseRadius+1).west(), random, min, max);
        makeColumn(map, pos.west(baseRadius+1).north(), random, min, max);
        makeColumn(map, pos.west(baseRadius+1).south(), random, min, max);
        min = (int) (maxHeight*0.03) + trunkOffset;
        max = (int) (maxHeight*0.05) + trunkOffset;
        makeColumn(map, pos.north(baseRadius+1).east(2), random, min, max);
        makeColumn(map, pos.north(baseRadius+1).west(2), random, min, max);
        makeColumn(map, pos.east(baseRadius+1).north(2), random, min, max);
        makeColumn(map, pos.east(baseRadius+1).south(2), random, min, max);
        makeColumn(map, pos.south(baseRadius+1).east(2), random, min, max);
        makeColumn(map, pos.south(baseRadius+1).west(2), random, min, max);
        makeColumn(map, pos.west(baseRadius+1).north(2), random, min, max);
        makeColumn(map, pos.west(baseRadius+1).south(2), random, min, max);
        min = (int) (maxHeight*0.06) + trunkOffset;
        max = (int) (maxHeight*0.08) + trunkOffset;
        makeColumn(map, pos.north(baseRadius+2), random, min, max);
        makeColumn(map, pos.east(baseRadius+2), random, min, max);
        makeColumn(map, pos.south(baseRadius+2), random, min, max);
        makeColumn(map, pos.west(baseRadius+2), random, min, max);
        pos.move(0, 3, 0);

        int offset = random.nextInt(-4, 4);
        for (int i = 0; i <= maxHeight; i++) {
            pos.move(0, 1, 0);
            int currentHeight = i+offset;
            if (currentHeight >= maxHeight/1.25) {
                if (baseRadius > 1) {
                    makeSquare(map, pos.immutable(), random, baseRadius-1, false);
                } else {
                    map.put(pos.immutable(), getWood(random, pos.immutable()));
                }
                if (!singleCanopy && i < maxHeight-1 && random.nextInt(0, 10) < branchChance) {
                    canopies.add(makeBranch(map, pos, random, random.nextInt(1, 2)+baseRadius));
                }
            } else if (currentHeight >= maxHeight/1.75) {
                makeSquare(map, pos.immutable(), random, baseRadius, false);
                if (!singleCanopy && random.nextInt(0, 10) < branchChance) {
                    canopies.add(makeBranch(map, pos, random, random.nextInt(1, 2)+baseRadius));
                }
            } else {
                makeSquare(map, pos.immutable(), random, baseRadius, true);
                double trunkHeight = HydrolMath.gradient(pos.getY(), origin.getY()+(maxHeight/4), origin.getY()+maxHeight, 3, 1);
                if (!singleCanopy && trunkHeight != 1 && random.nextInt(0, 10) < trunkHeight) {
                    canopies.add(makeBranch(map, pos, random, random.nextInt(1, 2)+baseRadius));
                }
            }
        }
        canopies.add(origin.above(maxHeight+1));

        return new GeneratedTrunk(map, canopies, maxHeight);
    }

    private void makeColumn(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random, int minHeight, int maxHeight) {
        minHeight--;
        if (minHeight >= maxHeight) {
            minHeight = maxHeight-1;
        }
        BlockPos.MutableBlockPos newPos = pos.mutable();
        for (int y = 0; y <= random.nextInt(minHeight, maxHeight); y++) {
            newPos.move(0, 1, 0);
            map.put(newPos.immutable(), getWood(random, newPos.immutable()));
        }
    }

    private void makeSquare(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random, int radius, boolean corners) {
        int minX = pos.getX()-radius;
        int maxX = pos.getX()+radius;
        int minZ = pos.getZ()-radius;
        int maxZ = pos.getZ()+radius;
        for (int x = pos.getX()-radius; x <= maxX; x++) {
            for (int z = pos.getZ()-radius; z <= maxZ; z++) {
                if (!((x == minX || x == maxX) && (z == minZ || z == maxZ)) || corners) {
                    BlockPos newPos = new BlockPos(x, pos.getY(), z);
                    map.put(newPos, getWood(random, newPos));
                }
            }
        }
    }

    private BlockPos makeBranch(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random, int length) {
        BlockPos.MutableBlockPos dir = new BlockPos.MutableBlockPos(random.nextBoolean() ? 1 : -1, 0, random.nextBoolean() ? 1 : -1);
        for (int i = 1; i <= length; i++) {
            BlockPos newPos = new BlockPos(pos.getX()+(dir.getX()*i), pos.getY(), pos.getZ()+(dir.getZ()*i));
            map.put(newPos, getWood(random, newPos));
            if (i == length) {
                return newPos.above();
            }
        }
        return pos;
    }
}
