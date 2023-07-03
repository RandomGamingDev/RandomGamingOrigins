package me.randomgamingdev.randomgamingorigins.core.types;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class PlayerData {
    public Origin origin;
    public int abilityTimer;
    public Inventory pouch;
    public String deathCause;
    public boolean removeDeathCause;
    public boolean feared;
    public ArrayList<Entity> summons;
    public long lastOrb;

    public void Init(Origin origin,
                     int abilityTimer,
                     Inventory inventory,
                     String deathCause,
                     boolean removeDeathCause,
                     boolean feared,
                     ArrayList<Entity> summons,
                     long lastOrb) {
        this.origin = origin;
        this.abilityTimer = abilityTimer;
        this.pouch = inventory;
        this.deathCause = deathCause;
        this.removeDeathCause = removeDeathCause;
        this.feared = feared;
        this.summons = summons;
        this.lastOrb = lastOrb;
    }

    public PlayerData(Origin origin,
                      int abilityTimer,
                      Inventory inventory,
                      String deathCause,
                      boolean removeDeathCause,
                      boolean feared,
                      ArrayList<Entity> summons,
                      long lastOrb) {
        Init(origin, abilityTimer, inventory, deathCause, removeDeathCause, feared, summons, lastOrb);
    }

    public void Init() {
        Init(Origin.Null, 0, null, null, false, false, new ArrayList<Entity>(), 0);
    }

    public PlayerData() {
        Init();
    }
}
