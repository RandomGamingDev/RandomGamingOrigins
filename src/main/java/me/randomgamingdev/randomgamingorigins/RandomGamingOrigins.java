package me.randomgamingdev.randomgamingorigins;

import com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class RandomGamingOrigins extends JavaPlugin {
    public static Gson gson = new Gson();

    @Override
    public void onEnable() {
        System.out.println("RandomGamingOrigins is starting up!");
        this.getCommand("ping").setExecutor(new CommandPing());
        this.getServer().getPluginManager().registerEvents(new OriginsGui(this), this);
        this.getServer().getPluginManager().registerEvents(new Origins(), this);
        this.getCommand("originsgui").setExecutor(new CommandOriginsGui());
        this.getCommand("origins").setExecutor(new CommandOrigins());
        this.getCommand("getorigin").setExecutor(new CommandGetOrigin());
        this.getCommand("save").setExecutor(new CommandSave());
        this.getCommand("load").setExecutor(new CommandLoad());
        Origins.Load(Origins.saveFileName);
        for (Player player : this.getServer().getOnlinePlayers())
            if (Origins.playersData.get(player.getUniqueId()) == null)
                OriginsGui.onJoin(player);
        new OriginsSecondCalcTask(this).runTaskTimer(this, 0, 20);
        new OriginsTickCalcTask(this).runTaskTimer(this, 0, 1);
        new SaveTask().runTaskTimer(this, 60 * 20, 60 * 20);
    }

    @Override
    public void onDisable() {
        Origins.Save(Origins.saveFileName);
        System.out.println("RandomGamingOrigins is starting shutting down!");
    }
}
