package com.gmail.nuclearcat1337.xpSystem.main;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem;
import com.gmail.nuclearcat1337.anniPro.main.AnniArgument;
import com.gmail.nuclearcat1337.anniPro.utils.IDTools;
import com.google.common.base.Predicate;

public class XPArgument implements AnniArgument
{
	private XPSystem xpSystem;
	public XPArgument(XPSystem system)
	{
		this.xpSystem = system;
	}
	
	@Override
	public void executeCommand(final CommandSender sender, String label, final String[] args)
	{
		if(args != null && args.length > 2)
		{
			try
			{
				if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("subtract"))
				{
					final int XP = Integer.parseInt(args[1]);
					IDTools.getUUID(args[2], new Predicate<UUID>(){
						@Override
						public boolean apply(UUID id)
						{
							if(id != null)
							{
								if(args[0].equalsIgnoreCase("add"))
								{
									xpSystem.giveXP(id, XP);
									sender.sendMessage("Gave "+XP+" XP");
								}
								else
								{
									xpSystem.removeXP(id, XP);
									sender.sendMessage("Removed "+XP+" XP");
								}
							}
							else 
								sender.sendMessage(ChatColor.RED+"Could not locate the player you specified.");
							return false;
						}});	
				}
				else sender.sendMessage("You did not specify a valid XP operation.");
			}
			catch(NumberFormatException e)
			{
				sender.sendMessage("The XP values specified was not a number");
			}
		}
	}

	@Override
	public String getArgumentName()
	{
		return "XP";
	}

	@Override
	public String getHelp()
	{			
		return ChatColor.LIGHT_PURPLE+"XP [add,substract] <amount> <player>--"+ChatColor.GREEN+"adds or subtracts XP from a from player.";
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


