package com.gmail.nuclearcat1337.kits;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.nuclearcat1337.anniPro.anniEvents.AnnihilationEvent;
import com.gmail.nuclearcat1337.anniPro.anniEvents.ResourceBreakEvent;
import com.gmail.nuclearcat1337.anniPro.kits.Loadout;
import com.gmail.nuclearcat1337.base.ConfigurableKit;

public class Enchanter extends ConfigurableKit
{	
	private Random rand;
	
	@Override
	protected void setUp()
	{
		rand = new Random(System.currentTimeMillis());
	}

	@Override
	protected String getInternalName()
	{
		return "Enchanter";
	}

	@Override
	protected ItemStack getIcon()
	{
		return new ItemStack(Material.EXP_BOTTLE);
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
					aqua+"Gain extra exp when gathering",
					aqua+"resources which enables",
					aqua+"quicker level succession.",
					aqua+"",
					aqua+"There is a small chance",
					aqua+"to obtain experience bottles",
					aqua+"when mining ores and chopping",
					aqua+"wood.",
				});
		return l;
	}


	@Override
	public void cleanup(Player arg0)
	{
		
	}
	
	//Increase the xp gained from mining blocks and potentially gives you an XP bottle (1% chance)
	@AnnihilationEvent
	public void onResourceBreak(ResourceBreakEvent event)
	{
		if(event.getPlayer().getKit().equals(this))
		{
			int xp = event.getXP();
			//Bukkit.getLogger().info("Inital XP: "+xp);
			if(xp > 0)
			{
				//I guess this needs to be verified to actuall give you more XP
				xp = (int)Math.ceil(xp*2);
				event.setXP(xp);
				//Bukkit.getLogger().info("New XP: "+xp);
				if(rand.nextInt(100) == 4)
				{
					Player pl = event.getPlayer().getPlayer();
					if(pl != null)
						pl.getInventory().addItem(new ItemStack(Material.EXP_BOTTLE));
				}
			}
		}
	}

//	@Override
//	public IconPackage getIconPackage()
//	{
//		return new IconPackage(new ItemStack(Material.EXP_BOTTLE), 
//				new String[]{
//								aqua+"Gain extra exp when gathering",
//								aqua+"resources which enables",
//								aqua+"quicker level succession.",
//								aqua+"",
//								aqua+"There is a small chance",
//								aqua+"to obtain experience bottles",
//								aqua+"when mining ores and chopping",
//								aqua+"wood.",
//							});
//	}
//
//	@Override
//	public String getName()
//	{
//		return "Enchanter";
//	}

//	@Override
//	public void onPlayerSpawn(Player player)
//	{
//		KitUtils.giveTeamArmor(player);
//		player.getInventory().addItem(KitUtils.getGoldSword());
//		player.getInventory().addItem(KitUtils.getWoodPick());
//		player.getInventory().addItem(KitUtils.getWoodAxe());
//		player.getInventory().addItem(KitUtils.getNavCompass());
//	}
	
	@Override
	protected Loadout getFinalLoadout()
	{
		return new Loadout().addGoldSword().addWoodPick().addWoodAxe();
	}
}
