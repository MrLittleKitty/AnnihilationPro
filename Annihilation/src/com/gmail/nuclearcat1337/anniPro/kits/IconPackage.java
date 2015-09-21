package com.gmail.nuclearcat1337.anniPro.kits;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

public class IconPackage
{
	private final ItemStack stack;
	private String name = null;
	private String[] lore = null;
	
	public IconPackage(ItemStack stack)
	{
		this.stack = stack;
	}
	
	public IconPackage(ItemStack stack, String name)
	{
		this(stack);
		this.name = name;
	}
	
	public IconPackage(ItemStack stack, String[] lore)
	{
		this(stack);
		this.lore = lore;
	}
	
	public IconPackage(ItemStack stack, String name, String[] lore)
	{
		this(stack);
		this.name = name;
		this.lore = lore;
	}
	
	
	public ItemStack getFinalIcon()
	{
		ItemStack s = stack.clone();
		if(name != null)
			KitUtils.setName(s, name);
		if(lore != null)
		{
			ArrayList<String> str = new ArrayList<String>();
			for(String x : lore)
				str.add(x);
			KitUtils.setLore(s,str);
		}
		return s;
	}
	
	public ItemStack getIcon()
	{
		return this.stack;
	}
	
	public String[] getLore()
	{
		return lore;
	}
	
	public String getName()
	{
		return name;
	}
}
