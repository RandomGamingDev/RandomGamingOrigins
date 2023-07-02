package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.core.tasks.OriginsSecondCalcTask;
import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import me.randomgamingdev.randomgamingorigins.other.Pair;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import static me.randomgamingdev.randomgamingorigins.core.OriginsGui.createGuiItem;

public class BlazebornOrigin extends NullOrigin {
    public BlazebornOrigin() {
        this.origin = Origin.Blazeborn;
        this.name = "Blazeborn";
        this.dispItem = createGuiItem(Material.BLAZE_POWDER, true,
                            "§r§fBlazeborn",
                            "§7- Fire Immunity: You don't take fire damage",
                            "§7- Burning Wrath: You deal more damage while on fire",
                            "§7- Hydrophobia: You take damage in water as if it were lava");
        this.initEffects = new Object[]{ new Pair(PotionEffectType.FIRE_RESISTANCE, 0) };
        this.maxHealth = 10 * 2;
    }

    @Override
    public void perSecond(Player player, PlayerData playerData) {
        if (player.getFireTicks() > 0)
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(4);
        else
            player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1);
        OriginsSecondCalcTask.WaterDamage(player, playerData, 2);
        OriginsSecondCalcTask.RainDamage(player, playerData, 1);
    }
}
