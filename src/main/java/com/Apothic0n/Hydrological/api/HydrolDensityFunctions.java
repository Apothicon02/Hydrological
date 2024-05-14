package com.Apothic0n.Hydrological.api;

import com.Apothic0n.Hydrological.Hydrological;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.joml.SimplexNoise;

import java.util.concurrent.ConcurrentHashMap;

import static com.Apothic0n.Hydrological.api.HydrolMath.progressBetweenInts;

public final class HydrolDensityFunctions {
    public static final DeferredRegister<Codec<? extends DensityFunction>> DENSITY_FUNCTION_TYPES = DeferredRegister.create(Registries.DENSITY_FUNCTION_TYPE, Hydrological.MODID);

    public static final RegistryObject<Codec<? extends DensityFunction>> NORMAL_TERRAIN_DENSITY_FUNCTION_TYPE = DENSITY_FUNCTION_TYPES.register("normal_terrain", NormalTerrain.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> FLOATING_BEACHES_DENSITY_FUNCTION_TYPE = DENSITY_FUNCTION_TYPES.register("floating_beaches", FloatingBeaches.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> FLOATING_ISLANDS_DENSITY_FUNCTION_TYPE = DENSITY_FUNCTION_TYPES.register("floating_islands", FloatingIslands.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> TO_HEIGHTMAP_DENSITY_FUNCTION_TYPE = DENSITY_FUNCTION_TYPES.register("to_heightmap", ToHeightmap.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> SHIFT_DENSITY_FUNCTION_TYPE = DENSITY_FUNCTION_TYPES.register("shift", Shift.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> FLATTEN_DENSITY_FUNCTION_TYPE = DENSITY_FUNCTION_TYPES.register("flatten", Flatten.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> STORE_TEMPERATURE_DENSITY_FUNCTION_TYPE = DENSITY_FUNCTION_TYPES.register("store_temperature", StoreTemperature.CODEC::codec);
    public static final RegistryObject<Codec<? extends DensityFunction>> STORE_HUMIDITY_DENSITY_FUNCTION_TYPE = DENSITY_FUNCTION_TYPES.register("store_humidity", StoreHumidity.CODEC::codec);

    public static void register(IEventBus eventBus) {
        DENSITY_FUNCTION_TYPES.register(eventBus);
    }

    public static ConcurrentHashMap<String, Double> heightmap = new ConcurrentHashMap<>();
    public static DensityFunction temperature;
    public static DensityFunction humidity;
    public static boolean isFloatingIslands = false;

    protected record NormalTerrain(DensityFunction input) implements DensityFunction {
        private static final MapCodec<NormalTerrain> DATA_CODEC = RecordCodecBuilder.mapCodec((data) -> {
            return data.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(NormalTerrain::input)).apply(data, NormalTerrain::new);
        });
        public static final KeyDispatchDataCodec<NormalTerrain> CODEC = HydrolDensityFunctions.makeCodec(DATA_CODEC);

        @Override
        public double compute(@NotNull FunctionContext context) {
            int x = context.blockX();
            int y = context.blockY();
            int z = context.blockZ();
            SinglePointContext blockPos = new DensityFunction.SinglePointContext(x, y, z);
            double temp = temperature.compute(blockPos);
            double humi = humidity.compute(blockPos);
            double computedInput = input.compute(context);
            double mountainous = Math.abs(humi);
            mountainous = mountainous * mountainous;
            mountainous *= 3;
            double height = (Math.max(0, computedInput-0.25)*(Math.min(0.25, Math.max(0, computedInput))*10))*(computedInput*(computedInput*HydrolMath.gradient(y, 64, 86, 3F, 1F)));
            float scale = (float) ((0.01F-Math.max(0, (height*0.07)-0.01F))*(0.2F+Math.abs(temp-0.2)));
            double baseNoise = SimplexNoise.noise(x*scale, z*scale);
            double floor = ((baseNoise + (Math.max(0, height-0.03)*5)) + (HydrolMath.gradient(y, -64, (int) (32+(height*128)), 0.75F, 0.5F) - (2 * (0.1 + HydrolMath.gradient(y, 56, 320, (float) (0.76F-mountainous), 0F))))) +
                    (((1 + (Math.max(0, height-0.04)*5)) + (HydrolMath.gradient(y, -64, (int) (32+(height*112)), 0.75F, 0.5F) - (2 * (0.1 + HydrolMath.gradient(y, 56, 244, (float) (0.76F-mountainous), 0F))))) * HydrolMath.gradient(y, 64, 100, 1.5F, 0.5F));
            return floor*(height*5);
        }

        @Override
        public void fillArray(double @NotNull [] densities, ContextProvider context) {
            context.fillAllDirectly(densities, this);
        }

        @Override
        public @NotNull DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new NormalTerrain(this.input().mapAll(visitor)));
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
    protected record Shift(DensityFunction input, int shiftX, int shiftY, int shiftZ) implements DensityFunction {
        private static final MapCodec<Shift> DATA_CODEC = RecordCodecBuilder.mapCodec((data) -> {
            return data.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(Shift::input), Codec.INT.fieldOf("shift_x").forGetter(Shift::shiftX), Codec.INT.fieldOf("shift_y").forGetter(Shift::shiftY), Codec.INT.fieldOf("shift_z").forGetter(Shift::shiftZ)).apply(data, Shift::new);
        });
        public static final KeyDispatchDataCodec<Shift> CODEC = HydrolDensityFunctions.makeCodec(DATA_CODEC);

        @Override
        public double compute(@NotNull FunctionContext context) {
            return input.compute(new DensityFunction.SinglePointContext(context.blockX()+shiftX(), context.blockY()+shiftY(), context.blockZ()+shiftZ()));
        }

        @Override
        public void fillArray(double @NotNull [] densities, ContextProvider context) {
            context.fillAllDirectly(densities, this);
        }

        @Override
        public @NotNull DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new Shift(this.input().mapAll(visitor), shiftX(), shiftY(), shiftZ()));
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

    protected record Flatten(DensityFunction input, int atY) implements DensityFunction {
        private static final MapCodec<Flatten> DATA_CODEC = RecordCodecBuilder.mapCodec((data) -> {
            return data.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(Flatten::input), Codec.INT.fieldOf("at_y").forGetter(Flatten::atY)).apply(data, Flatten::new);
        });
        public static final KeyDispatchDataCodec<Flatten> CODEC = HydrolDensityFunctions.makeCodec(DATA_CODEC);

        @Override
        public double compute(@NotNull FunctionContext context) {
            return input.compute(new DensityFunction.SinglePointContext(context.blockX(), atY(), context.blockZ()));
        }

        @Override
        public void fillArray(double @NotNull [] densities, ContextProvider context) {
            context.fillAllDirectly(densities, this);
        }

        @Override
        public @NotNull DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new Flatten(this.input().mapAll(visitor), atY()));
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
    protected record StoreHumidity(DensityFunction input) implements DensityFunction {
        private static final MapCodec<StoreHumidity> DATA_CODEC = RecordCodecBuilder.mapCodec((data) -> {
            return data.group(DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(StoreHumidity::input)).apply(data, StoreHumidity::new);
        });
        public static final KeyDispatchDataCodec<StoreHumidity> CODEC = HydrolDensityFunctions.makeCodec(DATA_CODEC);

        @Override
        public double compute(@NotNull FunctionContext context) {
            if (humidity == null) {
                humidity = input();
            }
            return input.compute(context);
        }

        @Override
        public void fillArray(double @NotNull [] densities, ContextProvider context) {
            context.fillAllDirectly(densities, this);
        }

        @Override
        public @NotNull DensityFunction mapAll(Visitor visitor) {
            return visitor.apply(new StoreHumidity(this.input().mapAll(visitor)));
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
            int z = context.blockZ();
            String id = x+"/"+z;
            Double storedValue = heightmap.get(id);
            if (storedValue != null) {
                return storedValue;
            } else {
                for (int newY = maxY(); newY > minY(); newY--) {
                    double value = input().compute(new SinglePointContext(x, newY, z));
                    if (value > 0) {
                        double returnValue = progressBetweenInts(minY(), maxY(), newY);
                        heightmap.put(id, returnValue);
                        return returnValue;
                    }
                }
            }
            heightmap.put(id, 0D);
            return 0;
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

    static <O> KeyDispatchDataCodec<O> makeCodec(MapCodec<O> codec) {
        return KeyDispatchDataCodec.of(codec);
    }
}
