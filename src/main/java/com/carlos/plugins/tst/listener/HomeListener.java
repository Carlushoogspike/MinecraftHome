package com.carlos.plugins.tst.listener;

import com.carlos.plugins.tst.HomePlugin;
import com.carlos.plugins.tst.config.CooldownManager;
import com.carlos.plugins.tst.config.HomeConfig;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Classe para registar os eventos necessario para as homes
 */
@RequiredArgsConstructor
public class HomeListener implements Listener {

    private final CooldownManager manager;
    private final HomeConfig config;

    //evento para detectar a movimentação
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer(); //obtem o jogador

        if (config.isOpIgnorePerms() && player.isOp()) return; //verifica se ele tem op e se ta para ignorar as permissões

        if (!config.isCooldown()) return; //se o cooldown ta ativado
        if (!config.isCancelToMove()) return; //se ta proibido de se mexer

        //obtem as localização de
        //ONDE -> local de dados do momento agora
        //FROM -> local de futuro dados

        int toX = event.getTo().getBlockX();
        int toY = event.getTo().getBlockY();
        int toZ = event.getTo().getBlockZ();

        int fromX = event.getFrom().getBlockX();
        int fromY = event.getFrom().getBlockY();
        int fromZ = event.getFrom().getBlockZ();

        //se eles forem diferentes, irá cancelar a task
        if (toX != fromX || toY != fromY || toZ != fromZ) {
            if (manager.isCooldown(player)){
                manager.leaveCooldown(player);
            }
        }


    }

}
