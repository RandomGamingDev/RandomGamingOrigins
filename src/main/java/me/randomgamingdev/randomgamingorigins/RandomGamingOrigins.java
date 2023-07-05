package me.randomgamingdev.randomgamingorigins;

import com.google.gson.Gson;
import me.randomgamingdev.randomgamingorigins.commands.*;
import me.randomgamingdev.randomgamingorigins.core.OriginManager;
import me.randomgamingdev.randomgamingorigins.core.OriginsGui;
import me.randomgamingdev.randomgamingorigins.core.tasks.OriginsSecondCalcTask;
import me.randomgamingdev.randomgamingorigins.core.tasks.OriginsTickCalcTask;
import me.randomgamingdev.randomgamingorigins.core.tasks.SaveTask;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public final class RandomGamingOrigins extends JavaPlugin {
    public static Gson gson = new Gson();
    public static Random rand = new Random();

    @Override
    public void onEnable() {
        System.out.println("RandomGamingOrigins is starting up!");
        OriginManager.plugin = this;
        this.getCommand("ping").setExecutor(new CommandPing());
        this.getServer().getPluginManager().registerEvents(new OriginsGui(this), this);
        this.getServer().getPluginManager().registerEvents(new OriginManager(), this);
        this.getCommand("originsgui").setExecutor(new CommandOriginsGui());
        this.getCommand("origins").setExecutor(new CommandOrigins());
        this.getCommand("getorigin").setExecutor(new CommandGetOrigin());
        this.getCommand("originssave").setExecutor(new CommandOriginsSave());
        this.getCommand("originsload").setExecutor(new CommandOriginsLoad());
        this.getCommand("weeklyorb").setExecutor(new CommandWeeklyOrb());
        this.getCommand("getpouch").setExecutor(new CommandGetPouch());
        this.getCommand("setorigin").setExecutor(new CommandSetOrigin());
        this.getCommand("ability").setExecutor(new CommandAbility());
        OriginManager.Load(OriginManager.saveFileName);
        for (Player player : this.getServer().getOnlinePlayers())
            if (OriginManager.GetPlayerData(player) == null)
                OriginsGui.onJoin(player);
        new OriginsTickCalcTask(this).runTaskTimer(this, 0, 1);
        new OriginsSecondCalcTask(this).runTaskTimer(this, 0, 20);
        new SaveTask().runTaskTimer(this, 60 * 20, 60 * 20);
    }

    @Override
    public void onDisable() {
        OriginManager.Save(OriginManager.saveFileName);
        System.out.println("RandomGamingOrigins is shutting down!");
    }
}
