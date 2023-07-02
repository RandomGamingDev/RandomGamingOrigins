package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import me.randomgamingdev.randomgamingorigins.other.Pair;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

import static me.randomgamingdev.randomgamingorigins.core.OriginManager.illagers;
import static me.randomgamingdev.randomgamingorigins.core.OriginsGui.createGuiItem;

public class EvokerOrigin extends NullOrigin {
    public EvokerOrigin() {
        this.origin = Origin.Evoker;
        this.name = "Evoker";
        this.dispItem = createGuiItem(Material.TOTEM_OF_UNDYING, true,
                        "§r§fEvoker",
                        "§7- Feared: You constantly have bad omen 5",
                        "§7- Fearsome: Villagers refuse to trade with you and",
                        "§7iron golems attack on sight",
                        "§7- Ally: You're allies with the rest of the illagers and",
                        "§7their spawn unless you provoke them",
                        "§7- Magician: You can summon vexes to fight for you at will",
                        "§7- Old Age: You have 1 less heart than a normal player and",
                        "§7 permanent slowness 1",
                        "§7- Unwieldy: You cannot use shields");
        this.initEffects = new Object[] {
                new Pair(PotionEffectType.BAD_OMEN, 4),
                new Pair(PotionEffectType.SLOW, 0),
        };
        this.maxHealth = 9 * 2;
    }

    @Override
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event, PlayerData playerData) {
        if (event.getRightClicked() instanceof Villager)
            event.setCancelled(true);
    }

    @Override
    public void onEntityDamageByPlayerEvent(EntityDamageByEntityEvent event, PlayerData playerData) {
        Entity aggressor = event.getDamager();
        if (!(aggressor instanceof Player))
            return;

        EntityType victim = event.getEntity().getType();

        for (EntityType illager : illagers)
            if (victim.equals(illager)) {
                playerData.feared = true;
                return;
            }
    }

    @Override
    public void onEntityTargetPlayerEvent(EntityTargetEvent event, PlayerData playerData) {
        EntityType entityType = event.getEntityType();

        if (playerData.feared)
            return;
        for (EntityType illager : illagers)
            if (entityType.equals(illager)) {
                event.setCancelled(true);
                break;
            }
    }

    @Override
    public void onPlayerInteractEvent(PlayerInteractEvent event, PlayerData playerData) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        Material itemType = item.getType();
        PlayerInventory inventory = player.getInventory();
        ItemStack handItem = inventory.getItemInMainHand();

        if (!itemType.equals(Material.SHIELD))
            return;
        player.sendMessage(String.format("%s's can't use shields!", this.name));

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
        Player player = event.getPlayer();

        if (playerData.abilityTimer > 0)
            return;

        playerData.abilityTimer = 60;
        event.setCancelled(true);
        for (int i = 0; i < 3; i++)
            player.getWorld().spawnEntity(player.getLocation(), EntityType.VEX);
    }

    @Override
    public void perSecond(Player player, PlayerData playerData) {
        GameMode gameMode = player.getGameMode();
        if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE)
            return;
        for (Entity entity : player.getNearbyEntities(32, 32, 32))
            if (entity.getType().equals(EntityType.IRON_GOLEM))
                ((IronGolem)entity).setTarget(player);
    }
}