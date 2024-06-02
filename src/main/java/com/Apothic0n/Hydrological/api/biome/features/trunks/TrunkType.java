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
