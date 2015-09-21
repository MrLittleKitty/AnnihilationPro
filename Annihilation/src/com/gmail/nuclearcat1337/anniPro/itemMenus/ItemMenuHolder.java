package com.gmail.nuclearcat1337.anniPro.itemMenus;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Allows you to set the {@link ninja.amp.ampmenus.menus.MenuItem} that created
 * the Inventory as the Inventory's holder.
 */
public class ItemMenuHolder implements InventoryHolder
{
	private ItemMenu menu;
	private Inventory inventory;

	public ItemMenuHolder(ItemMenu menu, Inventory inventory)
	{
		this.menu = menu;
		this.inventory = inventory;
	}

	/**
	 * Gets the {@link ninja.amp.ampmenus.menus.MenuItem} holding the Inventory.
	 *
	 * @return The {@link ninja.amp.ampmenus.menus.MenuItem} holding the
	 *         Inventory.
	 */
	public ItemMenu getMenu()
	{
		return menu;
	}

	@Override
	public Inventory getInventory()
	{
		return inventory;
	}
}