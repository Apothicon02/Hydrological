package com.Apothic0n.Hydrological.api.biome.features.canopies;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.Apothic0n.Hydrological.Hydrological.MODID;

public class CanopyType<P extends Canopy> {
    public static final DeferredRegister<CanopyType<?>> CANOPY_TYPES = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(MODID, "canopy_types"), MODID);
    public static final Registry<CanopyType<?>> CANOPY_TYPE_REGISTRY = CANOPY_TYPES.makeRegistry(builder -> {});

    public static final DeferredHolder<CanopyType<?>, ?> PALM_CANOPY_TYPE = CANOPY_TYPES.register("palm_canopy", () -> new CanopyType<>(PalmCanopyType.CODEC));
    public static final DeferredHolder<CanopyType<?>, ?> POINTED_CANOPY_TYPE = CANOPY_TYPES.register("pointed_canopy", () -> new CanopyType<>(PointedCanopyType.CODEC));
    public static final DeferredHolder<CanopyType<?>, ?> SQUARE_CANOPY_TYPE = CANOPY_TYPES.register("square_canopy", () -> new CanopyType<>(SquareCanopyType.CODEC));
    public static final DeferredHolder<CanopyType<?>, ?> SPHERE_CANOPY_TYPE = CANOPY_TYPES.register("sphere_canopy", () -> new CanopyType<>(SphereCanopyType.CODEC));
    public static final DeferredHolder<CanopyType<?>, ?> FIR_CANOPY_TYPE = CANOPY_TYPES.register("fir_canopy", () -> new CanopyType<>(FirCanopyType.CODEC));
    public static final DeferredHolder<CanopyType<?>, ?> BIRCH_CANOPY_TYPE = CANOPY_TYPES.register("birch_canopy", () -> new CanopyType<>(BirchCanopyType.CODEC));
    public static final DeferredHolder<CanopyType<?>, ?> SPRUCE_CANOPY_TYPE = CANOPY_TYPES.register("spruce_canopy", () -> new CanopyType<>(SpruceCanopyType.CODEC));
    public static final DeferredHolder<CanopyType<?>, ?> PINE_CANOPY_TYPE = CANOPY_TYPES.register("pine_canopy", () -> new CanopyType<>(PineCanopyType.CODEC));
    public static final DeferredHolder<CanopyType<?>, ?> DROOPING_CANOPY_TYPE = CANOPY_TYPES.register("drooping_canopy", () -> new CanopyType<>(DroopingCanopyType.CODEC));
    public static final DeferredHolder<CanopyType<?>, ?> JUNGLE_CANOPY_TYPE = CANOPY_TYPES.register("jungle_canopy", () -> new CanopyType<>(JungleCanopyType.CODEC));

    private final MapCodec<P> codec;

    protected CanopyType(MapCodec<P> codec) {
        this.codec = codec;
    }

    public static void register(IEventBus eventBus) {
        CANOPY_TYPES.register(eventBus);
    }
    public MapCodec<P> codec() {
        return this.codec;
    }
}
