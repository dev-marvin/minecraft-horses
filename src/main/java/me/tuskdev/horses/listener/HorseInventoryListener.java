package me.tuskdev.horses.listener;

import me.tuskdev.horses.inventory.ViewFrame;
import me.tuskdev.horses.view.HorseInventoryView;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.HorseInventory;

public class HorseInventoryListener implements Listener {

    private final ViewFrame viewFrame;

    public HorseInventoryListener(ViewFrame viewFrame) {
        this.viewFrame = viewFrame;
    }

    @EventHandler
    public void onOpenInventory(InventoryOpenEvent event) {
        if (event.getInventory() instanceof HorseInventory) {
            event.setCancelled(true);
            viewFrame.open(HorseInventoryView.class, (Player) event.getPlayer());
        }
    }

}
