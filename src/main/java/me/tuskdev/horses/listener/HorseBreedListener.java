package me.tuskdev.horses.listener;

import me.tuskdev.horses.cache.CustomHorseCache;
import me.tuskdev.horses.model.CustomHorse;
import me.tuskdev.horses.util.HorseUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;

public class HorseBreedListener implements Listener {

    private final CustomHorseCache customHorseCache;

    public HorseBreedListener(CustomHorseCache customHorseCache) {
        this.customHorseCache = customHorseCache;
    }

    @EventHandler
    public void onEntityBreed(EntityBreedEvent event) {
        if (event.getEntityType() != EntityType.HORSE) return;

        if (!HorseUtil.getBreed((Horse) event.getFather()).equals(HorseUtil.getBreed((Horse) event.getMother()))) {
            event.setCancelled(true);
            return;
        }

        CustomHorse father = customHorseCache.select(event.getFather().getUniqueId());
        CustomHorse mother = customHorseCache.select(event.getMother().getUniqueId());
        if (father == null || mother == null || father.isMale() != mother.isMale()) return;

        event.setCancelled(true);
    }

}
