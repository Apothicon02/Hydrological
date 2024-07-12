package com.Apothic0n.Hydrological.api.biome.features.placement_modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.stream.Stream;

public class OffsetPlacement extends PlacementModifier {
    public static final Codec<OffsetPlacement> CODEC = RecordCodecBuilder.create((p_191761_) -> {
        return p_191761_.group(Codec.INT.fieldOf("x").forGetter((v) -> {
            return v.x;
        }), Codec.INT.fieldOf("y").forGetter((v) -> {
            return v.y;
        }), Codec.INT.fieldOf("z").forGetter((v) -> {
            return v.z;
        })).apply(p_191761_, OffsetPlacement::new);
    });
    private final int x;
    private final int y;
    private final int z;

    private OffsetPlacement(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static OffsetPlacement of(int min, int max, int chance) {
        return new OffsetPlacement(min, max, chance);
    }

    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        return Stream.of(new BlockPos(pos.getX()+x, pos.getY()+y, pos.getZ()+z));
    }

    public PlacementModifierType<?> type() {
        return HydrolPlacementModifierTypes.NOISE_COVER.get();
    }
}
