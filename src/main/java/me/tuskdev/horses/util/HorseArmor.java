package me.tuskdev.horses.util;

import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.ItemStack;

public class HorseArmor {

    public static int getArmorLevel(Horse horse) {
        ItemStack itemStack = horse.getInventory().getArmor();
        if (itemStack == null) return 1;

        switch (itemStack.getType()) {
            case DIAMOND_BARDING: return 4;
            case GOLD_BARDING: return 3;
            case IRON_BARDING: return 2;
            default: return 1;
        }
    }

    public static ItemStack getArmorItem(int level) {
        switch (level) {
            case 4: return new ItemStack(Material.DIAMOND_BARDING);
            case 3: return new ItemStack(Material.GOLD_BARDING);
            case 2: return new ItemStack(Material.IRON_BARDING);
            default: return new ItemStack(Material.AIR);
        }
    }

}
