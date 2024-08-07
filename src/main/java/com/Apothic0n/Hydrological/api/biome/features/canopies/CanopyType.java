package com.Apothic0n.Hydrological.api.biome.features.canopies;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.Apothic0n.Hydrological.Hydrological.MODID;

public class CanopyType<P extends Canopy> {
    public static final DeferredRegister<CanopyType<?>> CANOPY_TYPES = DeferredRegister.create(new ResourceLocation(MODID, "canopy_types"), MODID);
    public static final Supplier<IForgeRegistry<CanopyType<?>>> CANOPY_TYPE_REGISTRY = CANOPY_TYPES.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<CanopyType<PalmCanopyType>> PALM_CANOPY_TYPE = CANOPY_TYPES.register("palm_canopy", () -> new CanopyType<>(PalmCanopyType.CODEC));
    public static final RegistryObject<CanopyType<PointedCanopyType>> POINTED_CANOPY_TYPE = CANOPY_TYPES.register("pointed_canopy", () -> new CanopyType<>(PointedCanopyType.CODEC));
    public static final RegistryObject<CanopyType<SquareCanopyType>> SQUARE_CANOPY_TYPE = CANOPY_TYPES.register("square_canopy", () -> new CanopyType<>(SquareCanopyType.CODEC));
    public static final RegistryObject<CanopyType<SphereCanopyType>> SPHERE_CANOPY_TYPE = CANOPY_TYPES.register("sphere_canopy", () -> new CanopyType<>(SphereCanopyType.CODEC));
    public static final RegistryObject<CanopyType<FirCanopyType>> FIR_CANOPY_TYPE = CANOPY_TYPES.register("fir_canopy", () -> new CanopyType<>(FirCanopyType.CODEC));
    public static final RegistryObject<CanopyType<BirchCanopyType>> BIRCH_CANOPY_TYPE = CANOPY_TYPES.register("birch_canopy", () -> new CanopyType<>(BirchCanopyType.CODEC));
    public static final RegistryObject<CanopyType<SpruceCanopyType>> SPRUCE_CANOPY_TYPE = CANOPY_TYPES.register("spruce_canopy", () -> new CanopyType<>(SpruceCanopyType.CODEC));
    public static final RegistryObject<CanopyType<PineCanopyType>> PINE_CANOPY_TYPE = CANOPY_TYPES.register("pine_canopy", () -> new CanopyType<>(PineCanopyType.CODEC));
    public static final RegistryObject<CanopyType<DroopingCanopyType>> DROOPING_CANOPY_TYPE = CANOPY_TYPES.register("drooping_canopy", () -> new CanopyType<>(DroopingCanopyType.CODEC));
    public static final RegistryObject<CanopyType<JungleCanopyType>> JUNGLE_CANOPY_TYPE = CANOPY_TYPES.register("jungle_canopy", () -> new CanopyType<>(JungleCanopyType.CODEC));

    private final Codec<P> codec;

    protected CanopyType(Codec<P> codec) {
        this.codec = codec;
    }

    public static void register(IEventBus eventBus) {
        CANOPY_TYPES.register(eventBus);
    }
    public Codec<P> codec() {
        return this.codec;
    }
}
