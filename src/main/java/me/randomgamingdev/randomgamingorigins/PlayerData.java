package me.randomgamingdev.randomgamingorigins;

import org.bukkit.inventory.Inventory;

public class PlayerData {
    public Origin origin;
    public int abilityTimer;
    public Inventory inventory;
    public String deathCause;
    public boolean removeDeathCause;

    public void Init(Origin origin,
                     int abilityTimer,
                     Inventory inventory,
                     String deathCause,
                     boolean removeDeathCause) {
        this.origin = origin;
        this.abilityTimer = abilityTimer;
        this.inventory = inventory;
        this.deathCause = deathCause;
        this.removeDeathCause = removeDeathCause;
    }

    public PlayerData(Origin origin,
                      int abilityTimer,
                      Inventory inventory,
                      String deathCause,
                      boolean removeDeathCause) {
        Init(origin, abilityTimer, inventory, deathCause, removeDeathCause);
    }

    public PlayerData(Origin origin) {
        Init(origin, 0, null, null, false);
    }

    public PlayerData() {
        Init(Origin.Null, 0, null, null, false);
    }
}
