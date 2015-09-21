package com.gmail.nuclearcat1337.anniPro.utils;

import org.bukkit.Location;

public class MapKey implements Comparable<MapKey>
{
	public static MapKey getKey(Location location)
	{
		return location != null ? new MapKey(location.getBlockX()+" "+location.getBlockY()+" "+location.getBlockZ()+" "+location.getWorld().getName()) : null;
	}
	
	public static MapKey getKey(Loc location)
	{
		return location != null ? getKey(location.toLocation()) : null;
	}
	
	private String key;	
	private MapKey(String key)
	{
		this.key = key;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MapKey other = (MapKey) obj;
		if (key == null)
		{
			if (other.key != null)
				return false;
		}
		else if (!key.equals(other.key))
			return false;
		return true;
	}

	@Override
	public int compareTo(MapKey o)
	{
		return key.compareTo(o.key);
	}
}
