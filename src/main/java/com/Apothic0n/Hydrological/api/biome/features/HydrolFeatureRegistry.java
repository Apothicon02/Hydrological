package com.Apothic0n.Hydrological.api.biome.features;

import com.Apothic0n.Hydrological.Hydrological;
import com.Apothic0n.Hydrological.api.biome.features.configurations.*;
import com.Apothic0n.Hydrological.api.biome.features.configurations.SpikeConfiguration;
import com.Apothic0n.Hydrological.api.biome.features.types.*;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FossilFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public abstract class HydrolFeatureRegistry {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, Hydrological.MODID);

    public static final DeferredHolder<Feature<?>, ?> VENT = FEATURES.register("vent", () ->
            new VentFeature(VentConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, ?> FLOATING_ROCK_FEATURE = FEATURES.register("floating_rock", () ->
            new FloatingRockFeature(AnvilRockConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, ?> NEW_TREE_FEATURE = FEATURES.register("tree", () ->
            new NewTreeFeature(NewTreeConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, ?> SIMPLE_BLOCK_FEATURE = FEATURES.register("simple_block", () ->
            new SimpleBlockFeature(ReplaceableBlockConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, ?> BLOCK_COLUMN_FEATURE = FEATURES.register("block_column", () ->
            new BlockColumnFeature(BlockColumnConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, ?> ROCK_FEATURE = FEATURES.register("rock", () ->
            new RockFeature(RockConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, ?> STEMMED_2X2X2_CUBE_FEATURE = FEATURES.register("stemmed_2x2x2_cube", () ->
            new Stemmed2x2x2CubeFeature(SimpleBlockConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, ?> ANVIL_ROCK_FEATURE = FEATURES.register("anvil_rock", () ->
            new AnvilRockFeature(AnvilRockConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, ?> SPIKE_FEATURE = FEATURES.register("spike", () ->
            new SpikeFeature(SpikeConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, ?> CLIFF_CARVING_FEATURE = FEATURES.register("cliff_carving", () ->
            new CliffCarvingFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, ?> CATCHING_FALL = FEATURES.register("catching_fall", () ->
            new CatchingFallFeature(CatchingFallConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, ?> COLUMN = FEATURES.register("column", () ->
            new ColumnFeature(ColumnConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, ?> FOSSIL = FEATURES.register("fossil", () ->
            new FossilFeature(FossilFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, ?> FIRE_CORAL = FEATURES.register("fire_coral", () ->
            new FireCoralFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, ?> FREEZE = FEATURES.register("freeze", () ->
            new FreezeFeature(NoneFeatureConfiguration.CODEC));


    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}
