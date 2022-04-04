package me.tuskdev.horses.listener;

import me.tuskdev.horses.cache.CustomHorseCache;
import me.tuskdev.horses.controller.CustomHorseController;
import me.tuskdev.horses.model.CustomHorse;
import me.tuskdev.horses.task.ActionInfoTask;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.spigotmc.event.entity.EntityMountEvent;

public class HorseMountListener implements Listener {

    private final ActionInfoTask actionInfoTask;
    private final CustomHorseCache customHorseCache;
    private final CustomHorseController customHorseController;

    public HorseMountListener(ActionInfoTask actionInfoTask, CustomHorseCache customHorseCache, CustomHorseController customHorseController) {
        this.actionInfoTask = actionInfoTask;
        this.customHorseCache = customHorseCache;
        this.customHorseController = customHorseController;
    }

    @EventHandler
    public void onEntityMount(EntityMountEvent event) {
        if (event.getMount().getType() != EntityType.HORSE) return;

        Horse horse = (Horse) event.getMount();
        if (horse.getOwner() == null || event.getEntity().getUniqueId().equals(horse.getOwner().getUniqueId())) return;

        event.setCancelled(true);
        event.getEntity().sendMessage("§cYou cannot ride that horse, for it does not belong to you.");
    }

    @EventHandler
    public void onEntityTame(EntityTameEvent event) {
        if (event.getEntityType() != EntityType.HORSE) return;

        CustomHorse customHorse = new CustomHorse(event.getEntity().getUniqueId(), event.getOwner().getUniqueId());
        customHorseCache.insert(customHorse);
        customHorseController.insert(customHorse.getUniqueId(), customHorse.getOwner(), customHorse.isMale());
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (event.getEntered().getType() != EntityType.PLAYER || event.getVehicle().getType() != EntityType.HORSE) return;

        actionInfoTask.add((Player) event.getEntered());
    }

}
