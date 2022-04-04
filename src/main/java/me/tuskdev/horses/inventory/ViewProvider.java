package me.tuskdev.horses.inventory;

import org.bukkit.plugin.Plugin;

public interface ViewProvider {

	Plugin getHolder();

	ViewFrame getFrame();

}
