package com.gmail.nuclearcat1337.anniPro.anniMap;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.gmail.nuclearcat1337.anniPro.anniGame.Game;
import com.gmail.nuclearcat1337.anniPro.utils.Loc;

public abstract class AnniMap
{	
	private String world;
	private Areas areas;
	private Signs signs;
	
	protected final File configFile;
	private final YamlConfiguration config;
	
	public AnniMap(String worldName, File configFile)
	{
		this.configFile = configFile;
		com.gmail.nuclearcat1337.anniPro.utils.Util.tryCreateFile(configFile);
		config = YamlConfiguration.loadConfiguration(configFile);
			
		this.world = worldName;
		this.areas = new Areas(worldName);	
		this.signs = new Signs();
		
		//ConfigurationSection mapSec = config.getConfigurationSection("Map");
		//if(mapSec != null)
		//{
			String world = config.getString("WorldName");
			if(world != null)
				this.world = world;
			ConfigurationSection areaSec = config.getConfigurationSection("Areas");
            areas.loadAreas(areaSec);
//			if(areaSec != null)
//			{
//				for(String key : areaSec.getKeys(false))
//				{
//					ConfigurationSection area = areaSec.getConfigurationSection(key);
//					if(area != null)
//					{
//						Loc one = new Loc(area.getConfigurationSection("Corner1"));
//						Loc two = new Loc(area.getConfigurationSection("Corner2"));
//						areas.createNew(area.getString("Name"), one, two,area.getBoolean("AllowPVP"));
//					}
//				}
//			}
			ConfigurationSection signSec = config.getConfigurationSection("Signs");
			if(signSec != null)
			{
				signs = new Signs(signSec);
			}
			loadFromConfig(config);
		//}	
	}
	
	protected abstract void loadFromConfig(ConfigurationSection section);
	
	protected abstract void saveToConfig(ConfigurationSection section);
	
//	public void unloadMap()
//	{
//		ConfigurationSection map = config.createSection("Map");
//		this.saveConfigVars(map);
//		saveConfig();
//		unload(map);
//		//Bukkit.unloadWorld(this.getWorldName(), false);
//	}
	
	public YamlConfiguration getConfig()
	{
		return config;
	}
	
//	public ConfigurationSection getMapSection()
//	{
//		return config.isConfigurationSection("Map") ? config.getConfigurationSection("Map") : config.createSection("Map");
//	}
	
	public void saveConfig()
	{
		try
		{
			config.save(configFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void registerListeners(Plugin plugin)
	{
		areas.registerListener(plugin);
		signs.registerListener(plugin);
	}
	
	public void unregisterListeners()
	{
		areas.unregisterListener();
		signs.unregisterListener();
	}
	
	public Areas getAreas()
	{
		return areas;
	}
	
	public Signs getSigns()
	{
		return signs;
	}
	
	public String getWorldName()
	{
		return world;
	}
	
	public World getWorld()
	{
		return Bukkit.getWorld(getWorldName());
	}
	
	public String getNiceWorldName()
	{
		return this.toString();
	}
	
	@Override
	public String toString()
	{
		String str = Game.getNiceWorldName(this.world);
		return str == null ? this.getWorldName() : str;
	}
	
	protected void setWorldName(String name)
	{
		this.world = name;
	}
	
	public void saveToConfig()
	{
		//ConfigurationSection map = config.createSection("Map");

		config.set("WorldName", this.getWorldName());
		ConfigurationSection areaSection = config.createSection("Areas");
        areas.saveToConfig(areaSection);
//		for(Area a : areas)
//		{
//			ConfigurationSection area = areaSection.createSection(a.getName());
//			area.set("Name", a.getName());
//			area.set("AllowPVP", a.isPVPAllowed());
//			//ConfigManager.saveLocation(a.getCorner1(), area.createSection("Corner1"));
//			//ConfigManager.saveLocation(a.getCorner2(), area.createSection("Corner2"));
//			a.getCorner1().saveToConfig(area.createSection("Corner1"));
//			a.getCorner2().saveToConfig(area.createSection("Corner2"));
//		}
		ConfigurationSection signSection = config.createSection("Signs");
		signs.saveToConfig(signSection);
		
		saveToConfig(config);
		
		this.saveConfig();
		
	}
}
