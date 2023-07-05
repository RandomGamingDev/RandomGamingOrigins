package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.core.tasks.OriginsSecondCalcTask;
import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import static me.randomgamingdev.randomgamingorigins.core.OriginsGui.createGuiItem;
import static me.randomgamingdev.randomgamingorigins.core.tasks.OriginsSecondCalcTask.RainDamage;

public class EnderianOrigin extends NullOrigin {
    public EnderianOrigin() {
        this.origin = Origin.Enderian;
        this.name = "Enderian";
        this.dispItem = createGuiItem(Material.ENDER_PEARL, true,
                        "§r§fEnderian",
                        "§7- Teleportation: Press you offhand swap key to throw a enderpearl",
                              "§7every 15 seconds",
                              "§7- Hydrophobia: You take damage in water as if it were lava");
        this.initEffects = new Object[]{};
        this.maxHealth = 10 * 2;
    }

    @Override
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event, PlayerData playerData) {
        Player player = event.getPlayer();

        if (playerData.abilityTimer > 0)
            return;
        playerData.abilityTimer = 15;
        event.setCancelled(true);
        player.launchProjectile(EnderPearl.class);
    }

    @Override
    public void perPlayerPerSecond(Player player, PlayerData playerData) {
        OriginsSecondCalcTask.WaterDamage(player, playerData, 1);
        OriginsSecondCalcTask.RainDamage(player, playerData, 1);
    }
}