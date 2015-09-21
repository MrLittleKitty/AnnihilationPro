package com.gmail.nuclearcat1337.anniPro.mapBuilder;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniTeam;
import com.gmail.nuclearcat1337.anniPro.kits.KitUtils;

public class TeamBlock
{
	public static final TeamBlock Red = new TeamBlock(AnniTeam.Red);
	public static final TeamBlock Blue = new TeamBlock(AnniTeam.Blue);
	public static final TeamBlock Green = new TeamBlock(AnniTeam.Green);
	public static final TeamBlock Yellow = new TeamBlock(AnniTeam.Yellow);
	
	public static TeamBlock getByTeam(AnniTeam team)
	{
		if(team.getName().equalsIgnoreCase("red"))
			return Red;
		else if(team.getName().equalsIgnoreCase("blue"))
			return Blue;
		else if(team.getName().equalsIgnoreCase("green"))
			return Green;
		else return Yellow;
	}
	
	interface TeamBlockHandler
	{
		void onBlockClick(final Player player, final AnniTeam team, final Action action, final Block block, final BlockFace face);
	}
	
	public final AnniTeam Team;
	private final List<String> lore;
	private final byte datavalue;
	
	private TeamBlock(final AnniTeam team)
	{
		this.Team = team;
		this.lore = new ArrayList<String>();
		if(team.equals(AnniTeam.Red))
			datavalue = (byte)14;
		else if(team.equals(AnniTeam.Blue))
			datavalue = (byte)11;
		else if(team.equals(AnniTeam.Green))
			datavalue = (byte)13;
		else
			datavalue = (byte)4;
	}
	
	public TeamBlock addLine(Action action, ChatColor color1, String message)
	{
		String str = "";
		if(action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR)
			str = color1+"Left click to ";
		else if(action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR)
			str = color1+"Right click to ";
		str += message;
		lore.add(str);
		return this;
	}
	
	public void clearLines()
	{
		this.lore.clear();
	}
	
	public String getName()
	{
		return Team.getColor()+Team.getName()+" Team";
	}
	
	@Override
	public String toString()
	{
		return this.getName();
	}
	
	private ItemStack toItemStack() 
	{
		@SuppressWarnings("deprecation")
		ItemStack stack = new ItemStack(Material.WOOL,1,(short)0,datavalue);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(getName());
		if (lore != null) 
			meta.setLore(lore);
		stack.setItemMeta(meta);
		return KitUtils.addSoulbound(stack);
	}
	
	public void giveToPlayer(final Player player)
	{
		ItemStack[] inv = player.getInventory().getContents();
		for(int x = 0; x < inv.length; x++)
		{
			if(inv[x] != null && inv[x].getType() == Material.WOOL)
			{
				if(KitUtils.itemHasName(inv[x], this.getName()))
					player.getInventory().clear(x);
			}
		}
		player.getInventory().addItem(this.toItemStack());
	}
}
