package com.Apothic0n.Hydrological.api.biome;

import com.Apothic0n.Hydrological.Hydrological;
import com.Apothic0n.Hydrological.api.biome.features.placement_modifiers.NoiseCoverPlacement;
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
    public static final RegistryObject<Codec<? extends SurfaceRules.ConditionSource>> NOISE_THRESHOLD_SURFACE_RULE_TYPE = SURFACE_RULE_TYPES.register("noise_threshold", HydrolSurfaceRules.NoiseThreshold.CODEC::codec);

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

    protected record NoiseThreshold(float minThreshold, float maxThreshold) implements SurfaceRules.ConditionSource {
        static final KeyDispatchDataCodec<HydrolSurfaceRules.NoiseThreshold> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec((data) -> {
            return data.group(Codec.FLOAT.fieldOf("min_threshold").forGetter(HydrolSurfaceRules.NoiseThreshold::minThreshold),
                    Codec.FLOAT.fieldOf("max_threshold").forGetter(HydrolSurfaceRules.NoiseThreshold::maxThreshold)).apply(data, HydrolSurfaceRules.NoiseThreshold::new);
        }));

        public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
            return CODEC;
        }

        public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
            class NoiseThresholdCondition extends SurfaceRules.LazyXZCondition {
                NoiseThresholdCondition() {
                    super(ruleContext);
                }

                protected boolean compute() {
                    double d0 = NoiseCoverPlacement.HEIGHT_NOISE.getValue((double)this.context.blockX, (double)this.context.blockZ, false);
                    return d0 >= HydrolSurfaceRules.NoiseThreshold.this.minThreshold && d0 <= HydrolSurfaceRules.NoiseThreshold.this.maxThreshold;
                }
            }

            return new NoiseThresholdCondition();
        }
    }
}
