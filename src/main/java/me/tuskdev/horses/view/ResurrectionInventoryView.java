package me.tuskdev.horses.view;

import me.tuskdev.horses.controller.CustomHorseController;
import me.tuskdev.horses.inventory.View;
import me.tuskdev.horses.inventory.ViewContext;
import me.tuskdev.horses.model.CustomHorse;
import me.tuskdev.horses.util.HorseUtil;
import me.tuskdev.horses.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;

import java.util.Set;
import java.util.UUID;

public class ResurrectionInventoryView extends View {

    private final CustomHorseController customHorseController;

    public ResurrectionInventoryView(CustomHorseController customHorseController) {
        super(5, "Resurrection Horse");

        setCancelOnClick(true);

        for (int i = 0; i < 6; i++) {
            int slot = i*9;
            slot(i == 5 ? 44 : slot, new ItemBuilder(Material.RAILS).build());
            if (i != 5 && i != 0) slot(slot-1, new ItemBuilder(Material.RAILS).build());
        }

        this.customHorseController = customHorseController;
    }

    @Override
    protected void onRender(ViewContext context) {
        Set<CustomHorse> customHorses = customHorseController.selectAll(context.getPlayer().getUniqueId());

        int slot = 11;
        for (CustomHorse customHorse : customHorses) {
            Entity entity = Bukkit.getEntity(customHorse.getUniqueId());
            if (entity != null && !entity.isDead()) continue;

            context.slot(slot, new ItemBuilder(Material.SKULL_ITEM, 1).durability((short) 3).owner("MHF_HORSE").displayName("§cDeath Horse").lore("", String.format("  §7Jump Level: §a%s", customHorse.getJump()), String.format("  §7Speed Level: §a%s", customHorse.getSpeed()), String.format("  §7Health Level: §a%s", customHorse.getLife()), "", "  §7Click to respawn.").build()).closeOnClick().onClick(handle -> handleResurrection(handle.getPlayer(), customHorse));
            slot += (slot == 15 || slot == 24 ? 5 : 1);
        }
    }

    void handleResurrection(Player player, CustomHorse customHorse) {
        customHorseController.delete(customHorse.getUniqueId());
        player.sendMessage("§eYour horse will respawn in 10 seconds.");

        Location location = player.getLocation();
        Bukkit.getScheduler().runTaskLater(getFrame().getOwner(), () -> handleSpawn(location, customHorse, player.getName()), 200L);
    }

    void handleSpawn(Location location, CustomHorse customHorse, String ownerName) {
        location.getWorld().strikeLightningEffect(location);
        location.getWorld().strikeLightningEffect(location);
        location.getWorld().strikeLightningEffect(location);

        Horse horse = (Horse) location.getWorld().spawnEntity(location, EntityType.HORSE);
        horse.setAdult();
        horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(HorseUtil.getSpeed(horse) + customHorse.getSpeed()*0.05);
        horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).setBaseValue(HorseUtil.getJump(horse) + customHorse.getJump());
        horse.setMaxHealth(HorseUtil.getHealth(horse) + customHorse.getLife()*2);
        horse.setHealth(horse.getMaxHealth());
        horse.setCustomName(String.format("§e%s | %,.2f", HorseUtil.getName(horse), horse.getHealth()));
        horse.setCustomNameVisible(true);

        horse.setOwner(new AnimalTamer() {
            @Override
            public String getName() {
                return ownerName;
            }

            @Override
            public UUID getUniqueId() {
                return customHorse.getOwner();
            }
        });

        customHorseController.reinsert(horse.getUniqueId(), customHorse.getOwner(), customHorse.isMale(), customHorse.getSpeed(), customHorse.getLife(), customHorse.getJump());
    }
}
