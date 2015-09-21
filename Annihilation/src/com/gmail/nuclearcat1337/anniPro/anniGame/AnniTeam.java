package com.gmail.nuclearcat1337.anniPro.anniGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scoreboard.Team;

import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.anniPro.main.Lang;
import com.gmail.nuclearcat1337.anniPro.utils.Loc;
import com.gmail.nuclearcat1337.anniPro.voting.ScoreboardAPI;

public final class AnniTeam
{
	public static final AnniTeam Red = new AnniTeam(ChatColor.RED);
	public static final AnniTeam Blue = new AnniTeam(ChatColor.BLUE);
	public static final AnniTeam Green = new AnniTeam(ChatColor.GREEN);
	public static final AnniTeam Yellow = new AnniTeam(ChatColor.YELLOW);
	public static final AnniTeam[] Teams = new AnniTeam[]{Red,Blue,Green,Yellow};
	
	
	public static AnniTeam getTeamByName(String name)
	{
		if(name.equalsIgnoreCase(Red.getName()) || name.equalsIgnoreCase(Red.getExternalName()))
			return Red;
		else if(name.equalsIgnoreCase(Blue.getName()) || name.equalsIgnoreCase(Blue.getExternalName()))
			return Blue;
		else if(name.equalsIgnoreCase(Green.getName()) || name.equalsIgnoreCase(Green.getExternalName()))
			return Green;
		else if(name.equalsIgnoreCase(Yellow.getName()) || name.equalsIgnoreCase(Yellow.getExternalName()))
			return Yellow;
		else return null;
	}
	
	public static AnniTeam getTeamByColor(ChatColor color)
	{
		if(color.equals(ChatColor.RED))
			return Red;
		else if(color.equals(ChatColor.BLUE))
			return Blue;
		else if(color.equals(ChatColor.GREEN))
			return Green;
		else if(color.equals(ChatColor.YELLOW))
			return Yellow;
		else 
			return null;
	}
	
	private final ChatColor Color;
	private final Nexus Nexus;
	
	private List<AnniPlayer> players;
	private Team scoreboardTeam;
	
	private final String externalName;
	private final String name;
	private int Health = 75;
	private LinkedList<Loc> spawns;
	private Random rand;
	private Loc spectatorLocation;

	private AnniTeam(ChatColor color)
	{
		rand = new Random(System.currentTimeMillis());
		players = new ArrayList<AnniPlayer>();
		this.Color = color;
		this.spawns = new LinkedList<Loc>();
		Nexus = new Nexus(this);
		this.spectatorLocation = null;
		Bukkit.getPluginManager().registerEvents(Nexus, AnnihilationMain.getInstance());
		name = this.Color.name().substring(0, 1)+this.Color.name().substring(1).toLowerCase();
		
		if(color == ChatColor.RED)
			externalName = Lang.REDTEAM.toString();
		else if(color == ChatColor.BLUE)
			externalName = Lang.BLUETEAM.toString();
		else if(color == ChatColor.GREEN)
			externalName = Lang.GREENTEAM.toString();
		else
			externalName = Lang.YELLOWTEAM.toString();
		
		scoreboardTeam = ScoreboardAPI.getScoreboard().registerNewTeam(name);
		scoreboardTeam.setAllowFriendlyFire(false);
		scoreboardTeam.setCanSeeFriendlyInvisibles(true);
		scoreboardTeam.setPrefix(this.getColor().toString());
	}
	
	public ChatColor getColor()
	{
		return Color;
	}
	
	public Nexus getNexus()
	{
		return Nexus;
	}
	
	public void joinTeam(AnniPlayer player)
	{
		if(player.getTeam() == null)
			player.setTeam(this);
		else
		{
			player.getTeam().leaveTeam(player);
			player.setTeam(this);
		}
		players.add(player);
		this.scoreboardTeam.addPlayer(player.getPlayer());
	}
	
	public void leaveTeam(AnniPlayer player)
	{
//		if(player.getTeam() != null)
//		{
		player.setTeam(null);
		players.remove(player);
//		}
		this.scoreboardTeam.removePlayer(player.getPlayer());
	}
	
	public List<AnniPlayer> getPlayers()
	{
		return Collections.unmodifiableList(players);
	}
	
//	public Location getSpectatorLocation()
//	{
//		if(this.spectatorLocation != null)
//			return this.spectatorLocation.toLocation();
//		else return null;
//	}
	
	public Loc getSpectatorLocation()
	{
		return this.spectatorLocation;
	}
	
	public int getPlayerCount()
	{
		return players.size();
	}
	
	public void setSpectatorLocation(Loc loc)
	{
		this.spectatorLocation = loc;
	}
	
	public void setSpectatorLocation(Location loc)
	{
		this.setSpectatorLocation(new Loc(loc,true));
	}
	
	public int addSpawn(Loc loc)
	{
		int val = this.spawns.size();
		this.spawns.addLast(loc);
		return val+1;
	}
	
	public int addSpawn(Location loc)
	{
		return addSpawn(new Loc(loc,true));
	}
	
	public boolean removeSpawn(int index)
	{
		if(spawns.size() >= index+1)
		{
			spawns.remove(index);
			return true;
		}
		return false;
	}
	
	public int getHealth()
	{
		return Health;
	}
	
	public void setHealth(int Health)
	{
		if(Health < 0)
			Health = 0;
		
		if(this.Health > 0)
		{
			this.Health = Health;
			ScoreboardAPI.setScore(this, this.Health);
		}
	}
	
	public void clearSpawns()
	{
		this.spawns.clear();
	}
	
	public boolean isTeamDead()
	{
		return Health <= 0;
	}
	
	public Location getRandomSpawn()
	{
		if(!spawns.isEmpty())
			return spawns.get(rand.nextInt(spawns.size())).toLocation();
		else
		{
			Bukkit.getLogger().warning("NO SPAWNS SET FOR TEAM "+this.getName().toUpperCase()+". SENDING TO LOBBY IF POSSIBLE.");
			if(Game.LobbyMap != null)
				return Game.LobbyMap.getSpawn();
			else 
				return null;
		}
	}
	
	public List<Loc> getSpawnList()
	{
		return Collections.unmodifiableList(this.spawns);
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getColoredName()
	{
		return this.Color+this.getName();
	}
	
	public String getExternalName()
	{
		return this.externalName;
	}
	
	public String getExternalColoredName()
	{
		return this.Color+this.getExternalName();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof AnniTeam)
			return ((AnniTeam)obj).Color == this.Color;
		else return false;
	}
	
	@Override
	public String toString()
	{
		return this.getName();
	}
	
	public AnniTeam loadFromConfig(ConfigurationSection configSection)
	{
		if(configSection != null)
		{
			//Location nexusloc = ConfigManager.getLocation(configSection.getConfigurationSection("Nexus.Location"));
			Loc nexusloc = new Loc(configSection.getConfigurationSection("Nexus.Location"));
			if(nexusloc != null)
			{
				Nexus.setLocation(nexusloc);
			}
			
			//Location spectatorspawn = ConfigManager.getLocation(configSection.getConfigurationSection("SpectatorLocation"));
			Loc spectatorspawn = new Loc(configSection.getConfigurationSection("SpectatorLocation"));
			if(spectatorspawn != null)
				setSpectatorLocation(spectatorspawn);
			
			ConfigurationSection spawns = configSection.getConfigurationSection("Spawns");
			if(spawns != null)
			{
				for(String key : spawns.getKeys(false))
				{
					//Location loc = ConfigManager.getPreciseLocation(spawns.getConfigurationSection(key));
					Loc loc = new Loc(spawns.getConfigurationSection(key));
					if(loc != null)
					{
						//int num = Integer.parseInt(key); //incase I do numbered spawns, then we can add at each number
						addSpawn(loc);
					}
				}
			}
		}
		return this;
	}
	
	public void saveToConfig(ConfigurationSection configSection)
	{
		if(configSection != null)
		{
			Loc nexusLoc = Nexus.getLocation();
			if(nexusLoc != null)
				nexusLoc.saveToConfig(configSection.createSection("Nexus.Location"));
				//ConfigManager.saveLocation(nexusLoc,configSection.createSection("Nexus.Location"));
			
			Loc spectatorspawn = getSpectatorLocation();
			if(spectatorspawn != null)
				spectatorspawn.saveToConfig(configSection.createSection("SpectatorLocation"));
				//ConfigManager.saveLocation(spectatorspawn,configSection.createSection("SpectatorLocation"));
			
			ConfigurationSection spawnSection = configSection.createSection("Spawns");
			List<Loc> spawns = getSpawnList();
			if(spawns != null && !spawns.isEmpty())
			{
				for(int x = 0; x < spawns.size(); x++)
				{
					spawns.get(x).saveToConfig(spawnSection.createSection(x+""));
					//ConfigManager.savePreciseLocation(spawns.get(x), spawnSection.createSection(x+""));
				}
			}
			//ConfigurationSection signSection = configSection.createSection("Signs");
//			Map<String,JoinSign> teamsigns = SignHandler.getTeamSigns();
//			int counter = 1;
//			for(Entry<String,JoinSign> entry : teamsigns.entrySet())
//			{
//				//only save signs for this team
//				if(entry.getValue().TeamName.equals(team.getName()))
//				{
//					ConfigManager.saveLocation(Loc.fromMapKey(entry.getKey()), signSection.createSection(counter+".Location"));
//					signSection.set(counter+".Direction", entry.getValue().Face.name());
//					signSection.set(counter+".SignPost", entry.getValue().signPost);
//					counter++;
//				}
//			}
		}
	}
}