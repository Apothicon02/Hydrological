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
