package com.Apothic0n.Hydrological.api.biome.features.placement_modifiers;

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
import net.minecraft.world.level.levelgen.synth.SimplexNoise;

import java.util.List;
import java.util.stream.Stream;

public class VeinPlacement extends PlacementModifier {
    public static final MapCodec<VeinPlacement> CODEC = RecordCodecBuilder.mapCodec((p_191761_) -> {
        return p_191761_.group(Codec.DOUBLE.fieldOf("min").forGetter((v) -> {
            return v.min;
        }), Codec.DOUBLE.fieldOf("max").forGetter((v) -> {
            return v.max;
        }), Codec.DOUBLE.fieldOf("chance").forGetter((v) -> {
            return v.chance;
        }), Codec.INT.fieldOf("altitude").forGetter((v) -> {
            return v.altitude;
        })).apply(p_191761_, VeinPlacement::new);
    });
    private final double min;
    private final double max;
    private final double chance;
    private final int altitude;

    public static final SimplexNoise NOISE = new SimplexNoise(new WorldgenRandom(new LegacyRandomSource(5432L)));
    public static float scale = 0.01f;

    private VeinPlacement(double min, double max, double chance, int altitude) {
        this.min = min;
        this.max = max;
        this.chance = chance;
        this.altitude = altitude;
    }

    public static VeinPlacement of(double min, double max, double chance, int altitude) {
        return new VeinPlacement(min, max, chance, altitude);
    }

    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        List<BlockPos> list = new java.util.ArrayList<>(List.of());
        for (int x = pos.getX()+1; x <= pos.getX() + 16; x++) {
            for (int z = pos.getZ()+1; z <= pos.getZ() + 16; z++) {
                for (int y = context.getMinGenY()+1; y < altitude; y++) {
                    if (chance > random.nextInt(0, 1000) / 1000D) {
                        double noise = NOISE.getValue(x*scale, y*scale, z*scale);
                        if (noise < max && noise > min) {
                            list.add(list.size(), new BlockPos(x, y, z));
                        }
                    }
                }
            }
        }
        return list.stream();
    }

    public PlacementModifierType<?> type() {
        return HydrolPlacementModifierTypes.VEIN.get();
    }
}
