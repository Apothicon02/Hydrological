package com.Apothic0n.Hydrological.core.events;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

public final class HydrolEventHandler {
    public static EntityLoadEvent entityLoadEvent(Entity entity, Level level) {
        EntityLoadEvent event = new EntityLoadEvent(entity, level);
        MinecraftForge.EVENT_BUS.post(event);
        return event;
    }
}