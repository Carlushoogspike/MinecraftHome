package com.carlos.plugins.tst.sql.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SQLPath {

    public static final String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS home_users ("
            + "uuid VARCHAR(255),"
            + "name VARCHAR(255),"
            + "last_died LONGTEXT,"
            + "homes LONGTEXT NOT NULL)",

    INSERT_ONE_QUERY = "INSERT INTO home_users VALUES(?,?,?, ?)",

    UPDATE_ONE_QUERY = "UPDATE home_users SET "
            + "homes = ?, "
            + "last_died = ?,"
            + "name = ? WHERE uuid = ?",

    SELECT_ONE_QUERY = "SELECT * FROM home_users WHERE uuid = ?";

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


}
