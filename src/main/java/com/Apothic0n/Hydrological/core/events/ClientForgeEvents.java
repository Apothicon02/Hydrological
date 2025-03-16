package com.Apothic0n.Hydrological.core.events;

import com.Apothic0n.Hydrological.Hydrological;
import com.Apothic0n.Hydrological.api.HydrolDensityFunctions;
import com.Apothic0n.Hydrological.api.HydrolJsonReader;
import com.Apothic0n.Hydrological.api.HydrolMath;
import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;

@EventBusSubscriber(modid = Hydrological.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientForgeEvents {
    @SubscribeEvent
    public static void renderFog(ViewportEvent.RenderFog event) {
        if (HydrolJsonReader.customOverworldFog) {
            Minecraft instance = Minecraft.getInstance();
            ClientLevel level = instance.level;
            if (event.getType() == FogType.NONE && level != null && level.dimension().location().toString().contains("overworld")) {
                float distance = event.getNearPlaneDistance() / getTimeOffset(level, 32, 3);
                event.setNearPlaneDistance(distance);
                event.setFogShape(FogShape.SPHERE);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void computeFogColor(ViewportEvent.ComputeFogColor event) {
        if (HydrolJsonReader.customOverworldFog) {
            Minecraft instance = Minecraft.getInstance();
            ClientLevel level = instance.level;
            if (level != null && level.dimension().location().toString().contains("overworld") && event.getCamera().getFluidInCamera() == FogType.NONE) {
                float y = (float) event.getCamera().getPosition().y();
                float temp = level.getBiome(event.getCamera().getBlockPosition()).value().getBaseTemperature() * 3;
                if (HydrolDensityFunctions.temperature != null) { //smoother transitions if this is here
                    temp = (float) HydrolDensityFunctions.temperature.compute(new DensityFunction.SinglePointContext((int) event.getCamera().getPosition().x(), (int) y, (int) event.getCamera().getPosition().z())) * 3;
                }
                float brightness = (getTimeOffset(level, 5, 0)/10) + ((Math.max(-0.75f, Math.min(-0.7f, temp/3))+0.7f)*getTimeOffsetInv(level, 5f, 0));
                event.setRed(Math.min(0.95f, 0.7f - brightness));
                event.setGreen(Math.min(0.95f, 0.8f - brightness - (Math.max((Math.max(0.4f, Math.min(0.5f, temp/3))-0.4f)*0.66f, getTimeOffset(level, 0.2f, 0)))));
                event.setBlue(Math.min(1, 1 - brightness - (((Math.max(0.3f, Math.min(0.5f, temp/3))-0.4f)*0.66f)*getTimeOffsetInv(level, 5f, 0))));
            } else if (level != null && event.getCamera().getFluidInCamera() == FogType.WATER) {
                event.setRed((float) Mth.clamp((HydrolMath.getMiddleDouble(event.getRed(), 0.025) / 15), 0.025, 0.05));
                event.setGreen((float) Mth.clamp((HydrolMath.getMiddleDouble(event.getGreen(), 0.175) / 15), 0.175, 0.35));
                event.setBlue((float) Mth.clamp((HydrolMath.getMiddleDouble(event.getBlue(), 0.175) / 15), 0.175, 0.35));
            }
        }
    }

    private static float getTimeOffset(ClientLevel level, float scale, int outOfBounds) {
        float offset = outOfBounds;
        long dayTime = level.getDayTime();
        if (dayTime >= 11800 && dayTime <= 13000) { //dusk
            offset += HydrolMath.invLerp(dayTime, scale, 11800, 13000);
        } else if (dayTime > 13000 && dayTime < 22000) { //night
            offset += scale;
        } else if (dayTime >= 22000 && dayTime <= 23500) { //dawn
            offset += HydrolMath.invLerp(dayTime, scale, 22000, 23500);
        }
        return offset; //day
    }

    private static float getTimeOffsetInv(ClientLevel level, float scale, int outOfBounds) {
        float offset = outOfBounds;
        long dayTime = level.getDayTime();
        if (dayTime >= 11800 && dayTime <= 13000) { //dusk
            offset += HydrolMath.invLerp(dayTime, scale, 13000, 11800);
        } else if (dayTime < 11800 || dayTime > 23500) { //day
            offset += scale;
        } else if (dayTime >= 22000) { //dawn
            offset += HydrolMath.invLerp(dayTime, scale, 23500, 22000);
        }
        return offset; //night
    }
}
