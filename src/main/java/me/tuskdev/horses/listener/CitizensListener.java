package me.tuskdev.horses.listener;

import me.tuskdev.horses.inventory.ViewFrame;
import me.tuskdev.horses.view.ResurrectionInventoryView;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CitizensListener implements Listener {

    private final ViewFrame viewFrame;

    public CitizensListener(ViewFrame viewFrame) {
        this.viewFrame = viewFrame;
    }

    @EventHandler
    public void onNPCRightClick(NPCRightClickEvent event) {
        if (!event.getNPC().getName().equals("Resurrection Horse")) return;

        viewFrame.open(ResurrectionInventoryView.class, event.getClicker());
    }

}
