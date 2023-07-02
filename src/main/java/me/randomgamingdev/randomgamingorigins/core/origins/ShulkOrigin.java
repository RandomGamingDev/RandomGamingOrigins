package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import me.randomgamingdev.randomgamingorigins.other.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

import static me.randomgamingdev.randomgamingorigins.core.OriginsGui.createGuiItem;

public class ShulkOrigin extends NullOrigin {
    public ShulkOrigin() {
        this.origin = Origin.Shulk;
        this.name = "Shulk";
        this.dispItem = createGuiItem(Material.SHULKER_SHELL, true,
                        "§r§fShulk",
                        "§7- Heavy Shell: You loose hunger faster",
                        "§7- Shulker Inventory: By pressing your offhand swap key you",
                        "§7can open up a second inventory with 9 slots that's preserved",
                        "§7even through death",
                        "§7- Hard Shell: You have natural armor",
                        "§7- Unwieldy: You cannot use shields");
        this.initEffects = new Object[]{ new Pair(PotionEffectType.HUNGER, 0) };
        this.maxHealth = 10 * 2;
    }

    @Override
    public void applyInv(Player player, PlayerData playerData) {
        playerData.pouch = Bukkit.createInventory(null, 1 * 9,
                String.format("§2Your %s Inventory", this.name));
    }

    @Override
    public void applyAttribs(Player player, PlayerData playerData) {
        super.applyAttribs(player, playerData);
        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(6);
    }

    @Override
    public void onPlayerInteractEvent(PlayerInteractEvent event, PlayerData playerData) {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();
        if (item == null)
            return;
        final Material itemType = item.getType();

        final PlayerInventory inventory = player.getInventory();
        final ItemStack handItem = inventory.getItemInMainHand();

        if (!itemType.equals(Material.SHIELD))
            return;

        player.sendMessage(String.format("%s's can't use shields!", origin.origin.name));

        Location location = player.getLocation();
        World world = player.getWorld();
        if (handItem.getType().equals(Material.SHIELD))
            inventory.setItemInMainHand(null);
        else
            inventory.setItemInOffHand(null);
        world.dropItem(location, item);
    }

    @Override
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event, PlayerData playerData) {
        final Player player = event.getPlayer();

        event.setCancelled(true);
        player.openInventory(playerData.pouch);
    }
}
