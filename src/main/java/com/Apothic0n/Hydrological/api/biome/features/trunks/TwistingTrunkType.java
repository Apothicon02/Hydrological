package com.Apothic0n.Hydrological.api.biome.features.trunks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TwistingTrunkType extends Trunk {
    public static final Codec<TwistingTrunkType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (IntProvider.codec(1, 64).fieldOf("height")).forGetter(v -> v.height),
            (IntProvider.codec(-100, 100).fieldOf("overgrown_chance")).forGetter(v -> v.overgrownChance),
            (BlockStateProvider.CODEC.fieldOf("wood")).forGetter(v -> v.wood)
    ).apply(instance, TwistingTrunkType::new));

    private final IntProvider height;
    private final IntProvider overgrownChance;
    private final BlockStateProvider wood;

    public TwistingTrunkType(IntProvider height, IntProvider overgrownChance, BlockStateProvider wood) {
        this.height = height;
        this.overgrownChance = overgrownChance;
        this.wood = wood;
    }

    @Override
    protected TrunkType<?> type() {
        return TrunkType.TWISTING_TRNUK_TYPE.get();
    }

    private BlockState getWood(RandomSource random, BlockPos pos) {
        return this.wood.getState(random, pos);
    }

    @Override
    public GeneratedTrunk generateTrunk(RandomSource random, BlockPos origin) {
        Map<BlockPos, BlockState> map = new java.util.HashMap<>(Map.of());
        Set<BlockPos> canopies = new HashSet<>();

        boolean overgrown = overgrownChance.sample(random) > 0;
        int prevXPositive = 0;
        int prevZPositive = 0;
        int twistable = 0;
        int maxHeight = origin.getY()+this.height.sample(random);
        BlockPos.MutableBlockPos pos = origin.mutable();
        for (int height = origin.getY()-1; height <= maxHeight; height++) {
            twistable--;
            boolean branch = false;
            BlockPos.MutableBlockPos dir = new BlockPos.MutableBlockPos(0, 0, 0);
            if (height > origin.getY()+2 && twistable <= 0 && random.nextInt(0, 10) < 4) {
                int xOff = random.nextInt(-10, 10);
                int zOff = random.nextInt(-10, 10);
                boolean xPositive = xOff >= prevXPositive;
                boolean zPositive = zOff >= prevZPositive;
                if (xPositive) {
                    prevXPositive = 5;
                    dir.move(new Vec3i(1, 0, 0));
                } else {
                    prevXPositive = -5;
                    dir.move(new Vec3i(-1, 0, 0));
                }
                if (zPositive) {
                    prevZPositive = 5;
                    dir.move(new Vec3i(0, 0, 1));
                } else {
                    prevZPositive = -5;
                    dir.move(new Vec3i(0, 0, -1));
                }
                branch = true;
                twistable = 2;
            }
            pos.move(dir).setY(height);
            makeSquare(map, pos.immutable().below(), random);
            makeSquare(map, pos.immutable(), random);
            if (branch) {
                canopies.add(makeBranch(map, pos, random, dir.immutable()));
                if (overgrown) {
                    canopies.add(makeBranch(map, pos, random, new BlockPos(dir.getX() * (random.nextBoolean() ? 1 : 0), +2, dir.getZ() * (random.nextBoolean() ? 1 : 0))));
                }
            }
            if (pos.getY() == maxHeight) {
                canopies.add(pos.above());
                if (overgrown) {
                    canopies.add(pos.below().north(3));
                    canopies.add(pos.east(3));
                    canopies.add(pos.below().south(3));
                    canopies.add(pos.west(3));
                }
            }
        }

        return new GeneratedTrunk(map, canopies, maxHeight);
    }

    private void makeSquare(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random) {
        map.put(pos, getWood(random, pos));
        map.put(pos.north(), getWood(random, pos.north()));
        map.put(pos.east(), getWood(random, pos.east()));
        map.put(pos.north().east(), getWood(random, pos.north().east()));
    }

    private BlockPos makeBranch(Map<BlockPos, BlockState> map, BlockPos pos, RandomSource random, BlockPos dir) {
        makeSquare(map, new BlockPos(pos.getX()+dir.getX(), pos.getY(), pos.getZ()+dir.getZ()), random);
        makeSquare(map, new BlockPos(pos.getX()+(dir.getX()*2), pos.getY()-1, pos.getZ()+(dir.getZ()*2)), random);
        makeSquare(map, new BlockPos(pos.getX()+(dir.getX()*3), pos.getY()-1, pos.getZ()+(dir.getZ()*3)), random);
        map.put(new BlockPos(pos.getX()+(dir.getX()*4), pos.getY(), pos.getZ()+(dir.getZ()*4)), getWood(random, pos));
        map.put(new BlockPos(pos.getX()+(dir.getX()*5), pos.getY(), pos.getZ()+(dir.getZ()*5)), getWood(random, pos));
        return new BlockPos(pos.getX()+(dir.getX()*5), pos.getY()+1, pos.getZ()+(dir.getZ()*5));
    }
}
