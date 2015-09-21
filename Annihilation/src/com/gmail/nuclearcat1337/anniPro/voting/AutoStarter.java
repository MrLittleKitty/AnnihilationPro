package com.gmail.nuclearcat1337.anniPro.voting;

import com.gmail.nuclearcat1337.anniPro.announcementBar.AnnounceBar;
import com.gmail.nuclearcat1337.anniPro.announcementBar.Announcement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import com.gmail.nuclearcat1337.anniPro.anniGame.Game;
import com.gmail.nuclearcat1337.anniPro.anniGame.GameVars;
import com.gmail.nuclearcat1337.anniPro.announcementBar.MessageBar;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;

public class AutoStarter implements Listener
{
	private final int players;
	private final int countdown;
	private boolean canRun;
	
	public AutoStarter(Plugin p, int playersToStart, int countdown)
	{
		Bukkit.getPluginManager().registerEvents(this, p);
		this.players = playersToStart;
		this.countdown = countdown;
		canRun = true;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerCheck(PlayerJoinEvent event)
	{
		check();
	}
	
	private void check()
	{
		if(!Game.isGameRunning() && canRun)
		{
			int count = Bukkit.getOnlinePlayers().size();
			
			if(count >= players)
			{
				canRun = false;
                Announcement ann = new Announcement(ChatColor.GREEN + "Starting in: {#}").setTime(countdown);
				if(GameVars.getVoting())
				{
                    ann.setCallback(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                String winner = VoteMapManager.getWinningMap();
                                Bukkit.broadcastMessage(ChatColor.GREEN + winner + " selected. Loading map.");
                                if (Game.loadGameMap(winner))
                                    Game.startGame();
                                else
                                {
                                    Bukkit.broadcastMessage(ChatColor.RED + "There has been an error in loading the map: " + winner);
                                    Bukkit.broadcastMessage(ChatColor.RED + "The game will not start.");
                                }
                            } catch (Exception e)
                            {
                                Bukkit.getLogger().warning("[ANNIHILATION] FATAL ERROR. VOTING IS ENABLED BUT THERE ARE NO MAPS IN THE WORLDS FOLDER!");
                                Bukkit.getPluginManager().disablePlugin(AnnihilationMain.getInstance());
                            }
                        }
                    });
				}
				else
				{
					ann.setCallback(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if (Game.loadGameMap(GameVars.getMap()))
                                Game.startGame();
                            else
                            {
                                Bukkit.broadcastMessage(ChatColor.RED + "There has been an error in loading the fixed map: " + GameVars.getMap().getName());
                                Bukkit.broadcastMessage(ChatColor.RED + "The game will not start.");
                            }
                        }
                    });
				}
                AnnounceBar.getInstance().countDown(ann);
			}
		}
	}
}
