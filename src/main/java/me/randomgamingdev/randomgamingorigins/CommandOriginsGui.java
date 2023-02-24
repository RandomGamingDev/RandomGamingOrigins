package me.randomgamingdev.randomgamingorigins;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.command.*;

public class CommandOriginsGui implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            if (args.length == 0)
                return false;
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null)
                return false;
            OriginsGui.Gui(target, true);
            return true;
        }
        if (!(sender instanceof Player))
            return false;
        Player player = (Player)sender;
        if (args.length == 0) {
            OriginsGui.Gui(player, true);
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null)
            return false;
        OriginsGui.Gui(target, true);
        return true;
    }
}
