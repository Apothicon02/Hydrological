package com.Apothic0n.Hydrological.api.biome.features.trunks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.Apothic0n.Hydrological.Hydrological.MODID;

public class TrunkType<P extends Trunk> {
    public static final DeferredRegister<TrunkType<?>> TRUNK_TYPES = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(MODID, "trunk_types"), MODID);
    public static final Registry<TrunkType<?>> TRUNK_TYPE_REGISTRY = TRUNK_TYPES.makeRegistry(builder -> {});

    public static final DeferredHolder<TrunkType<?>, ?> BENDING_TRNUK_TYPE = TRUNK_TYPES.register("bending_trunk", () -> new TrunkType<>(BendingTrunkType.CODEC));
    public static final DeferredHolder<TrunkType<?>, ?> STRAIGHT_TRNUK_TYPE = TRUNK_TYPES.register("straight_trunk", () -> new TrunkType<>(StraightTrunkType.CODEC));
    public static final DeferredHolder<TrunkType<?>, ?> STRAIGHT_DEAD_BRANCHED_TRNUK_TYPE = TRUNK_TYPES.register("straight_dead_branched_trunk", () -> new TrunkType<>(StraightDeadBranchedTrunkType.CODEC));
    public static final DeferredHolder<TrunkType<?>, ?> TILTED_TRNUK_TYPE = TRUNK_TYPES.register("tilted_trunk", () -> new TrunkType<>(TiltedTrunkType.CODEC));
    public static final DeferredHolder<TrunkType<?>, ?> BRANCHING_TRNUK_TYPE = TRUNK_TYPES.register("branching_trunk", () -> new TrunkType<>(BranchingTrunkType.CODEC));
    public static final DeferredHolder<TrunkType<?>, ?> TWISTING_TRNUK_TYPE = TRUNK_TYPES.register("twisting_trunk", () -> new TrunkType<>(TwistingTrunkType.CODEC));
    public static final DeferredHolder<TrunkType<?>, ?> THICK_TRUNK_TYPE = TRUNK_TYPES.register("thick_trunk", () -> new TrunkType<>(ThickTrunkType.CODEC));

    private final MapCodec<P> codec;

    protected TrunkType(MapCodec<P> codec) {
        this.codec = codec;
    }

    public static void register(IEventBus eventBus) {
        TRUNK_TYPES.register(eventBus);
    }
    public MapCodec<P> codec() {
        return this.codec;
    }
}
