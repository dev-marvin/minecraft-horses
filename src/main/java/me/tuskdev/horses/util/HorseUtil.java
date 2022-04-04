package me.tuskdev.horses.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Horse;

public class HorseUtil {

    public static String getBreed(Horse horse) {
        switch (horse.getColor()) {
            case BLACK:
            case GRAY:
            case BROWN: return "Suids Warmbloed";
            case WHITE: return "Desert Arabier";
            case CREAMY: return "Boeraviaans Hooglander";
            case CHESTNUT:
            case DARK_BROWN:
            default: return "None";
        }
    }

    public static double getJump(Horse horse) {
        switch (horse.getColor()) {
            case BLACK:
            case GRAY:
            case BROWN: return 0.25D;
            case WHITE: return 0.3D;
            case CREAMY: return 0.5D;
            case CHESTNUT:
            case DARK_BROWN:
            default: return 0.7D;
        }
    }

    public static double getSpeed(Horse horse) {
        switch (horse.getColor()) {
            case BLACK:
            case GRAY:
            case BROWN: return 0.2D;
            case CREAMY: return 0.25D;
            case CHESTNUT:
            case DARK_BROWN:
            default: return 0.35D;
        }
    }

    public static double getHealth(Horse horse) {
        switch (horse.getColor()) {
            case BLACK:
            case GRAY:
            case BROWN: return 30D;
            case WHITE: return 10D;
            case CHESTNUT:
            case DARK_BROWN:
            default: return 20D;
        }
    }

    public static String getName(Horse horse) {
        if (horse.getCustomName() == null || horse.getCustomName().contains("No Name")) return "No Name";

        return ChatColor.stripColor(horse.getCustomName().split("\\|")[0]);
    }

}
