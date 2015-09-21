package com.gmail.nuclearcat1337.anniPro.itemMenus;

import org.bukkit.inventory.ItemStack;

public class ComboMenuItem extends SubMenuItem
{
	private ItemClickHandler handler;
	public ComboMenuItem(String displayName, ItemMenu menu, ItemClickHandler handler, ItemStack icon, String... lore)
	{
		super(displayName, menu, icon, lore);
		this.handler = handler;
	}

	@Override
	public void onItemClick(ItemClickEvent event)
	{
		if(handler != null)
			handler.onItemClick(event);
		super.onItemClick(event);
	}
}
