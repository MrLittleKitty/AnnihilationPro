package com.gmail.nuclearcat1337.anniPro.anniMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniTeam;
import com.gmail.nuclearcat1337.anniPro.main.Lang;
import com.gmail.nuclearcat1337.anniPro.utils.Loc;
import com.gmail.nuclearcat1337.anniPro.utils.MapKey;
import com.gmail.nuclearcat1337.anniPro.utils.ShopMenu;

public final class Signs implements Iterable<AnniSign>, Listener
{
	private Map<MapKey,AnniSign> signs;
	public Signs()
	{
		signs = new HashMap<MapKey,AnniSign>();
	}
	
	public Signs(ConfigurationSection configSection)
	{
		this();
		if(configSection != null)
		{
			for(String key : configSection.getKeys(false))
			{
				ConfigurationSection sign = configSection.getConfigurationSection(key);
				addSign(new AnniSign(sign));
			}
		}
	}
	
	public void registerListener(Plugin p)
	{
		Bukkit.getPluginManager().registerEvents(this, p);
	}
	
	public void unregisterListener()
	{
		HandlerList.unregisterAll(this);
	}
	
	@Override
	public Iterator<AnniSign> iterator()
	{
		return Collections.unmodifiableMap(signs).values().iterator();
	}
	
	public boolean addSign(AnniSign sign)
	{
		ChatColor g = ChatColor.DARK_GRAY;
		MapKey key = MapKey.getKey(sign.getLocation());
		if(!signs.containsKey(key))
		{
			String[] lore;
//			if(sign.getType().equals(SignType.Brewing))
//				lore = new String[]{g+"["+ChatColor.DARK_PURPLE+"Shop"+g+"]",ChatColor.BLACK+"Brewing"};
//			else if(sign.getType().equals(SignType.Weapon))
//				lore = new String[]{g+"["+ChatColor.DARK_PURPLE+"Shop"+g+"]",ChatColor.BLACK+"Weapon"};
//			else
//			{
//				AnniTeam team = sign.getType().getTeam();
//				lore = new String[]{ChatColor.DARK_PURPLE+"Right Click",ChatColor.DARK_PURPLE+"To Join:",team.getColoredName()+" Team"};
//			}
			if(sign.getType().equals(SignType.Brewing))
				lore = new String[]{g+"["+Lang.SHOP.toString()+g+"]",Lang.BREWINGSIGN.toString()};
			else if(sign.getType().equals(SignType.Weapon))
				lore = new String[]{g+"["+Lang.SHOP.toString()+g+"]",Lang.WEAPONSIGN.toString()};
			else
			{
				AnniTeam team = sign.getType().getTeam();
				lore = Lang.TEAMSIGN.toStringArray(team.getExternalColoredName());
			}
			placeSignInWorld(sign,lore);
			signs.put(key, sign);
			return true;
		}
		return false;
	}
	
	private void placeSignInWorld(AnniSign asign, String[] lore)
	{
		Location loc = asign.getLocation().toLocation();
		Block block = loc.getWorld().getBlockAt(loc);//asign.getLocation().toLocation().getBlock();
		if(block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST)
			block.getWorld().getBlockAt(loc).setType(asign.isSignPost() ? Material.SIGN_POST : Material.WALL_SIGN);
		
		Sign sign = (Sign)block.getState();
		if(sign != null)
		{
			for(int x = 0; x < lore.length; x++)
				sign.setLine(x, lore[x]);
			org.bukkit.material.Sign matSign = new org.bukkit.material.Sign(block.getType());
			matSign.setFacingDirection(asign.getFacingDirection());
			sign.setData(matSign);
			sign.update(true);
		}
	}
	
	public boolean removeSign(Loc sign)
	{
		return removeSign(sign.toLocation());
	}
	
	public boolean removeSign(Location sign)
	{
		boolean b = signs.remove(MapKey.getKey(sign)) == null ? false : true;
		if(b)
			sign.getWorld().getBlockAt(sign).setType(Material.AIR);
		return b;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void signClickCheck(PlayerInteractEvent event)
	{
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			Block b = event.getClickedBlock();
			if(b != null)
			{
				if(b.getType() == Material.WALL_SIGN || b.getType() == Material.SIGN_POST)
				{
					final Location loc = b.getLocation();
					final Player p = event.getPlayer();
					AnniSign sign = this.signs.get(MapKey.getKey(loc));
					if(sign != null)
					{
						event.setCancelled(true);
						if(sign.getType().equals(SignType.Team))
						{
							AnniTeam team = sign.getType().getTeam();
							if(team != null)
							{
								p.performCommand("team "+team.getName());
							}
						}
						else if(sign.getType().equals(SignType.Brewing))
						{
							ShopMenu.openBrewingShop(p);
						}
						else if(sign.getType().equals(SignType.Weapon))
						{
							ShopMenu.openWeaponShop(p);
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW,ignoreCancelled = true)
	public void signBreakCheck(BlockBreakEvent event)
	{
		if(event.getBlock() != null && event.getPlayer().getGameMode() != GameMode.CREATIVE)
		{
			if(event.getBlock().getType() == Material.WALL_SIGN || event.getBlock().getType() == Material.SIGN_POST)
			{
				MapKey key = MapKey.getKey(event.getBlock().getLocation());
				if(this.signs.containsKey(key))
					event.setCancelled(true);
			}
		}
	}
	
	public void saveToConfig(ConfigurationSection configSection)
	{
		if(configSection != null)
		{
			int counter = 1;
			for(AnniSign sign : this)
			{
				sign.saveToConfig(configSection.createSection(counter+""));
				counter++;
			}
		}
	}
}
