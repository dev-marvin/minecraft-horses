package me.tuskdev.horses.command;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import me.tuskdev.horses.cache.CustomHorseCache;
import me.tuskdev.horses.controller.CustomHorseController;
import me.tuskdev.horses.model.CustomHorse;
import me.tuskdev.horses.util.HorseUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class TransferHorseCommand implements CommandExecutor, TabCompleter {

    private final Cache<UUID, UUID> CONFIRM_TRANSFER = Caffeine.newBuilder().expireAfterWrite(Duration.ofSeconds(20)).build();
    private final CustomHorseCache customHorseCache;
    private final CustomHorseController customHorseController;

    public TransferHorseCommand(CustomHorseCache customHorseCache, CustomHorseController customHorseController) {
        this.customHorseCache = customHorseCache;
        this.customHorseController = customHorseController;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length <= 0) return Collections.emptyList();

        return Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(name -> name.startsWith(strings[0].toLowerCase())).collect(Collectors.toList());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player && commandSender.hasPermission("cmd.transfer"))
            handleCommand((Player) commandSender, strings);
        return false;
    }

    void handleCommand(Player player, String[] args) {
        if (args.length <= 0) {
            player.sendMessage("§cUsage: /transfer <player>.");
            return;
        }

        Entity vehicle = player.getVehicle();
        if (vehicle == null || vehicle.getType() != EntityType.HORSE) {
            player.sendMessage("§cYou must be riding a horse to transfer it.");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§cPlayer not found.");
            return;
        }

        Horse horse = (Horse) vehicle;
        if (!horse.getOwner().getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage("§cYou must be the owner of the horse to transfer it.");
            return;
        }

        UUID transferId = CONFIRM_TRANSFER.getIfPresent(player.getUniqueId());
        if (transferId == null || !transferId.equals(horse.getUniqueId())) {
            CONFIRM_TRANSFER.put(player.getUniqueId(), horse.getUniqueId());
            player.sendMessage("§aAre you sure you want to transfer this horse to " + target.getName() + "? (run the command again to confirm)");
            return;
        }

        CONFIRM_TRANSFER.invalidate(player.getUniqueId());

        HorseUtil.setOwner(horse, target.getUniqueId());
        customHorseController.updateOwner(horse.getUniqueId(), target.getUniqueId());

        CustomHorse customHorse = customHorseCache.select(horse.getUniqueId());
        if (customHorse != null && !customHorse.getOwner().equals(target.getUniqueId())) customHorse.setOwner(target.getUniqueId());

        player.leaveVehicle();
        player.sendMessage("§aHorse has been transferred.");
    }

}
