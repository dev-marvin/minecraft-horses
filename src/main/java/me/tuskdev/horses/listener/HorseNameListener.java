package me.tuskdev.horses.listener;

import me.tuskdev.horses.util.HorseUtil;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

public class HorseNameListener implements Listener {

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked().getType() != EntityType.HORSE) return;

        ItemStack itemStack = event.getPlayer().getEquipment().getItemInMainHand();
        if (itemStack == null || itemStack.getType() != Material.NAME_TAG) return;

        event.setCancelled(true);
        event.getPlayer().getEquipment().setItemInMainHand(new ItemStack(Material.AIR));

        Horse horse = (Horse) event.getRightClicked();
        horse.setCustomName(String.format("§e%s | ❤ %,.2f", itemStack.getItemMeta().getDisplayName(), horse.getHealth()));
        horse.setCustomNameVisible(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.HORSE) return;

        Horse horse = (Horse) event.getEntity();
        horse.setCustomName(String.format("§e%s | ❤ %,.2f", HorseUtil.getName(horse), horse.getHealth()));
        horse.setCustomNameVisible(true);
    }

}
