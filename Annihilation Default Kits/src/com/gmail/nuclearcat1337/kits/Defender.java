package com.gmail.nuclearcat1337.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.nuclearcat1337.anniPro.anniEvents.AnnihilationEvent;
import com.gmail.nuclearcat1337.anniPro.anniEvents.PlayerKilledEvent;
import com.gmail.nuclearcat1337.anniPro.anniEvents.PlayerKilledEvent.KillAttribute;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.kits.KitUtils;
import com.gmail.nuclearcat1337.anniPro.kits.Loadout;
import com.gmail.nuclearcat1337.base.ConfigurableKit;

public class Defender extends ConfigurableKit
{

	@Override
	protected void setUp() 
	{
		
	}

	@Override
	protected String getInternalName() 
	{
		return "Defender";
	}

	@Override
	protected ItemStack getIcon() 
	{
		return new ItemStack(Material.WOOD_SWORD);
	}

	@Override
	protected int setDefaults(ConfigurationSection section) 
	{
		return 0;
	}
	
//	@Override
//	public boolean hasPermission(Player player)
//	{
//		return false;
//	}
	
	@Override
	protected List<String> getDefaultDescription() 
	{
		List<String> l = new ArrayList<String>();
		addToList(l,new String[]
				{
					aqua+"You are the last line.",
					"",
					aqua+"While around the nexus",
					aqua+"you gain the regeneration",
					aqua+"buff and killing players",
					aqua+"while in the vicinity",
					aqua+"of the nexus rewards you",
					aqua+"with extra experience",
					aqua+"points.",
				});
		return l;
	}
	
	@AnnihilationEvent(priority = EventPriority.MONITOR)
	public void checkXP(PlayerKilledEvent event)
	{
		if(event.getKiller().getTeam().equals(this) && event.getAttributes().contains(KillAttribute.NEXUSDEFENSE))
			event.getKiller().getPlayer().giveExp(20);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void damageHandler(EntityDamageEvent event) 
	{
		if(event.getEntityType() == EntityType.PLAYER)
		{
			AnniPlayer p = AnniPlayer.getPlayer(event.getEntity().getUniqueId());
			if(p != null && p.getTeam() != null && !p.getTeam().isTeamDead() && p.getTeam().getNexus().getLocation() != null && p.getKit().equals(this))
			{
				Player player = (Player)event.getEntity();
				if(player.getLocation().distanceSquared(p.getTeam().getNexus().getLocation().toLocation()) <= 20*20)
					player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,Integer.MAX_VALUE,0));
				else 
					player.removePotionEffect(PotionEffectType.REGENERATION);
			}
		}
	}

	@Override
	public void cleanup(Player arg0) 
	{
		
	}

	@Override
	protected Loadout getFinalLoadout()
	{
		return new Loadout().addWoodSword().addWoodPick().addWoodAxe().addWoodShovel().setUseDefaultArmor(true).setArmor(2,KitUtils.addSoulbound(new ItemStack(Material.CHAINMAIL_CHESTPLATE)));
	}

}
