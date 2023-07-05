package me.randomgamingdev.randomgamingorigins.core.origins;

import me.randomgamingdev.randomgamingorigins.core.OriginManager;
import me.randomgamingdev.randomgamingorigins.core.types.Origin;
import me.randomgamingdev.randomgamingorigins.core.types.PlayerData;
import me.randomgamingdev.randomgamingorigins.other.Pair;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static me.randomgamingdev.randomgamingorigins.core.OriginsGui.createGuiItem;

public class UndeadOrigin extends NullOrigin {
    public UndeadOrigin() {
        //this.origin = Origin.Undead;
        this.name = "Undead";
        this.dispItem = createGuiItem(Material.ROTTEN_FLESH, true,
            "§r§Undead",
            "§7- Herobrine's Conscript: Having been reanimated by Herobrine",
                "§7makes all undead creatures, spiders, and creepers ignore you",
                "§7- Herobrine's Strength: At night, your connection to Herobrine's",
                "§7strengthened, removing your weakness and slowness",
                "§7- Undead Steed: You can now ride spiders and undead horse variants",
                "§7- Reduced Metabolism: As a reanimated undead creature you have little",
                "§7to no metabolism, making you lose hunger a lot more slowly",
                "§7- Feeble Body: Being undead, you're weak and have weakness during the night",
                "§7when not connected to Herobrine as strongly and 8 hearts",
                "§7- Strength in Numbers: As fellow and lower levelled undeads, other undeads",
                "§7will rush to aid you in battle if you get hurt or attack",
                "§7- Unholy creature: You will burn whenever exposed to the sun unless you're",
                "§7wearing a helmet. If you're wearing a helmet that helmet will take 1 damage",
                "§7every second",
                "§7- Herobrine's Curse: You will crave flesh, and be poisoned by plant",
                "§7based foods (especially so for golden apples). Healing potions will hurt you ",
                "§7and instant damage potions will heal you",
                "§7- Outcast: You won't be able to tame living entities nor interact with villagers",
                "§7and both iron golems and snow golems will attack you");
        this.initEffects = new Object[]{};
        this.maxHealth = 10 * 2;
    }

    @Override
    public void onEntityTargetPlayerEvent(EntityTargetEvent event, PlayerData playerData) {
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity))
            return;

        LivingEntity lEntity = (LivingEntity)entity;
        if (OriginManager.IsHerobrineEntity(lEntity))
            event.setCancelled(true);
    }

    @Override
    public void perPlayerPerSecond(Player player, PlayerData playerData) {
        World world = player.getWorld();
        long time = world.getTime();
        boolean isDay = time > 0 && time < 12300;

        if (!isDay) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 2 * 20, 0, true, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2 * 20, 0, true, false));
        }
        else
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2 * 20, 0, true, false));
    }
}
