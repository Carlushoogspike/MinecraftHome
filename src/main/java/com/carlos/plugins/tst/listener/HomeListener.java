package com.carlos.plugins.tst.listener;

import com.carlos.plugins.tst.HomePlugin;
import com.carlos.plugins.tst.config.CooldownManager;
import com.carlos.plugins.tst.config.HomeConfig;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

@RequiredArgsConstructor
public class HomeListener implements Listener {

    private final CooldownManager manager;
    private final HomeConfig config;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (config.isOpIgnorePerms() && player.isOp()) return;

        if (!config.isCooldown()) return;
        if (!config.isCancelToMove()) return;


        int toX = event.getTo().getBlockX();
        int toY = event.getTo().getBlockY();
        int toZ = event.getTo().getBlockZ();

        int fromX = event.getFrom().getBlockX();
        int fromY = event.getFrom().getBlockY();
        int fromZ = event.getFrom().getBlockZ();

        if (toX != fromX || toY != fromY || toZ != fromZ) {
            if (manager.isCooldown(player)){
                manager.leaveCooldown(player);
            }
        }


    }

}
