package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import me.randomgamingdev.randomgamingorigins.other.Pair;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NullOrigin {
    public Origin origin = Origin.Null;
    public String name = "Null";
    public ItemStack dispItem = null;
    public Object[] initEffects = new Object[]{};
    public int maxHealth = 10 * 2;

    public void applyInv(Player player, PlayerData playerData) {
        playerData.pouch = null;
    }

    public void dropInv(Player player, PlayerData playerData) {
        if (playerData.pouch != null)
            for (ItemStack item : playerData.pouch.getContents())
                if (item != null)
                    player.getWorld().dropItem(player.getLocation(), item);
    }

    public void applyAttribs(Player player, PlayerData playerData) {
        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0);
        player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(0);
        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1);
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0);
        player.getAttribute(Attribute.GENERIC_LUCK).setBaseValue(0);
        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1);
    }

    public void applyCustom(Player player, PlayerData playerData) {

    }

    public void clearEffects(Player player, PlayerData playerData) {
        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
    }

    public void applyEffects(Player player, PlayerData playerData) {
        for (Object effectObj : origin.origin.initEffects) {
            Pair<PotionEffectType, Integer> effect = (Pair<PotionEffectType, Integer>)effectObj;
            player.addPotionEffect(new PotionEffect(effect.first, Integer.MAX_VALUE, effect.second, true, false));
        }
    }

    public void applyMaxHealth(Player player, PlayerData playerData) {
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(playerData.origin.origin.maxHealth);
    }

    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event, PlayerData playerData) {

    }

    public void onEntityDamageByPlayerEvent(EntityDamageByEntityEvent event, PlayerData playerData) {

    }

    public void onEntityTargetPlayerEvent(EntityTargetEvent event, PlayerData playerData) {

    }

    public void onPlayerResurrectEvent(EntityResurrectEvent event, PlayerData playerData) {

    }

    public void onPlayerDamageEvent(EntityDamageEvent event, PlayerData playerData) {

    }

    public void onPlayerItemDamageEvent(PlayerItemDamageEvent event, PlayerData playerData) {

    }

    public void onPlayerInteractEvent(PlayerInteractEvent event, PlayerData playerData) {

    }

    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event, PlayerData playerData) {

    }

    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event, PlayerData playerData) {

    }

    public void onPlayerDeathEvent(PlayerDeathEvent event, PlayerData playerData) {

    }

    public void onPlayerRespawnEvent(PlayerRespawnEvent event, PlayerData playerData) {

    }

    public void perSecond(Player player, PlayerData playerData) {

    }

    public void perTick(Player player, PlayerData playerData) {

    }
}