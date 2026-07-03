package com.Apothic0n.Hydrological.core.objects;

import com.Apothic0n.Hydrological.Hydrological;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class HydrolParticleTypes {
    private HydrolParticleTypes() {}

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, Hydrological.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> OAK_LEAVES = PARTICLE_TYPES.register("oak_leaves", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DARK_OAK_LEAVES = PARTICLE_TYPES.register("dark_oak_leaves", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> BIRCH_LEAVES = PARTICLE_TYPES.register("birch_leaves", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPRUCE_LEAVES = PARTICLE_TYPES.register("spruce_leaves", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> JUNGLE_LEAVES = PARTICLE_TYPES.register("jungle_leaves", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ACACIA_LEAVES = PARTICLE_TYPES.register("acacia_leaves", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MANGROVE_LEAVES = PARTICLE_TYPES.register("mangrove_leaves", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> AZALEA_LEAVES = PARTICLE_TYPES.register("azalea_leaves", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FLOWERING_AZALEA_LEAVES = PARTICLE_TYPES.register("flowering_azalea_leaves", () -> new SimpleParticleType(false));
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> FIRE_FLIES = PARTICLE_TYPES.register("fire_flies", () -> new SimpleParticleType(false));

}
