package com.Apothic0n.Hydrological.api.biome.features.trunks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class BendingTrunkType extends Trunk {
    public static final Codec<BendingTrunkType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (IntProvider.codec(1, 64).fieldOf("height")).forGetter(v -> v.height),
            (BlockStateProvider.CODEC.fieldOf("wood")).forGetter(v -> v.wood)
    ).apply(instance, BendingTrunkType::new));

    private final IntProvider height;
    private final BlockStateProvider wood;

    public BendingTrunkType(IntProvider height, BlockStateProvider wood) {
        this.height = height;
        this.wood = wood;
    }

    @Override
    protected TrunkType<?> type() {
        return TrunkType.BENDING_TRNUK_TYPE.get();
    }

    private BlockState getWood(RandomSource random, BlockPos pos) {
        return this.wood.getState(random, pos);
    }

    @Override
    public GeneratedTrunk generateTrunk(RandomSource random, BlockPos origin) {
        Map<BlockPos, BlockState> map = new java.util.HashMap<>(Map.of());
        Set<BlockPos> canopies = new HashSet<>();
        int extra = random.nextInt(0, 10);
        if (extra >= 10) {
            extra = 1;
        } else if (extra >= 6) {
            extra = 0;
        } else {
            extra = -1;
        }
        int highestHeight = 0;

        for (int trunks = 0; trunks <= extra+1; trunks++) {
            int offsetX = origin.getX();
            int offsetZ = origin.getZ();
            int maxHeight = origin.getY()+this.height.sample(random);
            if (maxHeight > highestHeight) {
                highestHeight = maxHeight;
            }
            for (int height = origin.getY()-1; height <= maxHeight; height++) {
                float bendFactor = ((float) maxHeight /height)*2F;
                BlockPos pos = new BlockPos(offsetX, height, offsetZ);
                map.put(pos, getWood(random, pos));
                if (height == maxHeight) {
                    canopies.add(pos.above());
                } else if (height < maxHeight-4) {
                    if (trunks == 0) {
                        if (random.nextInt(0, 5) < 3-bendFactor) {
                            offsetX += 1;
                        }
                        if (random.nextInt(0, 5) < 3-bendFactor) {
                            offsetZ += 1;
                        }
                    } else {
                        if (random.nextInt(0, 5) < 4-bendFactor) {
                            offsetX -= 1;
                        }
                        if (random.nextInt(0, 5) < 4-bendFactor) {
                            offsetZ -= 1;
                        }
                    }
                    pos = new BlockPos(offsetX, height, offsetZ);
                    map.put(pos, getWood(random, pos));
                }
            }
        }

        return new GeneratedTrunk(map, canopies, highestHeight);
    }
}
