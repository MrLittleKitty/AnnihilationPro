package com.gmail.nuclearcat1337.anniPro.voting;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.gmail.nuclearcat1337.anniPro.anniEvents.AnniEvent;
import com.gmail.nuclearcat1337.anniPro.anniEvents.GameStartEvent;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniTeam;
import com.gmail.nuclearcat1337.anniPro.anniGame.Game;
import com.gmail.nuclearcat1337.anniPro.main.Lang;
import com.gmail.nuclearcat1337.anniPro.utils.Util;

public class ScoreboardAPI
{
	public static void registerListener(JavaPlugin p)
	{
		final BoardListeners l = new BoardListeners();
		Bukkit.getPluginManager().registerEvents(l,p);
//		AnniEvent.registerListener(l);
	}
	
	private static class BoardListeners implements Listener
	{
		public BoardListeners()
		{
			if(Game.isGameRunning())
				for(Player pl : Bukkit.getOnlinePlayers())
					ScoreboardAPI.setScoreboard(pl);
		}
		
		@EventHandler
		public void onGameStart(GameStartEvent event)
		{
			ScoreboardAPI.showGameBoard(ChatColor.BOLD+(ChatColor.GOLD+Lang.SCOREBOARDMAP.toString()+" "+(Game.getGameMap().getNiceWorldName())));
			for(Player pl : Bukkit.getOnlinePlayers())
				ScoreboardAPI.setScoreboard(pl);
		}
				
		@EventHandler(priority = EventPriority.MONITOR)
		public void playerCheck(PlayerJoinEvent event)
		{
			if(Game.isGameRunning())
				ScoreboardAPI.setScoreboard(event.getPlayer());
		}
	}
	
	private static final Scoreboard anniScoreboard;
	private static final Objective obj;
	static
	{
		anniScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		obj = anniScoreboard.registerNewObjective("CAT", "MEOW MEOW");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	public static Scoreboard getScoreboard()
	{
		return anniScoreboard;
	}
	
	private static void showGameBoard(String name)
	{
		obj.setDisplayName(name);
		for(AnniTeam team : AnniTeam.Teams)
		{
			Score score = obj.getScore(Util.shortenString(team.getExternalColoredName()+" Nexus", 16));
			score.setScore(team.getHealth());
		}
		Score score = obj.getScore(Util.shortenString(Lang.SCOREBOARDPHASE.toString(), 16));
		score.setScore(1);
	}
	
	public static void updatePhase()
	{
		if(Game.isGameRunning() && Game.getGameMap() != null)
		{
			Score score = obj.getScore(Util.shortenString(Lang.SCOREBOARDPHASE.toString(), 16));
			score.setScore(Game.getGameMap().getCurrentPhase());
		}
	}
	
	public static void setScore(AnniTeam team, int score)
	{
		if(obj != null)
			obj.getScore(Util.shortenString(team.getExternalColoredName()+" Nexus", 16)).setScore(score);	
	}
	
	public static void removeTeam(AnniTeam team)
	{
		anniScoreboard.resetScores(Util.shortenString(team.getExternalColoredName()+" Nexus", 16));
	}
	
	public static void setScoreboard(final Player player)
	{
		player.setScoreboard(anniScoreboard);
	}
	
//	public static void addPlayer(AnniTeam team, Player p)
//	{
//		anniScoreboard.getTeam(team.getName()).addPlayer(p);
//		//updateSigns(team);
//	}
//	
//	public static void removePlayer(AnniTeam team, Player p)
//	{
//		anniScoreboard.getTeam(team.getName()).removePlayer(p);
//		//updateSigns(team);
//	}
	
//	public static Integer[] teamCounts()
//	{
//		Integer[] x = new Integer[4];
//		int i = 0;
//		for(AnniTeam t : AnniTeam.Teams)
//		{
//			if(t.isTeamDead())
//				x[i] = 0;
//			else
//				x[i] = anniScoreboard.getTeam(t.getName()).getSize();
//			i++;
//		}
//		Arrays.sort(x);
//		return x;
////		Integer[] x = new Integer[4];
////		int i = 0;
////		for(Team t : anniScoreboard.getTeams())
////		{
////			x[i] = t.getSize();
////			i++;
////		}
////		Arrays.sort(x);
////		return x;
//	}
//	
//	public static Set<OfflinePlayer> getPlayers(AnniTeam t)
//	{
//		return anniScoreboard.getTeam(t.getName()).getPlayers();
//	}
//	
//	public static int getPlayerCount(AnniTeam team)
//	{
//		return anniScoreboard.getTeam(team.getName()).getSize();
//	}
}
