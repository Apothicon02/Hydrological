package com.Apothic0n.Hydrological.api.biome.features.trunks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.Set;

public class GeneratedTrunk {
    public final Map<BlockPos, BlockState> map;
    public final Set<BlockPos> canopies;
    public final int height;
    public GeneratedTrunk(Map<BlockPos, BlockState> map, Set<BlockPos> canopies, int height) {
        this.map = map;
        this.canopies = canopies;
        this.height = height;
    }

    public Map<BlockPos, BlockState> getMap() {
        return this.map;
    }

    public Set<BlockPos> getCanopies() {
        return this.canopies;
    }
    public int getHeight() {
        return this.height;
    }
}
