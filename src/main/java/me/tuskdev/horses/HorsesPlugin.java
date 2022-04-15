package me.tuskdev.horses;

import me.tuskdev.horses.cache.CustomHorseCache;
import me.tuskdev.horses.command.SpawnHorseCommand;
import me.tuskdev.horses.controller.CustomHorseController;
import me.tuskdev.horses.controller.DeathHorseController;
import me.tuskdev.horses.inventory.ViewFrame;
import me.tuskdev.horses.listener.*;
import me.tuskdev.horses.task.ActionInfoTask;
import me.tuskdev.horses.view.HorseInventoryView;
import me.tuskdev.horses.view.ResurrectionInventoryView;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class HorsesPlugin extends JavaPlugin {

    private PooledConnection pooledConnection;

    @Override
    public void onLoad() {
        saveDefaultConfig();

        pooledConnection = new PooledConnection(getConfig().getConfigurationSection("database"));
    }

    @Override
    public void onEnable() {
        CustomHorseController customHorseController = new CustomHorseController(pooledConnection);
        DeathHorseController deathHorseController = new DeathHorseController(pooledConnection);
        CustomHorseCache customHorseCache = new CustomHorseCache(customHorseController);

        ViewFrame viewFrame = new ViewFrame(this);
        viewFrame.register(new HorseInventoryView(customHorseCache, customHorseController), new ResurrectionInventoryView(customHorseController, deathHorseController));

        ActionInfoTask actionInfoTask = new ActionInfoTask();
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, actionInfoTask, 0L, 20L);

        Bukkit.getPluginManager().registerEvents(new CitizensListener(viewFrame), this);
        Bukkit.getPluginManager().registerEvents(new HorseDeathListener(customHorseCache, customHorseController, deathHorseController), this);
        Bukkit.getPluginManager().registerEvents(new HorseInventoryListener(viewFrame), this);
        Bukkit.getPluginManager().registerEvents(new HorseMountListener(actionInfoTask, customHorseCache, customHorseController), this);
        Bukkit.getPluginManager().registerEvents(new HorseNameListener(), this);
        Bukkit.getPluginManager().registerEvents(new HorseSpawnListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);

        getCommand("horse").setExecutor(new SpawnHorseCommand());
        getCommand("horse").setTabCompleter(new SpawnHorseCommand());
    }

    @Override
    public void onDisable() {
        pooledConnection.close();
    }
}
