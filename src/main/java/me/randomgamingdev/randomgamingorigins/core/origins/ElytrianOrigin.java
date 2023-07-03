package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static me.randomgamingdev.randomgamingorigins.core.OriginManager.elytraCode;
import static me.randomgamingdev.randomgamingorigins.core.OriginsGui.createGuiItem;

public class ElytrianOrigin extends NullOrigin {
    public ElytrianOrigin() {
        this.origin = Origin.Elytrian;
        this.name = "Elytrian";
        this.dispItem = createGuiItem(Material.ELYTRA, true,
                        "§r§fElytrian",
                        "§7- Winged: You have a elytra",
                        "§7- Gift of Wings: You can launch yourself by clicking",
                        "§7your offhand swap key every 15 seconds",
                        "§7- Arial Combat: While flying you deal more damage",
                        "§7- Need for Mobility: You cant wear chestplates");
        this.initEffects = new Object[]{};
        this.maxHealth = 10 * 2;
    }

    @Override
    public void applyCustom(Player player, PlayerData playerData) {
        PlayerInventory inventory = player.getInventory();
        ItemStack chestplate = inventory.getChestplate();

        World world = player.getWorld();
        Location location = player.getLocation();

        if (inventory.getChestplate() != null)
            for (ItemStack item : inventory.addItem(chestplate).values())
                world.dropItem(location, item);

        ItemStack elytra = new ItemStack(Material.ELYTRA, 1);
        ItemMeta meta = elytra.getItemMeta();
        meta.setCustomModelData(elytraCode);
        meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(String.format("%s's Elytrian Wings", player.getName()));
        elytra.setItemMeta(meta);
        player.getInventory().setChestplate(elytra);
    }

    @Override
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event, PlayerData playerData) {
        Player player = event.getPlayer();

        if (playerData.abilityTimer > 0)
            return;
        playerData.abilityTimer = 15;
        event.setCancelled(true);
        player.setVelocity(player.getVelocity().setY(2));
    }

    @Override
    public void perPlayerPerTick(Player player, PlayerData playerData) {
        if (player.isGliding())
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2, 0, true, false));
    }
}
