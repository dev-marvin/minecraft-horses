package me.tuskdev.horses.view;

import me.tuskdev.horses.controller.CustomHorseController;
import me.tuskdev.horses.controller.DeathHorseController;
import me.tuskdev.horses.inventory.View;
import me.tuskdev.horses.inventory.ViewContext;
import me.tuskdev.horses.model.CustomHorse;
import me.tuskdev.horses.model.DeathHorse;
import me.tuskdev.horses.util.HorseArmor;
import me.tuskdev.horses.util.HorseUtil;
import me.tuskdev.horses.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;

import java.util.Set;
import java.util.UUID;

public class ResurrectionInventoryView extends View {

    private final CustomHorseController customHorseController;
    private final DeathHorseController deathHorseController;

    public ResurrectionInventoryView(CustomHorseController customHorseController, DeathHorseController deathHorseController) {
        super(5, "Resurrection Horse");

        setCancelOnClick(true);

        for (int i = 0; i < 6; i++) {
            int slot = i*9;
            slot(i == 5 ? 44 : slot, new ItemBuilder(Material.RAILS).build());
            if (i != 5 && i != 0) slot(slot-1, new ItemBuilder(Material.RAILS).build());
        }

        this.customHorseController = customHorseController;
        this.deathHorseController = deathHorseController;
    }

    @Override
    protected void onRender(ViewContext context) {
        Set<DeathHorse> deathHorses = deathHorseController.selectAll(context.getPlayer().getUniqueId());

        int slot = 11;
        for (DeathHorse deathHorse : deathHorses) {
            context.slot(slot, new ItemBuilder(Material.SKULL_ITEM, 1).durability((short) 3).owner("MHF_HORSE").displayName("§cDeath Horse").lore("", String.format("  §7Horse Name: §a%s", deathHorse.getName()), String.format("  §7Gender: §a%s", deathHorse.isMale() ? "Male ♂" : "Mare ♀"), String.format("  §7Jump Level: §a%s", deathHorse.getJump()), String.format("  §7Speed Level: §a%s", deathHorse.getSpeed()), String.format("  §7Health Level: §a%s", deathHorse.getLife()), "", "  §7Click to respawn.").build()).closeOnClick().onClick(handle -> handleResurrection(handle.getPlayer(), deathHorse));
            slot += (slot == 15 || slot == 24 ? 5 : 1);
        }
    }

    void handleResurrection(Player player, DeathHorse deathHorse) {
        deathHorseController.delete(deathHorse.getId());
        player.sendMessage("§eYour horse will respawn in 10 seconds.");

        Location location = player.getLocation();
        Bukkit.getScheduler().runTaskLater(getFrame().getOwner(), () -> handleSpawn(location, deathHorse, player.getUniqueId()), 200L);
    }

    void handleSpawn(Location location, DeathHorse deathHorse, UUID owner) {
        location.getWorld().strikeLightningEffect(location);
        location.getWorld().strikeLightningEffect(location);
        location.getWorld().strikeLightningEffect(location);

        Horse horse = (Horse) location.getWorld().spawnEntity(location, EntityType.HORSE);
        horse.setAdult();
        horse.setColor(deathHorse.getColor());
        horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(HorseUtil.getSpeed(horse) + deathHorse.getSpeed()*0.05);
        horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).setBaseValue(HorseUtil.getJump(horse) + deathHorse.getJump());
        horse.setMaxHealth(HorseUtil.getHealth(horse) + deathHorse.getLife()*2);
        horse.setHealth(horse.getMaxHealth());
        horse.getInventory().setArmor(HorseArmor.getArmorItem(deathHorse.getArmor()));
        horse.setCustomName(String.format("§e%s | %,.2f", deathHorse.getName(), horse.getHealth()));
        horse.setCustomNameVisible(true);

        horse.setOwner(new AnimalTamer() {
            @Override
            public String getName() {
                return Bukkit.getOfflinePlayer(owner).getName();
            }

            @Override
            public UUID getUniqueId() {
                return owner;
            }
        });

        customHorseController.reinsert(horse.getUniqueId(), owner, deathHorse.isMale(), deathHorse.getSpeed(), deathHorse.getLife(), deathHorse.getJump());
    }
}
