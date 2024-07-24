package com.carlos.plugins.tst.sql.adapter;

import com.carlos.plugins.tst.model.Home;
import com.carlos.plugins.tst.model.UserHome;
import com.google.common.reflect.TypeToken;
import org.bukkit.Location;
import sqlmodule.executor.adapter.DataResultAdapter;
import sqlmodule.executor.result.DataResultSet;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import static com.carlos.plugins.tst.sql.repository.SQLPath.GSON;

public class SQLAdapter implements DataResultAdapter<UserHome> {

    private final Type homesType = new TypeToken<List<Home>>() {}.getType();

    @Override
    public UserHome adaptResult(DataResultSet dataResultSet) {
        final UUID uuid = UUID.fromString(dataResultSet.get("uuid").toString());
        final String name = dataResultSet.get("name");
        final Location location = dataResultSet.get("location");
        final List<Home> homes = GSON.fromJson((String) dataResultSet.get("homes"), homesType);

        return new UserHome(name, uuid, location, homes);
    }
}
