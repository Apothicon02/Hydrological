package com.Apothic0n.Hydrological.api.biome.features.placement_modifiers;

import com.Apothic0n.Hydrological.api.HydrolMath;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

public class HeightBasedChancePlacement extends PlacementFilter {
    public static final Codec<HeightBasedChancePlacement> CODEC = RecordCodecBuilder.create((p_191761_) -> {
        return p_191761_.group(Codec.INT.fieldOf("impossible").forGetter((v) -> {
            return v.impossible;
        }), Codec.INT.fieldOf("guaranteed").forGetter((v) -> {
            return v.guaranteed;
        })).apply(p_191761_, HeightBasedChancePlacement::new);
    });
    private final int impossible;
    private final int guaranteed;

    private HeightBasedChancePlacement(int impossible, int guaranteed) {
        this.impossible = impossible;
        this.guaranteed = guaranteed;
    }

    public static HeightBasedChancePlacement of(int impossible, int guaranteed) {
        return new HeightBasedChancePlacement(impossible, guaranteed);
    }

    @Override
    public boolean shouldPlace(PlacementContext context, RandomSource random, BlockPos pos) {
        return HydrolMath.gradient(pos.getY(), this.guaranteed, this.impossible, 0, 1) >= random.nextInt(0, 100) / 100D;
    }

    public PlacementModifierType<?> type() {
        return HydrolPlacementModifierTypes.HEIGHT_BASED_CHANCE.get();
    }
}
