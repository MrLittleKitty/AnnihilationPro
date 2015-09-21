package com.gmail.nuclearcat1337.anniPro.anniGame;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.bobacadodl.imgmessage.ImageChar;
import com.bobacadodl.imgmessage.ImageMessage;
import com.gmail.nuclearcat1337.anniPro.anniEvents.AnniEvent;
import com.gmail.nuclearcat1337.anniPro.anniEvents.GameEndEvent;
import com.gmail.nuclearcat1337.anniPro.anniEvents.NexusHitEvent;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.anniPro.main.Lang;
import com.gmail.nuclearcat1337.anniPro.utils.Loc;
import com.gmail.nuclearcat1337.anniPro.voting.ScoreboardAPI;

public class Nexus implements Listener
{
	public Nexus(AnniTeam team)
	{
		this.Team = team;
		this.Location = null;
	}
	
	public final AnniTeam Team;
	//private ImageMessage message;
	private Loc Location;
	
	public void setLocation(Loc loc)
	{
		this.Location = loc;
	}
	
	public Loc getLocation()
	{
		return Location;
	}
	
	private void gameOverCheck()
	{
		int total = AnniTeam.Teams.length;
		int destroyed = 0;
		AnniTeam winner = null;
		for(AnniTeam team : AnniTeam.Teams)
		{
			if(team.isTeamDead())
				destroyed++;
			else winner = team;
		}
		
		if(destroyed == total-1)
		{
			AnniEvent.callEvent(new GameEndEvent(winner));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	private void NexusCheck(BlockBreakEvent event)
	{
		if(event.getPlayer().getGameMode() != GameMode.CREATIVE && Game.isGameRunning() && Game.getGameMap() != null)
		{
			if(Location != null && !Team.isTeamDead())
			{
				Location loc = event.getBlock().getLocation();
				if(this.Location.equals(loc))
				{
					event.setCancelled(true);
					if(Game.getGameMap().canDamageNexus())
					{
						AnniPlayer p = AnniPlayer.getPlayer(event.getPlayer().getUniqueId());
						if(p != null && p.getTeam() != null && !p.getTeam().equals(Team))	
						{
							NexusHitEvent e = new NexusHitEvent(p,this,1*Game.getGameMap().getDamageMultiplier());
							AnniEvent.callEvent(e);
							if(!e.isCancelled() && e.getDamage() > 0)
							{
								loc.getWorld().playSound(loc, Sound.ANVIL_LAND, 1F, (float)Math.random());
								Team.setHealth(Team.getHealth()-(e.getDamage()));
								
								for(AnniPlayer player : Team.getPlayers())
								{
									Player pl = player.getPlayer();
									if(pl != null)
										pl.playSound(pl.getLocation(), Sound.NOTE_PIANO, 1f, 2.1f);
								}
								
								for(AnniPlayer pl : p.getTeam().getPlayers())
								{
									pl.sendMessage(p.getTeam().getColor()+p.getName()+ChatColor.GRAY+" has damaged "+this.Team.getColor()+this.Team.getName()+" nexus");
								}
								
								if(Team.isTeamDead())
								{
									ScoreboardAPI.removeTeam(Team);
//									for(AnniPlayer player : AnniPlayer.getPlayers().values())
//									{
//										Player pl = player.getPlayer();
//										if(pl != null)
//
//											pl.getWorld().playSound(pl.getLocation(), Sound.EXPLODE, 1F, .8F);
//									}
									World w = loc.getWorld();
									w.getBlockAt(loc).setType(Material.BEDROCK);
									try
									{
										BufferedImage image = ImageIO.read(AnnihilationMain.getInstance().getResource("Images/"+Team.getName()+"Team.png"));
										String[] lore = new String[]
										{
											"",
											"",
											"",
											"",
											Lang.TEAMDESTROYED.toStringReplacement(Team.getExternalColoredName()),
										};
										ImageMessage message =  new ImageMessage(image, 10, ImageChar.MEDIUM_SHADE.getChar()).appendText(lore);
										for(Player pl : Bukkit.getOnlinePlayers())
										{
											pl.getWorld().playSound(pl.getLocation(), Sound.EXPLODE, 1F, .8F);
											message.sendToPlayer(pl);
										}
									}
									catch(Throwable t)
									{
										
									}
									gameOverCheck();									
								}
							}
						}
					}
				}
			}
		}
	}
}
