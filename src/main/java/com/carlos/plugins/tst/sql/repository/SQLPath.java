package com.carlos.plugins.tst.sql.repository;

import com.carlos.plugins.tst.sql.serializer.LocationDeserializer;
import com.carlos.plugins.tst.sql.serializer.LocationSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Location;

public class SQLPath {

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS home_users (" + "uuid VARCHAR(255)," + "name VARCHAR(255)," + "homes LONGTEXT NOT NULL)",

    INSERT_ONE_QUERY = "INSERT INTO home_users VALUES(?,?, ?)",

    UPDATE_ONE_QUERY = "UPDATE home_users SET " + "homes = ?, " + "name = ? WHERE uuid = ?",

    SELECT_ONE_QUERY = "SELECT * FROM home_users WHERE uuid = ?";

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Location.class, new LocationSerializer())
            .registerTypeAdapter(Location.class, new LocationDeserializer())
            .create();
}

