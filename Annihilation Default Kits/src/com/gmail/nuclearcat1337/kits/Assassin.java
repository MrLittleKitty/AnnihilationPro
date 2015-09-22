package com.gmail.nuclearcat1337.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.kits.KitUtils;
import com.gmail.nuclearcat1337.anniPro.kits.Loadout;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.base.SpecialItemKit;

public class Assassin extends SpecialItemKit
{
	//private ItemStack leap;
	
	@Override
	protected void onInitialize()
	{
		
	}

	@Override
	protected ItemStack specialItem()
	{
		ItemStack leap = KitUtils.addSoulbound(new ItemStack(Material.FEATHER));
		ItemMeta meta = leap.getItemMeta();
		meta.setDisplayName(getSpecialItemName()+" "+ChatColor.GREEN+"READY");
		leap.setItemMeta(meta);
		return leap;
	}

	@Override
	protected String defaultSpecialItemName()
	{
		return ChatColor.AQUA+"Leap";
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
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void damageHandler(EntityDamageByEntityEvent event) 
	{
		if(event.getDamager().getType() == EntityType.PLAYER)
		{
			Player player = (Player)event.getDamager();
			AnniPlayer p = AnniPlayer.getPlayer(player.getUniqueId());
			if(p != null && p.getKit().equals(this) && p.getData("Cur") != null)
				endLeap(player,p);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void damageHandler(EntityDamageEvent event) 
	{
		if(event.getEntityType() == EntityType.PLAYER)
		{
			AnniPlayer p = AnniPlayer.getPlayer(event.getEntity().getUniqueId());
			if(p != null && p.getKit().equals(this) && p.getData("Cur") != null)
			{
				if(event.getCause() == DamageCause.FALL)
						event.setCancelled(true);
				else
					endLeap((Player)event.getEntity(),p);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected boolean performSpecialAction(Player player, AnniPlayer p)
	{
		p.setData("Arm",player.getInventory().getArmorContents().clone());
		p.setData("Cur", true);
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,160,0));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,160,0));
		player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,160,1));
		player.getInventory().setArmorContents(null);
		player.updateInventory();
		player.setVelocity(player.getLocation().getDirection().setY(1).multiply(1));
		new EndLeap(player,p).runTaskLater(AnnihilationMain.getInstance(), 160);
		return true;
	}
	
	private void endLeap(Player player, AnniPlayer p)
	{
		if(p.getData("Cur") != null)
		{
			Object obj = p.getData("Arm");
			if(obj != null && player != null)
				player.getInventory().setArmorContents((ItemStack[])obj);
			p.setData("Arm", null);
			if(player != null)
			{
				player.removePotionEffect(PotionEffectType.INVISIBILITY);
				player.removePotionEffect(PotionEffectType.SPEED);
				player.removePotionEffect(PotionEffectType.FAST_DIGGING);
			}
			p.setData("Cur", null);
		}
	}
	
	private class EndLeap extends BukkitRunnable
	{
		private final Player player;
		private final AnniPlayer p;
		
		public EndLeap(Player player, AnniPlayer p)
		{
			this.player = player;
			this.p = p;
		}
		
		@Override
		public void run() 
		{
			endLeap(player,p);
		}
		
	}

	@Override
	protected long getDelayLength()
	{
		return 40000;
	}

	@Override
	protected boolean useDefaultChecking()
	{
		return true;
	}

	@Override
	protected String getInternalName()
	{
		return "Assassin";
	}

	@Override
	protected ItemStack getIcon()
	{
		return new ItemStack(Material.GOLD_SWORD);
	}

	@Override
	protected List<String> getDefaultDescription()
	{
		List<String> l = new ArrayList<String>();
		addToList(l,new String[]
			{
				aqua+"Leap over high objects",
				aqua+"while not taking any fall",
				aqua+"damage and sneakily take",
				aqua+"out all your opponents.",
				"",
				aqua+"While you have leapt you",
				aqua+"have the invisibility,",
				aqua+"haste and speed buffs.",
				"",
				aqua+"When using the ability",
				aqua+"your armor dissapears.",
				aqua+"However, when you are hit",
				aqua+"or when you hit someone",
				aqua+"your armor reappears.",
			});
		return l;
	}

	@Override
	public void cleanup(Player player)
	{
		
	}

//	@Override
//	public void onPlayerSpawn(Player player)
//	{
//		KitUtils.giveTeamArmor(player);
//		player.getInventory().addItem(KitUtils.getWoodSword());
//		player.getInventory().addItem(KitUtils.getWoodPick());
//		player.getInventory().addItem(KitUtils.getWoodAxe());
//		player.getInventory().addItem(KitUtils.getWoodShovel());
//		super.giveSpecialItem(player);
//	}
	
	@Override
	protected Loadout getFinalLoadout()
	{
		return new Loadout().addWoodSword().addWoodPick().addWoodAxe().addWoodShovel().addItem(super.getSpecialItem());
	}

}
