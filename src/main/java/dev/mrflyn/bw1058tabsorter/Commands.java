package dev.mrflyn.bw1058tabsorter;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.hasPermission("bwTab.admin")){
            commandSender.sendMessage(ChatColor.RED+"You don't have the required permission to execute this command.");
            return true;
        }
        Main.plugin.reload();
        commandSender.sendMessage(ChatColor.GREEN+"BedWars1058-TabSorter Reloaded Successfully.");
        return true;
    }
}
