package com.gmail.nuclearcat1337.anniPro.anniMap;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.gmail.nuclearcat1337.anniPro.anniGame.GameVars;
import com.gmail.nuclearcat1337.anniPro.kits.CustomItem;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.anniPro.utils.Loc;

public class LobbyMap extends AnniMap
{
	private Location spawn;
	
	public LobbyMap(Location spawn)
	{
		super(spawn.getWorld().getName(),new File(AnnihilationMain.getInstance().getDataFolder(),"AnniLobbyConfig.yml"));
		this.spawn = spawn;
	}
	
	public LobbyMap(File configFile)
	{
		super(null,configFile);
	//	spawn = ConfigManager.getLocation(configSection.getConfigurationSection("SpawnLocation"));
	}
	
	public Location getSpawn()
	{
		return spawn;
	}
	
	public void setSpawn(Location newSpawn)
	{
		this.spawn = newSpawn;
		super.setWorldName(newSpawn.getWorld().getName());
	}
	
	public void sendToSpawn(final Player player)
	{
		if(spawn != null && player != null)
		{
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			player.setHealth(player.getMaxHealth());
			player.setFoodLevel(20);
			player.setGameMode(GameVars.getDefaultGamemode());
			player.getInventory().addItem(CustomItem.KITMAP.toItemStack());
			player.getInventory().addItem(CustomItem.TEAMMAP.toItemStack());
			if(GameVars.getVoting())
				player.getInventory().addItem(CustomItem.VOTEMAP.toItemStack());
			player.teleport(getSpawn());
		}
	}

	@Override
	protected void loadFromConfig(ConfigurationSection section)
	{
		if(section != null && section.isConfigurationSection("SpawnLocation"))
		{
			spawn = new Loc(section.getConfigurationSection("SpawnLocation")).toLocation();
			super.setWorldName(spawn.getWorld().getName());
		}
	}

	@Override
	protected void saveToConfig(ConfigurationSection section)
	{
		if(this.getSpawn() != null && section != null)
			new Loc(this.getSpawn(),true).saveToConfig(section.createSection("SpawnLocation"));
		//super.saveConfig();
	}
	
//	public void saveToConfig(ConfigurationSection configSection)
//	{
//		if(configSection != null)
//		{
//			super.saveToConfig(configSection);
//			//ConfigManager.saveLocation(this.getSpawn(), configSection.createSection("SpawnLocation"));
//			
//		}
//	}
}
