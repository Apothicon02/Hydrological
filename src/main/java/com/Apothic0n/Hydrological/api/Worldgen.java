package com.Apothic0n.Hydrological.api;

import com.Apothic0n.Hydrological.noise.Noises;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.Random;

import static com.Apothic0n.Hydrological.api.HydrolMath.distance;

public class Worldgen {
    public static int size = 1024;
    public static int halfSize = 1024/2;
    public static int height = 320;
    public static int seaLevel = 62;
    public static BlockState[] blocks = new BlockState[size*size*height];
    public static short[] heightmap = new short[size*size];
    public static short[] surfaceHeightmap = new short[size*size];
    public static Random seededRand = new Random(35311350L);

    public static int condensePos(int x, int z) {
        return (x * size) + z;
    }
    public static int condensePos(int x, int y, int z) {
        return ((((z*height)+y)*size)+x);
    }

    public static void setBlock(int x, int y, int z, BlockState state) {
        blocks[condensePos(x, y, z)] = state;
    }
    public static BlockState getBlock(int x, int y, int z) {
        return blocks[condensePos(x, y, z)];
    }

    public static void generate() {
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                float basePerlinNoise = (Noises.COHERERENT_NOISE.sample(x, z)+0.5f)/2;
                float baseCellularNoise = Noises.CELLULAR_NOISE.sample(x, z)/2;
                float centDist = (float) (distance(x, z, size/2, size/2)/halfSize);
                float centDistExp = (Math.max(0.5f, centDist)-0.5f);
                centDistExp *= centDistExp;
                int surface = (int)(((200*(Math.max(0.1f, baseCellularNoise)*basePerlinNoise))+70)-(centDistExp*300));
                surface = Math.max(8, surface);
                heightmap[condensePos(x, z)] = (short)(surface);
                for (int y = surface; y >= 0; y--) {
                    setBlock(x, y, z, Blocks.DIRT.defaultBlockState());
                }
            }
        }

        surfaceHeightmap = Arrays.copyOf(heightmap, heightmap.length);

        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                int maxSteepness = 0;
                int minNeighborY = height - 1;
                int condensedPos = condensePos(x, z);
                int surface = heightmap[condensedPos];
                for (int pos : new int[]{condensePos(Math.min(size - 1, x + 3), z), condensePos(Math.max(0, x - 3), z), condensePos(x, Math.min(size - 1, z + 3)), condensePos(x, Math.max(0, z - 3)),
                        condensePos(Math.max(0, x - 3), Math.max(0, z - 3)), condensePos(Math.min(size - 1, x + 3), Math.max(0, z - 3)), condensePos(Math.max(0, x - 3), Math.min(size - 1, z + 3)), condensePos(Math.min(size - 1, x + 3), Math.min(size - 1, z + 3))}) {
                    int nY = heightmap[pos];
                    minNeighborY = Math.min(minNeighborY, nY);
                    int steepness = Math.abs(surface - nY);
                    maxSteepness = Math.max(maxSteepness, steepness);
                }
                boolean flat = maxSteepness < 3;
                if (flat) {
                    if (surface < seaLevel) {
                        //setBlock(x, surface+1, z, Blocks.SEAGRASS.defaultBlockState());
                    } else if (surface < seaLevel+3) {
                        setBlock(x, surface, z, Blocks.SAND.defaultBlockState());
                        for (int newY = surface-1; newY >= surface-5; newY--) {
                            setBlock(x, newY, z, Blocks.SANDSTONE.defaultBlockState());
                        }
                    } else {
                        setBlock(x, surface, z, Blocks.GRASS_BLOCK.defaultBlockState());
//                        double flowerChance = seededRand.nextDouble();
//                        int decoration = 4 + (flowerChance > 0.95f ? (flowerChance > 0.97f ? 14 : 1) : 0);
//                        setBlock(x, surface+1, z, decoration == 4 ? Blocks.SHORT_GRASS.defaultBlockState() : (decoration == 5 ? Blocks.POPPY.defaultBlockState() : Blocks.BLUE_ORCHID.defaultBlockState()));
                    }
                } else {
                    for (int newY = surface; newY >= surface-5; newY--) {
                        setBlock(x, newY, z, Blocks.GRAVEL.defaultBlockState());
                    }
                }
            }
        }
    }
}
