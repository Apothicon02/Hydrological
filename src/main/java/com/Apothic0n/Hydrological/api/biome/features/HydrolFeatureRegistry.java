package com.Apothic0n.Hydrological.api.biome.features;

import com.Apothic0n.Hydrological.Hydrological;
import com.Apothic0n.Hydrological.api.biome.features.configurations.*;
import com.Apothic0n.Hydrological.api.biome.features.configurations.SpikeConfiguration;
import com.Apothic0n.Hydrological.api.biome.features.types.*;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FossilFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class HydrolFeatureRegistry {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Hydrological.MODID);

    public static final RegistryObject<Feature<VentConfiguration>> VENT = FEATURES.register("vent", () ->
            new VentFeature(VentConfiguration.CODEC));
    public static final RegistryObject<Feature<AnvilRockConfiguration>> FLOATING_ROCK_FEATURE = FEATURES.register("floating_rock", () ->
            new FloatingRockFeature(AnvilRockConfiguration.CODEC));
    public static final RegistryObject<Feature<NewTreeConfiguration>> NEW_TREE_FEATURE = FEATURES.register("tree", () ->
            new NewTreeFeature(NewTreeConfiguration.CODEC));
    public static final RegistryObject<Feature<ReplaceableBlockConfiguration>> SIMPLE_BLOCK_FEATURE = FEATURES.register("simple_block", () ->
            new SimpleBlockFeature(ReplaceableBlockConfiguration.CODEC));
    public static final RegistryObject<Feature<BlockColumnConfiguration>> BLOCK_COLUMN_FEATURE = FEATURES.register("block_column", () ->
            new BlockColumnFeature(BlockColumnConfiguration.CODEC));
    public static final RegistryObject<Feature<RockConfiguration>> ROCK_FEATURE = FEATURES.register("rock", () ->
            new RockFeature(RockConfiguration.CODEC));
    public static final RegistryObject<Feature<SimpleBlockConfiguration>> STEMMED_2X2X2_CUBE_FEATURE = FEATURES.register("stemmed_2x2x2_cube", () ->
            new Stemmed2x2x2CubeFeature(SimpleBlockConfiguration.CODEC));
    public static final RegistryObject<Feature<AnvilRockConfiguration>> ANVIL_ROCK_FEATURE = FEATURES.register("anvil_rock", () ->
            new AnvilRockFeature(AnvilRockConfiguration.CODEC));
    public static final RegistryObject<Feature<SpikeConfiguration>> SPIKE_FEATURE = FEATURES.register("spike", () ->
            new SpikeFeature(SpikeConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> CLIFF_CARVING_FEATURE = FEATURES.register("cliff_carving", () ->
            new CliffCarvingFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<CatchingFallConfiguration>> CATCHING_FALL = FEATURES.register("catching_fall", () ->
            new CatchingFallFeature(CatchingFallConfiguration.CODEC));
    public static final RegistryObject<Feature<ColumnConfiguration>> COLUMN = FEATURES.register("column", () ->
            new ColumnFeature(ColumnConfiguration.CODEC));
    public static final RegistryObject<Feature<FossilFeatureConfiguration>> FOSSIL = FEATURES.register("fossil", () ->
            new FossilFeature(FossilFeatureConfiguration.CODEC));

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}
