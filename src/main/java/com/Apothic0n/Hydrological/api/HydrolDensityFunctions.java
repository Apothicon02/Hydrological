package com.Apothic0n.Hydrological.api;

import com.Apothic0n.Hydrological.Hydrological;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.longs.Long2DoubleOpenHashMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.joml.SimplexNoise;

import java.util.concurrent.ConcurrentHashMap;

import static com.Apothic0n.Hydrological.api.HydrolMath.progressBetweenInts;
import static com.Apothic0n.Hydrological.api.HydrolMath.unprogressBetweenInts;

public final class HydrolDensityFunctions {
    public static final DeferredRegister<Codec<? extends DensityFunction>> DENSITY_FUNCTION_TYPES = DeferredRegister.create(Registries.DENSITY_FUNCTION_TYPE, Hydrological.MODID);

    public static final RegistryObject<Codec<? extends DensityFunction>> FLOATING_BEACHES_DENSITY_FUNCTION_TYPE = DENSITY_FUNCTION_TYPES.register("floating_beaches", FloatingBeaches.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> FLOATING_ISLANDS_DENSITY_FUNCTION_TYPE = DENSITY_FUNCTION_TYPES.register("floating_islands", FloatingIslands.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> CUBICAL_SCALE_DENSITY_FUNCTION_TYPE = DENSITY_FUNCTION_TYPES.register("cubical_scale", CubicalScale.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> SCALABLE_NOISE_DENSITY_FUNCTION_TYPE = DENSITY_FUNCTION_TYPES.register("scalable_noise", ScalableNoise.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> TO_HEIGHTMAP_DENSITY_FUNCTION_TYPE = DENSITY_FUNCTION_TYPES.register("to_heightmap", ToHeightmap.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> POLARIZE_FUNCTION_TYPE = DENSITY_FUNCTION_TYPES.register("polarize", Polarize.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> RANGE_CHOICE_DENSITY_FUNCTION_TYPE = DENSITY_FUNCTION_TYPES.register("range_choice", RangeChoice.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> COMPARE_DENSITY_FUNCTION_TYPE = DENSITY_FUNCTION_TYPES.register("compare", Compare.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> GRADIENT_DENSITY_FUNCTION_TYPE = DENSITY_FUNCTION_TYPES.register("gradient", Gradient.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> STRETCH_DENSITY_FUNCTION_TYPE = DENSITY_FUNCTION_TYPES.register("stretch", Stretch.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> SHIFT_DENSITY_FUNCTION_TYPE = DENSITY_FUNCTION_TYPES.register("shift", Shift.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> FLATTEN_DENSITY_FUNCTION_TYPE = DENSITY_FUNCTION_TYPES.register("flatten", Flatten.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> STORE_TEMPERATURE_DENSITY_FUNCTION_TYPE = DENSITY_FUNCTION_TYPES.register("store_temperature", StoreTemperature.CODEC::codec);

    public static void register(IEventBus eventBus) {
        DENSITY_FUNCTION_TYPES.register(eventBus);
    }

    public static final Long2DoubleOpenHashMap heightmap = new Long2DoubleOpenHashMap();
    public static DensityFunction temperature;
    public static boolean isFloatingIslands = false;

    protected record FloatingBeaches(DensityFunction input) implements DensityFunction {
        private static final MapCodec<FloatingBeaches> DATA_CODEC = RecordCodecBuilder.mapCodec((data) -> {
            return data.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(FloatingBeaches::input)).apply(data, FloatingBeaches::new);
        });
        public static final KeyDispatchDataCodec<FloatingBeaches> CODEC = HydrolDensityFunctions.makeCodec(DATA_CODEC);

        @Override
        public double compute(@NotNull FunctionContext context) {
            int x = context.blockX();
            int y = context.blockY();
            int z = context.blockZ();
            double floor = 36 - (Math.abs(SimplexNoise.noise(x*0.0007F, z*0.0007F)) * 128);
            if (floor < y) {
                return 0.24D;
            } else {
                return 0.2D;
            }
        }

        @Override
        public void fillArray(double @NotNull [] densities, ContextProvider context) {
            context.fillAllDirectly(densities, this);
        }

        @Override
        public @NotNull DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new FloatingBeaches(this.input().mapAll(visitor)));
        }

        @Override
        public double minValue() {
            return -1875000d;
        }

        @Override
        public double maxValue() {
            return 1875000d;
        }

        @Override
        public KeyDispatchDataCodec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }
    protected record FloatingIslands(DensityFunction input, boolean hollow) implements DensityFunction {
        private static final MapCodec<FloatingIslands> DATA_CODEC = RecordCodecBuilder.mapCodec((data) -> {
            return data.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(FloatingIslands::input), Codec.BOOL.fieldOf("hollow").forGetter(FloatingIslands::hollow)).apply(data, FloatingIslands::new);
        });
        public static final KeyDispatchDataCodec<FloatingIslands> CODEC = HydrolDensityFunctions.makeCodec(DATA_CODEC);

        @Override
        public double compute(@NotNull FunctionContext context) {
            isFloatingIslands = true;
            double floatingIsland;
            int x = context.blockX();
            int y = context.blockY();
            int z = context.blockZ();
            if (y < 256) {
                if (y > -32) {
                    double airPart = SimplexNoise.noise(x * 0.02F, y * 0.005F, z * 0.02F);
                    double solidPart = SimplexNoise.noise(x * 0.0024F, y * 0.0016F, z * 0.0024F);
                    if (context.blockY() > 162) {
                        floatingIsland = Math.min(0.5 - (airPart * -1 + HydrolMath.gradient(y, 164, 292, -1, 1.5F)),
                                solidPart * -1 + (HydrolMath.gradient(y, 228, 356, 0.75F, 0.5F) - (2 * (0.1 + HydrolMath.gradient(y, 169, 259, 0.76F, 0F)))));
                    } else {
                        floatingIsland = Math.min(0.5 - (airPart + HydrolMath.gradient(y, 64, 192, -1, 1.5F)),
                                solidPart + (HydrolMath.gradient(y, 128, 256, 0.75F, 0.5F) - (2 * (0.1 + HydrolMath.gradient(y, 69, 159, 0.76F, 0F)))));
                    }
                    double caves = 0;
                    if (hollow()) {
                        caves = Math.min(0, ((floatingIsland - 0.2) * -5));
                    }
                    return caves + floatingIsland + input().compute(context);
                } else {
                    double floor = (Math.abs(SimplexNoise.noise(x * 0.007F, z * 0.007F)) + (HydrolMath.gradient(y, -64, -50, 0.35F, 0.25F) - (2 * (0.1 + HydrolMath.gradient(y, -52, -36, 0.76F, 0F))))) * HydrolMath.gradient(y, -64, -36, 1F, 0F);
                    return floor + input().compute(context);
                }
            } else {
                return input().compute(context);
            }
        }

        @Override
        public void fillArray(double @NotNull [] densities, ContextProvider context) {
            context.fillAllDirectly(densities, this);
        }

        @Override
        public @NotNull DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new FloatingIslands(this.input().mapAll(visitor), hollow()));
        }

        @Override
        public double minValue() {
            return -1875000d;
        }

        @Override
        public double maxValue() {
            return 1875000d;
        }

        @Override
        public KeyDispatchDataCodec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }
    protected record Polarize(DensityFunction input, DensityFunction polar, DensityFunction negativeMul) implements DensityFunction {
        private static final MapCodec<Polarize> DATA_CODEC = RecordCodecBuilder.mapCodec((data) -> {
            return data.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(Polarize::input),
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("polar").forGetter(Polarize::polar),
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("negative_mul").forGetter(Polarize::negativeMul)).apply(data, Polarize::new);
        });
        public static final KeyDispatchDataCodec<Polarize> CODEC = HydrolDensityFunctions.makeCodec(DATA_CODEC);

        @Override
        public double compute(@NotNull FunctionContext context) {
            double pole = 1;
            if (polar().compute(context) < 0) {
                pole = negativeMul().compute(context);
            }
            return input.compute(context)*pole;
        }

        @Override
        public void fillArray(double @NotNull [] densities, ContextProvider context) {
            context.fillAllDirectly(densities, this);
        }

        @Override
        public @NotNull DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new Polarize(input().mapAll(visitor), polar().mapAll(visitor), negativeMul().mapAll(visitor)));
        }

        @Override
        public double minValue() {
            return -1875000d;
        }

        @Override
        public double maxValue() {
            return 1875000d;
        }

        @Override
        public KeyDispatchDataCodec<? extends DensityFunction> codec() {
            return CODEC;
        }

    }
    protected record RangeChoice(DensityFunction input, DensityFunction minInclusive, DensityFunction maxExclusive, DensityFunction whenInRange, DensityFunction whenOutOfRange) implements DensityFunction {
        private static final MapCodec<RangeChoice> DATA_CODEC = RecordCodecBuilder.mapCodec((data) -> {
            return data.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(RangeChoice::input), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("min_inclusive").forGetter(RangeChoice::minInclusive),
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("max_exclusive").forGetter(RangeChoice::maxExclusive), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("when_in_range").forGetter(RangeChoice::whenInRange),
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("when_out_of_range").forGetter(RangeChoice::whenOutOfRange)).apply(data, RangeChoice::new);
        });
        public static final KeyDispatchDataCodec<RangeChoice> CODEC = HydrolDensityFunctions.makeCodec(DATA_CODEC);

        @Override
        public double compute(@NotNull FunctionContext context) {
            double d0 = this.input.compute(context);
            return d0 >= this.minInclusive.compute(context) && d0 < this.maxExclusive.compute(context) ? this.whenInRange.compute(context) : this.whenOutOfRange.compute(context);
        }

        @Override
        public void fillArray(double @NotNull [] densities, ContextProvider context) {
            context.fillAllDirectly(densities, this);
        }

        @Override
        public @NotNull DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new RangeChoice(input().mapAll(visitor), minInclusive().mapAll(visitor), maxExclusive().mapAll(visitor), whenInRange().mapAll(visitor), whenOutOfRange().mapAll(visitor)));
        }

        @Override
        public double minValue() {
            return -1875000d;
        }

        @Override
        public double maxValue() {
            return 1875000d;
        }

        @Override
        public KeyDispatchDataCodec<? extends DensityFunction> codec() {
            return CODEC;
        }

    }
    protected record Compare(DensityFunction less, DensityFunction greater, DensityFunction correct, DensityFunction incorrect) implements DensityFunction {
        private static final MapCodec<Compare> DATA_CODEC = RecordCodecBuilder.mapCodec((data) -> {
            return data.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("less").forGetter(Compare::less), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("greater").forGetter(Compare::greater),
                    DensityFunction.HOLDER_HELPER_CODEC.fieldOf("correct").forGetter(Compare::correct), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("incorrect").forGetter(Compare::incorrect)).apply(data, Compare::new);
        });
        public static final KeyDispatchDataCodec<Compare> CODEC = HydrolDensityFunctions.makeCodec(DATA_CODEC);

        @Override
        public double compute(@NotNull FunctionContext context) {
            if (greater().compute(context) > less().compute(context)) {
                return correct().compute(context);
            } else {
                return incorrect().compute(context);
            }
        }

        @Override
        public void fillArray(double @NotNull [] densities, ContextProvider context) {
            context.fillAllDirectly(densities, this);
        }

        @Override
        public @NotNull DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new Compare(less().mapAll(visitor), greater().mapAll(visitor), correct().mapAll(visitor), incorrect().mapAll(visitor)));
        }

        @Override
        public double minValue() {
            return -1875000d;
        }

        @Override
        public double maxValue() {
            return 1875000d;
        }

        @Override
        public KeyDispatchDataCodec<? extends DensityFunction> codec() {
            return CODEC;
        }

    }
    protected record Gradient(DensityFunction from_y, DensityFunction to_y, DensityFunction from_value, DensityFunction to_value) implements DensityFunction {
        private static final MapCodec<Gradient> DATA_CODEC = RecordCodecBuilder.mapCodec((data) -> {
            return data.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("from_y").forGetter(Gradient::from_y), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("to_y").forGetter(Gradient::to_y), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("from_value").forGetter(Gradient::from_value), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("to_value").forGetter(Gradient::to_value)).apply(data, Gradient::new);
        });
        public static final KeyDispatchDataCodec<Gradient> CODEC = HydrolDensityFunctions.makeCodec(DATA_CODEC);

        @Override
        public double compute(@NotNull FunctionContext context) {
            return HydrolMath.gradient(context.blockY(), (int) from_y().compute(context), (int) to_y().compute(context), (float) from_value().compute(context), (float) to_value().compute(context));
        }

        @Override
        public void fillArray(double @NotNull [] densities, ContextProvider context) {
            context.fillAllDirectly(densities, this);
        }

        @Override
        public @NotNull DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new Gradient(from_y().mapAll(visitor), to_y().mapAll(visitor), from_value().mapAll(visitor), to_value().mapAll(visitor)));
        }

        @Override
        public double minValue() {
            return -1875000d;
        }

        @Override
        public double maxValue() {
            return 1875000d;
        }

        @Override
        public KeyDispatchDataCodec<? extends DensityFunction> codec() {
            return CODEC;
        }

    }
    protected record Stretch(DensityFunction input, DensityFunction fromY, boolean up) implements DensityFunction {
        private static final MapCodec<Stretch> DATA_CODEC = RecordCodecBuilder.mapCodec((data) -> {
            return data.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(Stretch::input), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("from_y").forGetter(Stretch::fromY), Codec.BOOL.fieldOf("up").forGetter(Stretch::up)).apply(data, Stretch::new);
        });
        public static final KeyDispatchDataCodec<Stretch> CODEC = HydrolDensityFunctions.makeCodec(DATA_CODEC);

        @Override
        public double compute(@NotNull FunctionContext context) {
            int y = context.blockY();
            int from = (int) fromY().compute(context);
            if ((y > from && up()) || (y < from && !up())) {
                y = from;
            }
            return input.compute(new DensityFunction.SinglePointContext(context.blockX(), y, context.blockZ()));
        }

        @Override
        public void fillArray(double @NotNull [] densities, ContextProvider context) {
            context.fillAllDirectly(densities, this);
        }

        @Override
        public @NotNull DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new Stretch(input().mapAll(visitor), fromY().mapAll(visitor), up()));
        }

        @Override
        public double minValue() {
            return -1875000d;
        }

        @Override
        public double maxValue() {
            return 1875000d;
        }

        @Override
        public KeyDispatchDataCodec<? extends DensityFunction> codec() {
            return CODEC;
        }

    }
    protected record Shift(DensityFunction input, DensityFunction shiftX, DensityFunction shiftY, DensityFunction shiftZ) implements DensityFunction {
        private static final MapCodec<Shift> DATA_CODEC = RecordCodecBuilder.mapCodec((data) -> {
            return data.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(Shift::input), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_x").forGetter(Shift::shiftX), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_y").forGetter(Shift::shiftY), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_z").forGetter(Shift::shiftZ)).apply(data, Shift::new);
        });
        public static final KeyDispatchDataCodec<Shift> CODEC = HydrolDensityFunctions.makeCodec(DATA_CODEC);

        @Override
        public double compute(@NotNull FunctionContext context) {
            return input.compute(new DensityFunction.SinglePointContext((int) (context.blockX()+shiftX().compute(context)), (int) (context.blockY()+shiftY().compute(context)), (int) (context.blockZ()+shiftZ().compute(context))));
        }

        @Override
        public void fillArray(double @NotNull [] densities, ContextProvider context) {
            context.fillAllDirectly(densities, this);
        }

        @Override
        public @NotNull DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new Shift(input().mapAll(visitor), shiftX().mapAll(visitor), shiftY().mapAll(visitor), shiftZ().mapAll(visitor)));
        }

        @Override
        public double minValue() {
            return -1875000d;
        }

        @Override
        public double maxValue() {
            return 1875000d;
        }

        @Override
        public KeyDispatchDataCodec<? extends DensityFunction> codec() {
            return CODEC;
        }

    }
    protected record Flatten(DensityFunction input, DensityFunction atY) implements DensityFunction {
        private static final MapCodec<Flatten> DATA_CODEC = RecordCodecBuilder.mapCodec((data) -> {
            return data.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(Flatten::input), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("at_y").forGetter(Flatten::atY)).apply(data, Flatten::new);
        });
        public static final KeyDispatchDataCodec<Flatten> CODEC = HydrolDensityFunctions.makeCodec(DATA_CODEC);

        @Override
        public double compute(@NotNull FunctionContext context) {
            return input.compute(new DensityFunction.SinglePointContext(context.blockX(), (int) atY().compute(context), context.blockZ()));
        }

        @Override
        public void fillArray(double @NotNull [] densities, ContextProvider context) {
            context.fillAllDirectly(densities, this);
        }

        @Override
        public @NotNull DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new Flatten(this.input().mapAll(visitor), atY().mapAll(visitor)));
        }

        @Override
        public double minValue() {
            return -1875000d;
        }

        @Override
        public double maxValue() {
            return 1875000d;
        }

        @Override
        public KeyDispatchDataCodec<? extends DensityFunction> codec() {
            return CODEC;
        }

    }
    protected record StoreTemperature(DensityFunction input) implements DensityFunction {
        private static final MapCodec<StoreTemperature> DATA_CODEC = RecordCodecBuilder.mapCodec((data) -> {
            return data.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(StoreTemperature::input)).apply(data, StoreTemperature::new);
        });
        public static final KeyDispatchDataCodec<StoreTemperature> CODEC = HydrolDensityFunctions.makeCodec(DATA_CODEC);

        @Override
        public double compute(@NotNull FunctionContext context) {
            if (temperature == null) {
                temperature = input();
            }
            return input.compute(context);
        }

        @Override
        public void fillArray(double @NotNull [] densities, ContextProvider context) {
            context.fillAllDirectly(densities, this);
        }

        @Override
        public @NotNull DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new StoreTemperature(this.input().mapAll(visitor)));
        }

        @Override
        public double minValue() {
            return -1875000d;
        }

        @Override
        public double maxValue() {
            return 1875000d;
        }

        @Override
        public KeyDispatchDataCodec<? extends DensityFunction> codec() {
            return CODEC;
        }

    }
    protected record ToHeightmap(int minY, int maxY, DensityFunction input) implements DensityFunction {
        private static final MapCodec<ToHeightmap> DATA_CODEC = RecordCodecBuilder.mapCodec((data) -> {
            return data.group(Codec.INT.fieldOf("min_y").forGetter(ToHeightmap::minY), Codec.INT.fieldOf("max_y").forGetter(ToHeightmap::maxY), DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(ToHeightmap::input)).apply(data, ToHeightmap::new);
        });
        public static final KeyDispatchDataCodec<ToHeightmap> CODEC = HydrolDensityFunctions.makeCodec(DATA_CODEC);

        @Override
        public double compute(@NotNull FunctionContext context) {
            int x = context.blockX();
            int y = context.blockY();
            int z = context.blockZ();
            long key = ChunkPos.asLong(x/4, z/4);
            double storedValue;
            synchronized (heightmap) {
                heightmap.defaultReturnValue(Double.NaN);
                storedValue = heightmap.get(key);
            }
            if (!Double.isNaN(storedValue)) {
                int newY = unprogressBetweenInts(minY(), maxY(), storedValue);
                return newY-30 > y ? -1 : storedValue;
            } else {
                for (int newY = maxY(); newY > minY(); newY -= 4) {
                    double value = input().compute(new SinglePointContext(x, newY, z));
                    if (value > 0.0D) {
                        double returnValue = progressBetweenInts(minY(), maxY(), newY);
                        synchronized (heightmap) {
                            heightmap.put(key, returnValue);
                        }
                        return newY-20 > y ? -1 : returnValue;
                    }
                }
            }
            synchronized (heightmap) {
                heightmap.put(key, 0D);
            }
            return minY()-30 > y ? -1 : 0;
        }

        @Override
        public void fillArray(double @NotNull [] densities, ContextProvider context) {
            context.fillAllDirectly(densities, this);
        }

        @Override
        public @NotNull DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new ToHeightmap(minY(), maxY(), this.input().mapAll(visitor)));
        }

        @Override
        public double minValue() {
            return -1875000d;
        }

        @Override
        public double maxValue() {
            return 1875000d;
        }

        @Override
        public KeyDispatchDataCodec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }
    protected record CubicalScale(DensityFunction input) implements DensityFunction {
        private static final MapCodec<CubicalScale> DATA_CODEC = RecordCodecBuilder.mapCodec((data) -> {
            return data.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(CubicalScale::input)).apply(data, CubicalScale::new);
        });
        public static final KeyDispatchDataCodec<CubicalScale> CODEC = HydrolDensityFunctions.makeCodec(DATA_CODEC);

        @Override
        public double compute(@NotNull FunctionContext context) {
            int scale = HydrolJsonReader.cubicalTerrainScale;
            int x = ((int)(context.blockX()/scale))*scale;
            int y = ((int)((context.blockY() - ((scale ^ 1) == scale + 1 ? scale/2 : 0))/scale))*scale;
            int z = ((int)(context.blockZ()/scale))*scale;
            return input().compute(new SinglePointContext(x, y, z));
        }

        @Override
        public void fillArray(double @NotNull [] densities, ContextProvider context) {
            context.fillAllDirectly(densities, this);
        }

        @Override
        public @NotNull DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new CubicalScale(this.input().mapAll(visitor)));
        }

        @Override
        public double minValue() {
            return -1875000d;
        }

        @Override
        public double maxValue() {
            return 1875000d;
        }

        @Override
        public KeyDispatchDataCodec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }
    protected record ScalableNoise(DensityFunction.NoiseHolder noise, double xzScale, double yScale) implements DensityFunction {
        private static final MapCodec<ScalableNoise> DATA_CODEC = RecordCodecBuilder.mapCodec((data) -> {
            return data.group(DensityFunction.NoiseHolder.CODEC.fieldOf("noise").forGetter(ScalableNoise::noise),
                    Codec.DOUBLE.fieldOf("xz_scale").forGetter(ScalableNoise::xzScale),
                    Codec.DOUBLE.fieldOf("y_scale").forGetter(ScalableNoise::yScale)).apply(data, ScalableNoise::new);
        });
        public static final KeyDispatchDataCodec<ScalableNoise> CODEC = HydrolDensityFunctions.makeCodec(DATA_CODEC);

        @Override
        public double compute(@NotNull FunctionContext context) {
            float scale = HydrolJsonReader.noiseScale;
            return this.noise.getValue((double)context.blockX() * this.xzScale * scale, (double)context.blockY() * this.yScale, (double)context.blockZ() * this.xzScale * scale);
        }

        @Override
        public void fillArray(double @NotNull [] densities, ContextProvider context) {
            context.fillAllDirectly(densities, this);
        }

        @Override
        public @NotNull DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new ScalableNoise(visitor.visitNoise(this.noise), this.xzScale, this.yScale));
        }

        @Override
        public double minValue() {
            return -this.maxValue();
        }

        @Override
        public double maxValue() {
            return this.noise.maxValue();
        }

        @Override
        public KeyDispatchDataCodec<? extends DensityFunction> codec() {
            return CODEC;
        }
    }

    static <O> KeyDispatchDataCodec<O> makeCodec(MapCodec<O> codec) {
        return KeyDispatchDataCodec.of(codec);
    }
}
