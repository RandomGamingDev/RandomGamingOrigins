package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.RandomGamingOrigins;
import me.randomgamingdev.randomgamingorigins.core.OriginManager;
import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PersistentKey;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

import static me.randomgamingdev.randomgamingorigins.core.OriginManager.*;
import static me.randomgamingdev.randomgamingorigins.core.OriginsGui.createGuiItem;

public class PiglinOrigin extends NullOrigin {
    public PiglinOrigin() {
        this.origin = Origin.Piglin;
        this.name = "Piglin";
        this.dispItem = createGuiItem(Material.GOLDEN_SWORD, true,
                        "§r§fPiglin",
                        "§7- Gold Digger: Gold tools deal 1.5x more damage",
                        "§7- Piglin Lineage: You have 8 hearts instead of 10",
                        "§7- Praise the Gods: Piglins have a natural connection",
                        "§7to their god and can use it to trade the gold in their hand",
                        "§7- Golden tools break WAY slower",
                        "§7Gold armor better protects you and from all forms",
                        "§7- Ally: As a piglin you're automatically allied with them",
                        "§7unless you hurt one of them",
                        "§7of damage with a full armor set giving 20% reduction");
        this.initEffects = new Object[]{};
        this.maxHealth = 8 * 2;
    }

    @Override
    public void onPlayerItemDamageEvent(PlayerItemDamageEvent event, PlayerData playerData) {
        for (Material tool : goldenTools)
            if (event.getItem().getType() == tool && RandomGamingOrigins.rand.nextBoolean())
                event.setCancelled(true);
    }

    @Override
    public void onEntityDamageByPlayerEvent(EntityDamageByEntityEvent event, PlayerData playerData) {
        Entity victim = event.getEntity();
        Player player = (Player)event.getDamager();
        Material item = player.getInventory().getItemInMainHand().getType();

        for (Material tool : OriginManager.goldenTools)
            if (item == tool)
                event.setDamage(event.getDamage() * 1.5);

        EntityType victimType = victim.getType();

        for (EntityType piglin : piglins)
            if (victimType.equals(piglin)) {
                playerData.feared = true;
                return;
            }

        if (playerData.feared)
            return;

        Entity targetEntity = event.getEntity();

        if (!(targetEntity instanceof LivingEntity))
            return;

        for (Entity entity : ((Player)event.getDamager()).getNearbyEntities(32, 32, 32))
            for (EntityType piglin : piglins)
                if (entity.getType().equals(piglin))
                    ((Mob)entity).setTarget((LivingEntity)targetEntity);
    }

    @Override
    public void onPlayerDamageByEntityEvent(EntityDamageByEntityEvent event, PlayerData playerData) {
        Player player = (Player)event.getEntity();
        PlayerInventory inventory = player.getInventory();

        // The armor cancelling effect at most goes up to 20%
        final int maxArmorVal = 5 + 8 + 7 + 4;
        int armorVal = 0;

        ItemStack helmet = inventory.getHelmet();
        if (helmet != null && helmet.getType() == OriginManager.goldenArmors[0])
            armorVal += 5;
        ItemStack chestplate = inventory.getChestplate();
        if (chestplate != null && chestplate.getType() == OriginManager.goldenArmors[1])
            armorVal += 8;
        ItemStack leggings = inventory.getLeggings();
        if (leggings != null && leggings.getType() == OriginManager.goldenArmors[2])
            armorVal += 7;
        ItemStack boots = inventory.getBoots();
        if (boots != null && boots.getType() == OriginManager.goldenArmors[3])
            armorVal += 4;

        double gArmorPerc = (1.0 - 0.2 * ((double)armorVal / (double)maxArmorVal));
        event.setDamage(event.getDamage() * gArmorPerc);

        if (playerData.feared)
            return;
        for (Entity entity : ((Player)event.getEntity()).getNearbyEntities(32, 32, 32))
            for (EntityType piglin : piglins)
                if (entity.getType().equals(piglin))
                    ((Mob)entity).setTarget((LivingEntity)event.getDamager());
    }

    @Override
    public void onEntityTargetPlayerEvent(EntityTargetEvent event, PlayerData playerData) {
        Entity entity = event.getEntity();
        EntityType entityType = event.getEntityType();

        if (playerData.feared)
            return;

        for (EntityType piglin : piglins)
            if (entityType.equals(piglin)) {
                event.setCancelled(true);
                break;
            }
    }

    @Override
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event, PlayerData playerData) {
        event.setCancelled(true);

        Player player = event.getPlayer();
        PlayerInventory playerInventory = player.getInventory();
        ItemStack handItem = playerInventory.getItemInMainHand();
        if (handItem == null)
            return;

        if (handItem.getType() != Material.GOLD_INGOT)
            return;

        handItem.setAmount(handItem.getAmount() - 1);
        playerInventory.setItemInMainHand(handItem);

        Material drop = new Material[]{
                Material.OBSIDIAN,
                Material.QUARTZ,
                Material.GOLDEN_APPLE,
                Material.PIGLIN_BANNER_PATTERN,
                Material.GILDED_BLACKSTONE,
                Material.PORKCHOP
        }[(int)Math.floor(Math.random() * 5)];

        World world = player.getWorld();
        Location location = player.getLocation();

        int amount = 1;
        if (drop != Material.PIGLIN_BANNER_PATTERN)
            amount = (int)Math.floor(Math.random() * 4) + 1;
        world.dropItem(location, new ItemStack(drop, amount));
    }
}