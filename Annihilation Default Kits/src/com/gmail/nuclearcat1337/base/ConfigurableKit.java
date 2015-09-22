package com.gmail.nuclearcat1337.base;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.kits.IconPackage;
import com.gmail.nuclearcat1337.anniPro.kits.Kit;
import com.gmail.nuclearcat1337.anniPro.kits.Loadout;
import com.gmail.nuclearcat1337.anniPro.voting.ConfigManager;

public abstract class ConfigurableKit extends Kit
{
	private String name;
	private ItemStack icon;
	private String[] kitDescription;
	protected KitConfig instance;
	private Loadout loadout;
	private boolean isFree;
	
	@Override
	public boolean Initialize()
	{
		//TODO------Change all the class instances to use this one instance instead of KitConfig.getInstance()
		instance = KitConfig.getInstance();
		int x = 0;
		ConfigurationSection sec = instance.getKitSection(getInternalName());
		if(sec == null)
		{
			sec = instance.createKitSection(getInternalName());
			x++;
		}
		
		x += ConfigManager.setDefaultIfNotSet(sec, "Name", getInternalName());
		x += ConfigManager.setDefaultIfNotSet(sec, "Kit Description", getDefaultDescription());
		x += ConfigManager.setDefaultIfNotSet(sec, "Disable", false);
		x += ConfigManager.setDefaultIfNotSet(sec, "Free", false);
		x += setDefaults(sec);
		
		if(x > 0)
			instance.saveConfig();
		
		this.isFree = sec.getBoolean("Free");
		
		if(sec.getBoolean("Disable"))
			return false;
		
		loadKitStuff(sec);
		if(instance.useDefaultPermissions())
		{
			Permission perm = new Permission("Anni.Kits."+getName());
			perm.setDefault(PermissionDefault.FALSE);
			Bukkit.getPluginManager().addPermission(perm);
			perm.recalculatePermissibles();
		}
		icon = getIcon();
		setUp();
		this.loadout = getFinalLoadout().addNavCompass().finalizeLoadout();
		return true;
	}
	
	@Override
	public IconPackage getIconPackage()
	{
		return new IconPackage(icon,kitDescription);
	}
	
	protected abstract void setUp();
	protected abstract String getInternalName();
	protected abstract ItemStack getIcon();
	protected abstract int setDefaults(ConfigurationSection section);
	protected abstract List<String> getDefaultDescription();
	protected abstract Loadout getFinalLoadout();
	
	protected void addToList(List<String> list, String... strings)
	{
		for(String str : strings)
			list.add(str);
	}
	
	@Override
	public String getDisplayName()
	{
		return name;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean hasPermission(Player player)
	{
		//Bukkit.getLogger().info("Perms Check on "+player.getName());
		if(instance.useAllKits())
			return true;
		if(this.isFree)
			return true;
		if(instance.useDefaultPermissions())
			return player.hasPermission("Anni.Kits."+ChatColor.stripColor(getName()));
		else
		{
			//Bukkit.getLogger().info("This 1");
			AnniPlayer p = AnniPlayer.getPlayer(player.getUniqueId());
			if(p != null)
			{
				//Bukkit.getLogger().info("This 2");
				Object obj = p.getData("Kits");
				if(obj != null && obj instanceof List)
				{
					//Bukkit.getLogger().info("This 3");
					List<String> l = (List<String>)obj;
					return l.contains(getName().toLowerCase());
				}
			}
			return false;
		}
	}

	protected void loadKitStuff(ConfigurationSection section)
	{
		this.name = section.getString("Name");
		this.kitDescription = getArrayFromList(section.getStringList("Kit Description"));
	}
	
	private String[] getArrayFromList(List<String> list)
	{
		String[] r = new String[list.size()];
		for(int x = 0; x < list.size(); x++)
			r[x] = list.get(x);
		return r;
	}
	
	@Override
	public void onPlayerSpawn(Player player)
	{
		loadout.giveLoadout(player);
	}
}
