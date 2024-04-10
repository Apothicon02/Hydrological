package com.Apothic0n.Hydrological.api.biome.features.foliage_placers;

import com.Apothic0n.Hydrological.Hydrological;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
public class HydrolFoliagePlacerType {
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPE = DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, Hydrological.MODID);

    public static final RegistryObject<FoliagePlacerType<GiantPineFoliagePlacer>> GIANT_PINE_FOLIAGE_PLACER = FOLIAGE_PLACER_TYPE.register("giant_pine_foliage_placer", () ->
            new FoliagePlacerType<>(GiantPineFoliagePlacer.CODEC));
    public static final RegistryObject<FoliagePlacerType<TallFoliagePlacer>> TALL_FOLIAGE_PLACER = FOLIAGE_PLACER_TYPE.register("tall_foliage_placer", () ->
            new FoliagePlacerType<>(TallFoliagePlacer.CODEC));
    public static final RegistryObject<FoliagePlacerType<SphericalCapFoliagePlacer>> SPHERICAL_CAP_FOLIAGE_PLACER = FOLIAGE_PLACER_TYPE.register("spherical_cap_foliage_placer", () ->
            new FoliagePlacerType<>(SphericalCapFoliagePlacer.CODEC));


    public static void register(IEventBus eventBus) {
        FOLIAGE_PLACER_TYPE.register(eventBus);
    }
}