package com.gmail.nuclearcat1337.anniPro.anniMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Furnace;
import org.bukkit.plugin.Plugin;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniTeam;
import com.gmail.nuclearcat1337.anniPro.anniGame.Game;
import com.gmail.nuclearcat1337.anniPro.anniGame.GameVars;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.anniPro.utils.Loc;
import com.gmail.nuclearcat1337.anniPro.utils.MapKey;
import com.gmail.nuclearcat1337.anniPro.voting.AutoRestarter;

public final class GameMap extends AnniMap implements Listener
{
	//private final File mapDirec;
//	private final File tempDirec;
	
	private RegeneratingBlocks blocks;
	private List<Loc> diamondLocs;
	private Map<MapKey,FacingObject> enderFurnaces;
	private Map<Material,UnplaceableBlock> unplaceableBlocks;
	private int currentphase;
	private int PhaseTime;
	private boolean canDamageNexus;
	private int damageMultiplier;
	private AutoRestarter restarter = null;
	
	public GameMap(String worldName, File mapDirectory)
	{
		super(worldName, new File(mapDirectory,"AnniMapConfig.yml"));
		if(GameVars.getAutoRestart())
			restarter = new AutoRestarter(AnnihilationMain.getInstance(),GameVars.getPlayersToRestart(),GameVars.getCountdownToRestart());
		//mapDirec = mapDirectory;
//		tempDirec = new File(AnnihilationMain.getInstance().getDataFolder()+"/TempWorld");
//		if(!tempDirec.exists())
//			tempDirec.mkdirs();
		//this.backupConfig();
		//this.backUpWorld();
	}
	
	@Override
	protected void loadFromConfig(ConfigurationSection section)
	{
		blocks = new RegeneratingBlocks(this.getWorldName(),section.getConfigurationSection("RegeneratingBlocks"));
		unplaceableBlocks = new EnumMap<Material,UnplaceableBlock>(Material.class);
		diamondLocs = new ArrayList<Loc>();
		enderFurnaces = new HashMap<MapKey,FacingObject>();
		this.currentphase = 0;
		this.canDamageNexus = false;
		this.damageMultiplier = 1;
		this.PhaseTime = (int)TimeUnit.SECONDS.convert(10, TimeUnit.MINUTES);
		if(section != null)
		{
			if(section.isInt("PhaseTime"))
				this.PhaseTime = section.getInt("PhaseTime");
			ConfigurationSection furnaces = section.getConfigurationSection("EnderFurnaces");
			if(furnaces != null)
			{
				for(String key : furnaces.getKeys(false))
				{
					FacingObject obj = FacingObject.loadFromConfig(furnaces.getConfigurationSection(key));
					this.addEnderFurnace(obj);
					//enderFurnaces.put(MapKey.getKey(obj.getLocation()), obj);
				}
			}
			ConfigurationSection diamonds = section.getConfigurationSection("DiamondLocations");
			if(diamonds != null)
			{
				for(String key : diamonds.getKeys(false))
				{
					//Location loc = ConfigManager.getLocation(diamonds.getConfigurationSection(key));
					Loc loc = new Loc(diamonds.getConfigurationSection(key));
					diamondLocs.add(loc);
				}
			}
			//blocks = new RegeneratingBlocks(worldName,configSection.getConfigurationSection("RegeneratingBlocks"));
			ConfigurationSection teams = section.getConfigurationSection("Teams");
			if(teams != null)
			{
				for(AnniTeam team : AnniTeam.Teams)
				{
					ConfigurationSection teamSection = teams.getConfigurationSection(team.getName()+" Team");
					if(teamSection != null)
					{
						//Location nexusloc = ConfigManager.getLocation(teamSection.getConfigurationSection("Nexus.Location"));
						ConfigurationSection nexSec = teamSection.getConfigurationSection("Nexus.Location");
						if(nexSec == null)
							team.getNexus().setLocation(null);
						else
						{
							Loc nexusloc = new Loc(teamSection.getConfigurationSection("Nexus.Location"));
							team.getNexus().setLocation(nexusloc);
						}
						
						//Location spectatorspawn = ConfigManager.getLocation(teamSection.getConfigurationSection("SpectatorLocation"));
						ConfigurationSection specSec = teamSection.getConfigurationSection("SpectatorLocation");
						if(specSec == null)
							team.setSpectatorLocation((Loc)null);
						else
						{
							Loc spectatorspawn = new Loc(teamSection.getConfigurationSection("SpectatorLocation"));
							team.setSpectatorLocation(spectatorspawn);
						}
						
						team.clearSpawns();
						ConfigurationSection spawns = teamSection.getConfigurationSection("Spawns");
						if(spawns != null)
						{	
							for(String key : spawns.getKeys(false))
							{
								//Location loc = ConfigManager.getPreciseLocation(spawns.getConfigurationSection(key));
								Loc loc = new Loc(spawns.getConfigurationSection(key));
								if(loc != null)
								{
									//int num = Integer.parseInt(key); //incase I do numbered spawns, then we can add at each number
									team.addSpawn(loc.toLocation());
								}
							}
						}
					}
				}
			}
			ConfigurationSection unplaceableSec = section.getConfigurationSection("UnplaceableBlocks");
			if(unplaceableSec != null)
			{
				for(String key : unplaceableSec.getKeys(false))
				{
					ConfigurationSection matSec = unplaceableSec.getConfigurationSection(key);
					Material mat = Material.valueOf(matSec.getString("Material"));
					List<Byte> b = matSec.getByteList("Values");
					if(b != null)
						for(Byte bt : b)
							addUnplaceableBlock(mat,bt);
				}
			}
		}
	}
	
	public void unLoadMap()
	{
		//World tpworld = Bukkit.getWorlds().size() > 0 ? Bukkit.getWorlds().get(0) : null;
		World tpworld = Game.LobbyMap != null ? Game.LobbyMap.getWorld() : null;
		if(tpworld == null)
			tpworld = Bukkit.getWorlds().size() > 0 ? Bukkit.getWorlds().get(0) : null;
		for(Player p : Bukkit.getOnlinePlayers())
		{
			if(p.getWorld().getName().equals(this.getWorldName()))
			{
				if(tpworld != null)
					p.teleport(tpworld.getSpawnLocation());
				else
					p.kickPlayer("Unloading the world and we dont want you to get trapped or glitched!");
			}
		}
		this.unregisterListeners();

		boolean b = Bukkit.unloadWorld(super.getWorldName(), false);
        Bukkit.getLogger().info("[Annihilation] "+super.getNiceWorldName()+" was unloaded successfully: "+b);
//		try
//		{
//			FileUtils.deleteDirectory(this.mapDirec);
//			FileUtils.copyDirectory(this.tempDirec, this.mapDirec);
//			FileUtils.deleteDirectory(this.tempDirec);
//		}
//		catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public void backUpWorld()
	{
		super.getWorld().save();
//		try
//		{		
//			File[] files = this.tempDirec.listFiles(new FilenameFilter(){
//	
//				@Override
//				public boolean accept(File file, String name)
//				{
//					return !name.contains("MapConfig");
//				}});
//			for(int x = 0; x < files.length; x++)
//			{
//				if(files[x].isDirectory())
//					FileUtils.deleteDirectory(files[x]);
//				else FileUtils.deleteQuietly(files[x]);
//			}
//			
//			FileUtils.copyDirectory(this.mapDirec, this.tempDirec, new FileFilter(){
//
//				@Override
//				public boolean accept(File file)
//				{
//					return !file.getName().contains("MapConfig");
//				}});
//		}
//		catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public void backupConfig()
	{
		super.saveToConfig();
//		File configBackup = new File(this.tempDirec,"AnniMapConfig.yml");
//		FileUtils.deleteQuietly(configBackup);
//		try
//		{
//			FileUtils.copyFile(super.configFile, configBackup);
//		}
//		catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	@Override
	protected void saveToConfig(ConfigurationSection section)
	{
		if(section != null)
		{
			//super.saveToConfig(configSection);
			blocks.saveToConfig(section.createSection("RegeneratingBlocks"));
			section.set("PhaseTime", this.PhaseTime);
			int counter = 1;
			ConfigurationSection enderFurnaces = section.createSection("EnderFurnaces");
			for(FacingObject obj : this.enderFurnaces.values())
			{
				obj.saveToConfig(enderFurnaces.createSection(""+counter	));
				counter++;
			}
			counter=1;
			ConfigurationSection diamonds = section.createSection("DiamondLocations");
			for(Loc loc : this.diamondLocs)
			{
				//ConfigManager.saveLocation(loc, diamonds.createSection(""+counter));
				loc.saveToConfig(diamonds.createSection(""+counter));
				counter++;
			}
			counter=1;
			blocks.saveToConfig(section.createSection("RegeneratingBlocks"));
			ConfigurationSection teams = section.createSection("Teams");
			for(AnniTeam team : AnniTeam.Teams)
			{
				ConfigurationSection teamSection = teams.createSection(team.getName()+" Team");
				
				Loc nexusLoc = team.getNexus().getLocation();
				if(nexusLoc != null)
					nexusLoc.saveToConfig(teamSection.createSection("Nexus.Location"));
					//ConfigManager.saveLocation(nexusLoc,teamSection.createSection("Nexus.Location"));
				
				Loc spectatorspawn = team.getSpectatorLocation();
				if(spectatorspawn != null)
					spectatorspawn.saveToConfig(teamSection.createSection("SpectatorLocation"));
					//ConfigManager.saveLocation(spectatorspawn,teamSection.createSection("SpectatorLocation"));
				
				ConfigurationSection spawnSection = teamSection.createSection("Spawns");
				List<Loc> spawns = team.getSpawnList();
				if(spawns != null && !spawns.isEmpty())
				{
					for(int x = 0; x < spawns.size(); x++)
					{
						spawns.get(x).saveToConfig(spawnSection.createSection(x+""));
						//ConfigManager.savePreciseLocation(spawns.get(x), spawnSection.createSection(x+""));
					}
				}
			}
			ConfigurationSection unplaceableSec = section.createSection("UnplaceableBlocks");
			for(Entry<Material,UnplaceableBlock> entry : this.unplaceableBlocks.entrySet())
			{
				ConfigurationSection matSec = unplaceableSec.createSection(entry.getKey().toString());
				matSec.set("Material", entry.getKey().toString());
				matSec.set("Values", entry.getValue().getValues());
			}
		}
		
	}
	
	public boolean addUnplaceableBlock(Material mat, byte b)
	{
		UnplaceableBlock block = unplaceableBlocks.get(mat);
		if(block == null)
		{
			block = new UnplaceableBlock();
			unplaceableBlocks.put(mat, block);
		}
		return block.addData(b);
	}
	
	public boolean removeUnplaceableBlock(Material mat, byte b)
	{
		UnplaceableBlock block = unplaceableBlocks.get(mat);
		if(block == null)
		{
			block = new UnplaceableBlock();
			unplaceableBlocks.put(mat, block);
		}
		return block.removeData(b);
	}
	
	@Override
	public void registerListeners(Plugin plugin)
	{
		super.registerListeners(plugin);
		Bukkit.getPluginManager().registerEvents(this, plugin);
		blocks.registerListeners(plugin);
	}
	
	@Override
	public void unregisterListeners()
	{
		super.unregisterListeners();
		HandlerList.unregisterAll(this);
		blocks.unregisterListeners();
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void blockPlaceCheck(BlockPlaceEvent event)
	{
		if(event.getPlayer().getGameMode() != GameMode.CREATIVE)
		{
			Block b = event.getBlock();
			UnplaceableBlock block = this.unplaceableBlocks.get(b.getType());
			if(block != null)
			{
				if(block.isData((byte)-1) || block.isData(b.getData()))
					event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void signClickCheck(PlayerInteractEvent event)
	{
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			Block b = event.getClickedBlock();
			if(b != null)
			{
				//Bukkit.getLogger().info("Error test 1");
				if(b.getType() == Material.FURNACE || b.getType() == Material.BURNING_FURNACE)
				{
					//Bukkit.getLogger().info("Error test 1");
					MapKey key = MapKey.getKey(b.getLocation());
					if(this.enderFurnaces.containsKey(key))
					{
						//Bukkit.getLogger().info("Error test 2");
						event.setCancelled(true);
						AnniPlayer p = AnniPlayer.getPlayer(event.getPlayer().getUniqueId());
						if(p != null)
						{
//							Bukkit.getLogger().info("Error test 3");
//							if(p.getFurnace() != null)
//							{
//								Bukkit.getLogger().info("Error test 4");
								p.openFurnace();
//							}
//							else
//								Bukkit.getLogger().warning("[Annihilation] Someones enderfurnace was null!");
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW,ignoreCancelled = true)
	public void signBreakCheck(BlockBreakEvent event)
	{
		if(event.getBlock() != null && event.getPlayer().getGameMode() != GameMode.CREATIVE)
		{
			if(event.getBlock().getType() == Material.FURNACE || event.getBlock().getType() == Material.BURNING_FURNACE)
			{
				MapKey key = MapKey.getKey(event.getBlock().getLocation());
				if(this.enderFurnaces.containsKey(key))
					event.setCancelled(true);
			}
		}
	}
	
	public int getPhaseTime()
	{
		return this.PhaseTime;
	}
	
	public boolean setPhaseTime(int time)
	{
		if(time > 0)
			this.PhaseTime = time;
		return time > 0;
	}
	
	public int getDamageMultiplier()
	{
		return this.damageMultiplier;
	}
	
	public boolean setDamageMultiplier(int multiplier)
	{
		if(multiplier > 0)
			this.damageMultiplier = multiplier;
		return multiplier > 0;
	}
	
	public int getCurrentPhase()
	{
		return currentphase;
	}
	
	public boolean setPhase(int phase)
	{
		if(phase > -1)
			this.currentphase = phase;
		if(restarter != null)
			restarter.check();
		return phase > -1;
	}
	
	public boolean canDamageNexus()
	{
		return this.canDamageNexus;
	}
	
	public void setCanDamageNexus(boolean canDamage)
	{
		this.canDamageNexus = canDamage;
	}
	
	public RegeneratingBlocks getRegeneratingBlocks()
	{
		return blocks;
	}
	
	public List<Loc> getDiamondLocations()
	{
		return Collections.unmodifiableList(diamondLocs);
	}
	
	public Map<MapKey,FacingObject> getEnderFurnaces()
	{
		return Collections.unmodifiableMap(enderFurnaces);
	}
	
	public void addEnderFurnace(Loc loc, BlockFace direction)
	{
		this.addEnderFurnace(new FacingObject(direction,loc));
	}
	
//	public void addEnderFurnace(FacingObject furnace)
//	{
//		this.enderFurnaces.put(Loc.toMapKey(furnace.getLocation()), furnace);
//	}
	
	public void addEnderFurnace(FacingObject furnace)
	{
		MapKey key = MapKey.getKey(furnace.getLocation());
		if(!enderFurnaces.containsKey(key))
		{
			try
			{
				Block block = furnace.getLocation().toLocation().getBlock();
				if(block.getType() != Material.FURNACE && block.getType() != Material.BURNING_FURNACE)
					block.setType(Material.BURNING_FURNACE);
				
				Furnace f = new Furnace(Material.BURNING_FURNACE);
				f.setFacingDirection(furnace.getFacingDirection());
				BlockState s = block.getState();
				s.setData(f);
				s.update(true);
			}
			catch(Exception e)
			{
				
			}
			enderFurnaces.put(key, furnace);
		}
	}
	
	public boolean removeEnderFurnace(Loc loc)
	{
		return removeEnderFurnace(loc.toLocation());
	}
	
	public boolean removeEnderFurnace(Location loc)
	{
		MapKey key = MapKey.getKey(loc);
		if(enderFurnaces.containsKey(key))
		{
			enderFurnaces.remove(key);
			loc.getWorld().getBlockAt(loc).setType(Material.AIR);
			return true;
		}
		return false;
	}
	
	public void addDiamond(Loc loc)
	{
		diamondLocs.add(loc);
	}
	
	public boolean removeDiamond(Loc loc)
	{
		for(int x = 0; x < diamondLocs.size(); x++)
		{
			Loc l = diamondLocs.get(x);
			if(l.equals(loc))
			{
				diamondLocs.remove(x);
				return true;
			}
		}
		return false;
	}

//	@Override
//	public void saveToConfig(ConfigurationSection configSection)
//	{
//		if(configSection != null)
//		{
//			super.saveToConfig(configSection);
//			blocks.saveToConfig(configSection.createSection("RegeneratingBlocks"));
//			configSection.set("PhaseTime", this.PhaseTime);
//			int counter = 1;
//			ConfigurationSection enderFurnaces = configSection.createSection("EnderFurnaces");
//			for(FacingObject obj : this.enderFurnaces.values())
//			{
//				obj.saveToConfig(enderFurnaces.createSection(""+counter	));
//				counter++;
//			}
//			counter=1;
//			ConfigurationSection diamonds = configSection.createSection("DiamondLocations");
//			for(Loc loc : this.diamondLocs)
//			{
//				//ConfigManager.saveLocation(loc, diamonds.createSection(""+counter));
//				loc.saveToConfig(diamonds.createSection(""+counter));
//				counter++;
//			}
//			counter=1;
//			blocks.saveToConfig(configSection.createSection("RegeneratingBlocks"));
//			ConfigurationSection teams = configSection.createSection("Teams");
//			for(AnniTeam team : AnniTeam.Teams)
//			{
//				ConfigurationSection teamSection = teams.createSection(team.getName()+" Team");
//				
//				Loc nexusLoc = team.Nexus.getLocation();
//				if(nexusLoc != null)
//					nexusLoc.saveToConfig(teamSection.createSection("Nexus.Location"));
//					//ConfigManager.saveLocation(nexusLoc,teamSection.createSection("Nexus.Location"));
//				
//				Loc spectatorspawn = team.getSpectatorLocation();
//				if(spectatorspawn != null)
//					spectatorspawn.saveToConfig(teamSection.createSection("SpectatorLocation"));
//					//ConfigManager.saveLocation(spectatorspawn,teamSection.createSection("SpectatorLocation"));
//				
//				ConfigurationSection spawnSection = teamSection.createSection("Spawns");
//				List<Loc> spawns = team.getSpawnList();
//				if(spawns != null && !spawns.isEmpty())
//				{
//					for(int x = 0; x < spawns.size(); x++)
//					{
//						spawns.get(x).saveToConfig(spawnSection.createSection(x+""));
//						//ConfigManager.savePreciseLocation(spawns.get(x), spawnSection.createSection(x+""));
//					}
//				}
//			}
//			ConfigurationSection unplaceableSec = configSection.createSection("UnplaceableBlocks");
//			for(Entry<Material,UnplaceableBlock> entry : this.unplaceableBlocks.entrySet())
//			{
//				ConfigurationSection matSec = unplaceableSec.createSection(entry.getKey().toString());
//				matSec.set("Material", entry.getKey().toString());
//				matSec.set("Values", entry.getValue().getValues());
//			}
//		}
//		
//	}
	
	private class UnplaceableBlock
	{
		private ArrayList<Byte> dataVals;
		public UnplaceableBlock()
		{
			dataVals = new ArrayList<Byte>();
		}
		
		public boolean addData(Byte b)
		{
			return dataVals.add(b);
		}
		
		public boolean removeData(Byte b)
		{
			return dataVals.remove(b);
		}
		
		public boolean isData(Byte b)
		{
			return dataVals.contains(b);
		}
		
		public List<Byte> getValues()
		{
			return Collections.unmodifiableList(dataVals);
		}
	}
}
