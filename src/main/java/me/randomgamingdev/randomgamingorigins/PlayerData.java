package me.randomgamingdev.randomgamingorigins;

import org.bukkit.inventory.Inventory;

public class PlayerData {
    public Origin origin;
    public int abilityTimer;
    public Inventory inventory;

    public void Init(Origin origin, int abilityTimer, Inventory inventory) {
        this.origin = origin;
        this.abilityTimer = abilityTimer;
        this.inventory = inventory;
    }

    public PlayerData(Origin origin, int abilityTimer, Inventory inventory) {
        Init(origin, abilityTimer, inventory);
    }

    public PlayerData(Origin origin) {
        Init(origin, 0, null);
    }

    public PlayerData() {
        Init(Origin.Null, 0, null);
    }
}
