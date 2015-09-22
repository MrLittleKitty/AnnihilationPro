package com.gmail.nuclearcat1337.kits;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.kits.Loadout;
import com.gmail.nuclearcat1337.base.ConfigurableKit;

public class Vampire extends ConfigurableKit
{
	private Random rand;
	@Override
	protected void setUp()
	{
		rand = new Random();
	}

	@Override
	protected String getInternalName()
	{
		return "Vampire";
	}

	@Override
	protected ItemStack getIcon()
	{
		return new ItemStack(Material.REDSTONE);
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
				aqua+"You are the fear.",
				"",
				aqua+"Every hit you land has",
				aqua+"a 30% chance to drain",
				aqua+"health from your victim",
				aqua+"and heal you.",
			});
		return l;
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void damageHandler(EntityDamageByEntityEvent event) 
	{
		if(event.getDamager().getType() == EntityType.PLAYER)
		{
			Player player = (Player)event.getDamager();
			AnniPlayer p = AnniPlayer.getPlayer(player.getUniqueId());
			if(p != null && p.getKit().equals(this))
			{
				if(rand.nextInt(3) == 1)
				{
					double health = player.getHealth()+1D;
					if(health > player.getMaxHealth())
						health = player.getMaxHealth();
					player.setHealth(health);
				}
			}
		}
	}

	@Override
	protected Loadout getFinalLoadout()
	{
		return new Loadout().addStoneSword().addWoodPick().addWoodAxe()
				.addItem(new Potion(PotionType.NIGHT_VISION).toItemStack(1));
	}

	@Override
	public void cleanup(Player arg0)
	{
		
	}

}
