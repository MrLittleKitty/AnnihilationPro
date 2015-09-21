package com.gmail.nuclearcat1337.anniPro.itemMenus;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;

/**
 * A {@link ninja.amp.ampmenus.items.MenuItem} that opens a sub
 * {@link ninja.amp.ampmenus.menus.ItemMenu}.
 */
public class SubMenuItem extends MenuItem
{
	//private final JavaPlugin plugin;
	private final ItemMenu menu;

	public SubMenuItem(String displayName, ItemMenu menu, ItemStack icon, String... lore)
	{
		super(displayName, icon, lore);
		//this.plugin = plugin;
		this.menu = menu;
	}

	@Override
	public void onItemClick(ItemClickEvent event)
	{
		event.setWillClose(true);
		final UUID ID = event.getPlayer().getUniqueId();
		Bukkit.getScheduler().scheduleSyncDelayedTask(AnnihilationMain.getInstance(), new Runnable()
		{
			public void run()
			{
				Player p = Bukkit.getPlayer(ID);
				if (p != null && menu != null)
				{
					menu.open(p);
				}
			}
		}, 3);
	}
}