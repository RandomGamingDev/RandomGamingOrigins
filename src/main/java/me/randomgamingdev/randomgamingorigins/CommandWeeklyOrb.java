package me.randomgamingdev.randomgamingorigins;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDateTime;
import java.util.Date;

public class CommandWeeklyOrb implements CommandExecutor { // Add weekly orbs with custom model and that look enchatned
    public static ItemStack orb = OriginsGui.createGuiItem(
            Material.SLIME_BALL, true, "Origin Orb",
            "You can sense the mysterious powers at play and as you",
            "see the shimmer of the orb dance across its surface.",
            "Yet, despite its unfamiliarity it feels both welcoming",
            "and warm. Perhaps this is the feeling of Pandora's Box.",
            "Perhaps we should try interacting with it.",
            "Who am I?",
            "You don't have to worry about that :)"
    );

    CommandWeeklyOrb() {
        ItemMeta orbMeta = orb.getItemMeta();
        orbMeta.setCustomModelData(1);
        orb.setItemMeta(orbMeta);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;
        Player player = (Player)sender;
        PlayerData playerData = Origins.playersData.get(player.getUniqueId());
        long currentTime = System.currentTimeMillis();
        long sinceLastOrb = currentTime - playerData.lastOrb;
        if (sinceLastOrb < Time.week) {
            player.sendMessage(String.format("You still have to wait %s to get the orb!",
                                Time.FormatLong(Time.week - sinceLastOrb)));
            return true;
        }

        if (player.getInventory().firstEmpty() == -1)
            player.getWorld().dropItem(player.getLocation(), orb);
        else
            player.getInventory().addItem(orb);

        player.sendMessage("You got the orb!");

        playerData.lastOrb = currentTime;
        return true;
    }
}