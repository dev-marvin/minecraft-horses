package me.tuskdev.horses.view;

import me.tuskdev.horses.cache.CustomHorseCache;
import me.tuskdev.horses.controller.CustomHorseController;
import me.tuskdev.horses.inventory.OpenViewContext;
import me.tuskdev.horses.inventory.View;
import me.tuskdev.horses.inventory.ViewContext;
import me.tuskdev.horses.model.CustomHorse;
import me.tuskdev.horses.util.HorseArmor;
import me.tuskdev.horses.util.HorseUtil;
import me.tuskdev.horses.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class HorseInventoryView extends View {

    private final CustomHorseCache customHorseCache;
    private final CustomHorseController customHorseController;

    public HorseInventoryView(CustomHorseCache customHorseCache, CustomHorseController customHorseController) {
        super(5);

        setCancelOnClick(true);

        for (int i = 0; i < 45; i++) if (i != 13 && i != 20 && i != 21 && i != 22 && i != 23 && i != 24 && i != 31) slot(i, new ItemBuilder(Material.STAINED_GLASS_PANE, 1).durability((short) 15).build());

        for (int i = 0; i < 6; i++) {
            int slot = i*9;
            slot(i == 5 ? 44 : slot, new ItemBuilder(Material.RAILS).build());
            if (i != 5 && i != 0) slot(slot-1, new ItemBuilder(Material.RAILS).build());
        }

        this.customHorseCache = customHorseCache;
        this.customHorseController = customHorseController;
    }

    @Override
    protected void onOpen(OpenViewContext context) {
        Horse horse = (Horse) context.getPlayer().getVehicle();
        context.setInventoryTitle(HorseUtil.getName(horse));
    }

    @Override
    protected void onRender(ViewContext context) {
        Horse horse = (Horse) context.getPlayer().getVehicle();
        CustomHorse customHorse = customHorseCache.select(horse.getUniqueId());
        if (customHorse == null) return;

        int armorLevel = HorseArmor.getArmorLevel(horse);

        String horseOwner = horse.getOwner().getName();
        context.slot(13, new ItemBuilder(Material.SKULL_ITEM, 1, "§eInfo:")
                .durability((short)3)
                .owner(horseOwner != null ? horseOwner : "MHF_HORSE")
                .lore(
                        "",
                        String.format("  §eOwner: §a%s", horseOwner),
                        String.format("  §eGender: §a%s", customHorse.isMale() ? "Male ♂" : "Mare ♀"),
                        String.format("  §eBreed: §a%s", HorseUtil.getBreed(horse)),
                        "",
                        String.format("  §eJump: §a%s §7+ §8(§b%s.00§8) [§7LvL: §d%s§8) (§7Actual: %,.2f§8)", HorseUtil.getJump(horse), customHorse.getJump(), customHorse.getJump(), horse.getJumpStrength()),
                        String.format("  §eSpeed: §a%s §7+ §8(§b%s§8) [§7LvL: §d%s§8) (§7Actual: %,.2f§8)", HorseUtil.getSpeed(horse), customHorse.getSpeed()*0.05, customHorse.getSpeed(), horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getValue()),
                        String.format("  §eHealth: §a%s §7+ §8(§b%s§8) [§7LvL: §d%s§8) (§7Actual: %,.2f§8)", HorseUtil.getHealth(horse), customHorse.getLife()*2.00, customHorse.getLife(), horse.getMaxHealth()),
                        String.format("  §eArmor: §a%,.2f §7+ §8(§b%s§8) [§7LvL: §d%s§8) (§7Actual: %,.2f§8)", horse.getAttribute(Attribute.GENERIC_ARMOR).getValue(), horse.getAttribute(Attribute.GENERIC_ARMOR).getValue(), armorLevel, horse.getAttribute(Attribute.GENERIC_ARMOR).getValue()),
                        String.format("  §eHas saddle: §a%s", horse.getInventory().getSaddle() == null ? "No" : "Yes"),
                        "",
                        String.format("  §eLifetime: §a%s", horse.getAge())
                ).build());

        context.slot(20, new ItemBuilder(Material.STAINED_GLASS_PANE, 1).durability((short) (customHorse.getJump() == 3 ? 5 : 14)).displayName("§cJump Upgrade").lore(customHorse.getJump() == 3 ? "§7Your horse is at maximum level." : String.format("§7Value: §a%s §7XP Levels", 10*(customHorse.getJump()+1))).build()).onClick(handle -> handleJumpUpgrade(handle.getPlayer(), customHorse, horse));
        context.slot(21, new ItemBuilder(Material.STAINED_GLASS_PANE, 1).durability((short) (customHorse.getSpeed() == 5 ? 5 : 14)).displayName("§cSpeed Upgrade").lore(customHorse.getSpeed() == 5 ? "§7Your horse is at maximum level." : String.format("§7Value: §a%s §7XP Levels", 10*(customHorse.getSpeed()+1))).build()).onClick(handle -> handleSpeedUpgrade(handle.getPlayer(), customHorse, horse));
        context.slot(22, new ItemBuilder(Material.STAINED_GLASS_PANE, 1).durability((short) (customHorse.getLife() == 5 ? 5 : 14)).displayName("§cHP Upgrade").lore(customHorse.getSpeed() == 5 ? "§7Your horse is at maximum level." : String.format("§7Value: §a%s §7XP Levels", 10*(customHorse.getLife()+1))).build()).onClick(handle -> handleHPUpgrade(handle.getPlayer(), customHorse, horse));
        context.slot(23, new ItemBuilder(Material.STAINED_GLASS_PANE, 1).durability((short) (customHorse.hasResurrection() ? 5 : 14)).displayName("§cResurrection Upgrade").lore(customHorse.hasResurrection() ? "§7Your horse is at maximum level." : "§7Value: §a100 §7XP Levels").build()).onClick(handle -> handleResurrectionUpgrade(handle.getPlayer(), customHorse, horse));
        context.slot(24, new ItemBuilder(Material.STAINED_GLASS_PANE, 1).durability((short) (armorLevel == 4 ? 5 : 14)).displayName("§cArmor Upgrade").lore(armorLevel == 4 ? "§7Your horse is at maximum level." : String.format("§7Value: §a%s §7XP Levels", 10*(armorLevel+1))).build()).onClick(handle -> handleArmorUpgrade(handle.getPlayer(), horse));

        if (horse.getInventory().getSaddle() != null) context.slot(31, horse.getInventory().getSaddle()).onClick(handle -> handleSaddleClick(handle.getPlayer(), handle.getInventory(), horse));
    }

    void handleJumpUpgrade(Player player, CustomHorse customHorse, Horse horse) {
        if (customHorse.getJump() == 3) return;

        int level = player.getLevel();
        int cost = (customHorse.getJump()+1)*10;
        if (cost > level) {
            player.sendMessage(String.format("§cYou still need %s levels of XP to be able to evolve.", cost-level));
            return;
        }

        customHorse.setJump(customHorse.getJump()+1);
        horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).setBaseValue(HorseUtil.getJump(horse) + customHorse.getJump()*0.1);
        player.setLevel(level-cost);

        customHorseController.updateJump(horse.getUniqueId(), customHorse.getJump());

        player.closeInventory();
        player.sendMessage("§eYay! The update was successfully acquired.");
    }

    void handleSpeedUpgrade(Player player, CustomHorse customHorse, Horse horse) {
        if (customHorse.getSpeed() == 5) return;

        int level = player.getLevel();
        int cost = (customHorse.getSpeed()+1)*10;
        if (cost > level) {
            player.sendMessage(String.format("§cYou still need %s levels of XP to be able to evolve.", cost-level));
            return;
        }

        customHorse.setSpeed(customHorse.getSpeed()+1);
        horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(HorseUtil.getSpeed(horse) + customHorse.getSpeed()*0.05);
        player.setLevel(level-cost);

        customHorseController.updateSpeed(horse.getUniqueId(), customHorse.getSpeed());

        player.closeInventory();
        player.sendMessage("§eYay! The update was successfully acquired.");
    }

    void handleHPUpgrade(Player player, CustomHorse customHorse, Horse horse) {
        if (customHorse.getLife() == 5) return;

        int level = player.getLevel();
        int cost = (customHorse.getLife()+1)*10;
        if (cost > level) {
            player.sendMessage(String.format("§cYou still need %s levels of XP to be able to evolve.", cost-level));
            return;
        }

        customHorse.setLife(customHorse.getLife()+1);
        horse.setMaxHealth(HorseUtil.getHealth(horse) + customHorse.getLife()*2);
        horse.setHealth(horse.getMaxHealth());

        player.setLevel(level-cost);

        customHorseController.updateLife(horse.getUniqueId(), customHorse.getLife());

        player.closeInventory();
        player.sendMessage("§eYay! The update was successfully acquired.");
    }

    void handleResurrectionUpgrade(Player player, CustomHorse customHorse, Horse horse) {
        if (customHorse.hasResurrection()) return;

        int level = player.getLevel();
        if (100 > level) {
            player.sendMessage("§cYou still need 100 levels of XP to be able to evolve.");
            return;
        }

        customHorse.setResurrection(true);
        player.setLevel(level-100);

        customHorseController.updateResurrection(horse.getUniqueId(), true);

        player.closeInventory();
        player.sendMessage("§eYay! The update was successfully acquired.");
    }

    void handleArmorUpgrade(Player player, Horse horse) {
        int armorLevel = HorseArmor.getArmorLevel(horse);
        if (armorLevel == 4) return;

        int level = player.getLevel();
        int cost = (armorLevel+1)*10;
        if (cost > level) {
            player.sendMessage(String.format("§cYou still need %s levels of XP to be able to evolve.", cost-level));
            return;
        }

        player.setLevel(level-cost);
        horse.getInventory().setArmor(HorseArmor.getArmorItem(armorLevel+1));

        player.closeInventory();
        player.sendMessage("§eYay! The update was successfully acquired.");
    }

    void handleSaddleClick(Player player, Inventory inventory, Horse horse) {
        if (horse.getInventory().getSaddle() == null) return;

        player.getInventory().addItem(horse.getInventory().getSaddle());
        horse.getInventory().setSaddle(null);
        inventory.clear(31);
    }
}
