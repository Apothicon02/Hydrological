package com.Apothic0n.Hydrological.api.biome.features.trunk_placers;

import com.Apothic0n.Hydrological.Hydrological;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class HydrolTrunkPlacerType {
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACER_TYPES = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, Hydrological.MODID);

    public static final RegistryObject<TrunkPlacerType<StraightBranchingTrunkPlacer>> STRAIGHT_BRANCHING_TRUNK_PLACER = TRUNK_PLACER_TYPES.register("straight_branching_trunk_placer", () ->
            new TrunkPlacerType<>(StraightBranchingTrunkPlacer.CODEC));
    public static final RegistryObject<TrunkPlacerType<GiantStraightBranchingTrunkPlacer>> GIANT_STRAIGHT_BRANCHING_TRUNK_PLACER = TRUNK_PLACER_TYPES.register("giant_straight_branching_trunk_placer", () ->
            new TrunkPlacerType<>(GiantStraightBranchingTrunkPlacer.CODEC));
    public static final RegistryObject<TrunkPlacerType<BranchingTrunkPlacer>> BRANCHING_TRUNK_PLACER = TRUNK_PLACER_TYPES.register("branching_trunk_placer", () ->
            new TrunkPlacerType<>(BranchingTrunkPlacer.CODEC));
    public static final RegistryObject<TrunkPlacerType<GiantBranchingTrunkPlacer>> GIANT_BRANCHING_TRUNK_PLACER = TRUNK_PLACER_TYPES.register("giant_branching_trunk_placer", () ->
            new TrunkPlacerType<>(GiantBranchingTrunkPlacer.CODEC));
    public static final RegistryObject<TrunkPlacerType<GiantCanopiedTrunkPlacer>> GIANT_CANOPIED_TRUNK_PLACER = TRUNK_PLACER_TYPES.register("giant_canopied_trunk_placer", () ->
            new TrunkPlacerType<>(GiantCanopiedTrunkPlacer.CODEC));

    public static void register(IEventBus eventBus) {
        TRUNK_PLACER_TYPES.register(eventBus);
    }
}