package com.carlos.plugins.tst;

import com.carlos.plugins.tst.command.HomeCommands;
import com.carlos.plugins.tst.data.UserCache;
import com.carlos.plugins.tst.sql.SQLRegistry;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

@Getter
public class HomePlugin extends JavaPlugin {

    @Getter
    private static HomePlugin instance;

    private UserCache cache;

    private SQLRegistry sql;

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        instance = this;

        cache = new UserCache();

        sql = new SQLRegistry(this);
        sql.onCreate();

        Arrays.asList("home","sethome","delhome","setpublic","setprivate").forEach(c -> getCommand(c).setExecutor(new HomeCommands(this)));
    }

    @Override
    public void onDisable() {
        sql.onDestroy();
    }
}
