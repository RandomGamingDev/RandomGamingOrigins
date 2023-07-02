package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import me.randomgamingdev.randomgamingorigins.other.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static me.randomgamingdev.randomgamingorigins.core.OriginsGui.createGuiItem;

public class MerlingOrigin extends NullOrigin {
    public MerlingOrigin() {
        this.origin = Origin.Merling;
        this.name = "Merling";
        this.dispItem = createGuiItem(Material.COD, true,
                        "§r§fMerling",
                        "§7- Gills: You can breathe under water",
                        "§7- Adaptation: Water invigorates you, allowing you to mine faster",
                        "§7- Fins: You have permanent dolphins grace",
                        "§7- Gills: You cannot breath on land");
        this.initEffects = new Object[]{ new Pair(PotionEffectType.DOLPHINS_GRACE, 0) };
        this.maxHealth = 10 * 2;
    }

    @Override
    public void perTick(Player player, PlayerData playerData) {
        int remainingAir = player.getRemainingAir();
        if (player.isInWater()) {
            if (remainingAir < 300)
                player.setRemainingAir(remainingAir + 5);
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 2, 14, true, false));
            return;
        }
        if (remainingAir >= 0 && !player.hasPotionEffect(PotionEffectType.WATER_BREATHING))
            player.setRemainingAir(remainingAir - 6);
    }

    @Override
    public void perSecond(Player player, PlayerData playerData) {
        if (player.getRemainingAir() > 0)
            return;
        playerData.deathCause = String.format("%s drowned on land", player.getName());
        player.damage(1);
    }
}
