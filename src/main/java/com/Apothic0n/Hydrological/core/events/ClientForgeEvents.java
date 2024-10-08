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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Hydrological.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents {
    @SubscribeEvent
    public static void renderFog(ViewportEvent.RenderFog event) {
        if (HydrolJsonReader.customOverworldFog) {
            Minecraft instance = Minecraft.getInstance();
            ClientLevel level = instance.level;
            if (event.getType() == FogType.NONE && level != null && level.dimension().location().toString().contains("overworld")) {
                float distance = event.getNearPlaneDistance() / getTimeOffset(level, 32);
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
                float temp = level.getBiome(event.getCamera().getBlockPosition()).get().getBaseTemperature() * 3;
                if (HydrolDensityFunctions.temperature != null) { //smoother transitions if this is here
                    temp = (float) HydrolDensityFunctions.temperature.compute(new DensityFunction.SinglePointContext((int) event.getCamera().getPosition().x(), (int) y, (int) event.getCamera().getPosition().z())) * 3;
                }
                event.setRed(event.getRed() + (((temp - 0.8F) / 25)));
                event.setGreen(event.getGreen() - (((temp - 0.8F) / 20)));
                event.setBlue(event.getBlue() - (((temp - 0.8F) / 15)));
            } else if (level != null && event.getCamera().getFluidInCamera() == FogType.WATER) {
                event.setRed((float) Mth.clamp((HydrolMath.getMiddleDouble(event.getRed(), 0.025) / 15), 0.025, 0.05));
                event.setGreen((float) Mth.clamp((HydrolMath.getMiddleDouble(event.getGreen(), 0.175) / 15), 0.175, 0.35));
                event.setBlue((float) Mth.clamp((HydrolMath.getMiddleDouble(event.getBlue(), 0.175) / 15), 0.175, 0.35));
            }
        }
    }

    private static float getTimeOffset(ClientLevel level, int scale) {
        float offset = 3;
        long dayTime = level.getDayTime();
        if (dayTime >= 11800 && dayTime <= 13000) { //dusk
            offset += HydrolMath.invLerp(dayTime, scale, 11800, 13000);
        } else if (dayTime > 13000 && dayTime < 22000) { //night
            offset += scale;
        } else if (dayTime >= 22000 && dayTime <= 23500) { //dawn
            offset += HydrolMath.invLerp(dayTime, scale, 22000, 23500);
        }
        return offset;
    }
}
