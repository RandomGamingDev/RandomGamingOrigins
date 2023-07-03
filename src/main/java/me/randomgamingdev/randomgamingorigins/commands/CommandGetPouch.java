package me.randomgamingdev.randomgamingorigins.commands;

import me.randomgamingdev.randomgamingorigins.core.OriginManager;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

import static me.randomgamingdev.randomgamingorigins.core.OriginManager.playersData;

public class CommandGetPouch implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1)
            return false;
        if (!(sender instanceof Player))
            return false;

        PlayerData playerData = OriginManager.GetPlayerData(args[0]);
        if (playerData == null)
            return false;

        if (playerData.pouch == null)
            return false;
        ((Player)sender).openInventory(playerData.pouch);

        return true;
    }
}
