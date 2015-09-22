package com.gmail.nuclearcat1337.base;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;

public class KitConfig
{
	private static KitConfig instance;
	public static KitConfig getInstance()
	{
		if(instance == null)
			instance = new KitConfig();
		return instance;
	}
	
	private YamlConfiguration kitConfig;
	private boolean useAllKits;
	private boolean useDatabase;
	//private Database database;
	//private final ChatColor aqua = ChatColor.AQUA;
	private KitConfig()
	{
		File f = new File(AnnihilationMain.getInstance().getDataFolder().getAbsolutePath(),"StarterKitsConfig.yml");
		boolean b = !f.exists();
		try
		{
			if(b)
				f.createNewFile();
			kitConfig = YamlConfiguration.loadConfiguration(f);
			if(b)
			{
				ConfigurationSection section = kitConfig.createSection("StarterKits");
				section.set("EnableAllKits", true);
				section.set("UseDatabase", false);
				kitConfig.save(f);
			}
			ConfigurationSection section = kitConfig.getConfigurationSection("StarterKits");
			useAllKits = section.getBoolean("EnableAllKits");
			useDatabase = section.getBoolean("UseDatabase");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public Player getPlayerInSightTest(Player player, int distance)
	{
		@SuppressWarnings("deprecation")
		Block[] bs = player.getLineOfSight(null, distance).toArray(new Block[0]);
		List<Entity> near = player.getNearbyEntities(distance, distance, distance);
		Player insight = null;
		for (Block b : bs)
		{
			for (Entity e : near)
			{
				if(e.getType() == EntityType.PLAYER)
				{
					if (e.getLocation().distance(b.getLocation()) < 1)
					{
						if(insight == null || insight.getLocation().distanceSquared(player.getLocation()) > e.getLocation().distanceSquared(player.getLocation()))
							insight = (Player)e;
					}
				}
			}
		}
		return insight;
	}
	
	public Player getPlayerInSight(Player player, int distance)
	{
        Location playerLoc = player.getLocation();
        Vector3D playerDirection = new Vector3D(playerLoc.getDirection());
        Vector3D start = new Vector3D(playerLoc);
        Vector3D end = start.add(playerDirection.multiply(distance));
        Player inSight = null;
        for(Entity nearbyEntity : player.getNearbyEntities(distance, distance, distance ))
        {
        	if(nearbyEntity.getType() == EntityType.PLAYER)
        	{
	            Vector3D nearbyLoc = new Vector3D(nearbyEntity.getLocation());
	 
	            //Bounding box
	            Vector3D min = nearbyLoc.subtract(0.5D, 1.6D, 0.5D);
	            Vector3D max = nearbyLoc.add(0.5D, 0.3D, 0.5D);
	 
	            if(hasIntersection(start, end, min, max))
	            {
	                if(inSight == null || inSight.getLocation().distanceSquared(playerLoc) > nearbyEntity.getLocation().distanceSquared(playerLoc))
	                {
	                    inSight = (Player)nearbyEntity;
	                    return inSight;
	                }
	            }
        	}
        }
 
        return inSight;
    }
 
    private boolean hasIntersection(Vector3D start, Vector3D end, Vector3D min, Vector3D max) 
    {
        final double epsilon = 0.0001f;
 
        Vector3D d = end.subtract(start).multiply(0.5);
        Vector3D e = max.subtract(min).multiply(0.5);
        Vector3D c = start.add(d).subtract(min.add(max).multiply(0.5));
        Vector3D ad = d.abs();
 
        if(Math.abs(c.getX()) > e.getX() + ad.getX()){
            return false;
        }
 
        if(Math.abs(c.getY()) > e.getY() + ad.getY()){
            return false;
        }
 
        if(Math.abs(c.getZ()) > e.getX() + ad.getZ()){
            return false;
        }
 
        if(Math.abs(d.getY() * c.getZ() - d.getZ() * c.getY()) > e.getY() * ad.getZ() + e.getZ() * ad.getY() + epsilon){
            return false;
        }
 
        if(Math.abs(d.getZ() * c.getX() - d.getX() * c.getZ()) > e.getZ() * ad.getX() + e.getX() * ad.getZ() + epsilon){
            return false;
        }
 
        if(Math.abs(d.getX() * c.getY() - d.getY() * c.getX()) > e.getX() * ad.getY() + e.getY() * ad.getX() + epsilon){
            return false;
        }
 
        return true;
    }
	
	public boolean useAllKits()
	{
		return this.useAllKits;
	}
	
	public boolean useDefaultPermissions()
	{
		return !this.useDatabase;
	}
	
	public void saveConfig()
	{
		try
		{
			kitConfig.save(new File(AnnihilationMain.getInstance().getDataFolder().getAbsolutePath(),"StarterKitsConfig.yml"));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ConfigurationSection createKitSection(String kitname)
	{
		return kitConfig.getConfigurationSection("StarterKits").createSection(kitname);
	}
	
	public ConfigurationSection getKitSection(String kit)
	{
		try
		{
			return kitConfig.getConfigurationSection("StarterKits").getConfigurationSection(kit);
		}
		catch(Exception e)
		{
			return null;
		}
	}
}
