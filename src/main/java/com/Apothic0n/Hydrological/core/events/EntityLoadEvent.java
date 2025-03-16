package com.Apothic0n.Hydrological.core.events;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;

public final class EntityLoadEvent extends Event {
    public EntityLoadEvent(Entity entity, Level level) {
        this.entity = entity;
        this.level = level;
    }
    public final Entity entity;
    public final Level level;
}