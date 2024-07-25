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

@RequiredArgsConstructor
public class HomeCommands implements CommandExecutor, TabCompleter {

    private final HomePlugin plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cComando apenas para jogadores");
            return true;
        }

        UserCache cache = plugin.getCache();
        UserHome user = cache.get(player.getUniqueId());

        if (label.equalsIgnoreCase("home")) {
            if (args.length == 0) {
                player.sendMessage(CustomTag.ERROR + "Use: /home <home>");
            }

            if (args.length == 1) {

                String home = args[0];

                if (!user.containsHome(home)) {
                    player.sendMessage(CustomTag.ERROR + "Você não tem uma home com esse nome");
                    return true;
                }

                Home h = user.getHome(home);

                if (plugin.getHomeConfig().isOpIgnorePerms() && player.isOp()){
                    player.teleport(h.getLocation());
                    player.sendMessage(CustomTag.TELEPORTED + "Você foi teleportado para §f" + h.getName());
                    GameUtils.particle(plugin.getHomeConfig(), player);
                    return true;
                }

                if (plugin.getHomeConfig().isCooldown()){
                    plugin.getCooldownManager().joinCooldown(player, p -> {
                        p.teleport(h.getLocation());
                        p.sendMessage(CustomTag.TELEPORTED + "Você foi teleportado para §f" + h.getName());
                        GameUtils.particle(plugin.getHomeConfig(), player);
                    });
                } else {
                    player.teleport(h.getLocation());
                    player.sendMessage(CustomTag.TELEPORTED + "Você foi teleportado para §f" + h.getName());
                    GameUtils.particle(plugin.getHomeConfig(), player);
                }
            }
        }

        if (label.equalsIgnoreCase("phome")){
            if (args.length == 0) {
                player.sendMessage(CustomTag.ERROR + "Use: /phome <player> <home>");
            }

            if (args.length == 2) {
                String target = args[0];

                Player targetPlayer = Bukkit.getPlayer(target);
                if (targetPlayer == null) {
                    player.sendMessage(CustomTag.ERROR + "Esse jogador não existe");
                    return true;
                }

                UserHome targetHome = cache.get(targetPlayer.getUniqueId());
                if (targetHome == null) {
                    player.sendMessage(CustomTag.ERROR + "Esse jogador não existe no nosso banco de dados");
                    return true;
                }

                String home = args[1];
                Home th = targetHome.getHome(home);
                if (th == null) {
                    player.sendMessage(CustomTag.ERROR + "Esse jogador não tem essa home");
                    return true;
                }

                if (!th.isOpenPublic()){
                    player.sendMessage(CustomTag.ERROR + "Essa home não está pública");
                    return true;
                }

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

        if (label.equalsIgnoreCase("sethome")) {
            if (args.length == 0) {
                player.sendMessage(CustomTag.ERROR + "Use: /sethome <home>");
            }

            if (args.length == 1) {
                String homeName = args[0];

                if (user.containsHome(homeName)){
                    player.sendMessage(CustomTag.ERROR + "Você já tem uma home como esse nome!");
                    return true;
                }

                Location location = player.getLocation();
                if (user.existHomeInLocation(location)){
                    player.sendMessage(CustomTag.ERROR + " já tem uma home nessa localização");
                    return true;
                }

                Home home = new Home(homeName, location, false);
                user.getHomeList().put(home.getName(), home);
                player.sendMessage(CustomTag.SUCCESS + "Você criou uma home chamada de §f" + home.getName());
                plugin.getSql().getController().update(user);
            }


        }

        if (label.equalsIgnoreCase("delhome")) {
            if (args.length == 0) {
                player.sendMessage(CustomTag.ERROR + "Use: /deletehome <home>");
            }

            if (args.length == 1) {
                String homeName = args[1];

                if (!user.containsHome(homeName)) {
                    player.sendMessage(CustomTag.ERROR + "Você não tem uma home com esse nome");
                    return true;
                }

                user.removeHome(homeName);
                player.sendMessage(CustomTag.SUCCESS + "Você removeu a home §f" + homeName);
            }
        }

        if (label.equalsIgnoreCase("setpublic")){
            if (args.length == 0) {
                player.sendMessage(CustomTag.ERROR + "Use: /sethome <home>");
            }

            if (args.length == 1) {
                String homeName = args[0];

                if (!user.containsHome(homeName)) {
                    player.sendMessage(CustomTag.ERROR + "Você não tem uma home com esse nome");
                    return true;
                }

                Home h = user.getHome(homeName);
                if (h.isOpenPublic()){
                    player.sendMessage(CustomTag.ERROR + "Essa home já está publica");
                    return true;
                }

                h.setOpenPublic(true);
                player.sendMessage(CustomTag.INFO + "Você definiu a home §f" + homeName + "§7 como §aPública");
            }
        }

        if (label.equalsIgnoreCase("setprivate")){
            if (args.length == 0) {
                player.sendMessage(CustomTag.ERROR + "Use: /setprivate <home>");
            }

            if (args.length == 1) {
                String homeName = args[0];

                if (!user.containsHome(homeName)) {
                    player.sendMessage(CustomTag.ERROR + "Você não tem uma home com esse nome");
                    return true;
                }

                Home h = user.getHome(homeName);
                if (!h.isOpenPublic()){
                    player.sendMessage(CustomTag.ERROR + "Essa home já está privada");
                    return true;
                }

                h.setOpenPublic(false);
                player.sendMessage(CustomTag.INFO + "Você definiu a home §f" + homeName + "§7 como §cPrivada");
                plugin.getSql().getController().update(user);
            }
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return List.of();
        }

        UserCache cache = plugin.getCache();
        UserHome user = cache.get(player.getUniqueId());

        if (args.length == 1) {
            if (label.equalsIgnoreCase("home") || label.equalsIgnoreCase("delhome")) {
                return user.getHomeList().values().stream()
                        .map(Home::getName)
                        .collect(Collectors.toList());
            }

            if (label.equalsIgnoreCase("setpublic")) {
                return user.getHomeList().values().stream()
                        .filter(h -> !h.isOpenPublic())
                        .map(Home::getName)
                        .collect(Collectors.toList());
            }

            if (label.equalsIgnoreCase("setprivate")) {
                return user.getHomeList().values().stream()
                        .filter(Home::isOpenPublic)
                        .map(Home::getName)
                        .collect(Collectors.toList());
            }

            if (label.equalsIgnoreCase("phome")) {
                return Bukkit.getOnlinePlayers().stream()
                        .filter(p -> !p.equals(player))
                        .map(Player::getName)
                        .collect(Collectors.toList());
            }
        }

        return List.of();
    }
}
