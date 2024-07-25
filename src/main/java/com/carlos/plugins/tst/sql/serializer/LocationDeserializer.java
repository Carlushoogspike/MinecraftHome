package com.carlos.plugins.tst.sql.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;

/**
 * A classe LocationDeserializer é responsável por desserializar um objeto JSON em uma instância de Location do Bukkit.
 */
public class LocationDeserializer implements JsonDeserializer<Location> {

    /**
     * Desserializa um elemento JSON em um objeto Location.
     *
     * @param jsonElement o elemento JSON a ser desserializado
     * @param type o tipo de destino da desserialização
     * @param jsonDeserializationContext o contexto da desserialização
     * @return a localização desserializada
     */
    @Override
    public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String worldName = jsonObject.get("world").getAsString();
        World world = Bukkit.getWorld(worldName);
        double x = jsonObject.get("x").getAsDouble();
        double y = jsonObject.get("y").getAsDouble();
        double z = jsonObject.get("z").getAsDouble();
        float yaw = jsonObject.get("yaw").getAsFloat();
        float pitch = jsonObject.get("pitch").getAsFloat();
        return new Location(world, x, y, z, yaw, pitch);
    }
}
