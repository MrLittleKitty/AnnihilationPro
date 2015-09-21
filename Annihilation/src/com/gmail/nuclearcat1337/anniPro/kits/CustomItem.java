package com.gmail.nuclearcat1337.anniPro.kits;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.nuclearcat1337.anniPro.main.Lang;

public enum CustomItem
{
	NAVCOMPASS(ChatColor.DARK_PURPLE+"Right click to change target nexus",Material.COMPASS,(byte)0,true,null),
	KITMAP(ChatColor.AQUA+"Right click to choose a kit",Material.BOOK,(byte)0,true,null),
	VOTEMAP(ChatColor.AQUA+"Right click to vote for a map",Material.GRASS,(byte)0,true,null),
	TEAMMAP(ChatColor.AQUA+"Right click to join a team",Material.NETHER_STAR,(byte)0,true,null),
	MAPBUILDER(ChatColor.AQUA+"Right click to open the map builder",Material.DIAMOND_PICKAXE,(byte)0,true,null),
	BREWINGSHOP(ChatColor.AQUA+"Brewing Shop Helper",Material.BREWING_STAND_ITEM,(byte)0,true, new String[] 
							{
								ChatColor.DARK_PURPLE+"Right click to add a brewing shop.",
								ChatColor.DARK_PURPLE+"Left click to remove a brewing shop."
							}),
	WEAPONSHOP(ChatColor.AQUA+"Weapon Shop Helper",Material.ARROW,(byte)0,true, new String[] 
							{
								ChatColor.DARK_PURPLE+"Right click to add a weapon shop.",
								ChatColor.DARK_PURPLE+"Left click to remove a weapon shop."
							}),
	ENDERFURNACE(ChatColor.AQUA+"Ender Furnace Helper",Material.EYE_OF_ENDER,(byte)0,true, new String[] 
							{
								ChatColor.DARK_PURPLE+"Right click to add an ender furnace.",
								ChatColor.DARK_PURPLE+"Left click to remove an ender furnace."
							}),
	REGENBLOCKHELPER(ChatColor.AQUA+"Regenerating Block Helper",Material.STICK,(byte)0,true, new String[] 
							{
								ChatColor.DARK_PURPLE+"Left or Right click a block to select it."
							}),
	AREAWAND(ChatColor.AQUA+"Area Wand",Material.BLAZE_ROD,(byte)0,true, new String[] 
							{
								ChatColor.DARK_PURPLE+"Left click a block to set it as corner one.",
								ChatColor.DARK_PURPLE+"Right click a block to set it as corner two."
							}),
	DIAMONDHELPER(ChatColor.AQUA+"Diamond Helper",Material.DIAMOND,(byte)0,true, new String[] 
							{
								ChatColor.DARK_PURPLE+"Left click a block to add it as a diamond.",
								ChatColor.DARK_PURPLE+"Right click a block to remove it as a diamond."
							}),
	UNPLACEABLEBLOCKSWAND(ChatColor.AQUA+"Unplaceable Blocks Wand",Material.DIAMOND_SPADE,(byte)0,true, new String[] 
							{
								ChatColor.DARK_PURPLE+"Left click a block to add it as unplaceable",
								ChatColor.DARK_PURPLE+"Right click a block to remove it as a diamond."
							});
							
	
	private String name;
	private String[] lore;
	private Material type;
	private byte data;
	private boolean soulBound;

	CustomItem(String name, Material type, byte data, boolean soulBound, String[] lore) 
	{
		this.name = name;
		this.lore = lore;
		this.type = type;
		this.data = data;
		this.soulBound = soulBound;
	}

	public ItemStack toItemStack(int amount) 
	{
		@SuppressWarnings("deprecation")
		ItemStack stack = new ItemStack(type,amount,(short)0,data);
		ItemMeta meta = stack.getItemMeta();
		if (name != null) 
			meta.setDisplayName(this.getName());
		if (lore != null) 
			meta.setLore(Arrays.asList(lore));
		stack.setItemMeta(meta);
		return (this.soulBound ? KitUtils.addSoulbound(stack) : stack);
	}
	
	public ItemStack toItemStack() 
	{
		return toItemStack(1);
	}
	
	public String getName()
	{
		if(this == CustomItem.NAVCOMPASS)
			return Lang.NAVCOMPASS.toString();
		else if(this == CustomItem.KITMAP)
			return Lang.KITMAP.toString();
		else if(this == CustomItem.VOTEMAP)
			return Lang.VOTEMAP.toString();
		else if(this == CustomItem.TEAMMAP)
			return Lang.TEAMMAP.toString();
		return this.name;
	}
	
	@Override
	public String toString()
	{
		return this.getName();
	}
}
