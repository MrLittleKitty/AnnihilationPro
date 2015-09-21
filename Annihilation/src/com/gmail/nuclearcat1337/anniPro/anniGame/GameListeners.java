package com.gmail.nuclearcat1337.anniPro.anniGame;


import com.gmail.nuclearcat1337.anniPro.anniGame.autoRespawn.RespawnHandler;
import com.gmail.nuclearcat1337.anniPro.utils.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.nuclearcat1337.anniPro.anniEvents.AnniEvent;
import com.gmail.nuclearcat1337.anniPro.anniEvents.PlayerKilledEvent;
import com.gmail.nuclearcat1337.anniPro.anniEvents.PlayerKilledEvent.KillAttribute;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.anniPro.main.Lang;
import com.gmail.nuclearcat1337.anniPro.utils.Loc;

public class GameListeners implements Listener
{
	public GameListeners(Plugin p)
	{
        Bukkit.getPluginManager().registerEvents(this, p);
        RespawnHandler.register(p);

		String version = VersionUtils.getVersion();
		if(version.equals("v1_8_R1") || version.equals("v1_8_R2"))
			new ArmorStandListener(p);
	}

	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void pingServer(ServerListPingEvent event)
	{
		if(GameVars.useMOTD())
		{
			if(Game.getGameMap() == null || Game.getGameMap().getCurrentPhase() < 1)
				event.setMotd("In Lobby");
			else event.setMotd("Phase "+Game.getGameMap().getCurrentPhase());
		}
	}
	
	//should make players instantly respawn
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void deathHandler(PlayerDeathEvent event)
	{
		final Player player = event.getEntity();
		final AnniPlayer p = AnniPlayer.getPlayer(player.getUniqueId());
		if(p != null)
			p.getKit().cleanup(player);
	}
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void AnniPlayersInit(PlayerCommandPreprocessEvent event)
	{
		final String[] args = event.getMessage().split(" ");
		if(args[0].equalsIgnoreCase("/tp"))
		{
			Player player = event.getPlayer();
			if(player.hasPermission("A.anni"))
			{
				if(args.length > 1)
				{
					AnniTeam team = AnniTeam.getTeamByName(args[1]);
					if(team != null)
					{
						Loc loc = team.getSpectatorLocation();
						if(loc != null)
						{
							event.setCancelled(true);
							player.teleport(loc.toLocation());
						}
					}
					else if(args[1].equalsIgnoreCase("lobby"))
					{
						if(Game.LobbyMap != null)
						{
							Location lobby = Game.LobbyMap.getSpawn();
							if(lobby != null)
							{
								event.setCancelled(true);
								player.teleport(lobby);
							}
						}
					}
//					else if(args[1].equalsIgnoreCase("map") && player.getName().equals("Mr_Little_Kitty"))
//					{
//						if(args.length > 2)
//						{
//							World w = Game.getWorld(args[2]);
//							if(w != null)
//							{
//								event.setCancelled(true);
//								player.teleport(w.getSpawnLocation());
//							}
//						}
//						else
//						{
//							for(World w : Bukkit.getWorlds())
//								player.sendMessage(w.getName());
//						}
//					}
				}
			}
		}
	}
	
	private final ChatColor g = ChatColor.GRAY;
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void AnniPlayersInit(AsyncPlayerChatEvent event)
	{
		if(event.isAsynchronous())
		{
			AnniPlayer p = AnniPlayer.getPlayer(event.getPlayer().getUniqueId());
			if(p.getTeam() == null)
				event.setFormat(g+"(All) ["+ChatColor.DARK_PURPLE+"Lobby"+g+"] %s"+ChatColor.WHITE+": %s");
			else if(event.getMessage().startsWith("!"))
			{
				event.setMessage(event.getMessage().substring(1));
				event.setFormat(g+"(All) ["+p.getTeam().getColor()+p.getTeam().toString()+g+"] %s"+ChatColor.WHITE+": %s");
			}
			else
			{
				event.setFormat(g+"(Team) ["+p.getTeam().getColor()+p.getTeam().toString()+g+"] %s"+ChatColor.WHITE+": %s");
				event.getRecipients().clear();
				for(AnniPlayer pl : p.getTeam().getPlayers())
					if(pl.isOnline())
						event.getRecipients().add(pl.getPlayer());
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void DeathListener(PlayerDeathEvent event)
	{
		String message = "";
		Player player = event.getEntity();
		Player killer = player.getKiller();
		
		if(Game.isGameRunning())
		{
			AnniPlayer p = AnniPlayer.getPlayer(player.getUniqueId());
			if(p != null)
			{
				if(killer != null)
				{
					AnniPlayer k = AnniPlayer.getPlayer(killer.getUniqueId());
					if(k != null)
					{
						PlayerKilledEvent e = new PlayerKilledEvent(k,p);//TODO--------This should be created earlier, then the message should be based off of attributes computed by the event
						
						message = p.getTeam().getColor()+player.getName()+"("+p.getKit().getName()+") "+Lang.DEATHPHRASE.toString()+" "+k.getTeam().getColor()+killer.getName()+"("+k.getKit().getName()+")";
						
						if(e.getAttributes().contains(KillAttribute.REMEMBRANCE))
							message += " "+Lang.REMEMBRANCE.toString();
						else if(e.getAttributes().contains(KillAttribute.NEXUSDEFENSE))
							message += " "+Lang.NEXUSKILL.toString();
							
						AnniEvent.callEvent(e);
						if(!e.shouldDropXP())
							event.setDroppedExp(0);
					}	
				}
				else 
					event.setDroppedExp(0);
				event.setDeathMessage(message);
			}
		}
	}
	
	@EventHandler(priority=EventPriority.MONITOR,ignoreCancelled = true)
	public void teleportToLobbyThing(PlayerJoinEvent event)
	{
		if(Game.LobbyMap != null && Game.LobbyMap.getSpawn() != null)
		{
			final Player pl = event.getPlayer();
			final AnniPlayer p = AnniPlayer.getPlayer(pl.getUniqueId());
			if(p != null )//&& Game.GameWorld != null)
			{
				if(!Game.isGameRunning() || p.getTeam() == null || p.getTeam().isTeamDead() || pl.getLocation().getWorld().getName().equalsIgnoreCase(Game.LobbyMap.getWorldName()))
				{
					new BukkitRunnable()
					{
						@Override
						public void run()
						{
							//Check if the lobbymap is not null when this actually runs
							if(Game.LobbyMap != null)
								Game.LobbyMap.sendToSpawn(pl);
							
//							pl.getInventory().clear();
//							pl.getInventory().setArmorContents(null);
//							pl.teleport(Game.LobbyLocation);
//							pl.getInventory().addItem(CustomItem.KITMAP.toItemStack(1));
//							pl.setHealth(pl.getMaxHealth());
//							pl.setFoodLevel(20);
							//pl.setGameMode(GameMode.ADVENTURE);
						}
					}.runTaskLater(AnnihilationMain.getInstance(),20L);
				}
			}
		}
	}
	
	//should set the respawn point of a player
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled = true)
	public void respawnHandler(PlayerRespawnEvent event)
	{
		final Player player = event.getPlayer();
		final AnniPlayer p = AnniPlayer.getPlayer(player.getUniqueId());
		if(p != null)
		{
			if(Game.isGameRunning())
			{
				if(p.getTeam() != null && !p.getTeam().isTeamDead())
				{
					event.setRespawnLocation(p.getTeam().getRandomSpawn());
					p.getKit().onPlayerSpawn(player);
					return;
				}
			}
			if(Game.LobbyMap != null && Game.LobbyMap.getSpawn() != null)
				event.setRespawnLocation(Game.LobbyMap.getSpawn());  //Set people to respawn in the lobby
		}
	}
}
