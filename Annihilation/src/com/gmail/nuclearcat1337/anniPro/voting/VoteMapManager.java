package com.gmail.nuclearcat1337.anniPro.voting;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.gmail.nuclearcat1337.anniPro.anniEvents.AnniEvent;
import com.gmail.nuclearcat1337.anniPro.anniEvents.GameStartEvent;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.anniGame.Game;
import com.gmail.nuclearcat1337.anniPro.anniGame.GameVars;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ActionMenuItem;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemClickEvent;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemClickHandler;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemMenu;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemMenu.Size;
import com.gmail.nuclearcat1337.anniPro.kits.CustomItem;
import com.gmail.nuclearcat1337.anniPro.kits.KitUtils;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;

public class VoteMapManager
{
	private static Map<String,String> voteMap;
	private static String[] maps = null;
	private static Scoreboard board;
	private static Objective obj;
	private static ItemMenu menu;
	//private static boolean active;
	
	public static void registerListener(JavaPlugin plugin)
	{
		voteMap = new HashMap<String,String>();
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		obj = board.registerNewObjective("Voting", "CAT CERASET");
		//active = false;
		
		Loader l = new Loader();
		plugin.getCommand("Vote").setExecutor(l);
		Bukkit.getPluginManager().registerEvents(l, plugin);
		//AnniEvent.registerListener(l);
	}
	
	private static class Loader implements CommandExecutor, Listener
	{	
		@EventHandler
		public void clearPlayers(GameStartEvent event)
		{
			voteMap.clear();
		}
		
		@EventHandler(priority = EventPriority.MONITOR)
		public void playerCheck(PlayerJoinEvent event)
		{
			final AnniPlayer p = AnniPlayer.getPlayer(event.getPlayer().getUniqueId());
			if(p != null && !Game.isGameRunning())
				event.getPlayer().setScoreboard(board);
		}
		
		@EventHandler(priority=EventPriority.HIGH)
		public void voteGUIcheck(PlayerInteractEvent event)
		{
			if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)
			{
				final Player player = event.getPlayer();
				if(KitUtils.itemHasName(player.getItemInHand(), CustomItem.VOTEMAP.getName()))
				{
					if(menu != null)
						menu.open(player);
					else player.sendMessage(ChatColor.RED+"There are no maps for voting!");
					event.setCancelled(true);
				}
			}
		}
		
//		private void checkLeave()
//		{
//			//Check if a person leaves, to remove their vote
//		}
//		
//		@EventHandler(priority = EventPriority.MONITOR)
//		public void playerCheck(PlayerQuitEvent event)
//		{
//			
//		}
//		
//		@EventHandler(priority = EventPriority.MONITOR)
//		public void playerCheck(PlayerKickEvent event)
//		{
//			
//		}

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
		{
			if(!Game.isGameRunning())
			{
//				final AnniPlayer p = AnniPlayer.getPlayer(((Player)sender).getUniqueId());
//				if(p != null)
//				{
				if(sender instanceof Player)
				{
					if(maps != null)
					{
						if(args.length > 0)
						{
							String str = args[0];
							String match = getMatch(str);
							if(match != null)
							{
								String old = voteForMap(sender.getName(),match);
								if(old == null)
									sender.sendMessage(ChatColor.DARK_PURPLE+"You voted for "+match);
								else
									sender.sendMessage(ChatColor.DARK_PURPLE+"You changed your vote from "+ChatColor.GOLD+old+ChatColor.DARK_PURPLE+" to "+ChatColor.GOLD+match);
							}
							else sender.sendMessage(ChatColor.GOLD+str+ChatColor.RED+" is not a valid map.");	
						}
					}
					else sender.sendMessage(ChatColor.RED+"There are no maps for voting!");
				}
//				}
//				else sender.sendMessage(ChatColor.RED+"You must be in an Annihilation game to vote!");
			}
			return true;
		}
	}
	
	public static void beginVoting()
	{
		obj.setDisplayName(ChatColor.GREEN+"/vote [map name] to vote");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		if(maps != null)
		{
			for(String str : maps)
			{
				board.resetScores(str);
			}
		}
		
		maps = getWorlds();
		if(maps != null)
		{
			for(String str : maps)
			{
				Score score = obj.getScore(str);
				score.setScore(0);
			}
		}
		
		if(maps != null)
		{
			menu = new ItemMenu("Vote for a Map",Size.fit(maps.length));
			int x = 0;
			for(final String str : maps)
			{
				ActionMenuItem item = new ActionMenuItem(ChatColor.GOLD+str, new ItemClickHandler(){
					@Override
					public void onItemClick(ItemClickEvent event)
					{
						event.getPlayer().performCommand("Vote "+str);
						event.setWillClose(true);
					}}, new ItemStack(Material.TRIPWIRE_HOOK), new String[]{});
				menu.setItem(x, item);
				x++;
			}
		}
		
		if(!Game.isGameRunning())
		{
//			for(AnniPlayer p : AnniPlayer.getPlayers())
//			{
//				final Player pl = p.getPlayer();
//				if(pl != null)
//					pl.setScoreboard(board);
//			}
			for(Player pl : Bukkit.getOnlinePlayers())
				pl.setScoreboard(board);
		}
	}

	public static String getWinningMap()
	{
		assert maps != null;
		
		int score = 0;
		String winner = null;
		for(String entry : maps)
		{
			Score s = obj.getScore(entry);
			if(s.getScore() >= score)
			{
				score = s.getScore();
				winner = entry;
			}
		}
		return winner;
	}
	
	private static String[] getWorlds()
	{
		File maps = new File(AnnihilationMain.getInstance().getDataFolder().getAbsolutePath(), "Worlds");
		if(!maps.exists())
			maps.mkdirs();
		
		File[] files = maps.listFiles(new FilenameFilter()
		{
			public boolean accept(File file, String name)
			{
				return file.isDirectory();
			}
		});
		
		if ((files != null) && (files.length > 0))
		{
			if(files.length <= GameVars.getMaxMapsForVoting())
			{
				String[] str = new String[files.length];
				for(int x = 0; x < str.length; x++)
					str[x] = files[x].getName();
				return str;
			}
			else
			{
				ArrayList<String> list = new ArrayList<String>();
				for(File f : files)
					list.add(f.getName());
				String[] str = new String[GameVars.getMaxMapsForVoting()];
				Random rand = new Random(System.currentTimeMillis());
				for(int x =0; x < GameVars.getMaxMapsForVoting(); x++)
					str[x] = list.remove(rand.nextInt(list.size()));
				return str;
			}
			
		}
		return null;
	}
	
	private static String getMatch(String str)
	{
		for(String s : maps)
		{
			if(s.equalsIgnoreCase(str))
				return s;
		}
		return null;
	}
	
	private static String voteForMap(String player,String map)
	{
		String old = voteMap.put(player, map);
		if(old != null)
		{
			Score s = obj.getScore(old);
			s.setScore(s.getScore()-1);
		}
		Score s = obj.getScore(map);
		s.setScore(s.getScore()+1);
		return old;
	}

}
