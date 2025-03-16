package com.Apothic0n.Hydrological.api.biome.features.placement_modifiers;

import com.Apothic0n.Hydrological.Hydrological;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class HydrolPlacementModifierTypes {
    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPE = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, Hydrological.MODID);

    public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<NoiseCoverPlacement>> NOISE_COVER = PLACEMENT_MODIFIER_TYPE.register("noise_cover", () -> () -> NoiseCoverPlacement.CODEC);
    public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<HeightBasedChancePlacement>> HEIGHT_BASED_CHANCE = PLACEMENT_MODIFIER_TYPE.register("height_based_chance", () -> () -> HeightBasedChancePlacement.CODEC);
    public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<OffsetPlacement>> OFFSET = PLACEMENT_MODIFIER_TYPE.register("offset", () -> () -> OffsetPlacement.CODEC);
    public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<VeinPlacement>> VEIN = PLACEMENT_MODIFIER_TYPE.register("vein", () -> () -> VeinPlacement.CODEC);

    public static void register(IEventBus eventBus) {
        PLACEMENT_MODIFIER_TYPE.register(eventBus);
    }
}
