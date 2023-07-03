package me.randomgamingdev.randomgamingorigins.commands;

import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import static me.randomgamingdev.randomgamingorigins.core.OriginManager.GetPlayerData;

public class CommandAbility implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;

        Player player = (Player)sender;
        PlayerData playerData = GetPlayerData(player);
        if (args.length > 0 && args[0].equals("2"))
            player.setSneaking(true);
        playerData.origin.origin.onPlayerSwapHandItemsEvent(new PlayerSwapHandItemsEvent(player, null, null), playerData);

        return true;
    }
}
