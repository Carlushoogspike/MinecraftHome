package com.carlos.plugins.tst;

import com.carlos.plugins.tst.data.UserCache;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class HomePlugin extends JavaPlugin {

    @Getter
    private static HomePlugin instance;

    private UserCache cache;

    @Override
    public void onLoad() {
        saveDefaultConfig();

        cache = new UserCache();
    }

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
    }
}
