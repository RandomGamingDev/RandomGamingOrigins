package me.randomgamingdev.randomgamingorigins.commands;

import me.randomgamingdev.randomgamingorigins.core.OriginsGui;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.command.*;

public class CommandOriginsGui implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player;

        if (args.length == 0) {
            if (!(sender instanceof Player))
                return false;
            player = (Player)sender;
        }
        else
            player = Bukkit.getPlayer(args[0]);

        if (player == null)
            return false;
        OriginsGui.Gui(player, true);
        return true;
    }
}
