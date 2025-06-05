package com.Apothic0n.Hydrological.api.biome.features.decorations;

import com.Apothic0n.Hydrological.api.biome.features.FeatureHelper;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.Apothic0n.Hydrological.Hydrological.MODID;

public class DecorationType<P extends Decoration> {
    public static final DeferredRegister<DecorationType<?>> DECORATION_TYPES = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(MODID, "decoration_types"), MODID);
    public static final Registry<DecorationType<?>> DECORATION_TYPE_REGISTRY = DECORATION_TYPES.makeRegistry(builder -> {});

    public static final DeferredHolder<DecorationType<?>, ?> COCOA_DECORATION_TYPE = DECORATION_TYPES.register("cocoa", () -> new DecorationType<>(CocoaDecorationType.CODEC));
    public static final DeferredHolder<DecorationType<?>, ?> HANGING_LEAVES_DECORATION_TYPE = DECORATION_TYPES.register("hanging_leaves", () -> new DecorationType<>(HangingLeavesDecorationType.CODEC));
    public static final DeferredHolder<DecorationType<?>, ?> LEAF_PILES_DECORATION_TYPE = DECORATION_TYPES.register("leaf_piles", () -> new DecorationType<>(LeafPilesDecorationType.CODEC));
    public static final DeferredHolder<DecorationType<?>, ?> ROOTS_DECORATION_TYPE = DECORATION_TYPES.register("roots", () -> new DecorationType<>(RootsDecorationType.CODEC));
    public static final DeferredHolder<DecorationType<?>, ?> GROUND_DECORATION_TYPE = DECORATION_TYPES.register("ground", () -> new DecorationType<>(GroundDecorationType.CODEC));

    private final MapCodec<P> codec;

    protected DecorationType(MapCodec<P> codec) {
        this.codec = codec;
    }

    public static void register(IEventBus eventBus) {
        DECORATION_TYPES.register(eventBus);
    }
    public MapCodec<P> codec() {
        return this.codec;
    }
}
