package me.randomgamingdev.randomgamingorigins;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandSave implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String saveFileName;

        if (args.length == 0)
            saveFileName = Origins.saveFileName;
        else
            saveFileName = args[0];

        boolean successfulSave = Origins.Save(Origins.saveFileName);

        if (!(sender instanceof Player))
            return true;

        String saveState;

        if (successfulSave)
            saveState = String.format("RandomGamingOrigins: Saved successfully to %s!", saveFileName);
        else
            saveState = String.format("RandomGamingOrigins: Something went wrong while saving to %s!", saveFileName);

        ((Player)sender).sendMessage(saveState);
        return true;
    }
}
