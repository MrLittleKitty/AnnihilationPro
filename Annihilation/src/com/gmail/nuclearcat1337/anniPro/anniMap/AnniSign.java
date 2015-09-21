package com.gmail.nuclearcat1337.anniPro.anniMap;

import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniTeam;
import com.gmail.nuclearcat1337.anniPro.utils.Loc;

public final class AnniSign
{
	private final FacingObject obj;
	private boolean signPost;
	private SignType type;
	
	public AnniSign(FacingObject obj, boolean signPost, SignType type)
	{
		this.obj = obj;
		this.signPost = signPost;
		this.type = type;
	}
	
	public AnniSign(ConfigurationSection configSection)
	{
		if(configSection == null)
			throw new NullPointerException();
		
		boolean signpost = configSection.getBoolean("isSignPost");
		//Location loc = ConfigManager.getLocation(configSection.getConfigurationSection("Location"));
		Loc loc = new Loc(configSection.getConfigurationSection("Location"));
		BlockFace facing = BlockFace.valueOf(configSection.getString("FacingDirection"));	
		obj = new FacingObject(facing, loc);
		this.signPost = signpost;
		String data = configSection.getString("Data");
		if(data.equalsIgnoreCase("Brewing"))
			type = SignType.Brewing;
		else if(data.equalsIgnoreCase("Weapon"))
			type = SignType.Weapon;
		else
			type = SignType.newTeamSign(AnniTeam.getTeamByName(data.split("-")[1]));
	}
	
	//public abstract void onSignClick(final Player player);
	
	public boolean isSignPost()
	{
		return this.signPost;
	}
	
	public Loc getLocation()
	{
		return obj.getLocation();
	}
	
	public BlockFace getFacingDirection()
	{
		return obj.getFacingDirection();
	}
	
	public SignType getType()
	{
		return this.type;
	}
	
	public void saveToConfig(ConfigurationSection configSection)
	{
		if(configSection != null)
		{
			configSection.set("isSignPost", this.isSignPost());
			//ConfigManager.saveLocation(this.getLocation(), configSection.createSection("Location"));
			getLocation().saveToConfig(configSection.createSection("Location"));
			configSection.set("FacingDirection", this.getFacingDirection().name());	
			String data;
			if(this.getType().equals(SignType.Brewing))
				data = "Brewing";
			else if(this.getType().equals(SignType.Weapon))
				data = "Weapon";
			else
				data = "Team-"+this.getType().getTeam().getName();	
			configSection.set("Data", data);
		}
	}
}
