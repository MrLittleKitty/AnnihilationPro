package com.gmail.nuclearcat1337.xpSystem.main;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.gmail.nuclearcat1337.anniPro.anniEvents.GameEndEvent;
import com.gmail.nuclearcat1337.anniPro.anniEvents.NexusHitEvent;
import com.gmail.nuclearcat1337.anniPro.anniEvents.PlayerKilledEvent;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniTeam;
import com.gmail.nuclearcat1337.anniPro.anniGame.Game;

public class XPListeners implements Listener
{
	private final XPSystem xpSystem;
	private final String xpMessage;
	private final int killXP;
	private final int nexusHitXP;
	private final int[] teamXPs;
	
	public XPListeners(XPSystem system, String message,int killXP,int nexusXP,int[] teamXPs)
	{
		this.xpSystem = system;
		this.xpMessage = message;
		this.killXP = killXP;
		this.nexusHitXP = nexusXP;
		this.teamXPs = teamXPs;
	}
	
	private void sendXPMessage(AnniPlayer player, int XP)
	{
		player.sendMessage(XPMain.formatString(xpMessage, XP));
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void gameEnd(GameEndEvent e)
	{
		if(e.getWinningTeam() != null && xpSystem.isActive())
		{
			for(AnniPlayer p : e.getWinningTeam().getPlayers())
			{
				if(p.getPlayer() != null)
				{
					int amount = XPMain.checkMultipliers(p.getPlayer(),teamXPs[0]);
					xpSystem.giveXP(p.getID(),amount);
					sendXPMessage(p,amount);
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void xpNexusHit(NexusHitEvent e)
	{
		if(xpSystem.isActive() && Game.isGameRunning() && !e.isCancelled())
		{
			Player player = e.getPlayer().getPlayer();
			assert player != null;
			
			for(AnniPlayer pl : e.getPlayer().getTeam().getPlayers())
			{
				int amount = XPMain.checkMultipliers(player,e.getDamage()*nexusHitXP);
				xpSystem.giveXP(pl.getID(),amount);
				sendXPMessage(pl,amount);
			}
			
			
			if(e.willKillTeam())
			{
				AnniTeam t = e.getHitNexus().Team;
				int alive = 0;
				for(AnniTeam team : AnniTeam.Teams)
				{
					if(!team.isTeamDead() && !team.equals(t))
						alive++;
				}
				for(AnniPlayer p : t.getPlayers())
				{
					int amount = XPMain.checkMultipliers(player,teamXPs[alive]);
					xpSystem.giveXP(p.getID(),amount);
					sendXPMessage(p,amount);
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void xpPlayerKill(PlayerKilledEvent e)
	{
		if(xpSystem.isActive() && Game.isGameRunning())
		{
			int amount = XPMain.checkMultipliers(e.getKiller().getPlayer(),killXP);
			xpSystem.giveXP(e.getKiller().getID(),amount);
			sendXPMessage(e.getKiller(),amount);
		}
	}
}
