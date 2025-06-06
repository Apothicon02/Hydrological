package com.Apothic0n.Hydrological.api.biome.features.configurations;

import com.Apothic0n.Hydrological.api.biome.features.canopies.Canopy;
import com.Apothic0n.Hydrological.api.biome.features.decorations.Decoration;
import com.Apothic0n.Hydrological.api.biome.features.trunks.Trunk;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public class NewTreeConfiguration implements FeatureConfiguration {
    public static final Codec<NewTreeConfiguration> CODEC = RecordCodecBuilder.create((fields) -> {
        return fields.group(Codec.BOOL.fieldOf("must_be_fully_submerged").orElse(false).forGetter((v) -> {
            return v.mustBeFullySubmerged;
        }), Codec.BOOL.fieldOf("intersect").orElse(false).forGetter((v) -> {
            return v.intersect;
        }), IntProvider.codec(-64, 64).fieldOf("canopy_offset").forGetter((v) -> {
            return v.canopyOffset;
        }), Trunk.CODEC.fieldOf("trunk").forGetter((v) -> {
            return v.trunk;
        }), Canopy.CODEC.fieldOf("canopy").forGetter((v) -> {
            return v.canopy;
        }), Decoration.CODEC.codec().listOf().fieldOf("decorations").forGetter((v) -> {
            return v.decorations;
        })).apply(fields, NewTreeConfiguration::new);
    });

    public final boolean mustBeFullySubmerged;
    public final boolean intersect;
    public final IntProvider canopyOffset;
    private final Trunk trunk;
    private final Canopy canopy;
    private final List<Decoration> decorations;

    public NewTreeConfiguration(boolean mustBeFullySubmerged, boolean intersect, IntProvider canopyOffset, Trunk trunk, Canopy canopy, List<Decoration> decorations) {
        this.mustBeFullySubmerged = mustBeFullySubmerged;
        this.intersect = intersect;
        this.canopyOffset = canopyOffset;
        this.trunk = trunk;
        this.canopy = canopy;
        this.decorations = decorations;
    }

    public boolean getMustBeFullySubmerged() {return this.mustBeFullySubmerged;}
    public boolean getIntersect() {return this.intersect;}
    public IntProvider getCanopyOffset() {return this.canopyOffset;}
    public Trunk getTrunk() {return this.trunk;}
    public Canopy getCanopy() {return this.canopy;}
    public List<Decoration> getDecorations() {return this.decorations;}
}
