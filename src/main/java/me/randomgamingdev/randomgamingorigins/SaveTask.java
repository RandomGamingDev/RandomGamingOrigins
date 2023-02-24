package me.randomgamingdev.randomgamingorigins;

import com.google.gson.Gson;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;
import java.util.UUID;

public class SaveTask extends BukkitRunnable {
    @Override
    public void run() {
        Origins.Save(Origins.saveFileName);
    }
}
