package com.Apothic0n.Hydrological.api.biome.features.configurations;

import com.Apothic0n.Hydrological.api.biome.features.canopies.Canopy;
import com.Apothic0n.Hydrological.api.biome.features.trunks.Trunk;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class NewTreeConfiguration implements FeatureConfiguration {
    public static final Codec<NewTreeConfiguration> CODEC = RecordCodecBuilder.create((fields) -> {
        return fields.group(Codec.BOOL.fieldOf("intersect").orElse(false).forGetter((v) -> {
            return v.intersect;
        }), Trunk.CODEC.fieldOf("trunk").forGetter((v) -> {
            return v.trunk;
        }), Canopy.CODEC.fieldOf("canopy").forGetter((v) -> {
            return v.canopy;
        })).apply(fields, NewTreeConfiguration::new);
    });

    public final boolean intersect;
    private final Trunk trunk;
    private final Canopy canopy;

    public NewTreeConfiguration(boolean intersect, Trunk trunk, Canopy canopy) {
        this.intersect = intersect;
        this.trunk = trunk;
        this.canopy = canopy;
    }

    public boolean getIntersect() {return this.intersect;}
    public Trunk getTrunk() {return this.trunk;}
    public Canopy getCanopy() {return this.canopy;}
}
