package com.gmail.nuclearcat1337.kits;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.nuclearcat1337.anniPro.anniEvents.AnnihilationEvent;
import com.gmail.nuclearcat1337.anniPro.anniEvents.ResourceBreakEvent;
import com.gmail.nuclearcat1337.anniPro.kits.Loadout;
import com.gmail.nuclearcat1337.base.ConfigurableKit;

public class Miner extends ConfigurableKit
{
//	private final ItemStack Pick = KitUtils.addEnchant(KitUtils.getStonePick(),Enchantment.DIG_SPEED,1);
//	private final ItemStack Furnace = KitUtils.addSoulbound(new ItemStack(Material.FURNACE));
//	private final ItemStack coal = new ItemStack(Material.COAL,4);
	
	private Random rand;
	
	@Override
	protected void setUp()
	{
		rand = new Random(System.currentTimeMillis());
	}

	@Override
	protected String getInternalName()
	{
		return "Miner";
	}

	@Override
	protected ItemStack getIcon()
	{
		return new ItemStack(Material.STONE_PICKAXE);
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
					aqua+"You are the hands.", 
					"",
					aqua+"Mine precious resources", 
					aqua+"to gear up your team as", 
					aqua+"well as yourself so you", 
					aqua+"will strike swiftly and", 
					aqua+"with strength on battlefield!", 
					"",
					aqua+"Start with an effeciency", 
					aqua+"pick, 4 coal, and a furnace", 
					aqua+"to get minerals quicker.", 
				});
		return l;
	}

	@Override
	public void cleanup(Player arg0)
	{
		
	}
	
	//Does the double loot from regenerating resources
	@AnnihilationEvent
	public void onResourceBreak(ResourceBreakEvent event)
	{
		if(event.getPlayer().getKit().equals(this))
		{
			if(event.getResource().Type != Material.LOG && event.getResource().Type != Material.MELON_BLOCK && event.getResource().Type != Material.GRAVEL)
			{
				ItemStack[] products = event.getProducts();
				if(products != null)
				{
					for(int x = 0; x < products.length; x++)
					{
						boolean y = rand.nextBoolean();
						if(y)
							products[x].setAmount(products[x].getAmount()*2);
					}
				}
				event.setProducts(products);	
			}
		}
	}
	
	@Override
	protected Loadout getFinalLoadout()
	{
		return new Loadout().addWoodSword().addSoulboundEnchantedItem(new ItemStack(Material.STONE_PICKAXE), Enchantment.DIG_SPEED, 1).addWoodAxe().addItem(new ItemStack(Material.COAL,4))
				.addSoulboundItem(new ItemStack(Material.FURNACE));
	}

//	@Override
//	public void onPlayerSpawn(Player player)
//	{
//		KitUtils.giveTeamArmor(player);
//		player.getInventory().addItem(KitUtils.getWoodSword());
//		player.getInventory().addItem(Pick);
//		player.getInventory().addItem(KitUtils.getWoodAxe());
//		player.getInventory().addItem(coal);
//		player.getInventory().addItem(Furnace);	
//		player.getInventory().addItem(KitUtils.getNavCompass());
//	}
}
