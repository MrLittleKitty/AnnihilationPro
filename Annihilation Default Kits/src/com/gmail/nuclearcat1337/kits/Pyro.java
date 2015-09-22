package com.gmail.nuclearcat1337.kits;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.kits.KitUtils;
import com.gmail.nuclearcat1337.anniPro.kits.Loadout;
import com.gmail.nuclearcat1337.base.SpecialItemKit;

public class Pyro extends SpecialItemKit
{
	
	private Random rand;
	
	@Override
	protected void onInitialize()
	{
		rand = new Random(System.currentTimeMillis());
	}

	@Override
	protected ItemStack specialItem()
	{
		ItemStack firestorm  = KitUtils.addSoulbound(new ItemStack(Material.FIREBALL));			
		ItemMeta meta = firestorm.getItemMeta();
		meta.setDisplayName(getSpecialItemName()+" "+ChatColor.GREEN+"READY");
		firestorm.setItemMeta(meta);
		return firestorm;
	}

	@Override
	protected String defaultSpecialItemName()
	{
		return ChatColor.AQUA+"Firestorm";
	}

	@Override
	protected boolean isSpecialItem(ItemStack stack)
	{
		if(stack != null && stack.hasItemMeta() && stack.getItemMeta().hasDisplayName())
		{
			String name = stack.getItemMeta().getDisplayName();
			if(name.contains(getSpecialItemName()) && KitUtils.isSoulbound(stack))
				return true;
		}
		return false;
	}

	@Override
	protected boolean performSpecialAction(Player player, AnniPlayer p)
	{
		if(p.getTeam() != null)
		{
			for(Entity e : player.getNearbyEntities(5, 5, 5))
			{
				if(e.getType() == EntityType.PLAYER)
				{
					AnniPlayer d = AnniPlayer.getPlayer(e.getUniqueId());
					if(d != null && d.getTeam() != null && !d.getTeam().equals(p.getTeam()))
						e.setFireTicks(40);
				}
			}
			player.sendMessage(ChatColor.DARK_RED+ChatColor.stripColor(getSpecialItemName()).toUpperCase()+"!");
			return true;
		}
		else return false;
	}

	@Override
	protected long getDelayLength()
	{
		return 40000;
	}

	@Override
	protected String getInternalName()
	{
		return "Pyro";
	}

	@Override
	protected ItemStack getIcon()
	{
		return new ItemStack(Material.FIRE);
	}

	@Override
	protected List<String> getDefaultDescription()
	{
		List<String> l = new ArrayList<String>();
		addToList(l,new String[]
				{
					aqua+"You are the flame.",
					aqua+"",
					aqua+"You are not afraid of",
					aqua+"lava and fire because",
					aqua+"you are immune, but your",
					aqua+"enemies are not.",
					aqua+"",
					aqua+"Every hit you land has",
					aqua+"a chance of igniting your",
					aqua+"enemy.",
				});
		return l;
	}

	@Override
	public void cleanup(Player player)
	{
		if(player != null)
			player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
	}

	@Override
	public void onPlayerSpawn(Player player)
	{
		player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
		super.onPlayerSpawn(player);
//		KitUtils.giveTeamArmor(player);
//		player.getInventory().addItem(KitUtils.getStoneSword());
//		player.getInventory().addItem(KitUtils.getWoodPick());
//		player.getInventory().addItem(KitUtils.getWoodAxe());
//		player.getInventory().addItem(KitUtils.getHealthPotion1());
//		super.giveSpecialItem(player);
//		player.getInventory().addItem(KitUtils.getNavCompass());
	}
	
	@Override
	protected Loadout getFinalLoadout()
	{
		return new Loadout().addStoneSword().addWoodPick().addWoodAxe().addHealthPotion1().addItem(super.getSpecialItem());
	}
	
	//Lites their arrow on fire
	@EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
	public void arrowLaunch(final ProjectileLaunchEvent event)
	{
		if(event.getEntityType() == EntityType.ARROW)
		{
			ProjectileSource shooter = event.getEntity().getShooter();
			if(shooter instanceof Player)
			{
				AnniPlayer p = AnniPlayer.getPlayer(((Player) shooter).getUniqueId());
				if(p != null && p.getKit().equals(this))
					event.getEntity().setFireTicks(999999);
			}
		}
	}
	
	//Adds the chance to ignite enemies when you hit them
	@EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
	public void damageListener(final EntityDamageByEntityEvent event)
	{
		Entity one = event.getDamager();
		if(one.getType() == EntityType.PLAYER && event.getEntity().getType() == EntityType.PLAYER)
		{
			Player damager = (Player)one;
			AnniPlayer d = AnniPlayer.getPlayer(damager.getUniqueId());
			if(d != null && d.getKit().equals(this))
			{
				if(rand.nextInt(100) < 37)
					event.getEntity().setFireTicks(40);
			}
		}
	}

	@Override
	protected boolean useDefaultChecking()
	{
		return true;
	}

}
