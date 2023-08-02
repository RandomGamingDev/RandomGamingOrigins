package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.core.OriginManager;
import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import me.randomgamingdev.randomgamingorigins.other.Pair;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import static me.randomgamingdev.randomgamingorigins.core.OriginsGui.createGuiItem;

public class StarbornOrigin extends NullOrigin {
    public StarbornOrigin() {
        this.origin = Origin.Starborn;
        this.name = "Starborn";
        this.dispItem = createGuiItem(Material.NETHER_STAR, true,
                  "§r§fStarborn",
                  "§7- Wanderer of the Stars: As night time comes you aren't",
                        "§7able to sleep. You love going at during the night to watch",
                        "§7and gaze at the beautiful stars",
                        "§7- Cosmic Warp: You can teleport 20 blocks where you're looking",
                        "§7with a cooldown of 15 seconds by pressing your offhands swap key",
                        "§7- Shooting Star: You can shoot a star beam to deal 5 hearts of",
                        "§7damage with a 30 second cooldown by shifting and pressing your",
                        "§7offhand swap key",
                        "§7- Born from the Stars: During the night when you can observe",
                        "§7the stars, the cosmos grants you a mysetrious power that results",
                        "§7in regeneration, speed, and jump 1",
                        "§7- Supernova: All the collected star energy that you've accumulated",
                        "§7throughout your time within the stars needs to go somewhere, and thus",
                        "§7manifests as an explosion upon your demise.",
                        "§7- Starwatcher: You just love to watch the stars, and when not exposed",
                        "§7to them you get a little sad and will get slowness 1 (this includes the",
                        "§7nether and end)");
        this.initEffects = new Object[]{};
        this.maxHealth = 10 * 2;
    }

    @Override
    public void perPlayerPerSecond(Player player, PlayerData playerData) {
        World world = player.getWorld();
        long time = world.getTime();
        boolean isDay = time > 0 && time < 12300;

        if (!isDay && world.getEnvironment().equals(World.Environment.NORMAL)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 2 * 20, 0, true, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2 * 20, 0, true, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2 * 20, 0, true, false));
        }
        else
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2 * 20, 0, true, false));
    }

    @Override
    public void onPlayerDeathEvent(PlayerDeathEvent event, PlayerData playerData) {
        Player player = event.getEntity().getPlayer();
        Location location = player.getLocation();
        World world = player.getWorld();

        world.createExplosion(location, 3, true, true);
    }

    @Override
    public void onPlayerBedEnterEvent(PlayerBedEnterEvent event, PlayerData playerData) {
        event.setCancelled(true);
    }

    @Override
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event, PlayerData playerData) {
        Player player = event.getPlayer();

        if (playerData.abilityTimer > 0)
            return;

        event.setCancelled(true);

        World world = player.getWorld();
        Location location = player.getLocation();
        Vector direction = location.getDirection();

        if (player.isSneaking()) {
            playerData.abilityTimer = 30;
            location.setY(location.getY() + 1);
            int distance = 20;
            RayTraceResult hitResult = world.rayTraceEntities(location, direction, distance, 0.5, entity -> !(entity == player));
            if (hitResult != null) {
                Entity hitEntity = hitResult.getHitEntity();
                if (hitEntity != null && hitEntity instanceof Damageable)
                    ((Damageable)hitEntity).damage(5 * 2, player);
            }

            for (int i = 1; i <= distance; i++)
                world.spawnParticle(Particle.ELECTRIC_SPARK, location.add(direction.multiply(1)), 16);
            return;
        }

        playerData.abilityTimer = 15;
        location.add(direction.multiply(20));
        player.teleport(location);
        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 15 * 20, 0, true, true));
    }
}