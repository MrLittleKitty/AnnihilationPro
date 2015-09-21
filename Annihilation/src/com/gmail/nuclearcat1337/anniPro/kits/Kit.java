package com.gmail.nuclearcat1337.anniPro.kits;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class Kit implements Listener, Comparable<Kit>
{
	public static final Kit CivilianInstance;
	private static final Map<String,Kit> kits;
	static
	{
		kits = new TreeMap<String,Kit>();
		CivilianInstance = new CivilianKit();
		registerKit(CivilianInstance);
	}
	
	static void registerKit(Kit kit)
	{
		kits.put(kit.getName().toLowerCase(), kit);
	}
	
	public static Collection<Kit> getKits()
	{
		return Collections.unmodifiableCollection(kits.values());
	}
	
	public static Kit getKit(String name)
	{
		return kits.get(ChatColor.stripColor(name).toLowerCase());
	}
	
	protected final ChatColor aqua = ChatColor.AQUA;
	
	public abstract boolean Initialize();
	
	public String getName()
	{
		return ChatColor.stripColor(getDisplayName());
	}
	
	public abstract String getDisplayName();
	
	public abstract IconPackage getIconPackage();
	
	public abstract boolean hasPermission(Player player);
	
	public abstract void onPlayerSpawn(Player player);
	
	public abstract void cleanup(Player player);
	
	@Override
	public int compareTo(Kit kit)
	{
		return this.getName().compareTo(kit.getName());
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
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
		Kit other = (Kit) obj;
		if (this.getName() == null)
		{
			if (other.getName() != null)
				return false;
		}
		else if (!this.getName().equals(other.getName()))
			return false;
		return true;
	}
}
