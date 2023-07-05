package me.randomgamingdev.randomgamingorigins.core.types;

import me.randomgamingdev.randomgamingorigins.core.origins.*;
import me.randomgamingdev.randomgamingorigins.other.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.Phantom;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import static me.randomgamingdev.randomgamingorigins.core.OriginsGui.createGuiItem;

public enum Origin {
    Null(new NullOrigin()),
    Avian(new AvianOrigin()),
    Phantom(new PhantomOrigin()),
    Human(new HumanOrigin()),
    Elytrian(new ElytrianOrigin()),
    Blazeborn(new BlazebornOrigin()),
    Feline(new FelineOrigin()),
    Enderian(new EnderianOrigin()),
    Piglin(new PiglinOrigin()),
    Shulk(new ShulkOrigin()),
    Fox(new FoxOrigin()),
    Merling(new MerlingOrigin()),
    Frog(new FrogOrigin()),
    Evoker(new EvokerOrigin()),
    Starborn(new StarbornOrigin()),
    Lunar(new LunarOrigin());
    //Undead(new UndeadOrigin());

    public NullOrigin origin;

    Origin(NullOrigin origin) {
         this.origin = origin;
    }
}