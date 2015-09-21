package com.gmail.nuclearcat1337.anniPro.anniGame;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;

import com.gmail.nuclearcat1337.anniPro.anniEvents.AnniEvent;
import com.gmail.nuclearcat1337.anniPro.anniEvents.GameStartEvent;
import com.gmail.nuclearcat1337.anniPro.anniMap.GameMap;
import com.gmail.nuclearcat1337.anniPro.anniMap.LobbyMap;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.anniPro.voting.VoteMapManager;

public class Game
{
//	private static File tempWorldDirec;
//	private static File worldDirec;
	
	private static GameMap GameMap = null;
	public static LobbyMap LobbyMap = null;
	
	private static Map<String,String> worldNames = new HashMap<String,String>();
	private static Map<String,String> niceNames = new HashMap<String,String>();
	
	private static boolean GameRunning = false;
	
	public static boolean isGameRunning()
	{
		return GameRunning;
	}
	
	public static World getWorld(String name)
	{
		World w = Bukkit.getWorld(name);
		if(w == null)
			w = Bukkit.getWorld(worldNames.get(name.toLowerCase()));
		return w;
	}
	
	public static String getNiceWorldName(String worldName)
	{
		String name = niceNames.get(worldName.toLowerCase());
		if(name == null)
			name = worldName;
		return name;
	}
	
	public static GameMap getGameMap()
	{
		return GameMap;
	}
	
//	public static void saveMap()
//	{
//		if(GameMap != null  && ConfigManager.getMapConfig() != null)
//		{
//			GameMap.saveToConfig(ConfigManager.getMapConfig());
//			ConfigManager.saveMapConfig();
////			try
////			{
////				FileUtils.deleteDirectory(tempWorldDirec);
////				FileUtils.copyDirectory(worldDirec, tempWorldDirec);
////			}
////			catch (IOException e)
////			{
////				e.printStackTrace();
////			}
//		}
//	}
	
//	public static void unloadGameMap()
//	{
//		if(GameMap != null)
//		{
//			//Do saving stuff of game map!
//			World tpworld = Bukkit.getWorlds().size() > 0 ? Bukkit.getWorlds().get(0) : null;
//			for(Player p : Bukkit.getOnlinePlayers())
//			{
//				if(p.getWorld().getName().equals(GameMap.getWorldName()))
//				{
//					if(tpworld != null)
//						p.teleport(tpworld.getSpawnLocation());
//					else
//						p.kickPlayer("Unloading the world and we dont want you to get trapped or glitched!");
//				}
//			}
//			saveMap();
//			GameMap.unregisterListeners();
//			Bukkit.unloadWorld(GameMap.getWorldName(), false);
//			
////			try
////			{
////				FileUtils.deleteDirectory(worldDirec);
////				FileUtils.copyDirectory(tempWorldDirec, worldDirec);
////			}
////			catch (IOException e)
////			{
////				e.printStackTrace();
////			}
//		}
//	}
	
	public static boolean loadGameMap(File worldFolder)
	{
//		if(tempWorldDirec == null)
//		{
//			tempWorldDirec = new File(AnnihilationMain.getInstance().getDataFolder()+"/TempWorld");
//			if(!tempWorldDirec.exists())
//				tempWorldDirec.mkdirs();
//		}
		
		if(worldFolder.exists() && worldFolder.isDirectory())
		{
			File[] files = worldFolder.listFiles(new FilenameFilter()
			{
				public boolean accept(File file, String name)
				{
					return name.equalsIgnoreCase("level.dat");
				}
			});
			
			if ((files != null) && (files.length == 1))
			{
				try
				{
					//We have confirmed that the folder has a level.dat
					//Now we should copy all the files into the temp world folder
					
					//worldDirec = worldFolder;
					
					//FileUtils.copyDirectory(worldDirec, tempWorldDirec);
					
					String path = worldFolder.getPath();
					if(path.contains("plugins"))
						path = path.substring(path.indexOf("plugins"));
					WorldCreator cr = new WorldCreator(path);
					//WorldCreator cr = new WorldCreator(new File(worldFolder,"level.dat").toString());
					cr.environment(Environment.NORMAL);
					World mapWorld = Bukkit.createWorld(cr);
					if(mapWorld != null)
					{
						if(GameMap != null)
						{
							GameMap.unLoadMap();
							GameMap = null;
						}
						mapWorld.setAutoSave(false);
						mapWorld.setGameRuleValue("doMobSpawning", "false");
						mapWorld.setGameRuleValue("doFireTick", "false");	
//						File anniConfig = new File(worldFolder,"AnniMapConfig.yml");
//						if(!anniConfig.exists())
//								anniConfig.createNewFile();
						//YamlConfiguration mapconfig = ConfigManager.loadMapConfig(anniConfig);
						Game.GameMap = new GameMap(mapWorld.getName(),worldFolder);
						GameMap.registerListeners(AnnihilationMain.getInstance());
						Game.worldNames.put(worldFolder.getName().toLowerCase(), mapWorld.getName());
						Game.niceNames.put(mapWorld.getName().toLowerCase(),worldFolder.getName());
						return true;
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
					GameMap = null;
					return false;
				}
			}
		}
		return false;
	}
	
	public static boolean loadGameMap(String mapName)
	{
		return loadGameMap(new File(AnnihilationMain.getInstance().getDataFolder().getAbsolutePath()+"/Worlds",mapName));
	}
	
	public static boolean startGame()
	{
		if(!isGameRunning())
		{
			if(Game.getGameMap() == null)
			{
				if(GameVars.getVoting())
				{
					String winner = VoteMapManager.getWinningMap();
					if(Game.loadGameMap(winner))
					{
						GameRunning = true;
						AnniEvent.callEvent(new GameStartEvent());
						return true;
					}
				}
				else if(Game.loadGameMap(GameVars.getMap()))
				{
					GameRunning = true;
					AnniEvent.callEvent(new GameStartEvent());
					return true;
				}
			}
			else
			{
				GameRunning = true;
				AnniEvent.callEvent(new GameStartEvent());
				return true;
			}
		}
		return false;
	}
	
//	public static boolean isInAnniWorld(final Loc loc)
//	{
//		return isInAnniWorld(loc.toLocation());
//	}
//	
//	public static boolean isInAnniWorld(final Location loc)
//	{
//		String lobbyworld = LobbyMap == null ? "" : LobbyMap.getWorldName();
//		String gameworld = GameMap == null ? "" : GameMap.getWorldName();
//		String locworld = loc == null ? "CAT" : loc.getWorld().getName();
//		return locworld.equalsIgnoreCase(lobbyworld) || locworld.equalsIgnoreCase(gameworld);
//	}
	
//	public static void broadcastMessage(String message)
//	{
//		Bukkit.getConsoleSender().sendMessage(message);
//		for(AnniPlayer p : AnniPlayer.getPlayers().values())
//		{
//			p.sendMessage(message); //Fails silently
//		}
//	}
}
