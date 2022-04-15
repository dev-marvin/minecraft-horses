package me.tuskdev.horses.command;

import me.tuskdev.horses.util.HorseUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpawnHorseCommand implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length <= 0) return Collections.emptyList();

        return Stream.of(Horse.Color.values()).map(Horse.Color::name).filter(name -> name.startsWith(strings[0].toUpperCase())).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player && commandSender.hasPermission("cmd.horse"))
            handleCommand((Player) commandSender, strings);
        return false;
    }

    void handleCommand(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage("§cUsage: /horse <bred>.");
            return;
        }

        Horse.Color color = tryParseColor(args[0]);
        if (color == null) {
            player.sendMessage("§cThe bred of horse is invalid.");
            return;
        }

        Horse horse = (Horse) player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
        horse.setAdult();
        horse.setColor(color);
        horse.setMaxHealth(HorseUtil.getHealth(horse));
        horse.setHealth(horse.getMaxHealth());
        horse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(HorseUtil.getSpeed(horse));
        horse.getAttribute(Attribute.HORSE_JUMP_STRENGTH).setBaseValue(HorseUtil.getJump(horse));
        horse.setCustomName(String.format("§e%s | ❤ %,.2f", HorseUtil.getName(horse), horse.getHealth()));
        horse.setCustomNameVisible(true);

        player.sendMessage("§eYay! The horse has spawned.");
    }

    Horse.Color tryParseColor(String value) {
        try {
            return Horse.Color.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
