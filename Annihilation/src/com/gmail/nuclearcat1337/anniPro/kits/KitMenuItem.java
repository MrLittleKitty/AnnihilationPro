package com.gmail.nuclearcat1337.anniPro.kits;

import java.util.ArrayList;
import java.util.List;

import com.gmail.nuclearcat1337.anniPro.main.Lang;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.anniGame.Game;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemClickEvent;
import com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem;

public class KitMenuItem extends MenuItem
{
	private final Kit kit;
	public KitMenuItem(final Kit kit)
	{
		super(kit.getName(), kit.getIconPackage().getIcon(), kit.getIconPackage().getLore());
		this.kit = kit;
	}
	
	public Kit getKit()
	{
		return kit;
	}
	
	@Override
	public ItemStack getFinalIcon(Player player)
	{
		List<String> str = new ArrayList<String>(getLore());
		str.add(ChatColor.GOLD+"--------------------------");
		if(kit.hasPermission(player))
			str.add(ChatColor.GREEN+"UNLOCKED");
		else
			str.add(ChatColor.RED+"LOCKED");
		return setNameAndLore(getIcon().clone(), getDisplayName(), str);
	}
	
	@Override
	public void onItemClick(ItemClickEvent event)
	{
		final Player player = event.getPlayer();
		if(player != null)
		{
			event.setWillClose(true);
			final AnniPlayer anniplayer = AnniPlayer.getPlayer(player.getUniqueId());
			if(kit != null && anniplayer != null)
			{
				if(kit.hasPermission(player))
				{
					if(Game.isGameRunning() && anniplayer.getKit() != null)
						anniplayer.getKit().cleanup(player);
					anniplayer.setKit(kit);
					player.sendMessage(ChatColor.DARK_PURPLE+kit.getName()+" selected.");
					if(Game.isGameRunning() && anniplayer.getTeam() != null)
						player.setHealth(0);
				}
				else player.sendMessage(Lang.CANT_SELECT_KIT.toString());
			}
		}	
	}
}
