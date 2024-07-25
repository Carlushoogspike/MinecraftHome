package com.carlos.plugins.tst.command;

import com.carlos.plugins.tst.HomePlugin;
import com.carlos.plugins.tst.data.UserCache;
import com.carlos.plugins.tst.model.Home;
import com.carlos.plugins.tst.model.UserHome;
import com.carlos.plugins.tst.utils.CustomTag;
import com.carlos.plugins.tst.utils.GameUtils;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe utilizada para registrar os comandos
 *
 */
@RequiredArgsConstructor
public class HomeCommands implements CommandExecutor, TabCompleter {
    //TabCompleter -> para completar o tab dos comandos
    //CommandExecutor -> classe necessaria para rodar os comandos

    private final HomePlugin plugin;
    //Puxando a classe principal do sistema

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cComando apenas para jogadores");
            //Apenas para jogadores
            return true;
        }

        UserCache cache = plugin.getCache();
        UserHome user = cache.get(player.getUniqueId());
        //Obtendo o usuario principal

        //Comando apenas para home
        if (label.equalsIgnoreCase("home")) {
            if (args.length == 0) {
                player.sendMessage(CustomTag.ERROR + "Use: /home <home>");
            }

            if (args.length == 1) {

                String home = args[0];

                //se tem home
                if (!user.containsHome(home)) {
                    player.sendMessage(CustomTag.ERROR + "Você não tem uma home com esse nome");
                    return true;
                }

                //obtendo a home
                Home h = user.getHome(home);

                //se ele tem op e tambem se o sistema está configurado para ignorar as permissões do plugin se ele tiver o OP
                if (plugin.getHomeConfig().isOpIgnorePerms() && player.isOp()){
                    //executa a funlção
                    player.teleport(h.getLocation());
                    player.sendMessage(CustomTag.TELEPORTED + "Você foi teleportado para §f" + h.getName());
                    GameUtils.particle(plugin.getHomeConfig(), player);
                    return true;
                }

                //se tem cooldown
                if (plugin.getHomeConfig().isCooldown()){
                    plugin.getCooldownManager().joinCooldown(player, p -> {
                        p.teleport(h.getLocation());
                        p.sendMessage(CustomTag.TELEPORTED + "Você foi teleportado para §f" + h.getName());
                        GameUtils.particle(plugin.getHomeConfig(), player);
                    });
                } else {
                    //se não tiver cooldown faz isso
                    player.teleport(h.getLocation());
                    player.sendMessage(CustomTag.TELEPORTED + "Você foi teleportado para §f" + h.getName());
                    GameUtils.particle(plugin.getHomeConfig(), player);
                }
            }
        }

        //comando para ir, para home do jogador desejado
        if (label.equalsIgnoreCase("phome")){
            if (args.length == 0) {
                player.sendMessage(CustomTag.ERROR + "Use: /phome <player> <home>");
            }

            if (args.length == 2) {
                String target = args[0];

                //obtem o jogador alvo
                Player targetPlayer = Bukkit.getPlayer(target);
                if (targetPlayer == null) {
                    player.sendMessage(CustomTag.ERROR + "Esse jogador não existe");
                    return true;
                }

                //verifica se está no banco de dados
                UserHome targetHome = cache.get(targetPlayer.getUniqueId());
                if (targetHome == null) {
                    player.sendMessage(CustomTag.ERROR + "Esse jogador não existe no nosso banco de dados");
                    return true;
                }

                //obtem a home e verifica se ela existe
                String home = args[1];
                Home th = targetHome.getHome(home);
                if (th == null) {
                    player.sendMessage(CustomTag.ERROR + "Esse jogador não tem essa home");
                    return true;
                }

                //se ela é publica
                if (!th.isOpenPublic()){
                    player.sendMessage(CustomTag.ERROR + "Essa home não está pública");
                    return true;
                }

                //irá realizar as mesmas verificações acima
                if (plugin.getHomeConfig().isOpIgnorePerms() && player.isOp()){
                    player.teleport(th.getLocation());
                    player.sendMessage(CustomTag.TELEPORTED + "Você foi teleportado para §f" + th.getName() + "§7 do jogador §f" + targetPlayer.getName());
                    GameUtils.particle(plugin.getHomeConfig(), player);
                    return true;
                }

                if (plugin.getHomeConfig().isCooldown()){
                    plugin.getCooldownManager().joinCooldown(player, p -> {
                        p.teleport(th.getLocation());
                        p.sendMessage(CustomTag.TELEPORTED + "Você foi teleportado para §f" + th.getName() + "§7 do jogador §f" + targetPlayer.getName());
                        GameUtils.particle(plugin.getHomeConfig(), player);
                    });
                } else {
                    player.teleport(th.getLocation());
                    player.sendMessage(CustomTag.TELEPORTED + "Você foi teleportado para §f" + th.getName() + "§7 do jogador §f" + targetPlayer.getName());
                    GameUtils.particle(plugin.getHomeConfig(), player);
                }

            }
        }

        //comando para definir uma home
        if (label.equalsIgnoreCase("sethome")) {
            if (args.length == 0) {
                player.sendMessage(CustomTag.ERROR + "Use: /sethome <home>");
            }

            if (args.length == 1) {
                String homeName = args[0];

                //verifica se ele já tem a home
                if (user.containsHome(homeName)){
                    player.sendMessage(CustomTag.ERROR + "Você já tem uma home como esse nome!");
                    return true;
                }

                //obtem a localização do jogador e verifica se nao tem uma home naquela localização
                Location location = player.getLocation();
                if (user.existHomeInLocation(location)){
                    player.sendMessage(CustomTag.ERROR + " já tem uma home nessa localização");
                    return true;
                }

                //cria uma home e adiciona no sistema
                Home home = new Home(homeName, location, false);
                user.getHomeList().put(home.getName(), home);
                player.sendMessage(CustomTag.SUCCESS + "Você criou uma home chamada de §f" + home.getName());
                plugin.getSql().getController().update(user);
            }


        }

        //comando de listar as homes
        if (label.equalsIgnoreCase("list")) {
            if (args.length == 0) {
                player.sendMessage(CustomTag.INFO + "Sua homes");
                //realiza um trabalho na String para que possa colocalas em 1 linha
                String homes = user.getHomeList().keySet().stream().sorted().collect(Collectors.joining("\n"));
                player.sendMessage("§f" + homes);
            }
        }

        //comando para deletar uma home
        if (label.equalsIgnoreCase("delhome")) {
            if (args.length == 0) {
                player.sendMessage(CustomTag.ERROR + "Use: /deletehome <home>");
            }

            if (args.length == 1) {
                String homeName = args[0];

                //verifica se ele possui a home
                if (!user.containsHome(homeName)) {
                    player.sendMessage(CustomTag.ERROR + "Você não tem uma home com esse nome");
                    return true;
                }

                //remove a home
                user.removeHome(homeName);
                player.sendMessage(CustomTag.SUCCESS + "Você removeu a home §f" + homeName);
                plugin.getSql().getController().update(user);
            }
        }

        //comando para definir a home como publica
        if (label.equalsIgnoreCase("setpublic")){
            if (args.length == 0) {
                player.sendMessage(CustomTag.ERROR + "Use: /sethome <home>");
            }

            if (args.length == 1) {
                String homeName = args[0];

                //verifica se tem a home
                if (!user.containsHome(homeName)) {
                    player.sendMessage(CustomTag.ERROR + "Você não tem uma home com esse nome");
                    return true;
                }

                //obtem ela e verifica se ela já está publica
                Home h = user.getHome(homeName);
                if (h.isOpenPublic()){
                    player.sendMessage(CustomTag.ERROR + "Essa home já está publica");
                    return true;
                }

                //define como publica
                h.setOpenPublic(true);
                player.sendMessage(CustomTag.INFO + "Você definiu a home §f" + homeName + "§7 como §aPública");
                plugin.getSql().getController().update(user);

            }
        }

        //comando para definir a home como privada
        if (label.equalsIgnoreCase("setprivate")){
            if (args.length == 0) {
                player.sendMessage(CustomTag.ERROR + "Use: /setprivate <home>");
            }

            if (args.length == 1) {
                String homeName = args[0];

                //verifica se tem a home
                if (!user.containsHome(homeName)) {
                    player.sendMessage(CustomTag.ERROR + "Você não tem uma home com esse nome");
                    return true;
                }

                //obtem ela e verifica se já está privada
                Home h = user.getHome(homeName);
                if (!h.isOpenPublic()){
                    player.sendMessage(CustomTag.ERROR + "Essa home já está privada");
                    return true;
                }

                //deixa ela privada
                h.setOpenPublic(false);
                player.sendMessage(CustomTag.INFO + "Você definiu a home §f" + homeName + "§7 como §cPrivada");
                plugin.getSql().getController().update(user);
            }
        }

        return false;
    }


    //completar o tab
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return List.of();
        }

        //obtem o jogador
        UserCache cache = plugin.getCache();
        UserHome user = cache.get(player.getUniqueId());

        //verifica se é o primeiro argumento do conjunto de argumentos da linha de comando/chat do spigot no caso /0 1 2...
        if (args.length == 1) {
             //obtem as homes do jogador
            if (label.equalsIgnoreCase("home") || label.equalsIgnoreCase("delhome")) {
                return user.getHomeList().values().stream()
                        .map(Home::getName)
                        .collect(Collectors.toList());
            }

            //obtem as homes que NÃO são publicas
            if (label.equalsIgnoreCase("setpublic")) {
                return user.getHomeList().values().stream()
                        .filter(h -> !h.isOpenPublic())
                        .map(Home::getName)
                        .collect(Collectors.toList());
            }

            //obtem as homes que NÃO são privadas
            if (label.equalsIgnoreCase("setprivate")) {
                return user.getHomeList().values().stream()
                        .filter(Home::isOpenPublic)
                        .map(Home::getName)
                        .collect(Collectors.toList());
            }

            //obtem o nome dos jogadores online excluindo o de si proprio
            if (label.equalsIgnoreCase("phome")) {
                return Bukkit.getOnlinePlayers().stream()
                        .filter(p -> !p.equals(player))
                        .map(Player::getName)
                        .collect(Collectors.toList());
            }
        }

        if (args.length == 2){
            if (label.equalsIgnoreCase("phome")) {
                //obtem o jogador alvo
                Player target = Bukkit.getPlayer(args[0]);
                //se ele existir
                if (target != null) {
                    //se ele estiver no banco de dados
                    UserHome targetHome = cache.get(target.getUniqueId());
                    if (targetHome != null) {
                        //retorna a lista de suas homes
                        return targetHome.getHomeList().values().stream()
                                .map(Home::getName)
                                .collect(Collectors.toList());
                    }
                }
            }
        }

        return List.of();
    }
}
