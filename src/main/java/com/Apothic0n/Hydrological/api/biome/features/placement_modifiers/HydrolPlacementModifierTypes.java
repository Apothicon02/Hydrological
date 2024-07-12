package com.Apothic0n.Hydrological.api.biome.features.placement_modifiers;

import com.Apothic0n.Hydrological.Hydrological;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class HydrolPlacementModifierTypes {
    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPE = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, Hydrological.MODID);

    public static final RegistryObject<PlacementModifierType<NoiseCoverPlacement>> NOISE_COVER = PLACEMENT_MODIFIER_TYPE.register("noise_cover", () -> () -> NoiseCoverPlacement.CODEC);
    public static final RegistryObject<PlacementModifierType<HeightBasedChancePlacement>> HEIGHT_BASED_CHANCE = PLACEMENT_MODIFIER_TYPE.register("height_based_chance", () -> () -> HeightBasedChancePlacement.CODEC);
    public static final RegistryObject<PlacementModifierType<OffsetPlacement>> OFFSET = PLACEMENT_MODIFIER_TYPE.register("offset", () -> () -> OffsetPlacement.CODEC);

    public static void register(IEventBus eventBus) {
        PLACEMENT_MODIFIER_TYPE.register(eventBus);
    }
}
