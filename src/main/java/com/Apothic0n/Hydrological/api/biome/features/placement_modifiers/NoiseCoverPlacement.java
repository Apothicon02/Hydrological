package com.Apothic0n.Hydrological.api.biome.features.placement_modifiers;

import com.Apothic0n.Hydrological.api.biome.features.types.CoverFeature;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import java.util.List;
import java.util.stream.Stream;

public class NoiseCoverPlacement extends PlacementModifier {
    public static final Codec<NoiseCoverPlacement> CODEC = RecordCodecBuilder.create((p_191761_) -> {
        return p_191761_.group(Codec.DOUBLE.fieldOf("min").forGetter((v) -> {
            return v.min;
        }), Codec.DOUBLE.fieldOf("max").forGetter((v) -> {
            return v.max;
        }), Codec.DOUBLE.fieldOf("chance").forGetter((v) -> {
            return v.chance;
        })).apply(p_191761_, NoiseCoverPlacement::new);
    });
    private final double min;
    private final double max;
    private final double chance;

    private NoiseCoverPlacement(double min, double max, double chance) {
        this.min = min;
        this.max = max;
        this.chance = chance;
    }

    public static NoiseCoverPlacement of(double min, double max, double chance) {
        return new NoiseCoverPlacement(min, max, chance);
    }

    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        List<BlockPos> list = new java.util.ArrayList<>(List.of());
        for (int x = pos.getX(); x <= pos.getX() + 16; x++) {
            for (int z = pos.getZ(); z <= pos.getZ() + 16; z++) {
                if (chance > random.nextInt(0, 100)/100D) {
                    double noise = CoverFeature.HEIGHT_NOISE.getValue(x, z, false);
                    if (noise < max && noise > min) {
                        list.add(list.size(), new BlockPos(x, pos.getY(), z));
                    }
                }
            }
        }
        return list.stream();
    }

    public PlacementModifierType<?> type() {
        return HydrolPlacementModifierTypes.NOISE_COVER.get();
    }
}
