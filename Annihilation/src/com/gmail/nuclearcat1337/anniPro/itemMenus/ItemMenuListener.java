package com.gmail.nuclearcat1337.anniPro.itemMenus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Passes inventory click events to their menus for handling.
 */
public class ItemMenuListener implements Listener
{
	private Plugin plugin = null;
	private static final ItemMenuListener INSTANCE = new ItemMenuListener();

	private ItemMenuListener()
	{
	}

	/**
	 * Gets the {@link ninja.amp.ampmenus.MenuListener} instance.
	 *
	 * @return The {@link ninja.amp.ampmenus.MenuListener} instance.
	 */
	public static ItemMenuListener getInstance()
	{
		return INSTANCE;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event)
	{
		if (event.getWhoClicked() instanceof Player && event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof ItemMenuHolder)
		{
			event.setCancelled(true);
			((ItemMenuHolder) event.getInventory().getHolder()).getMenu()
					.onInventoryClick(event);
		}
	}

	/**
	 * Registers the events of the {@link ninja.amp.ampmenus.MenuListener} to a
	 * plugin.
	 *
	 * @param plugin
	 *            The plugin used to register the events.
	 */
	public void register(JavaPlugin plugin)
	{
		if (!isRegistered(plugin))
		{
			plugin.getServer().getPluginManager()
					.registerEvents(INSTANCE, plugin);
			this.plugin = plugin;
		}
	}

	/**
	 * Checks if the {@link ninja.amp.ampmenus.MenuListener} is registered to a
	 * plugin.
	 *
	 * @param plugin
	 *            The plugin.
	 * @return True if the {@link ninja.amp.ampmenus.MenuListener} is registered
	 *         to the plugin, else false.
	 */
	public boolean isRegistered(JavaPlugin plugin)
	{
		if (plugin.equals(this.plugin))
		{
			for (RegisteredListener listener : HandlerList
					.getRegisteredListeners(plugin))
			{
				if (listener.getListener().equals(INSTANCE))
				{
					return true;
				}
			}
		}
		return false;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPluginDisable(PluginDisableEvent event)
	{
		if (event.getPlugin().equals(plugin))
		{
			closeOpenMenus();
			plugin = null;
		}
	}

	/**
	 * Closes all {@link ninja.amp.ampmenus.menus.ItemMenu}s currently open.
	 */
	public static void closeOpenMenus()
	{
		for (Player player : Bukkit.getOnlinePlayers())
		{
			if (player.getOpenInventory() != null)
			{
				Inventory inventory = player.getOpenInventory()
						.getTopInventory();
				if (inventory.getHolder() instanceof ItemMenuHolder)
				{
					player.closeInventory();
				}
			}
		}
	}
}