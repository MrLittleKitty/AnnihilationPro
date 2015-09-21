package com.gmail.nuclearcat1337.anniPro.itemMenus;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * An Item inside an {@link com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem}.
 */
public class MenuItem
{
	private String displayName;
	private ItemStack icon;
	private List<String> lore;

	public MenuItem(String displayName, ItemStack icon, String... lore)
	{
		this.displayName = displayName;
		this.icon = icon;
		this.lore = new ArrayList<String>();
		for(String str : lore)
			this.lore.add(str);
		//Arrays.asList(lore);
	}

	/**
	 * Gets the display name of the MenuItem.
	 *
	 * @return The display name.
	 */
	public String getDisplayName()
	{
		return displayName;
	}

	/**
	 * Gets the icon of the MenuItem.
	 *
	 * @return The icon.
	 */
	public ItemStack getIcon()
	{
		return icon;
	}
	
	public void setIcon(ItemStack newIcon)
	{
		this.icon = newIcon;
	}
	
	public void setDisplayName(String name)
	{
		this.displayName = name;
	}

	/**
	 * Gets the lore of the MenuItem.
	 *
	 * @return The lore.
	 */
	public List<String> getLore()
	{
		return lore;
	}

	/**
	 * Gets the ItemStack to be shown to the player.
	 *
	 * @param player
	 *            The player.
	 * @return The final icon.
	 */
	public ItemStack getFinalIcon(Player player)
	{
		return setNameAndLore(getIcon().clone(), getDisplayName(), getLore());
	}

	/**
	 * Called when the MenuItem is clicked.
	 *
	 * @param event
	 *            The {@link com.gmail.nuclearcat1337.anniPro.itemMenus.ItemClickEvent}.
	 */
	public void onItemClick(ItemClickEvent event)
	{
		// Do nothing by default
	}

	/**
	 * Sets the display name and lore of an ItemStack.
	 *
	 * @param itemStack
	 *            The ItemStack.
	 * @param displayName
	 *            The display name.
	 * @param lore
	 *            The lore.
	 * @return The ItemStack.
	 */
	public static ItemStack setNameAndLore(ItemStack itemStack,
			String displayName, List<String> lore)
	{
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
		return itemStack;
	}
}