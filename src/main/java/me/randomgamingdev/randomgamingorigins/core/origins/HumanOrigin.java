package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import org.bukkit.Material;

import static me.randomgamingdev.randomgamingorigins.core.OriginsGui.createGuiItem;

public class HumanOrigin extends NullOrigin {
    public HumanOrigin() {
        this.origin = Origin.Human;
        this.name = "Human";
        this.dispItem = createGuiItem(Material.PLAYER_HEAD, true,
                        "§r§fHuman",
                        "§7- You're a normal human");
        this.initEffects = new Object[]{};
        this.maxHealth = 10 * 2;
    }
}
