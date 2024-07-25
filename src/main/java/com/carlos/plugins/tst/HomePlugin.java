package com.carlos.plugins.tst;

import com.carlos.plugins.tst.command.HomeCommands;
import com.carlos.plugins.tst.config.CooldownManager;
import com.carlos.plugins.tst.config.HomeConfig;
import com.carlos.plugins.tst.data.UserCache;
import com.carlos.plugins.tst.listener.HomeListener;
import com.carlos.plugins.tst.sql.SQLRegistry;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

@Getter
public class HomePlugin extends JavaPlugin {

    @Getter
    private static HomePlugin instance;

    private HomeConfig homeConfig;
    private CooldownManager cooldownManager;

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

        homeConfig = new HomeConfig(getConfig());
        cooldownManager = new CooldownManager(this);

        Arrays.asList("home","phome","sethome","delhome","setpublic","setprivate").forEach(c -> {
            getCommand(c).setExecutor(new HomeCommands(this));
            getCommand(c).setTabCompleter(new HomeCommands(this));
        });

        getServer().getPluginManager().registerEvents(new HomeListener(cooldownManager, homeConfig), this);
    }

    @Override
    public void onDisable() {
        sql.onDestroy();
    }
}
