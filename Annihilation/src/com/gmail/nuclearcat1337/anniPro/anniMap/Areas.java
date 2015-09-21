package com.gmail.nuclearcat1337.anniPro.anniMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.plugin.Plugin;

import com.gmail.nuclearcat1337.anniPro.kits.CustomItem;
import com.gmail.nuclearcat1337.anniPro.kits.KitUtils;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.anniPro.utils.Loc;

public final class Areas implements Iterable<Area>, Listener
{
	private final Map<String,Area> areas;
	private final String world;
	public Areas(String world)
	{
		this.world = world;
		areas = new HashMap<String,Area>();
		//AreaListener l = new AreaListener(this);
		//Bukkit.getPluginManager().registerEvents(l, plugin);
	}
	
//	public Area createNew(String name, Loc one, Loc two, boolean allowPVP)
//	{
//		UseableArea a =  new UseableArea(this,name, one, two);
//		a.allowPVP = allowPVP;e
//		areas.put(name.toLowerCase(),a);
//		return a;
//	}

    public void addArea(Area a)
    {
        this.areas.put(a.getName().toLowerCase(),a);
    }

    public void removeArea(String name)
    {
        this.areas.remove(name.toLowerCase());
    }

    public boolean hasArea(String name)
    {
        return areas.containsKey(name.toLowerCase());
    }
	
	//public String getWorld()
//	{
//		return world;
//	}
	
	public Areas loadAreas(ConfigurationSection areaSection)
	{
		if(areaSection != null)
		{
			//areaSection.set("World",this.getWorld());
			for(String key : areaSection.getKeys(false))
			{
				ConfigurationSection area = areaSection.getConfigurationSection(key);
                Area a = new Area(area);
                areas.put(a.getName().toLowerCase(),a);
//				String name = area.getString("Name");
//				//Location loc1 = ConfigManager.getLocation(area.getConfigurationSection("Corner1Location"));
//				Loc loc1 = new Loc(area.getConfigurationSection("Corner1Location"));
//				//Location loc2 = ConfigManager.getLocation(area.getConfigurationSection("Corner2Location"));
//				Loc loc2 = new Loc(area.getConfigurationSection("Corner2Location"));
//				if(name != null && loc1 != null && loc2 != null)
//				{
//					UseableArea a = new UseableArea(this,name,loc1,loc2);
//					if(area.isBoolean("AllowPVP"))
//						a.setPVP(area.getBoolean("AllowPVP"));
//					areas.put(name.toLowerCase(), a);
//				}
			}
		}
		return this;
	}
	
//	private boolean fallsBetween(int one, int two, int num)
//	{
//		int min, max;
//		if(one < two)
//		{
//			min = one;
//			max = two;
//		}
//		else
//		{
//			min = two;
//			max = one;
//		}
//
//		return num >= min && num <= max;
//	}
	
	public Area getArea(String name)
	{
		return areas.get(name.toLowerCase());
	}
	
	public Area getArea(Loc loc)
	{
		//Bukkit.getLogger().info("Area world: "+this.getWorld()+". Event world: "+loc.getWorld());
		//if(!loc.getWorld().equals(this.getWorld()))
		//	return null;
		for(Area a : areas.values())
		{
//			if(a.getCorner1().getWorld().equals(loc.getWorld()))
//			{
//				if(	fallsBetween(a.getCorner1().getBlockX(),a.getCorner2().getBlockX(),loc.getBlockX()) &&
//					fallsBetween(a.getCorner1().getBlockY(),a.getCorner2().getBlockY(),loc.getBlockY()) &&
//					fallsBetween(a.getCorner1().getBlockZ(),a.getCorner2().getBlockZ(),loc.getBlockZ())	)
//					return a;
//			}
            if(a.isInArea(loc))
                return a;
		}
		return null;
	}
	
	public void saveToConfig(ConfigurationSection areaSection)
	{
		int counter = 1;
		for(Area a : areas.values())
		{
			ConfigurationSection sec = areaSection.createSection(counter+"");
            a.saveToConfig(sec);
//			sec.set("Name", a.getName());
//			sec.set("AllowPVP", a.isPVPAllowed());
//			//ConfigManager.saveLocation(a.getCorner1(), sec.createSection("Corner1Location"));
//			a.getCorner1().saveToConfig(sec.createSection("Corner1Location"));
//			//ConfigManager.saveLocation(a.getCorner2(), sec.createSection("Corner2Location"));
//			a.getCorner2().saveToConfig(sec.createSection("Corner2Location"));
			counter++;
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

	@EventHandler(priority = EventPriority.LOWEST,ignoreCancelled = true)
	public void checkBreaks(EntityDamageByEntityEvent e)
	{
		if(e.getDamager().getType() == EntityType.PLAYER && e.getEntity().getType() == EntityType.PLAYER)
		{
			Area a = this.getArea(new Loc(e.getDamager().getLocation(),false));
			if(a != null && !a.getAllowPVP())
			{
				e.setCancelled(true);
				return;
			}
			a = this.getArea(new Loc(e.getEntity().getLocation(),false));
			if(a != null && !a.getAllowPVP())
			{
				e.setCancelled(true);
				return;
			}
		}
	}

    @EventHandler(ignoreCancelled = true,priority = EventPriority.LOW)
    public void checkFood(FoodLevelChangeEvent event)
    {
        Area a = getArea(new Loc(event.getEntity().getLocation(),false));
        if(a != null && !a.getAllowHunger())
        {
            event.setFoodLevel(20);
            //event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true,priority = EventPriority.LOW)
    public void checkDamage(EntityDamageEvent event)
    {
        Area a = getArea(new Loc(event.getEntity().getLocation(), false));
        if(a != null && !a.getAllowDamage())
            event.setCancelled(true);
    }
	
	@EventHandler(priority = EventPriority.LOWEST,ignoreCancelled = true)
	public void checkBreaks(BlockBreakEvent e)
	{
		if(e.getPlayer().getGameMode() != GameMode.CREATIVE)
		{
			Area a = this.getArea(new Loc(e.getBlock().getLocation(),false));
			if(a != null)
			{
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST,ignoreCancelled = true)
	public void checkBreaks(BlockPlaceEvent e)
	{
		if(e.getPlayer().getGameMode() != GameMode.CREATIVE)
		{
			Area a = this.getArea(new Loc(e.getBlock().getLocation(),false));
			if(a != null)
			{
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST,ignoreCancelled = true)
	public void checkBreaks(PlayerBucketEmptyEvent event)
	{
		if(event.getPlayer().getGameMode() != GameMode.CREATIVE)
		{
			Area a = this.getArea(new Loc(event.getBlockClicked().getRelative(event.getBlockFace()).getLocation(),false));
			if(a != null)
			{
				event.setCancelled(true);
			}
		}
	}

	@Override
	public Iterator<Area> iterator()
	{
		return areas.values().iterator();
	}
}
