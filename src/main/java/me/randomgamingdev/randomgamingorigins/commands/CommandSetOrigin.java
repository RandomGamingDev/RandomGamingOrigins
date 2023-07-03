package me.randomgamingdev.randomgamingorigins.commands;

import me.randomgamingdev.randomgamingorigins.core.OriginManager;
import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetOrigin implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2)
            return false;

        PlayerData playerData = OriginManager.GetPlayerData(args[0]);
        if (playerData == null)
            return false;

        int origin;
        try {
            origin = Integer.parseInt(args[1]);
        }
        catch (Exception error) {
            return false;
        }

        Origin[] origins = Origin.values();
        if (origin < 0 || origin >= origins.length)
            return false;

        playerData.origin = origins[origin];

        return true;
    }
}
