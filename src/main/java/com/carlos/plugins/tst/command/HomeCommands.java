package com.carlos.plugins.tst.command;

import com.carlos.plugins.tst.HomePlugin;
import com.carlos.plugins.tst.data.UserCache;
import com.carlos.plugins.tst.model.Home;
import com.carlos.plugins.tst.model.UserHome;
import com.carlos.plugins.tst.utils.CustomTag;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class HomeCommands implements CommandExecutor {

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

                if (user.containsHome(home)) {
                    Home h = user.getHome(home);

                    player.teleportAsync(h.getLocation());
                    player.sendMessage(CustomTag.TELEPORTED + "Você foi teleportado para §f" + h.getName());
                    return true;
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
                user.getHomeList().add(home);
                player.sendMessage(CustomTag.SUCCESS + "Você criou uma home chamada de §f" + home.getName());
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
            }
        }

        return false;
    }
}
