package com.Apothic0n.Hydrological.core.events;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import static net.neoforged.neoforge.common.NeoForge.EVENT_BUS;

public final class HydrolEventHandler {
    public static EntityLoadEvent entityLoadEvent(Entity entity, Level level) {
        EntityLoadEvent event = new EntityLoadEvent(entity, level);
        EVENT_BUS.post(event);
        return event;
    }
}