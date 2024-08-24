package com.Apothic0n.Hydrological.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HydrolJsonReader {
    //Common
    public static boolean serverSidedOnlyMode = false;
    public static int cubicalTerrainScale = 1;
    public static float noiseScale = 1;
    public static boolean removeCollisionFromSnowLayers = false;

    //Client
    public static boolean customNightLighting = true;
    public static boolean customOverworldFog = true;
    public static boolean fireflies = true;

    public static void main() throws Exception {
        makeAndReadCommonConfig(Path.of(FMLPaths.CONFIGDIR.get().toString() + "/hydrol-common.json"));
        makeAndReadClientConfig(Path.of(FMLPaths.CONFIGDIR.get().toString() + "/hydrol-client.json"));
    }

    private static void makeAndReadCommonConfig(Path path) throws IOException {
        Gson gson = new Gson();
        if (!Files.exists(path)) {
            JsonWriter writer = new JsonWriter(new FileWriter(path.toString()));
            JsonObject defaultData = gson.fromJson("{\"serverSidedOnlyMode\":\"false\", \"cubicalTerrainScale\":1}", JsonObject.class);
            gson.toJson(defaultData, writer);
            writer.close();
        }
        JsonReader reader = new JsonReader(new FileReader(path.toString()));
        JsonObject data = gson.fromJson(reader, JsonObject.class);
        if (data.get("serverSidedOnlyMode") != null) {
            serverSidedOnlyMode = data.get("serverSidedOnlyMode").getAsBoolean();
        } else {
            data.addProperty("serverSidedOnlyMode", false);
        }
        if (data.get("cubicalTerrainScale") != null) {
            cubicalTerrainScale = data.get("cubicalTerrainScale").getAsInt();
            if (cubicalTerrainScale < 1) {
                cubicalTerrainScale = 1;
            } else if (cubicalTerrainScale > 16) {
                cubicalTerrainScale = 16;
            }
        } else {
            data.addProperty("cubicalTerrainScale", 1);
        }
        if (data.get("noiseScale") != null) {
            noiseScale = data.get("noiseScale").getAsFloat();
            if (noiseScale < 0.33) {
                noiseScale = 0.33f;
            } else if (noiseScale > 5) {
                noiseScale = 5;
            }
        } else {
            data.addProperty("noiseScale", 1);
        }
        if (data.get("removeCollisionFromSnowLayers") != null) {
            removeCollisionFromSnowLayers = data.get("removeCollisionFromSnowLayers").getAsBoolean();
        } else {
            data.addProperty("removeCollisionFromSnowLayers", true);
        }
        JsonWriter writer = new JsonWriter(new FileWriter(path.toString()));
        gson.toJson(data, writer);
        writer.close();
    }

    private static void makeAndReadClientConfig(Path path) throws IOException {
        Gson gson = new Gson();
        if (!Files.exists(path)) {
            JsonWriter writer = new JsonWriter(new FileWriter(path.toString()));
            JsonObject defaultData = gson.fromJson("{\"customNightLighting\":\"true\", \"customOverworldFog\":\"true\", \"fireflies\":\"true\"}", JsonObject.class);
            gson.toJson(defaultData, writer);
            writer.close();
        }
        JsonReader reader = new JsonReader(new FileReader(path.toString()));
        JsonObject data = gson.fromJson(reader, JsonObject.class);
        if (data.get("customNightLighting") != null) {
            customNightLighting = data.get("customNightLighting").getAsBoolean();
        } else {
            data.addProperty("customNightLighting", false);
        }
        if (data.get("customOverworldFog") != null) {
            customOverworldFog = data.get("customOverworldFog").getAsBoolean();
        } else {
            data.addProperty("customOverworldFog", false);
        }
        if (data.get("fireflies") != null) {
            fireflies = data.get("fireflies").getAsBoolean();
        } else {
            data.addProperty("fireflies", false);
        }
        JsonWriter writer = new JsonWriter(new FileWriter(path.toString()));
        gson.toJson(data, writer);
        writer.close();
    }
}
