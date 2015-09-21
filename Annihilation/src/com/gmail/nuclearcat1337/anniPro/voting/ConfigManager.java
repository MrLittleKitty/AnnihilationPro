package com.gmail.nuclearcat1337.anniPro.voting;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager
{
	private static YamlConfiguration mainConfig = null;
	private static File mainConfigFile = null;
	
	public static void load(Plugin p)
	{
		mainConfigFile = new File(p.getDataFolder().getAbsolutePath());
		if(!mainConfigFile.exists() || !mainConfigFile.isDirectory())
			mainConfigFile.mkdir();
		mainConfigFile = new File(p.getDataFolder().getAbsolutePath(), "AnniMainConfig.yml");
		try
		{
			if(!mainConfigFile.exists())
				mainConfigFile.createNewFile();
			mainConfig = YamlConfiguration.loadConfiguration(mainConfigFile);
			int save = 0;
			
			save += setDefaultIfNotSet(mainConfig,"useMOTD", false);
			save += setDefaultIfNotSet(mainConfig,"ProtocalHack", false);
			save += setDefaultIfNotSet(mainConfig,"EndGameCommand", "stop");
			save += setDefaultIfNotSet(mainConfig,"End-Of-Game-Countdown", 120);
			//save += setDefaultIfNotSet(mainConfig,"Kill-On-Leave", true);

            if(mainConfig.isSet("Kill-On-Leave"))
            {
                mainConfig.set("Kill-On-Leave", null); //We are clearing this from the config because its old and not necessary
                save++;
            }
			
			ConfigurationSection gameVars;
			if(!mainConfig.isConfigurationSection("GameVars"))
			{
				gameVars = mainConfig.createSection("GameVars");
				save += 1;
			}
			else 
				gameVars = mainConfig.getConfigurationSection("GameVars");
			
			save += setDefaultIfNotSet(gameVars,"DefaultGameMode", "adventure");
			
			save += setDefaultIfNotSet(gameVars,"AutoStart.On", false);
			save += setDefaultIfNotSet(gameVars,"AutoStart.PlayersToStart", 4);
			save += setDefaultIfNotSet(gameVars,"AutoStart.CountdownTime", 30);
			
			save += setDefaultIfNotSet(gameVars,"AutoRestart.On", false);
			save += setDefaultIfNotSet(gameVars,"AutoRestart.PlayersToAutoRestart", 0);
			save += setDefaultIfNotSet(gameVars,"AutoRestart.CountdownTime", 15);
			
			save += setDefaultIfNotSet(gameVars,"MapLoading.Voting", true);
			save += setDefaultIfNotSet(gameVars,"MapLoading.Max-Maps-For-Voting", 3);
			save += setDefaultIfNotSet(gameVars,"MapLoading.UseMap", "plugins/Annihilation/Worlds/Test");
			
			save += setDefaultIfNotSet(gameVars,"Team-Balancing.On", true);
			save += setDefaultIfNotSet(gameVars,"Team-Balancing.Tolerance", 2);

            save += setDefaultIfNotSet(gameVars,"Anti-Log-System.On", true);
            save += setDefaultIfNotSet(gameVars,"Anti-Log-System.NPC-Time", 20);
			
//			if(!mainConfig.isConfigurationSection("XP-System"))
//			{
//				XPSystem.setDefaults(mainConfig.createSection("XP-System"));
//				save += 1;
//			}
			
			if(save > 0)
				saveMainConfig();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static int setDefaultIfNotSet(ConfigurationSection section, String path, Object value)
	{
		if(section != null)
		{
			if(!section.isSet(path))
			{
				section.set(path, value);
				return 1;
			}
		}
		return 0;
	}
	
	public static YamlConfiguration getConfig()
	{
		return mainConfig;
	}
	
	public static void saveMainConfig()
	{
		if(mainConfig != null)
		{
			try
			{
				mainConfig.save(mainConfigFile);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
//	public static void Enable(Plugin p)
//	{
//		mainConfigFile = new File(p.getDataFolder().getAbsolutePath());
//		if(!mainConfigFile.exists() || !mainConfigFile.isDirectory())
//			mainConfigFile.mkdir();
//		mainConfigFile = new File(p.getDataFolder().getAbsolutePath(), "AnniMainConfig.yml");
//		try
//		{
//			if(!mainConfigFile.exists())
//				mainConfigFile.createNewFile();
//			mainConfig = YamlConfiguration.loadConfiguration(mainConfigFile);
//			int save = 0;
//			
//			save += setDefaultIfNotSet(mainConfig,"useMOTD", false);
//			save += setDefaultIfNotSet(mainConfig,"ProtocalHack", false);
//			save += setDefaultIfNotSet(mainConfig,"EndGameCommand", "stop");
//			
//			ConfigurationSection gameVars;
//			if(!mainConfig.isConfigurationSection("GameVars"))
//			{
//				gameVars = mainConfig.createSection("GameVars");
//				save += 1;
//			}
//			else 
//				gameVars = mainConfig.getConfigurationSection("GameVars");
//			
//			save += setDefaultIfNotSet(gameVars,"DefaultGameMode", "adventure");
//			
//			save += setDefaultIfNotSet(gameVars,"AutoStart.On", false);
//			save += setDefaultIfNotSet(gameVars,"AutoStart.PlayersToStart", 4);
//			
//			save += setDefaultIfNotSet(gameVars,"AutoRestart.On", false);
//			save += setDefaultIfNotSet(gameVars,"AutoRestart.PlayersToAutoRestart", 0);
//			
//			save += setDefaultIfNotSet(gameVars,"MapLoading.Voting", true);
//			save += setDefaultIfNotSet(gameVars,"MapLoading.UseMap", "plugins/Annihilation/Worlds/Test");
//			
//			save += setDefaultIfNotSet(gameVars,"Team-Balancing.On", true);
//			save += setDefaultIfNotSet(gameVars,"Team-Balancing.Tolerance", 2);
//			
//			if(!mainConfig.isConfigurationSection("XP-System"))
//			{
//				XPSystem.setDefaults(mainConfig.createSection("XP-System"));
//				save += 1;
//			}
//			
//			if(save > 0)
//				saveMainConfig();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//	
//	
//	
//	public static YamlConfiguration loadMapConfig(File mapConfigFile)
//	{
//		if(mapConfigFile != null && mapConfigFile.exists())
//		{
//			try
//			{
//				mapConfig = YamlConfiguration.loadConfiguration(mapConfigFile);
//				ConfigManager.mapConfigFile = mapConfigFile;
//				return mapConfig;
//			}
//			catch(IllegalArgumentException e)
//			{
//				return null;
//			}
//		}
//		return null;
//	}
//	
//	public static void reloadMainConfig()
//	{
//		try
//		{
//			if(mainConfig != null)
//				mainConfig.load(mainConfigFile);
//		}
//		catch (IOException | InvalidConfigurationException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public static void saveMapConfig()
//	{
//		try
//		{
//			mapConfig.save(mapConfigFile);
//		}
//		catch (Exception e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public static YamlConfiguration getMapConfig()
//	{
//		return mapConfig;
//	}
//	
//	public static YamlConfiguration getMainConfig()
//	{
//		return mainConfig;
//	}
//	
//	public static void saveMainConfig()
//	{
//		if(mainConfig != null)
//		{
//			try
//			{
//				mainConfig.save(mainConfigFile);
//			}
//			catch (IOException e)
//			{
//				e.printStackTrace();
//			}
//		}
//	}
	
	
	
//	public static void saveLocation(Location location, ConfigurationSection section)
//	{
//		if(section != null && location != null)
//		{
//			section.set("X", location.getBlockX());
//			section.set("Y", location.getBlockY());
//			section.set("Z", location.getBlockZ());
//			section.set("World", location.getWorld().getName());
//		}
//	}
//	
//	public static void saveLocation(Loc location, ConfigurationSection section)
//	{
//		saveLocation(location.toLocation(),section);
//	}
//	
//	public static void savePreciseLocation(Location location, ConfigurationSection section)
//	{
//		if(section != null && location != null)
//		{
//			section.set("X", location.getX());
//			section.set("Y", location.getY());
//			section.set("Z", location.getZ());
//			section.set("Pitch", location.getPitch());
//			section.set("Yaw", location.getYaw());
//			section.set("World", location.getWorld().getName());
//		}
//	}
//	
//	public static void savePreciseLocation(Loc location, ConfigurationSection section)
//	{
//		saveLocation(location.toLocation(),section);
//	}
//	
//	public static Location getPreciseLocation(ConfigurationSection section)
//	{
//		if(section == null)
//			return null;
//		double x,y,z,pitch,yaw;
//		String world;
//		x = section.getDouble("X");
//		y = section.getDouble("Y");
//		z = section.getDouble("Z");
//		pitch = section.getDouble("Pitch");
//		yaw = section.getDouble("Yaw");
//		world = section.getString("World");
//		if(world == null)
//			return null;
//		return new Location(Bukkit.getWorld(world), x,y,z,(float)yaw,(float)pitch);
//	}
//	
//	public static Location getLocation(ConfigurationSection section)
//	{
//		if(section == null)
//			return null;
//		int x,y,z;
//		String world;
//		x = section.getInt("X");
//		y = section.getInt("Y");
//		z = section.getInt("Z");
//		world = section.getString("World");
//		if(world == null)
//			return null;
//		return new Location(Bukkit.getWorld(world), x,y,z);
//	}
}
