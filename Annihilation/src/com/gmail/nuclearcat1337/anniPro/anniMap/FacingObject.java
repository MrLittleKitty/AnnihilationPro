package com.gmail.nuclearcat1337.anniPro.anniMap;

import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;

import com.gmail.nuclearcat1337.anniPro.utils.Loc;

public final class FacingObject
{
	private final BlockFace facing;
	private final Loc location;
	public FacingObject(BlockFace facingDirection, Loc location)
	{
		this.facing = facingDirection;
		this.location = location;
	}
	
	public BlockFace getFacingDirection()
	{
		return facing;
	}
	
	public Loc getLocation()
	{
		return location;
	}
	
	public void saveToConfig(ConfigurationSection configSection)
	{
		if(configSection != null)
		{
			configSection.set("FacingDirection", facing.name());
			location.saveToConfig(configSection.createSection("Location"));
			//ConfigManager.saveLocation(location, configSection.createSection("Location"));
		}
	}
	
	public static FacingObject loadFromConfig(ConfigurationSection configSection)
	{
		if(configSection != null)
		{
			//Location loc = ConfigManager.getLocation(configSection.getConfigurationSection("Location"));
			Loc loc = new Loc(configSection.getConfigurationSection("Location"));
			BlockFace facing = BlockFace.valueOf(configSection.getString("FacingDirection"));
			return new FacingObject(facing,loc);
		}
		return null;
	}
}
