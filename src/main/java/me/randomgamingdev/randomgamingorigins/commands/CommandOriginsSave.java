package me.randomgamingdev.randomgamingorigins.commands;

import me.randomgamingdev.randomgamingorigins.core.OriginManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandOriginsSave implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String saveFileName;

        if (args.length == 0)
            saveFileName = OriginManager.saveFileName;
        else
            saveFileName = args[0];

        boolean successfulSave = OriginManager.Save(saveFileName);

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
