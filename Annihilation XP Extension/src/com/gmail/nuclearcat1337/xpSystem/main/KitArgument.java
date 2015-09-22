package com.gmail.nuclearcat1337.xpSystem.main;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem;
import com.gmail.nuclearcat1337.anniPro.kits.Kit;
import com.gmail.nuclearcat1337.anniPro.main.AnniArgument;
import com.gmail.nuclearcat1337.anniPro.utils.IDTools;
import com.google.common.base.Predicate;

public class KitArgument implements AnniArgument
{
	private XPSystem xpSystem;
	public KitArgument(XPSystem system)
	{
		this.xpSystem = system;
	}
	
	@Override
	public void executeCommand(final CommandSender sender, String label, final String[] args)
	{
		if(args != null && args.length > 2)
		{
			IDTools.getUUID(args[2], new Predicate<UUID>(){
				@Override
				public boolean apply(UUID id)
				{
					if(id != null)
					{
						Kit kit = Kit.getKit(args[1]);
						if(kit != null)
						{
							if(args[0].equalsIgnoreCase("add"))
							{
								sender.sendMessage(ChatColor.GREEN+"Kit added.");
								xpSystem.addKit(id, kit);
								sender.sendMessage("Added kit "+kit.getName());
								//addKit(kit.getName(), id);
							}
							else if(args[0].equalsIgnoreCase("remove"))
							{
								sender.sendMessage(ChatColor.RED+"Kit removed.");
								//removeKit(kit.getName(), id);
								xpSystem.removeKit(id, kit);
								sender.sendMessage("Removed kit "+kit.getName());
							}
							else 
								sender.sendMessage(ChatColor.RED+"Operation "+ChatColor.GOLD+args[0]+ChatColor.RED+" is not supported.");
						}
						else 
							sender.sendMessage(ChatColor.RED+"Could not locate the kit you specified.");
					}
					else 
						sender.sendMessage(ChatColor.RED+"Could not locate the player you specified.");
					return false;
				}});			
		}
	}

	@Override
	public String getArgumentName()
	{
		return "Kit";
	}

	@Override
	public String getHelp()
	{			
		return ChatColor.LIGHT_PURPLE+"Kit [add,remove] <kit> <player>--"+ChatColor.GREEN+"adds or removes a kit from a player.";
	}

	@Override
	public MenuItem getMenuItem()
	{
		return null;
	}

	@Override
	public String getPermission()
	{
		return null;
	}

	@Override
	public boolean useByPlayerOnly()
	{
		return false;
	}
}
