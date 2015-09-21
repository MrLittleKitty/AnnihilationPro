package com.gmail.nuclearcat1337.anniPro.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public final class Loc
{
	private Number x,y,z;
	private float pitch,yaw;
	private String world;
	
	public Loc(Location loc, boolean precise)
	{
		this.world = loc.getWorld().getName();
		if(precise)
		{
			x = loc.getX();
			y = loc.getY();
			z = loc.getZ();
			pitch = loc.getPitch();
			yaw = loc.getYaw();
		}
		else
		{
			x = loc.getBlockX();
			y = loc.getBlockY();
			z = loc.getBlockZ();
			pitch = 0;
			yaw = 0;
		}
	}
	
	public Loc(String world, Number x, Number y, Number z)
	{
		this(world,x,y,z,0,0);
	}
	
	public Loc(String world, Number x, Number y, Number z, float pitch, float yaw)
	{
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
	}
	
	public Loc(ConfigurationSection section)
	{
		assert section != null;
		this.world = section.getString("World");
		this.pitch = (float)section.getDouble("Pitch");
		this.yaw = (float)section.getDouble("Yaw");
		if(section.isDouble("X"))
			x = section.getDouble("X");
		else x = section.getInt("X");
		if(section.isDouble("Y"))
			y = section.getDouble("Y");
		else y = section.getInt("Y");
		if(section.isDouble("Z"))
			z = section.getDouble("Z");
		else z = section.getInt("Z");
	}
	
	public int getBlockX()
	{
		//return (int)x.doubleValue();
		return x.intValue();
	}
	
	public int getBlockY()
	{
		//return (int)y.doubleValue();
		return y.intValue();
	}
	
	public int getBlockZ()
	{
		//return (int)z.doubleValue();
		return z.intValue();
	}
	
	public String getWorld()
	{
		return world;
	}
	
	public Location toLocation()
	{
		return new Location(Bukkit.getWorld(world),x.doubleValue(),y.doubleValue(),z.doubleValue(),yaw,pitch);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this)
			return true;
		if(obj instanceof Loc)
		{
			Loc l = (Loc)obj;
			return this.world.equals(l.world) && this.getBlockX() == l.getBlockX() && this.getBlockY() == l.getBlockY() && this.getBlockZ() == l.getBlockZ();
		}
		else if(obj instanceof Location)
		{
			Location l = (Location)obj;
			return this.world.equals(l.getWorld().getName()) && this.getBlockX() == l.getBlockX() && this.getBlockY() == l.getBlockY() && this.getBlockZ() == l.getBlockZ();
		}
		return false;
	}
	
	public void saveToConfig(ConfigurationSection section)
	{
		section.set("World", world);
		section.set("X", x);
		section.set("Y", y);
		section.set("Z", z);
		section.set("Pitch", (double)pitch);
		section.set("Yaw", (double)yaw);
	}
}