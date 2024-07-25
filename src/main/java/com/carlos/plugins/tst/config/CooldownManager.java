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

/**
 * Classe para trabalhar o Cooldown das homes
 */
public class CooldownManager {

    private final HomeConfig config;
    private final Map<UUID, BukkitTask> playersOnCooldown;

    public CooldownManager(HomePlugin plugin) {
        this.config = plugin.getHomeConfig();
        this.playersOnCooldown = new HashMap<>();
    }

    //para o jogador entrar em cooldown, pedindo o jogador, e um consumer do jogador
    //consumer é como se projetasse algo para acontecer se aquele função dentro do codigo finalizar
    public void joinCooldown(Player player, Consumer<Player> playerConsumer) {
        //obtem o id do jogador e se ele pode iniciar o cooldown
        UUID playerUUID = player.getUniqueId();
        if (playersOnCooldown.containsKey(playerUUID)) return;

        //o tempo do cooldown
        int cooldownTime = config.getCooldownSeconds();
        //incia uma task async -> não gera impacto na thread principal do servidor
        BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(HomePlugin.getInstance(), new Runnable() {
            //obtem o tempo que falta
            int timeLeft = cooldownTime;

            @Override
            public void run() {
                //enquanto ele não for zero
                if (timeLeft > 0) {
                    //envia a mensagem
                    player.sendActionBar(Component.text("§aTeleportando em §f" + timeLeft + "§a segundos"));
                    timeLeft--; //e decrementa
                } else {
                    //permite o consumer de rodar
                    playerConsumer.accept(player);
                    cancel(); //canela a task
                }
            }


            private void cancel() {
                //se o jogador estiver em task
                if (playersOnCooldown.containsKey(playerUUID)) {
                    //obtem a task no mesmo segundo de remover
                    BukkitTask task = playersOnCooldown.remove(playerUUID);
                    if (task != null) {
                        task.cancel(); //cancela a task
                    }
                }
            }
        }, 0L, 20L);

        //adiciona o jogador e a task no map
        playersOnCooldown.put(playerUUID, task);
    }

    //jogador sai do cooldown
    public void leaveCooldown(Player player) {
        //obtem o jogador e verifica se ele está em cooldown
        UUID playerUUID = player.getUniqueId();
        if (playersOnCooldown.containsKey(playerUUID)) {
            //realiza a mesma tarefa acima so que aqui pode ser chamado em qualquer parte do plugin
            //no codigo de cima, apenas pode ser usado dentro da task!
            BukkitTask task = playersOnCooldown.remove(playerUUID);
            if (task != null) {
                task.cancel();
                player.sendActionBar(Component.text("§cSeu teleporte foi cancelado!"));
            }
        }
    }

    //metodo de rapida verificação
    public boolean isCooldown(Player player) {
        return playersOnCooldown.containsKey(player.getUniqueId());
    }
}
