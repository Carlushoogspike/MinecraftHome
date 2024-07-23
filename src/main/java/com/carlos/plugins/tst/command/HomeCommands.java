package com.carlos.plugins.tst.command;

import com.carlos.plugins.tst.utils.CustomTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HomeCommands implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cComando apenas para jogadores");
            return true;
        }

        if (label.equalsIgnoreCase("home")){
            if (args.length == 0) {
                player.sendMessage(CustomTag.ERROR + "Use: /home <set/delete/tp/public/private> <home>");
            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("set")) {
                    player.sendMessage(CustomTag.ERROR + "Use: /home set <home>");
                }

                if (args[0].equalsIgnoreCase("delete")) {
                    player.sendMessage(CustomTag.ERROR + "Use: /home delete <home>");
                }

                if (args[0].equalsIgnoreCase("tp")) {
                    player.sendMessage(CustomTag.ERROR + "Use: /home tp <home>");
                }

                if (args[0].equalsIgnoreCase("public")) {
                    player.sendMessage(CustomTag.ERROR + "Use: /home public <home>");
                }

                if (args[0].equalsIgnoreCase("private")) {
                    player.sendMessage(CustomTag.ERROR + "Use: /home private <home>");
                }
            }

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("set")) {
                    String homeName = args[1];
                }
            }
        }

        return false;
    }
}
