package me.randomgamingdev.randomgamingorigins.commands;

import org.bukkit.entity.*;
import org.bukkit.command.*;

public class CommandPing implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;
        Player player = (Player)sender;
        player.sendMessage("Pong!");
        return true;
    }
}
