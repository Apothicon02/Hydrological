package com.Apothic0n.Hydrological.api.biome.features.placement_modifiers;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;

import java.util.List;
import java.util.stream.Stream;

public class NoiseCoverPlacement extends PlacementModifier {
    public static final MapCodec<NoiseCoverPlacement> CODEC = RecordCodecBuilder.mapCodec((p_191761_) -> {
        return p_191761_.group(Codec.DOUBLE.fieldOf("min").forGetter((v) -> {
            return v.min;
        }), Codec.DOUBLE.fieldOf("max").forGetter((v) -> {
            return v.max;
        }), Codec.DOUBLE.fieldOf("chance").forGetter((v) -> {
            return v.chance;
        }), Codec.DOUBLE.fieldOf("multilpier").forGetter((v) -> {
            return v.chance;
        })).apply(p_191761_, NoiseCoverPlacement::new);
    });
    private final double min;
    private final double max;
    private final double chance;
    private final double multiplier;

    public NoiseCoverPlacement(double min, double max, double chance, double multiplier) {
        this.min = min;
        this.max = max;
        this.chance = chance;
        this.multiplier = multiplier;
    }

    public static final PerlinSimplexNoise HEIGHT_NOISE = new PerlinSimplexNoise(new WorldgenRandom(new LegacyRandomSource(5432L)), ImmutableList.of(-6, 1));

    public static NoiseCoverPlacement of(double min, double max, double chance, double multiplier) {
        return new NoiseCoverPlacement(min, max, chance, multiplier);
    }

    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        List<BlockPos> list = new java.util.ArrayList<>(List.of());
        for (int x = pos.getX(); x <= pos.getX() + 15; x++) {
            for (int z = pos.getZ(); z <= pos.getZ() + 15; z++) {
                if (chance > random.nextInt(0, 1000)/1000D) {
                    double noise = HEIGHT_NOISE.getValue(x, z, false)*multiplier;
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
