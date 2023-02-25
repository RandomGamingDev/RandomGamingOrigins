package me.randomgamingdev.randomgamingorigins;

import org.bukkit.inventory.Inventory;

public class PlayerData {
    public Origin origin;
    public int abilityTimer;
    public Inventory inventory;
    public String deathCause;
    public boolean removeDeathCause;
    public boolean feared;

    public void Init(Origin origin,
                     int abilityTimer,
                     Inventory inventory,
                     String deathCause,
                     boolean removeDeathCause,
                     boolean feared) {
        this.origin = origin;
        this.abilityTimer = abilityTimer;
        this.inventory = inventory;
        this.deathCause = deathCause;
        this.removeDeathCause = removeDeathCause;
        this.feared = feared;
    }

    public PlayerData(Origin origin,
                      int abilityTimer,
                      Inventory inventory,
                      String deathCause,
                      boolean removeDeathCause,
                      boolean feared) {
        Init(origin, abilityTimer, inventory, deathCause, removeDeathCause, feared);
    }

    public PlayerData(Origin origin) {
        Init(origin, 0, null, null, false, false);
    }

    public PlayerData() {
        Init(Origin.Null, 0, null, null, false, false);
    }
}
