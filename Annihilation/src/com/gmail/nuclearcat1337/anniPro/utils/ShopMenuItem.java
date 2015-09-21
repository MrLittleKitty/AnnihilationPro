package com.gmail.nuclearcat1337.anniPro.utils;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemClickEvent;
import com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem;
import com.gmail.nuclearcat1337.anniPro.main.Lang;

public class ShopMenuItem extends MenuItem
{
	private final ItemStack display;
	private final ItemStack product;
	private final int cost;
	public ShopMenuItem(ItemStack displayStack, ItemStack productStack, int cost)
	{
		super(null, null,new String[0]);
		this.display = displayStack.clone();
		this.product = productStack.clone();
		ArrayList<String> l = new ArrayList<String>();
		l.add(Lang.COST.toStringReplacement(cost));
		l.add(Lang.QUANTITY.toStringReplacement(product.getAmount()));
		ItemMeta m = display.getItemMeta();
		m.setLore(l);
		display.setItemMeta(m);
		this.cost = cost;
	}
	
	@Override
	public void onItemClick(ItemClickEvent event)
	{
		Player player = event.getPlayer();
		PlayerInventory p = player.getInventory();
		if(p.containsAtLeast(new ItemStack(Material.GOLD_INGOT), cost))
		{
			int total = 0;
			for(ItemStack s : p.all(Material.GOLD_INGOT).values())
			{
				total += s.getAmount();
			}
			p.remove(Material.GOLD_INGOT);
			if(total-cost > 0)
				p.addItem(new ItemStack(Material.GOLD_INGOT,total-cost));
			p.addItem(product);
			player.sendMessage(Lang.PURCHASEDITEM.toString());
		}
		else player.sendMessage(Lang.COULDNOTPURCHASE.toString());
	}
	
	@Override
	public ItemStack getFinalIcon(Player player)
	{
		return display;
	}

}
