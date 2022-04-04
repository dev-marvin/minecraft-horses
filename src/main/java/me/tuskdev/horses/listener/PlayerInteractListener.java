package me.tuskdev.horses.listener;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked().getType() != EntityType.HORSE) return;

        Player player = event.getPlayer();
        Horse horse = (Horse) event.getRightClicked();
        if (horse.getOwner() == null
                || !horse.getOwner().getUniqueId().equals(player.getUniqueId())
                || player.getEquipment().getItemInMainHand() == null
                || player.getEquipment().getItemInMainHand().getType() != Material.SADDLE
        ) return;

        horse.getInventory().setSaddle(player.getEquipment().getItemInMainHand());
        player.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
    }

}
