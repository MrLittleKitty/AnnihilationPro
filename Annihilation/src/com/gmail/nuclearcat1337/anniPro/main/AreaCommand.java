package com.gmail.nuclearcat1337.anniPro.main;

import java.util.List;
import java.util.concurrent.Callable;

import com.gmail.nuclearcat1337.anniPro.kits.CustomItem;
import com.gmail.nuclearcat1337.anniPro.kits.KitUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.nuclearcat1337.anniPro.anniGame.Game;
import com.gmail.nuclearcat1337.anniPro.anniMap.Area;
import com.gmail.nuclearcat1337.anniPro.anniMap.Areas;
import com.gmail.nuclearcat1337.anniPro.utils.Loc;

public class AreaCommand implements CommandExecutor,Listener
{
	public AreaCommand(JavaPlugin plugin)
	{
		plugin.getCommand("Area").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this,plugin);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label ,String[] args)
	{
		if(sender instanceof Player)
		{
			final Player player = (Player)sender;
			if(player.hasPermission("A.Area"))
			{
				if(args.length > 0)
				{
					if(args[0].equalsIgnoreCase("create") && args.length > 1)
					{
						List<MetadataValue> corner1 = player.getMetadata("A.Loc1");
						List<MetadataValue> corner2 = player.getMetadata("A.Loc2");
						if(corner1 != null && corner2 != null && corner1.size() == 1 && corner2.size() == 1)
						{
							Loc one = (Loc)corner1.get(0).value();
							Loc two = (Loc)corner2.get(0).value();
							if(one != null && two != null)
							{
								player.removeMetadata("A.Loc1", AnnihilationMain.getInstance());
								player.removeMetadata("A.Loc2", AnnihilationMain.getInstance());
								Areas areas = null;
								if(one.getWorld().equals(two.getWorld()))
								{
									if(Game.LobbyMap != null && Game.LobbyMap.getWorldName().equalsIgnoreCase(one.getWorld()))
										areas = Game.LobbyMap.getAreas();
									else if(Game.getGameMap() != null && Game.getGameMap().getWorldName().equalsIgnoreCase(one.getWorld()))
										areas = Game.getGameMap().getAreas();
								}
								
								if(areas != null)
								{
                                    Area a = new Area(one, two, args[1]);
									boolean allowPVP = true;
                                    boolean allowDamage = true;
                                    boolean allowHuger = true;
									if(args.length > 2)
									{
										if(args[2].equalsIgnoreCase("allow"))
											allowPVP = true;
										else if(args[2].equalsIgnoreCase("disallow"))
											allowPVP = false;
                                        if(args.length > 3)
                                        {
                                            if(args[3].equalsIgnoreCase("allow"))
                                                allowDamage = true;
                                            else if(args[3].equalsIgnoreCase("disallow"))
                                                allowDamage = false;
                                            if(args.length > 4)
                                            {
                                                if(args[4].equalsIgnoreCase("allow"))
                                                    allowHuger = true;
                                                else if(args[4].equalsIgnoreCase("disallow"))
                                                    allowHuger = false;
                                            }
                                        }
									}
                                    a.setAllowPVP(allowPVP);
                                    a.setAllowDamage(allowDamage);
                                    a.setAllowHunger(allowHuger);
                                    areas.addArea(a);
									sender.sendMessage(ChatColor.LIGHT_PURPLE+"Area "+ChatColor.GOLD+a.getName()+ChatColor.LIGHT_PURPLE+" added");
                                    sender.sendMessage("Values: PVP = "+allowPVP+", Damage = "+allowDamage+", Hunger = "+allowHuger);
								}
								else sender.sendMessage(ChatColor.RED+"The worlds of the corners either don't match each other, dont match the loaded game world, or dont match the lobby world.");
							}
							else sender.sendMessage(ChatColor.RED+"You must have 2 corners set to create an area.");
						}
						else sender.sendMessage(ChatColor.RED+"You must have 2 corners set to create an area.");
					}
					else if(args[0].equalsIgnoreCase("delete") && args.length > 1)
					{
						handleDelete(player,args[1]);
					}
					else sender.sendMessage(ChatColor.LIGHT_PURPLE+"/Area "+ChatColor.GOLD+"[create,delete] [name] [allow-PvP?]");
				}
				else sender.sendMessage(ChatColor.LIGHT_PURPLE+"/Area "+ChatColor.GOLD+"[create,delete] [name] [allow-PvP?]");
			}
			else sender.sendMessage(ChatColor.RED+"You do not have permission to use this command.");
		}
		else sender.sendMessage("You must be a player to select areas.");
		return true;
	}
	
	private void handleDelete(Player player, String arg)
	{
		if(arg.equalsIgnoreCase("this"))
		{
			Areas areas = null;
			if(Game.LobbyMap != null && Game.LobbyMap.getWorldName().equalsIgnoreCase(player.getWorld().getName()))
				areas = Game.LobbyMap.getAreas();
			else if(Game.getGameMap() != null && Game.getGameMap().getWorldName().equalsIgnoreCase(player.getWorld().getName()))
				areas = Game.getGameMap().getAreas();
			if(areas != null)
			{
				Area a = areas.getArea(new Loc(player.getLocation(),false));
				if(a != null)
				{
					areas.removeArea(a.getName());
					player.sendMessage(ChatColor.LIGHT_PURPLE+"Area "+ChatColor.GOLD+a.getName()+ChatColor.LIGHT_PURPLE+" removed");
				}
				else player.sendMessage(ChatColor.RED+"Could not locate the area you wanted to delete.");
			}
			else player.sendMessage(ChatColor.RED+"Could not locate the area you wanted to delete.");
		}
		else
		{
			for(int x = 0; x < 2; x++)
			{
				Areas areas = null;
				if(x == 0 && Game.LobbyMap != null)
					areas = Game.LobbyMap.getAreas();
				else if(x == 1 && Game.getGameMap() != null)
					areas = Game.getGameMap().getAreas();
				if(areas != null)
				{
					//Area a = areas.getArea(arg);
					if(areas.hasArea(arg))
					{
						areas.removeArea(arg);
						player.sendMessage(ChatColor.LIGHT_PURPLE+"Area "+ChatColor.GOLD+arg+ChatColor.LIGHT_PURPLE+" removed");
						return;
					}
				}
				else player.sendMessage(ChatColor.RED+"Could not locate the area you wanted to delete.");
			}
			player.sendMessage(ChatColor.RED+"Could not locate the area you wanted to delete.");
		}
	}

    @EventHandler(priority = EventPriority.LOW,ignoreCancelled = true)
    public void playerCheck(PlayerInteractEvent event)
    {
        if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK)
        {
            final Player player = event.getPlayer();
            if(KitUtils.itemHasName(player.getItemInHand(), CustomItem.AREAWAND.getName()))
            {
                event.setCancelled(true);
                final Loc loc = new Loc(event.getClickedBlock().getLocation(),false);
                Callable<Object> b = new Callable<Object>(){
                    @Override
                    public Object call() throws Exception
                    {
                        return loc;
                    }};

                if(event.getAction() == Action.LEFT_CLICK_BLOCK)
                {
                    player.setMetadata("A.Loc1", new LazyMetadataValue(AnnihilationMain.getInstance(),b));
                    player.sendMessage(ChatColor.LIGHT_PURPLE+"Corner "+ChatColor.GOLD+"1 "+ChatColor.LIGHT_PURPLE+"set.");
                }
                else
                {
                    player.setMetadata("A.Loc2", new LazyMetadataValue(AnnihilationMain.getInstance(),b));
                    player.sendMessage(ChatColor.LIGHT_PURPLE+"Corner "+ChatColor.GOLD+"2 "+ChatColor.LIGHT_PURPLE+"set.");
                }
            }
        }
    }
}
