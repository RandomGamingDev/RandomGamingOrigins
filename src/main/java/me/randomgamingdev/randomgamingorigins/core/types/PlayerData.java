package me.randomgamingdev.randomgamingorigins.core.types;

import org.bukkit.inventory.Inventory;

public class PlayerData {
    public Origin origin;
    public int abilityTimer;
    public Inventory inventory;
    public String deathCause;
    public boolean removeDeathCause;
    public boolean feared;
    public long lastOrb;

    public void Init(Origin origin,
                     int abilityTimer,
                     Inventory inventory,
                     String deathCause,
                     boolean removeDeathCause,
                     boolean feared,
                     long lastOrb) {
        this.origin = origin;
        this.abilityTimer = abilityTimer;
        this.inventory = inventory;
        this.deathCause = deathCause;
        this.removeDeathCause = removeDeathCause;
        this.feared = feared;
        this.lastOrb = lastOrb;
    }

    public PlayerData(Origin origin,
                      int abilityTimer,
                      Inventory inventory,
                      String deathCause,
                      boolean removeDeathCause,
                      boolean feared,
                      long lastOrb) {
        Init(origin, abilityTimer, inventory, deathCause, removeDeathCause, feared, lastOrb);
    }

    public void Init() {
        Init(Origin.Null, 0, null, null, false, false, 0);
    }

    public PlayerData() {
        Init();
    }
}
