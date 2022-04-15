package me.tuskdev.horses.listener;

import me.tuskdev.horses.cache.CustomHorseCache;
import me.tuskdev.horses.controller.CustomHorseController;
import me.tuskdev.horses.controller.DeathHorseController;
import me.tuskdev.horses.model.CustomHorse;
import me.tuskdev.horses.util.HorseArmor;
import me.tuskdev.horses.util.HorseUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class HorseDeathListener implements Listener {

    private final CustomHorseCache customHorseCache;
    private final CustomHorseController customHorseController;
    private final DeathHorseController deathHorseController;

    public HorseDeathListener(CustomHorseCache customHorseCache, CustomHorseController customHorseController, DeathHorseController deathHorseController) {
        this.customHorseCache = customHorseCache;
        this.customHorseController = customHorseController;
        this.deathHorseController = deathHorseController;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getType() != EntityType.HORSE) return;

        event.getDrops().clear();

        Horse horse = (Horse) event.getEntity();
        if (horse.getOwner() == null) return;

        CustomHorse customHorse = customHorseCache.select(horse.getUniqueId());
        if (customHorse == null) return;

        customHorseCache.delete(horse.getUniqueId());
        customHorseController.delete(horse.getUniqueId());
        if (customHorse.hasResurrection()) deathHorseController.insert(customHorse.getOwner(), HorseUtil.getName(horse), horse.getColor(), customHorse.isMale(), customHorse.getSpeed(), customHorse.getLife(), customHorse.getJump(), HorseArmor.getArmorLevel(horse));
    }
}
