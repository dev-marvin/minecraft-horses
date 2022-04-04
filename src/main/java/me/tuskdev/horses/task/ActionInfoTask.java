package me.tuskdev.horses.task;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ActionInfoTask implements Runnable {

    private final Set<Player> players = new HashSet<>();

    @Override
    public void run() {
        Iterator<Player> iterator = players.iterator();
        while (iterator.hasNext()) {
            Player player = iterator.next();
            if (player.getVehicle() == null || player.getVehicle().getType() != EntityType.HORSE) {
                iterator.remove();
                continue;
            }

            Horse horse = (Horse) player.getVehicle();
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(String.format("§e%s | ❤ %,.2f", horse.getCustomName() == null ? "No Name" : horse.getCustomName(), horse.getHealth())));
        }
    }

    public void add(Player player) {
        players.add(player);
    }
}
