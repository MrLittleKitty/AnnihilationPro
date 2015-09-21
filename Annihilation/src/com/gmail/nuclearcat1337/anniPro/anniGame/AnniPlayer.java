package com.gmail.nuclearcat1337.anniPro.anniGame;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import com.gmail.nuclearcat1337.anniPro.anniEvents.GameStartEvent;
import com.gmail.nuclearcat1337.anniPro.enderFurnace.api.EnderFurnace;
import com.gmail.nuclearcat1337.anniPro.enderFurnace.api.IFurnace;
import com.gmail.nuclearcat1337.anniPro.kits.Kit;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;

public final class AnniPlayer
{
	private static final Map<UUID,AnniPlayer> players = new ConcurrentHashMap<UUID,AnniPlayer>();
		
	public static AnniPlayer getPlayer(UUID id)
	{
		return players.get(id);
	}
	
	public static Collection<AnniPlayer> getPlayers()
	{
		return players.values();
	}
	
	public static void RegisterListener(final Plugin p)
	{
		players.clear();
		PlayerLoader l = new PlayerLoader();
		Bukkit.getPluginManager().registerEvents(l, p);
		//AnniEvent.registerListener(l);
	}
	
	private static class PlayerLoader implements Listener
	{
		public PlayerLoader()
		{
			//If any players are online when this is called (hint: /reload), we load their anniPlayer
			for(Player p : Bukkit.getOnlinePlayers())
			{
				loadPlayer(p);
			}
		}
		
		@EventHandler(priority = EventPriority.LOWEST)
		public void playerCheck(PlayerJoinEvent event)
		{
			final Player p = event.getPlayer();
			if(!exists(p))
				loadPlayer(p);
		}
		
//		@EventHandler(priority = EventPriority.LOWEST)
//		public void playerCheck(PlayerQuitEvent event)
//		{
//			removePlayerCheck(event.getPlayer());
//		}
//		
//		@EventHandler(priority = EventPriority.LOWEST)
//		public void playerCheck(PlayerKickEvent event)
//		{
//			removePlayerCheck(event.getPlayer());
//		}
//		
//		private void removePlayerCheck(Player player)
//		{
//			if(!Game.isGameRunning())
//			{
//				AnniPlayer p = getPlayer(player.getUniqueId());
//				if(p != null && p.getTeam() != null)
//					p.getTeam().leaveTeam(p);
//				players.remove(player.getUniqueId());
//			}
//		}
		
		@EventHandler(priority = EventPriority.MONITOR)
		public void enderFuraceManagement(PlayerQuitEvent event)
		{
			checkLeave(event.getPlayer());
		}

		@EventHandler(priority = EventPriority.MONITOR)
		public void enderFuraceManagement(PlayerKickEvent event)
		{
			checkLeave(event.getPlayer());
		}
		
		private void checkLeave(Player player)
		{
			if(Game.isGameRunning())
			{
				AnniPlayer p = getPlayer(player.getUniqueId());
				if(p != null && p.enderfurnace != null)
				{
					p.setData("ED", p.enderfurnace.getFurnaceData());
					p.enderfurnace = null;
				}
//				if(GameVars.getUseAntiLog())
//					player.setHealth(0);
			}
		}
		
		@EventHandler(priority = EventPriority.MONITOR)
		public void enderFuraceManagement(PlayerJoinEvent event)
		{
			if(Game.isGameRunning())
			{
				AnniPlayer p = getPlayer(event.getPlayer().getUniqueId());
				if(p != null && p.enderfurnace == null)
					p.enderfurnace = EnderFurnace.getCreator().createFurnace(p);
			}
		}
		
		private boolean exists(final Player p)
		{
			return players.containsKey(p.getUniqueId());
		}
		
		private void loadPlayer(final Player p)
		{
			final AnniPlayer player = new AnniPlayer(p.getUniqueId(),p.getName());
			players.put(p.getUniqueId(), player);
		}
		
		@EventHandler(priority=EventPriority.LOWEST)
		public void onGameStart(GameStartEvent event)
		{
			Iterator<AnniPlayer> ps = players.values().iterator();
			while(ps.hasNext())
			{
				AnniPlayer player = ps.next();
//				if(!player.isOnline())
//				{
//					//This is where we should clear players who aren't online or stuff
//				}
                if(player.isOnline())
				    player.enderfurnace = EnderFurnace.getCreator().createFurnace(player);
                else player.enderfurnace = null;
			}
			new FurnaceTick().runTaskTimer(AnnihilationMain.getInstance(), 3L, 3L);
		}	
	}
	
	private static class FurnaceTick extends BukkitRunnable
	{
		  public void run()
		  {
			  if(Game.isGameRunning())
			  { 
				  for(AnniPlayer player : AnniPlayer.getPlayers())
				  {
					  IFurnace f = player.enderfurnace;
					  if(f != null)
						  f.tick();
				  }
			  }
			  else this.cancel();
		  }
	}
	
	private final UUID id;
	private final String name;
	
	private Map<Object,Object> data;
	private AnniTeam team;
	private Kit kit;
	private IFurnace enderfurnace;
	
	private AnniPlayer(UUID ID, String Name)
	{
		this.id = ID;
		this.name = Name;
		team = null;
		kit = Kit.CivilianInstance;
		enderfurnace = null;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public UUID getID()
	{
		return this.id;
	}
	
	public void openFurnace()
	{
		if(enderfurnace != null)
			enderfurnace.open();
	}
	
	public Object getData(Object key)
	{
		if(data == null)
			return null;
		return data.get(key);
	}
	
	public void setData(Object key, Object value)
	{
		if(data == null)
			data = new HashMap<Object,Object>();
		data.put(key, value);
	}
	
	void setTeam(AnniTeam t)
	{
		this.team = t;
		String playerName = this.name;
		String name = playerName.length() > 14 ? playerName.substring(0, 14) : playerName;
		if(t == null)
		{
			Player p = this.getPlayer();
			if(p != null)
				p.setPlayerListName(ChatColor.WHITE+name);
			return;
		}
		try 
		{
			Player p = this.getPlayer();
			if(p != null)
			{
				p.setPlayerListName(t.getColor() + name);
			}
		}
		catch(IllegalArgumentException e1) 
		{
			Random rnd = new Random();
			name = (name.length() > 11 ? name.substring(0, 11) : name) + "" + rnd.nextInt(9);
			try 
			{
				Player p = this.getPlayer();
				if(p != null)
				{
					p.setPlayerListName(t.getColor() + name);
				}
			}
			catch(IllegalArgumentException e2) 
			{
				Bukkit.getLogger().warning("[Annihilation] setPlayerListName error: " + e2.getMessage());
			}
		}
	}
	
	public AnniTeam getTeam()
	{
		return this.team;
	}
	
	public void setKit(Kit kit)
	{
		if(kit != null)
			this.kit = kit;
	}
	
	public void sendMessage(String message)
	{
		Player p =  Bukkit.getPlayer(id);
		if(p != null)
			p.sendMessage(message);
	}
	
	public Kit getKit()
	{
		return this.kit;
	}
	
	public Player getPlayer()
	{
		return Bukkit.getPlayer(this.id);
	}
	
	public boolean isOnline()
	{
		return getPlayer() != null;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof AnniPlayer)
		{
			AnniPlayer p = (AnniPlayer)obj;
			return this.id.equals(p.id);
		}
		else return false;
	}
}