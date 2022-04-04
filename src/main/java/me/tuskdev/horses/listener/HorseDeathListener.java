package me.tuskdev.horses.listener;

import me.tuskdev.horses.cache.CustomHorseCache;
import me.tuskdev.horses.controller.CustomHorseController;
import me.tuskdev.horses.model.CustomHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class HorseDeathListener implements Listener {

    private final CustomHorseCache customHorseCache;
    private final CustomHorseController customHorseController;

    public HorseDeathListener(CustomHorseCache customHorseCache, CustomHorseController customHorseController) {
        this.customHorseCache = customHorseCache;
        this.customHorseController = customHorseController;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getType() != EntityType.HORSE) return;

        event.getDrops().clear();

        Horse horse = (Horse) event.getEntity();
        if (horse.getOwner() == null) return;

        CustomHorse customHorse = customHorseCache.select(horse.getUniqueId());
        if (customHorse == null || customHorse.hasResurrection()) return;

        customHorseCache.delete(horse.getUniqueId());
        customHorseController.delete(horse.getUniqueId());
    }
}
