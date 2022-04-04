package me.tuskdev.horses.listener;

import me.tuskdev.horses.util.HorseUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class HorseSpawnListener implements Listener {

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntityType() != EntityType.HORSE) return;

        Horse horse = (Horse) event.getEntity();
        horse.setMaxHealth(HorseUtil.getHealth(horse));
        horse.setHealth(horse.getMaxHealth());
        horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(HorseUtil.getSpeed(horse));
        horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).setBaseValue(HorseUtil.getJump(horse));

        horse.setCustomName(String.format("§eNo Name | ❤ %,.2f", horse.getHealth()));
        horse.setCustomNameVisible(true);
    }

}
