package me.randomgamingdev.randomgamingorigins.core;

import me.randomgamingdev.randomgamingorigins.RandomGamingOrigins;
import me.randomgamingdev.randomgamingorigins.tasks.OpenInvTask;
import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.UUID;

public class OriginsGui implements Listener {
    private static RandomGamingOrigins plugin = null;
    public static Inventory inventory;

    public OriginsGui(RandomGamingOrigins plugin) {
        this.plugin = plugin;
        InitGui();
    }

    public static void InitGui() {
        Origin[] origins = Origin.values();
        inventory = Bukkit.createInventory(null, (int)Math.ceil((float)origins.length / 9) * 9, "§2Origins");
        for (int i = 1; i < origins.length; i++)
            inventory.addItem(origins[i].item);
    }

    public static void onJoin(Player player) {
        PlayerData playerData = OriginManager.playersData.get(player.getUniqueId());
        if (playerData == null) {
            OriginManager.playersData.put(player.getUniqueId(), new PlayerData());
            Gui(player, false);
            return;
        }
        if (playerData.origin == Origin.Null)
            Gui(player, false);
    }

    @EventHandler
    public static void onJoin(PlayerJoinEvent event) {
        onJoin(event.getPlayer());
    }

    public static ItemStack createGuiItem(Material material, boolean enchanted, String name, String... lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();

        if (enchanted) {
            meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);

        return item;
    }

    public static void Gui(Player player, boolean orb) {
        if (orb)
            OriginManager.playersData.get(player.getUniqueId()).origin = Origin.Null;
        player.openInventory(inventory);
    }

    @EventHandler
    public static void onInventoryClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(inventory))
            return;

        event.setCancelled(true);
        final ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null)
            return;
        int rawSlot = event.getRawSlot();
        if (rawSlot >= Origin.values().length - 1)
            return;
        Origin origin = Origin.values()[rawSlot + 1];

        Player player = (Player)event.getWhoClicked();
        UUID playerId = player.getUniqueId();
        PlayerData playerData = OriginManager.playersData.get(playerId);
        if (playerData.origin != Origin.Null)
            return;
        OriginManager.playersData.get(playerId).origin = origin;
        player.closeInventory();
        Bukkit.getServer().getScheduler().runTaskLater(plugin, player::updateInventory, 1L);
        OriginManager.ApplyOrigin(player, playerData);
    }

    @EventHandler
    public static void onInventoryDrag(InventoryDragEvent event) {
        if (event.getInventory().equals(inventory))
            event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent event){
        Player player = (Player)event.getPlayer();
        if (event.getInventory().equals(inventory) && OriginManager.playersData.get(player.getUniqueId()).origin == Origin.Null)
            new OpenInvTask(this.plugin, player, inventory).runTaskLater(this.plugin, 1);
    }
}