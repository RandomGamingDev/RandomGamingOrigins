package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.core.OriginManager;
import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PersistentKey;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import me.randomgamingdev.randomgamingorigins.other.Pair;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;

import javax.naming.Name;

import java.util.UUID;

import static me.randomgamingdev.randomgamingorigins.core.OriginManager.illagers;
import static me.randomgamingdev.randomgamingorigins.core.OriginsGui.createGuiItem;

public class EvokerOrigin extends NullOrigin {
    public EvokerOrigin() {
        this.origin = Origin.Evoker;
        this.name = "Evoker";
        this.dispItem = createGuiItem(Material.TOTEM_OF_UNDYING, true,
                        "§r§fEvoker",
                        "§7- Feared: You constantly have bad omen 5",
                        "§7- It's good for your bones: You can regain bad omen 5",
                        "§7by drinking milk",
                        "§7- Fearsome: Villagers refuse to trade with you and",
                        "§7iron golems attack on sight",
                        "§7- Ally: You're allies with the rest of the illagers and",
                        "§7their spawn  who will fight for you unless you provoke them",
                        "§7- Magician: You can summon 5 vexes to fight for you at will",
                        "§7every 30 seconds",
                        "§7- Chomp: You can summon a line of 16 fangs in front of you",
                        "§7to damage other players by pressing your offhand swap key and",
                        "§7sneaking",
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
        Entity victim = event.getEntity();
        EntityType victimType = victim.getType();

        for (EntityType illager : illagers)
            if (victimType.equals(illager)) {
                if (victimType.equals(EntityType.VEX)) {
                    PersistentDataContainer persistentDataContainer = victim.getPersistentDataContainer();
                    if (persistentDataContainer.has(new NamespacedKey(OriginManager.plugin, PersistentKey.Summoner.strVal), PersistentDataType.STRING)) {
                        if (UUID.fromString(
                                persistentDataContainer.get(new NamespacedKey(OriginManager.plugin, PersistentKey.Summoner.strVal), PersistentDataType.STRING)
                            ).equals(((Player)event.getDamager()).getUniqueId())
                        )
                            return;
                    }
                }
                playerData.feared = true;
                return;
            }

        if (playerData.feared)
            return;

        for (Entity entity : ((Player)event.getDamager()).getNearbyEntities(32, 32, 32))
            for (EntityType illager : illagers)
                if (entity.getType().equals(illager))
                    ((Mob)entity).setTarget((LivingEntity)event.getEntity());
    }

    @Override
    public void onPlayerDamageByEntityEvent(EntityDamageByEntityEvent event, PlayerData playerData) {
        if (playerData.feared)
            return;
        for (Entity entity : ((Player)event.getEntity()).getNearbyEntities(32, 32, 32))
            for (EntityType illager : illagers)
                if (entity.getType().equals(illager))
                    ((Mob)entity).setTarget((LivingEntity)event.getDamager());
    }

    @Override
    public void onEntityTargetPlayerEvent(EntityTargetEvent event, PlayerData playerData) {
        Entity entity = event.getEntity();
        EntityType entityType = event.getEntityType();

        if (!playerData.feared) {
            for (EntityType illager : illagers)
                if (entityType.equals(illager)) {
                    event.setCancelled(true);
                    break;
                }
        }
        else {
            if (entityType.equals(EntityType.VEX)) {
                PersistentDataContainer persistentDataContainer = entity.getPersistentDataContainer();
                if (persistentDataContainer.has(new NamespacedKey(OriginManager.plugin, PersistentKey.Summoner.strVal), PersistentDataType.STRING)) {
                    if (UUID.fromString(
                            persistentDataContainer.get(new NamespacedKey(OriginManager.plugin, PersistentKey.Summoner.strVal), PersistentDataType.STRING)
                    ).equals(((Player) event.getTarget()).getUniqueId())
                    )
                        event.setCancelled(true);
                }
            }
        }
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
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event, PlayerData playerData) {
        Player player = event.getPlayer();

        if (playerData.abilityTimer > 0)
            return;

        event.setCancelled(true);

        if (player.isSneaking()) {
            playerData.abilityTimer = 20;
            double y = player.getLocation().getY();
            for (int i = 1; i <= 16; i++) {
                Location fangLocation = player.getLocation();
                fangLocation.add(fangLocation.getDirection().multiply(i));
                fangLocation.setY(y);
                Material block = fangLocation.getBlock().getType();
                player.getWorld().spawnEntity(fangLocation, EntityType.EVOKER_FANGS);
            }
            return;
        }

        playerData.abilityTimer = 30;
        for (int i = 0; i < 5; i++) {
            Entity vex = player.getWorld().spawnEntity(player.getLocation(), EntityType.VEX);
            PersistentDataContainer persistentDataContainer = vex.getPersistentDataContainer();
            persistentDataContainer.set(new NamespacedKey(OriginManager.plugin, PersistentKey.Summoner.strVal), PersistentDataType.STRING, player.getUniqueId().toString());
            persistentDataContainer.set(new NamespacedKey(OriginManager.plugin, PersistentKey.SummonedAt.strVal), PersistentDataType.LONG, System.currentTimeMillis());
        }
    }


    @Override
    public void perSecond() {
        for (World world : OriginManager.plugin.getServer().getWorlds())
            for (Entity entity : world.getEntities())
                switch (entity.getType()) {
                    case VEX:
                        PersistentDataContainer persistentDataContainer = entity.getPersistentDataContainer();
                        if (!persistentDataContainer.has(new NamespacedKey(OriginManager.plugin, PersistentKey.SummonedAt.strVal), PersistentDataType.LONG))
                            break;
                        long spawnTime = persistentDataContainer.get(new NamespacedKey(OriginManager.plugin, PersistentKey.SummonedAt.strVal), PersistentDataType.LONG);
                        long currentTime = System.currentTimeMillis();
                        long sinceSpawn = currentTime - spawnTime;
                        if (sinceSpawn > 60 * 1000)
                            ((Mob)entity).setHealth(0);
                        break;
                }
    }

    @Override
    public void perPlayerPerSecond(Player player, PlayerData playerData) {
        GameMode gameMode = player.getGameMode();
        if (gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE)
            return;
        for (Entity entity : player.getNearbyEntities(32, 32, 32))
            switch (entity.getType()) {
                case IRON_GOLEM:
                    ((IronGolem)entity).setTarget(player);
                    break;
            }
    }
}