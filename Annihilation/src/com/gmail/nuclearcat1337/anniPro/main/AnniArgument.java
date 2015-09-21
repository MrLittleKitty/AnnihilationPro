package com.gmail.nuclearcat1337.anniPro.main;

import org.bukkit.command.CommandSender;

import com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem;

public interface AnniArgument
{
	String getHelp();
	boolean useByPlayerOnly();
	String getArgumentName();
	void executeCommand(CommandSender sender, String label, String[] args);
	String getPermission();
	MenuItem getMenuItem();
}
