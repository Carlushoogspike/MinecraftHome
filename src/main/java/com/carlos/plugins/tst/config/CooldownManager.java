package com.carlos.plugins.tst.config;

import com.carlos.plugins.tst.HomePlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class CooldownManager {

    private final HomeConfig config;
    private final Map<UUID, BukkitTask> playersOnCooldown;

    public CooldownManager(HomePlugin plugin) {
        this.config = plugin.getHomeConfig();
        this.playersOnCooldown = new HashMap<>();
    }

    public void joinCooldown(Player player, Consumer<Player> playerConsumer) {
        UUID playerUUID = player.getUniqueId();
        if (playersOnCooldown.containsKey(playerUUID)) {
            player.sendMessage("Você já está em cooldown.");
            return;
        }

        int cooldownTime = config.getCooldownSeconds();
        BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(HomePlugin.getInstance(), new Runnable() {
            int timeLeft = cooldownTime;

            @Override
            public void run() {
                if (timeLeft > 0) {
                    player.sendActionBar(Component.text("§aTeleportando em §f" + timeLeft + "§a segundos"));
                    timeLeft--;
                } else {
                    playerConsumer.accept(player);
                    cancel();
                }
            }

            private void cancel() {
                if (playersOnCooldown.containsKey(playerUUID)) {
                    BukkitTask task = playersOnCooldown.remove(playerUUID);
                    if (task != null) {
                        task.cancel();
                        System.out.println("CANCELANDO");
                    }
                }
            }
        }, 0L, 20L);

        playersOnCooldown.put(playerUUID, task);
    }

    public void leaveCooldown(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (playersOnCooldown.containsKey(playerUUID)) {
            BukkitTask task = playersOnCooldown.remove(playerUUID);
            if (task != null) {
                task.cancel();
                player.sendActionBar(Component.text("§cSeu teleporte foi cancelado!"));
            }
        }
    }

    public boolean isCooldown(Player player) {
        return playersOnCooldown.containsKey(player.getUniqueId());
    }
}
