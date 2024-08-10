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
import java.util.Set;

public class BranchingTrunkType extends Trunk {
    public static final Codec<BranchingTrunkType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            (Codec.BOOL.fieldOf("canopy_offset")).forGetter(v -> v.canopyOffset),
            (IntProvider.codec(1, 8).fieldOf("count")).forGetter(v -> v.count),
            (IntProvider.codec(1, 64).fieldOf("height")).forGetter(v -> v.height),
            (BlockStateProvider.CODEC.fieldOf("wood")).forGetter(v -> v.wood)
    ).apply(instance, BranchingTrunkType::new));

    private final boolean canopyOffset;
    private final IntProvider count;
    private final IntProvider height;
    private final BlockStateProvider wood;

    public BranchingTrunkType(Boolean canopyOffset, IntProvider count, IntProvider height, BlockStateProvider wood) {
        this.canopyOffset = canopyOffset;
        this.count = count;
        this.height = height;
        this.wood = wood;
    }

    @Override
    protected TrunkType<?> type() {
        return TrunkType.BRANCHING_TRNUK_TYPE.get();
    }

    private BlockState getWood(RandomSource random, BlockPos pos) {
        return this.wood.getState(random, pos);
    }

    @Override
    public GeneratedTrunk generateTrunk(RandomSource random, BlockPos origin) {
        Map<BlockPos, BlockState> map = new java.util.HashMap<>(Map.of());
        Set<BlockPos> canopies = new HashSet<>();
        int highestHeight = origin.getY()+this.height.sample(random);

        for (int trunks = 0; trunks <= this.count.sample(random)-1; trunks++) {
            int offsetX = origin.getX();
            int offsetZ = origin.getZ();
            int maxHeight = origin.getY()+this.height.sample(random);
            if (maxHeight > highestHeight) {
                highestHeight = maxHeight;
            }
            if (trunks != 0) {
                offsetX += random.nextInt(-1, 1);
                offsetZ += random.nextInt(-1, 1);
            }
            for (int height = origin.getY()-1; height <= maxHeight; height++) {
                float bendFactor = ((float) maxHeight /height)*2F;
                int xDist = offsetX-origin.getX();
                int zDist = offsetZ-origin.getZ();
                int dist = xDist*xDist+zDist*zDist;
                BlockPos pos = new BlockPos(offsetX, height-(dist/35), offsetZ);
                map.put(pos, getWood(random, pos));
                if (height == maxHeight) {
                    if (canopyOffset) {
                        canopies.add(pos.above((dist / 35) + 1));
                    } else {
                        canopies.add(pos.above(1));
                    }
                } else if (height > origin.getY()+(maxHeight/16F)) {
                    if (trunks == 1) {
                        if (random.nextInt(0, 5) < 4 - bendFactor) {
                            offsetX -= 1;
                        }
                        if (random.nextInt(0, 5) < 4 - bendFactor) {
                            offsetZ -= 1;
                        }
                    } else if (trunks == 2) {
                        if (random.nextInt(0, 5) < 4 - bendFactor) {
                            offsetX += 1;
                        }
                        if (random.nextInt(0, 5) < 4 - bendFactor) {
                            offsetZ -= 1;
                        }
                    } else if (trunks == 3) {
                        if (random.nextInt(0, 5) < 4 - bendFactor) {
                            offsetX -= 1;
                        }
                        if (random.nextInt(0, 5) < 4 - bendFactor) {
                            offsetZ += 1;
                        }
                    } else if (trunks == 4) {
                        if (random.nextInt(0, 5) < 4 - bendFactor) {
                            offsetX -= 1;
                        }
                    } else if (trunks == 5) {
                        if (random.nextInt(0, 5) < 4 - bendFactor) {
                            offsetZ -= 1;
                        }
                    } else if (trunks == 6) {
                        if (random.nextInt(0, 5) < 4 - bendFactor) {
                            offsetX += 1;
                        }
                    } else if (trunks == 7) {
                        if (random.nextInt(0, 5) < 4 - bendFactor) {
                            offsetZ += 1;
                        }
                    }
                    int newXDist = offsetX-origin.getX();
                    int newZDist = offsetZ-origin.getZ();
                    int newDist = newXDist*newXDist+newZDist*newZDist;
                    pos = new BlockPos(offsetX, height-(newDist/35), offsetZ);
                    map.put(pos, getWood(random, pos));
                }
            }
        }

        return new GeneratedTrunk(map, canopies, highestHeight);
    }
}
