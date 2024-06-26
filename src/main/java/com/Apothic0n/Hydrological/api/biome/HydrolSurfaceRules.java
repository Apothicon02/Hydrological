package com.Apothic0n.Hydrological.api.biome;

import com.Apothic0n.Hydrological.Hydrological;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class HydrolSurfaceRules {
    public static final DeferredRegister<Codec<? extends SurfaceRules.ConditionSource>> SURFACE_RULE_TYPES = DeferredRegister.create(Registries.MATERIAL_CONDITION, Hydrological.MODID);

    public static final RegistryObject<Codec<? extends SurfaceRules.ConditionSource>> ABOVE_SURFACE_RULE_TYPE = SURFACE_RULE_TYPES.register("above", HydrolSurfaceRules.Above.CODEC::codec);

    public static void register(IEventBus eventBus) {
        SURFACE_RULE_TYPES.register(eventBus);
    }

    protected record Above(int y) implements SurfaceRules.ConditionSource {
        static final KeyDispatchDataCodec<HydrolSurfaceRules.Above> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec((data) -> {
            return data.group(Codec.INT.fieldOf("y").forGetter(HydrolSurfaceRules.Above::y)).apply(data, HydrolSurfaceRules.Above::new);
        }));

        public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
            return CODEC;
        }

        public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
            class YCondition extends SurfaceRules.LazyYCondition {
                YCondition() {
                    super(ruleContext);
                }

                protected boolean compute() {
                    return this.context.blockY >= y();
                }
            }

            return new YCondition();
        }
    }
}
