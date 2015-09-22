package com.gmail.nuclearcat1337.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.kits.KitUtils;
import com.gmail.nuclearcat1337.anniPro.kits.Loadout;
import com.gmail.nuclearcat1337.base.SpecialItemKit;


public class Thor extends SpecialItemKit
{
	@Override
	protected void onInitialize()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected ItemStack specialItem()
	{
		ItemStack hammer  = KitUtils.addSoulbound(new ItemStack(Material.GOLD_AXE));			
		ItemMeta meta = hammer.getItemMeta();
		meta.setDisplayName(getSpecialItemName()+" "+ChatColor.GREEN+"READY");
		hammer.setItemMeta(meta);
		return hammer;
	}

	@Override
	protected String defaultSpecialItemName()
	{
		return ChatColor.GOLD+"Hammer";
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
		player.getWorld().strikeLightning(player.getLocation());
		return true;
	}
	
	@EventHandler(priority= EventPriority.NORMAL)
	public void checkStrike(EntityDamageEvent event)
	{
		if(event.getEntityType() == EntityType.PLAYER && event.getCause() == DamageCause.LIGHTNING)
		{
			AnniPlayer player = AnniPlayer.getPlayer(event.getEntity().getUniqueId());
			if(player != null && !player.getKit().equals(this))
			{
				Object obj = player.getData("LH");
				if(obj != null)
				{
					Long l = (Long)obj;
					if(System.currentTimeMillis()-l <= 30000)
					{
						event.setCancelled(true);
						player.setData("LH", null);
						return;
					}
				}
				event.setDamage(4);
				player.setData("LH", System.currentTimeMillis());
			}
		}
	}

	@Override
	protected long getDelayLength()
	{
		return 120000;
	}

	@Override
	protected boolean useDefaultChecking()
	{
		return true;
	}

	@Override
	protected String getInternalName()
	{
		return "Thor";
	}

	@Override
	protected ItemStack getIcon()
	{
		return new ItemStack(Material.GOLD_AXE);
	}

	@Override
	protected List<String> getDefaultDescription()
	{
		List<String> l = new ArrayList<String>();
		addToList(l,new String[]
				{
					aqua+"You are the hammer.",
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
	protected Loadout getFinalLoadout()
	{
		return new Loadout().addStoneSword().addWoodPick().addWoodAxe().addItem(super.getSpecialItem()).addNavCompass().finalizeLoadout();
	}

	@Override
	public void cleanup(Player arg0)
	{
		
	}

}
