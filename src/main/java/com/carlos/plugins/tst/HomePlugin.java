package com.carlos.plugins.tst;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class HomePlugin extends JavaPlugin {

    @Getter
    private static HomePlugin instance;

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
    }
}
