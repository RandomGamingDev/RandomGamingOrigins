package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import me.randomgamingdev.randomgamingorigins.other.Pair;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static me.randomgamingdev.randomgamingorigins.core.OriginsGui.createGuiItem;

public class FoxOrigin extends NullOrigin {
    public FoxOrigin() {
        this.origin = Origin.Fox;
        this.name = "Fox";
        this.dispItem = createGuiItem(Material.SWEET_BERRIES, true,
                        "§r§fFox",
                        "§7- Pounce: Press your offhand swap key to get launched in the air",
                        "§7while falling you deal a lot of damage",
                        "§7- Light Body: You have permanent jump boost 1 and speed 1",
                        "§7- Mighty Mouth: By pressing your offhand swap key and sneaking, thus",
                        "§7allowing you to open up a second inventory with 9 slots that's preserved",
                        "§7even through death",
                        "§7- Acrobatics: You never take fall damage",
                        "§7- Nocturnal: You have permanent night vision",
                        "§7- Loyal: You're an ally with fox-kind and as such they will help you battle.",
                        "§7 They're so loyal in fact they won't fight back if you attack them. Don't do",
                        "§7 that tho. You don't wanna make them sad do ya?",
                        "§7- Berry Craver: You gain 1 extra hunger point from sweat berries",
                        "§7- Thorn-proof: You're immune to thorns, including both the enchants and",
                        "§7- objects like cacti, berry bushes, and dripstone",
                        "§7- Unwieldy: You cannot use shields",
                        "§7- Smaller Heart: You have 6 max hearts");
        this.initEffects = new Object[]{
                                new Pair(PotionEffectType.NIGHT_VISION, 0),
                                new Pair(PotionEffectType.JUMP, 0),
                                new Pair(PotionEffectType.SPEED, 1)
                            };
        this.maxHealth = 6 * 2;
    }

    @Override
    public void applyInv(Player player, PlayerData playerData) {
        playerData.pouch = Bukkit.createInventory(null, 1 * 9,
                String.format("§2Your %s Inventory", this.name));
    }

    @Override
    public void onEntityDamageByPlayerEvent(EntityDamageByEntityEvent event, PlayerData playerData) {
        Entity victim = event.getEntity();
        for (Entity entity : ((Player)event.getDamager()).getNearbyEntities(32, 32, 32))
            if (entity.getType().equals(EntityType.FOX))
                ((Fox)entity).setTarget((LivingEntity)event.getEntity());
    }

    @Override
    public void onPlayerInteractEvent(PlayerInteractEvent event, PlayerData playerData) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null)
            return;
        Material itemType = item.getType();
        PlayerInventory inventory = player.getInventory();
        ItemStack handItem = inventory.getItemInMainHand();

        if (!itemType.equals(Material.SHIELD))
            return;
        player.sendMessage(String.format("%s's can't use shields!", this.name));

        Location location = player.getLocation();
        World world = player.getWorld();
        if (handItem != null && handItem.getType().equals(Material.SHIELD))
            inventory.setItemInMainHand(null);
        else
            inventory.setItemInOffHand(null);
        world.dropItem(location, item);
    }

    @Override
    public void onPlayerDamageEvent(EntityDamageEvent event, PlayerData playerData) {
        EntityDamageEvent.DamageCause damageCause = event.getCause();

        if (
                damageCause == EntityDamageEvent.DamageCause.FALL ||
                damageCause == EntityDamageEvent.DamageCause.THORNS ||
                damageCause == EntityDamageEvent.DamageCause.CONTACT
            )
                event.setCancelled(true);
    }

    @Override
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event, PlayerData playerData) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Material itemType = item.getType();

        if (itemType.equals(Material.SWEET_BERRIES))
            player.setFoodLevel(player.getFoodLevel() + 1);
    }

    @Override
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event, PlayerData playerData) {
        Player player = event.getPlayer();

        if (player.isSneaking()) {
            event.setCancelled(true);
            player.openInventory(playerData.pouch);
            return;
        }
        if (playerData.abilityTimer > 0)
            return;
        event.setCancelled(true);
        playerData.abilityTimer = 15;
        player.setVelocity(player.getVelocity().setY(1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2 * 20, 1, true, false));
    }

    @Override
    public void perPlayerPerSecond(Player player, PlayerData playerData) {
        GameMode gameMode = player.getGameMode();
        if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE)
            return;
        for (Entity entity : player.getNearbyEntities(8, 8, 8))
            if (entity.getType().equals(EntityType.FOX))
                ((Fox)entity).setFirstTrustedPlayer((AnimalTamer)player);
    }
}
