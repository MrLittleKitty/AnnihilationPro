package com.gmail.nuclearcat1337.base;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.base.Function;


public class StandardItemUpdater implements DelayUpdate
{
	private final Function<ItemStack,Boolean> function;
	private final String itemName;
	private final Material mat;
	public StandardItemUpdater(String itemName, Material mat, Function<ItemStack,Boolean> isTheRightItem)
	{
		function = isTheRightItem;
		this.mat = mat;
		this.itemName = itemName;
	}
	
	@Override
	public void update(Player player, int secondsLeft)
	{
		for(Entry<Integer,? extends ItemStack> entry : player.getInventory().all(mat).entrySet())
		{
			if(function.apply(entry.getValue()))
			{
				ItemMeta m = entry.getValue().getItemMeta();
				if(secondsLeft <= 0)
					m.setDisplayName(itemName+" "+ChatColor.GREEN+"READY");
				else
					m.setDisplayName(itemName+" "+ChatColor.RED+secondsLeft);
				entry.getValue().setItemMeta(m);
			}
		}
	}

}
