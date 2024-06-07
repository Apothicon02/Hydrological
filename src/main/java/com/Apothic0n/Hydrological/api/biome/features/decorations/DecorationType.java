package com.Apothic0n.Hydrological.api.biome.features.decorations;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static com.Apothic0n.Hydrological.Hydrological.MODID;

public class DecorationType<P extends Decoration> {
    public static final DeferredRegister<DecorationType<?>> DECORATION_TYPES = DeferredRegister.create(new ResourceLocation(MODID, "decoration_types"), MODID);
    public static final Supplier<IForgeRegistry<DecorationType<?>>> DECORATION_TYPE_REGISTRY = DECORATION_TYPES.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<DecorationType<CocoaDecorationType>> COCOA_DECORATION_TYPE = DECORATION_TYPES.register("cocoa", () -> new DecorationType<>(CocoaDecorationType.CODEC));
    public static final RegistryObject<DecorationType<HangingLeavesDecorationType>> HANGING_LEAVES_DECORATION_TYPE = DECORATION_TYPES.register("hanging_leaves", () -> new DecorationType<>(HangingLeavesDecorationType.CODEC));
    public static final RegistryObject<DecorationType<RootsDecorationType>> ROOTS_DECORATION_TYPE = DECORATION_TYPES.register("roots", () -> new DecorationType<>(RootsDecorationType.CODEC));

    private final Codec<P> codec;

    protected DecorationType(Codec<P> codec) {
        this.codec = codec;
    }

    public static void register(IEventBus eventBus) {
        DECORATION_TYPES.register(eventBus);
    }
    public Codec<P> codec() {
        return this.codec;
    }
}
