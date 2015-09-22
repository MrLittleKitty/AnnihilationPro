package com.gmail.nuclearcat1337.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.nuclearcat1337.anniPro.anniEvents.AnnihilationEvent;
import com.gmail.nuclearcat1337.anniPro.anniEvents.ResourceBreakEvent;
import com.gmail.nuclearcat1337.anniPro.kits.Loadout;
import com.gmail.nuclearcat1337.base.ConfigurableKit;

public class Lumberjack extends ConfigurableKit
{
		@Override
		protected void setUp()
		{
		}

		@Override
		protected String getInternalName()
		{
			return "Lumberjack";
		}

		@Override
		protected ItemStack getIcon()
		{
			return new ItemStack(Material.STONE_AXE);
		}

		@Override
		protected int setDefaults(ConfigurationSection section)
		{
			return 0;
		}

		@Override
		protected List<String> getDefaultDescription()
		{
			List<String> l = new ArrayList<String>();
			addToList(l,new String[]
				{
					aqua+"You are the wedge.",
					"",
					aqua+"Gather wood with an efficiency",
					aqua+"axe and with the chance",
					aqua+"of gaining double yeild,",
					aqua+"ensuring quick work of",
					aqua+"any trees in your way.",
				});
			return l;
		}

		@Override
		protected Loadout getFinalLoadout()
		{
			return new Loadout().addWoodSword().addWoodPick().addSoulboundEnchantedItem(new ItemStack(Material.STONE_AXE), Enchantment.DIG_SPEED, 1);
		}

		@Override
		public void cleanup(Player arg0)
		{
		}
		
		//Does the double loot for logs
		@AnnihilationEvent
		public void onResourceBreak(ResourceBreakEvent event)
		{
			if(event.getPlayer().getKit().equals(this))
			{
				if(event.getResource().Type == Material.LOG)
				{
					ItemStack[] stacks = event.getProducts();
					for(int x = 0; x < stacks.length; x++)
						stacks[x].setAmount(stacks[x].getAmount()*2);
					event.setProducts(stacks);	
				}
			}
		}
}
