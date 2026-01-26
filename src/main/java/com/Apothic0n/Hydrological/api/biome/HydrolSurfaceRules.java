package com.Apothic0n.Hydrological.api.biome;

import com.Apothic0n.Hydrological.Hydrological;
import com.Apothic0n.Hydrological.api.HydrolDensityFunctions;
import com.Apothic0n.Hydrological.api.HydrolJsonReader;
import com.Apothic0n.Hydrological.api.biome.features.placement_modifiers.NoiseCoverPlacement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class HydrolSurfaceRules {
    public static final DeferredRegister<MapCodec<? extends SurfaceRules.ConditionSource>> SURFACE_RULE_TYPES = DeferredRegister.create(Registries.MATERIAL_CONDITION, Hydrological.MODID);

    public static final DeferredHolder<MapCodec<? extends SurfaceRules.ConditionSource>, ?> ABOVE_SURFACE_RULE_TYPE = SURFACE_RULE_TYPES.register("above", HydrolSurfaceRules.Above.CODEC::codec);
    public static final DeferredHolder<MapCodec<? extends SurfaceRules.ConditionSource>, ?> NOISE_THRESHOLD_SURFACE_RULE_TYPE = SURFACE_RULE_TYPES.register("noise_threshold", HydrolSurfaceRules.NoiseThreshold.CODEC::codec);
    public static final DeferredHolder<MapCodec<? extends SurfaceRules.ConditionSource>, ?> HEIGHT_NOISE_THRESHOLD_SURFACE_RULE_TYPE = SURFACE_RULE_TYPES.register("height_noise_threshold", HydrolSurfaceRules.HeightNoiseThreshold.CODEC::codec);
    public static final DeferredHolder<MapCodec<? extends SurfaceRules.ConditionSource>, ?> TEMPERATURE = SURFACE_RULE_TYPES.register("temperature", HydrolSurfaceRules.Temperature.CODEC::codec);

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

    protected record NoiseThreshold(ResourceKey<NormalNoise.NoiseParameters> noise, float minThreshold, float maxThreshold) implements SurfaceRules.ConditionSource {
        static final KeyDispatchDataCodec<HydrolSurfaceRules.NoiseThreshold> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec((data) -> {
            return data.group(ResourceKey.codec(Registries.NOISE).fieldOf("noise").forGetter(HydrolSurfaceRules.NoiseThreshold::noise),
                    Codec.FLOAT.fieldOf("min_threshold").forGetter(HydrolSurfaceRules.NoiseThreshold::minThreshold),
                    Codec.FLOAT.fieldOf("max_threshold").forGetter(HydrolSurfaceRules.NoiseThreshold::maxThreshold)).apply(data, HydrolSurfaceRules.NoiseThreshold::new);
        }));

        public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
            return CODEC;
        }

        public SurfaceRules.Condition apply(final SurfaceRules.Context ruleContext) {
            class NoiseThresholdCondition extends SurfaceRules.LazyXZCondition {
                final NormalNoise normalnoise = ruleContext.randomState.getOrCreateNoise(noise);

                NoiseThresholdCondition() {
                    super(ruleContext);
                }

                protected boolean compute() {
                    float scale = HydrolJsonReader.noiseScale;
                    double d0 = normalnoise.getValue(this.context.blockX * scale, 0.0D, this.context.blockZ * scale);
                    return d0 >= HydrolSurfaceRules.NoiseThreshold.this.minThreshold && d0 <= HydrolSurfaceRules.NoiseThreshold.this.maxThreshold;
                }
            }

            return new NoiseThresholdCondition();
        }
    }

    protected record HeightNoiseThreshold(float minThreshold, float maxThreshold) implements SurfaceRules.ConditionSource {
        static final KeyDispatchDataCodec<HydrolSurfaceRules.HeightNoiseThreshold> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec((data) -> {
            return data.group(Codec.FLOAT.fieldOf("min_threshold").forGetter(HydrolSurfaceRules.HeightNoiseThreshold::minThreshold),
                    Codec.FLOAT.fieldOf("max_threshold").forGetter(HydrolSurfaceRules.HeightNoiseThreshold::maxThreshold)).apply(data, HydrolSurfaceRules.HeightNoiseThreshold::new);
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
                    double d0 = NoiseCoverPlacement.HEIGHT_NOISE.getValue(this.context.blockX, this.context.blockZ, false);
                    return d0 >= HydrolSurfaceRules.HeightNoiseThreshold.this.minThreshold && d0 <= HydrolSurfaceRules.HeightNoiseThreshold.this.maxThreshold;
                }
            }

            return new NoiseThresholdCondition();
        }
    }

    protected record Temperature(float threshold, boolean greater) implements SurfaceRules.ConditionSource {
        static final KeyDispatchDataCodec<HydrolSurfaceRules.Temperature> CODEC = KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec((data) -> {
            return data.group(Codec.FLOAT.fieldOf("threshold").forGetter(HydrolSurfaceRules.Temperature::threshold),
                    Codec.BOOL.fieldOf("greater").forGetter(HydrolSurfaceRules.Temperature::greater)).apply(data, HydrolSurfaceRules.Temperature::new);
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
                    double temperature = HydrolDensityFunctions.temperature.compute(new DensityFunction.SinglePointContext(ruleContext.blockX, ruleContext.blockY, ruleContext.blockZ));
                    return greater() ? (temperature >= threshold()) : (temperature < threshold());
                }
            }

            return new NoiseThresholdCondition();
        }
    }
}
