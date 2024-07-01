package com.Apothic0n.Hydrological.api.biome.features.trunks;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.Apothic0n.Hydrological.Hydrological.MODID;

public class TrunkType<P extends Trunk> {
    public static final DeferredRegister<TrunkType<?>> TRUNK_TYPES = DeferredRegister.create(new ResourceLocation(MODID, "trunk_types"), MODID);
    public static final Supplier<IForgeRegistry<TrunkType<?>>> TRUNK_TYPE_REGISTRY = TRUNK_TYPES.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<TrunkType<BendingTrunkType>> BENDING_TRNUK_TYPE = TRUNK_TYPES.register("bending_trunk", () -> new TrunkType<>(BendingTrunkType.CODEC));
    public static final RegistryObject<TrunkType<StraightTrunkType>> STRAIGHT_TRNUK_TYPE = TRUNK_TYPES.register("straight_trunk", () -> new TrunkType<>(StraightTrunkType.CODEC));
    public static final RegistryObject<TrunkType<StraightDeadBranchedTrunkType>> STRAIGHT_DEAD_BRANCHED_TRNUK_TYPE = TRUNK_TYPES.register("straight_dead_branched_trunk", () -> new TrunkType<>(StraightDeadBranchedTrunkType.CODEC));
    public static final RegistryObject<TrunkType<TiltedTrunkType>> TILTED_TRNUK_TYPE = TRUNK_TYPES.register("tilted_trunk", () -> new TrunkType<>(TiltedTrunkType.CODEC));
    public static final RegistryObject<TrunkType<BranchingTrunkType>> BRANCHING_TRNUK_TYPE = TRUNK_TYPES.register("branching_trunk", () -> new TrunkType<>(BranchingTrunkType.CODEC));
    public static final RegistryObject<TrunkType<TwistingTrunkType>> TWISTING_TRNUK_TYPE = TRUNK_TYPES.register("twisting_trunk", () -> new TrunkType<>(TwistingTrunkType.CODEC));
    public static final RegistryObject<TrunkType<ThickTrunkType>> THICK_TRUNK_TYPE = TRUNK_TYPES.register("thick_trunk", () -> new TrunkType<>(ThickTrunkType.CODEC));

    private final Codec<P> codec;

    protected TrunkType(Codec<P> codec) {
        this.codec = codec;
    }

    public static void register(IEventBus eventBus) {
        TRUNK_TYPES.register(eventBus);
    }
    public Codec<P> codec() {
        return this.codec;
    }
}
