package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.RandomGamingOrigins;
import me.randomgamingdev.randomgamingorigins.core.OriginManager;
import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static me.randomgamingdev.randomgamingorigins.core.OriginManager.goldenTools;
import static me.randomgamingdev.randomgamingorigins.core.OriginsGui.createGuiItem;

public class PiglinOrigin extends NullOrigin {
    public PiglinOrigin() {
        this.origin = Origin.Piglin;
        this.name = "Piglin";
        this.dispItem = createGuiItem(Material.GOLDEN_SWORD, true,
                        "§r§fPiglin",
                        "§7- Gold Digger: Gold tools deal more damage",
                        "§7- Piglin Lineage: You have 8 hearts instead of 10",
                        "§7- Golden tools break WAY slower",
                        "§7- More features coming soon!");
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
    public void perTick(Player player, PlayerData playerData) {
        Material item = player.getInventory().getItemInMainHand().getType();
        for (Material tool : OriginManager.goldenTools)
            if (item == tool)
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2, 1, true, false));
    }
}
