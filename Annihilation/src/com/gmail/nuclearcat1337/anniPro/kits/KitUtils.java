package com.gmail.nuclearcat1337.anniPro.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class KitUtils
{	
	public static ItemStack[] getLeatherArmor()
	{
		ItemStack[] stacks = new ItemStack[]{ new ItemStack(Material.LEATHER_BOOTS),
				new ItemStack(Material.LEATHER_LEGGINGS), 
				new ItemStack(Material.LEATHER_CHESTPLATE),
				new ItemStack(Material.LEATHER_HELMET)};
		
		for(int x = 0; x < stacks.length; x++)
			stacks[x] = KitUtils.addSoulbound(stacks[x]);
		return stacks;
	}
	public static boolean isSoulbound(ItemStack stack)
	{
		ItemMeta meta = stack.getItemMeta();
		if(meta == null)
			return false;
		List<String> lore = meta.getLore();
		if(lore == null)
			return false;
		return lore.contains(ChatColor.GOLD+"Soulbound");
	}
	
	public static ItemStack addSoulbound(ItemStack stack)
	{
		if(stack == null)
			return stack;
		ItemMeta meta = stack.getItemMeta();
		if(meta == null)
			meta = Bukkit.getItemFactory().getItemMeta(stack.getType());
		List<String> lore = meta.getLore();
		if(lore == null)
			lore = new ArrayList<String>();
		lore.add(ChatColor.GOLD+"Soulbound");
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}
	
	public static ItemStack addEnchant(ItemStack s, Enchantment m, int level)
	{
		s.addUnsafeEnchantment(m, level);
		return s;
	}
	
	public static boolean itemHasName(ItemStack stack, String name)
	{
		if(stack == null)
			return false;
		ItemMeta meta = stack.getItemMeta();
		if(meta == null)
			return false;
		if(!meta.hasDisplayName())
			return false;
		return meta.getDisplayName().equalsIgnoreCase(name);
	}
	
//	public static ItemStack[] coloredArmor(AnniTeam team)
//	{
//		Color c;
//		if(team.getColor() == ChatColor.RED)
//			c = Color.RED;
//		else if(team.getColor() == ChatColor.BLUE)
//			c = Color.BLUE;
//		else if(team.getColor() == ChatColor.GREEN)
//			c = Color.GREEN;
//		else
//			c = Color.YELLOW;
//		for(ItemStack stack : armor)
//		{
//			LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
//			meta.setColor(c);
//			stack.setItemMeta(meta);
//		}
//		return armor;
//	}
//	
//	public static void giveTeamArmor(Player player)
//	{
//		final AnniPlayer pl = AnniPlayer.getPlayer(player.getUniqueId());
//		if(pl != null)
//		{
//			final AnniTeam t = pl.getTeam();
//			if(t != null)
//			{
//				player.getInventory().setArmorContents(coloredArmor(t));	
//			}
//		}
//	}
	
	public static ItemStack setName(ItemStack itemStack, String name)
	{
		ItemMeta meta = itemStack.getItemMeta();
		if(meta == null)
			meta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
		meta.setDisplayName(name);
		itemStack.setItemMeta(meta);
		return itemStack;
	}
	
	public static ItemStack setLore(ItemStack itemStack, List<String> lore)
	{
		ItemMeta meta = itemStack.getItemMeta();
		if(meta == null)
			meta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());
		meta.setLore(lore);
		itemStack.setItemMeta(meta);
		return itemStack;
	}
	
	public static ItemStack setNameLore(ItemStack itemStack, String name, List<String> lore)
	{
		return setLore(setName(itemStack,name),lore);
	}
}
