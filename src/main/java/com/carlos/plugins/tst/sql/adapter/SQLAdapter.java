package com.carlos.plugins.tst.sql.adapter;

import com.carlos.plugins.tst.model.Home;
import com.carlos.plugins.tst.model.UserHome;
import com.google.common.reflect.TypeToken;
import org.bukkit.Location;
import sqlmodule.executor.adapter.DataResultAdapter;
import sqlmodule.executor.result.DataResultSet;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

import static com.carlos.plugins.tst.sql.repository.SQLPath.GSON;

public class SQLAdapter implements DataResultAdapter<UserHome> {

    private final Type homesType = new TypeToken<Map<String, Home>>() {}.getType();

    @Override
    public UserHome adaptResult(DataResultSet dataResultSet) {
        System.out.println("Adapting result set: " + dataResultSet);
        final UUID uuid = UUID.fromString(dataResultSet.get("uuid").toString());
        final String name = dataResultSet.get("name");
        final Map<String, Home> homes = GSON.fromJson((String) dataResultSet.get("homes"), homesType);
        System.out.println("Adapted homes: " + homes);
        return UserHome.builder().uuid(uuid).name(name).homeList(homes).build();
    }


}
