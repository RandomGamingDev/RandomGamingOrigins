package me.randomgamingdev.randomgamingorigins.core.tasks;

import me.randomgamingdev.randomgamingorigins.core.OriginManager;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveTask extends BukkitRunnable {
    @Override
    public void run() {
        OriginManager.Save(OriginManager.saveFileName);
    }
}
