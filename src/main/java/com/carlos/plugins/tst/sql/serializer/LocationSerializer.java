package com.carlos.plugins.tst.sql.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.bukkit.Location;

import java.lang.reflect.Type;

/**
 * A classe LocationSerializer é responsável por serializar uma instância de Location do Bukkit em um objeto JSON.
 */
public class LocationSerializer implements JsonSerializer<Location> {

    /**
     * Serializa um objeto Location em um elemento JSON.
     *
     * @param location a localização a ser serializada
     * @param type o tipo de origem da serialização
     * @param jsonSerializationContext o contexto da serialização
     * @return o elemento JSON representando a localização
     */
    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("world", location.getWorld().getName());
        jsonObject.addProperty("x", location.getX());
        jsonObject.addProperty("y", location.getY());
        jsonObject.addProperty("z", location.getZ());
        jsonObject.addProperty("yaw", location.getYaw());
        jsonObject.addProperty("pitch", location.getPitch());
        return jsonObject;
    }
}
