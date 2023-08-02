package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.core.tasks.OriginsSecondCalcTask;
import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import static jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle.block;
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
                              "§7- Silk touch: You can mine blocks that otherwise wouldn't",
                              "§7have dropped anything, like for instance spawners",
                              "§7- Resistance: You don't take damage from using enderpearls",
                              "§7- Hydrophobia: You take damage in water as if it were lava");
        this.initEffects = new Object[]{};
        this.maxHealth = 10 * 2;
    }

    @Override
    public void onPlayerTeleportEvent(PlayerTeleportEvent event, PlayerData playerData) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL)
            return;

        Player player = event.getPlayer();
        Location to = event.getTo();

        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            event.setCancelled(true);
            player.teleport(to);
        }
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
    public void onPlayerBrokenBlockDropItemEvent(BlockDropItemEvent event, PlayerData playerData) {
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR)
            return;

        BlockState blockState = event.getBlockState();
        if (blockState.getBlock() == null)
            return;

        Material blockType = blockState.getType();
        if (blockType == Material.AIR)
            return;

        if (event.getItems().size() == 0 && blockType.isItem())
            player.getWorld().dropItem(player.getLocation(), new ItemStack(blockType, 1));
    }

    @Override
    public void onPlayerBreakBlockEvent(BlockBreakEvent event, PlayerData playerData) {
        event.setDropItems(true);
    }

    @Override
    public void perPlayerPerSecond(Player player, PlayerData playerData) {
        OriginsSecondCalcTask.WaterDamage(player, playerData, 1);
        OriginsSecondCalcTask.RainDamage(player, playerData, 1);
    }
}