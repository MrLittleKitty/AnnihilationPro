package com.gmail.nuclearcat1337.xpSystem.main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandExecutor;

import com.gmail.nuclearcat1337.xpSystem.utils.Acceptor;

public class MyXPCommand implements CommandExecutor
{
	private final XPSystem xpSystem;
	private final String myXPMessage;
	
	public MyXPCommand(XPSystem system, String message)
	{
		this.xpSystem = system;	
		this.myXPMessage = message;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			final Player player = (Player)sender;
			if(xpSystem.isActive())
			{
				xpSystem.getXP(player.getUniqueId(), new Acceptor<Integer>(){
					@Override
					public void accept(Integer amount)
					{
						player.sendMessage(XPMain.formatString(myXPMessage,amount));
					}});
			}
			else sender.sendMessage(ChatColor.RED+"The XP system is not active.");
		}
		else sender.sendMessage("This command needs to be used by a player.");
		return true;
	}
}
