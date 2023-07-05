package me.randomgamingdev.randomgamingorigins.core.origins;

//made by ceeque :D
import me.randomgamingdev.randomgamingorigins.core.OriginManager;
import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import me.randomgamingdev.randomgamingorigins.other.Pair;
import org.bukkit.Material;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import static me.randomgamingdev.randomgamingorigins.core.OriginsGui.createGuiItem;

public class HealerOrigin extends NullOrigin {
    public HealerOrigin() {
        this.origin = Origin.Healer;
        this.name = "Healer";
        this.dispItem = createGuiItem(Material.LINGERING_POTION, true,
                "§r§fHealer",
                "§7- Heal: Spawn in Regeneration effect cloud using offhand key",
                "§7- Untrained: You have permanent weakness",
                "§7- Medic: Lack of battle training has caused you to only have 8 max hearts");
        this.initEffects = new Object[]{new Pair(PotionEffectType.WEAKNESS, 0) };
        this.maxHealth = 8 * 2;
    }


    @Override
    public void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event, PlayerData playerData) {
        Player player = event.getPlayer();

        if (playerData.abilityTimer > 0)
            return;
        playerData.abilityTimer = 30;
        event.setCancelled(true);
        ItemStack potion = new ItemStack(Material.LINGERING_POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();

        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 400, 0), true);
        potion.setItemMeta(potionMeta);

        ThrownPotion thrownPotion = player.launchProjectile(LingeringPotion.class);
        thrownPotion.setItem(potion);
    }
}
