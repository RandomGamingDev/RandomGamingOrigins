package me.randomgamingdev.randomgamingorigins;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.command.*;

public class CommandGetOrigin implements CommandExecutor {
    public String getOrigin(Player player) {
        Origin origin = Origins.playersData.get(player.getUniqueId()).origin;
        if (origin == Origin.Null || origin == null)
            return String.format("%s doesn't have an origin!", player.getName());
        return String.format("%s's a(n) %s", player.getName(), origin.name);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 0)
                return false;
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null)
                return false;
            System.out.println(getOrigin(target));
            return true;
        }
        if (!(sender instanceof Player))
            return false;

        Player player = (Player)sender;
        if (args.length == 0) {
            player.sendMessage(getOrigin(player));
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null)
            return false;
        player.sendMessage(getOrigin(target));
        return true;
    }
}