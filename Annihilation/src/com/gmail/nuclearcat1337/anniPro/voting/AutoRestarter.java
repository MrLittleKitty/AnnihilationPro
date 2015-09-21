package com.gmail.nuclearcat1337.anniPro.voting;

import com.gmail.nuclearcat1337.anniPro.announcementBar.AnnounceBar;
import com.gmail.nuclearcat1337.anniPro.announcementBar.Announcement;
import com.gmail.nuclearcat1337.anniPro.announcementBar.TempData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.gmail.nuclearcat1337.anniPro.anniGame.Game;
import com.gmail.nuclearcat1337.anniPro.anniGame.GameVars;
import com.gmail.nuclearcat1337.anniPro.announcementBar.MessageBar;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;

public class AutoRestarter implements Listener
{
	private final int players;
	private final int countdown;
	
	private boolean canRun;
	private boolean countingDown;
	
	private TempData data;
	
	public AutoRestarter(Plugin p, int playersToRestart, int countdown)
	{
		//Bukkit.getLogger().info("This is a thing");
		Bukkit.getPluginManager().registerEvents(this, p);
		if(playersToRestart < 0)
			playersToRestart = 0;
		if(countdown < 1)
			countdown = 1;
		this.players = playersToRestart;
		this.countdown = countdown;
		canRun = true;
		countingDown = false;
		data = null;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerCheck(PlayerQuitEvent event)
	{
		check();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void teleportCheck(PlayerKickEvent event)
	{
		check();
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerCheck(PlayerJoinEvent event)
	{
		check();
	}
	
	private void beginRestart()
	{
		canRun = false;
		countingDown = true;
		data = AnnounceBar.getInstance().getData();
		if(GameVars.getEndOfGameCommand().equals(""))
		{
			Bukkit.broadcastMessage("The auto restart feature has been activated, but no end of game command was specified.");
			Bukkit.broadcastMessage("Please have an admin set an end of game command if he wants this feature to work.");
		}
		else
		{
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED+"Auto-restart actvated! restarting the server in "+this.countdown+" seconds.");
            Announcement ann = new Announcement(ChatColor.DARK_PURPLE + "Auto-restart in: {#}");
            ann.setTime(this.countdown).setCallback(new Runnable()
            {
                @Override
                public void run()
                {
                    countingDown = false;
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), GameVars.getEndOfGameCommand());
                }
            });
            AnnounceBar.getInstance().countDown(ann);
		}
	}
	
	public void check()
	{
		Bukkit.getScheduler().runTaskLater(AnnihilationMain.getInstance(), new Runnable(){

			@Override
			public void run()
			{
				if(Game.isGameRunning() && Game.getGameMap().getCurrentPhase() > 2)
				{
					if(countingDown && !canRun)
					{
						int count = Bukkit.getOnlinePlayers().size();
						if(count > players)
						{
							//MessageBar.Reset();

                            AnnounceBar.getInstance().countDown(new Announcement(ChatColor.RED + "Auto-restart aborted!").setTime(2).setCallback(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    //MessageBar.countDown(data);
                                    AnnounceBar.getInstance().countDown(data);
                                    data = null;
                                }
                            }));
							canRun = true;
							countingDown = false;
						}
					}
					else if(canRun)
					{
						int count = Bukkit.getOnlinePlayers().size();
						
						if(count <= players)
							beginRestart();
					}
				}
			}}, 40);
	}
	
	
}
