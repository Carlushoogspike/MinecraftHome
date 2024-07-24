package com.carlos.plugins.tst.sql;

import com.carlos.plugins.tst.HomePlugin;
import com.carlos.plugins.tst.model.UserHome;
import com.carlos.plugins.tst.sql.controller.SQLController;
import com.carlos.plugins.tst.sql.repository.DataProvider;
import com.carlos.plugins.tst.sql.repository.SQLRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import sqlmodule.connector.SQLConnector;
import sqlmodule.executor.SQLExecutor;

@RequiredArgsConstructor
public class SQLRegistry implements Listener {

    private final HomePlugin plugin;

    @Getter(AccessLevel.PUBLIC)
    private SQLController controller;

    public void onCreate(){
        SQLConnector connector = new DataProvider(plugin, "users").setup(null);
        SQLExecutor executor = new SQLExecutor(connector);

        SQLRepository repository = new SQLRepository(executor);
        repository.createTable();

        controller = new SQLController(repository, plugin.getCache());

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void onDestroy(){
        controller.updateAll();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        final Player player = event.getPlayer();
        controller.load(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        final Player player = event.getPlayer();
        final UserHome user = plugin.getCache().get(player.getUniqueId());

        if (user == null) return;
        if (!user.isDirty()) return;

        controller.update(user);
        plugin.getCache().remove(user.getUuid());
    }

}
