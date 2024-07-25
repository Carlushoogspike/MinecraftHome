package com.carlos.plugins.tst.sql.repository;

import com.carlos.plugins.tst.sql.serializer.LocationDeserializer;
import com.carlos.plugins.tst.sql.serializer.LocationSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Location;

/**
 * A classe SQLPath define as consultas SQL usadas no plugin e configura o objeto Gson
 * para serialização e desserialização de objetos Location do Bukkit.
 */
public class SQLPath {

    // Consulta SQL para criar a tabela home_users se ela não existir
    public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS home_users ("
            + "uuid VARCHAR(255),"
            + "name VARCHAR(255),"
            + "homes LONGTEXT NOT NULL)";

    // Consulta SQL para inserir um registro na tabela home_users
    public static final String INSERT_ONE_QUERY = "INSERT INTO home_users VALUES(?,?,?)";

    // Consulta SQL para atualizar um registro na tabela home_users
    public static final String UPDATE_ONE_QUERY = "UPDATE home_users SET "
            + "homes = ?, "
            + "name = ? WHERE uuid = ?";

    // Consulta SQL para selecionar um registro da tabela home_users pelo UUID
    public static final String SELECT_ONE_QUERY = "SELECT * FROM home_users WHERE uuid = ?";

    // Objeto Gson configurado para serialização e desserialização de objetos Location
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Location.class, new LocationSerializer())
            .registerTypeAdapter(Location.class, new LocationDeserializer())
            .create();
}
