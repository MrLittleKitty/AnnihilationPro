
package com.gmail.nuclearcat1337.anniPro.main;

import java.io.File;
import java.io.IOException;

import com.gmail.nuclearcat1337.anniPro.anniEvents.PluginDisableEvent;
import com.gmail.nuclearcat1337.anniPro.announcementBar.AnnounceBar;
import com.gmail.nuclearcat1337.anniPro.announcementBar.Announcement;
import net.techcable.npclib.api.NPCMain;
import com.gmail.nuclearcat1337.anniPro.utils.DamageControl;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.nuclearcat1337.anniPro.anniEvents.GameEndEvent;
import com.gmail.nuclearcat1337.anniPro.anniEvents.GameStartEvent;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniTeam;
import com.gmail.nuclearcat1337.anniPro.anniGame.Game;
import com.gmail.nuclearcat1337.anniPro.anniGame.GameListeners;
import com.gmail.nuclearcat1337.anniPro.anniGame.GameVars;
import com.gmail.nuclearcat1337.anniPro.anniGame.StandardPhaseHandler;
import com.gmail.nuclearcat1337.anniPro.anniMap.GameMap;
import com.gmail.nuclearcat1337.anniPro.anniMap.LobbyMap;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ActionMenuItem;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemClickEvent;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemClickHandler;
import com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem;
import com.gmail.nuclearcat1337.anniPro.kits.CustomItem;
import com.gmail.nuclearcat1337.anniPro.kits.KitLoading;
import com.gmail.nuclearcat1337.anniPro.mapBuilder.MapBuilder;
import com.gmail.nuclearcat1337.anniPro.utils.InvisibilityListeners;
import com.gmail.nuclearcat1337.anniPro.voting.AutoStarter;
import com.gmail.nuclearcat1337.anniPro.voting.ConfigManager;
import com.gmail.nuclearcat1337.anniPro.voting.ScoreboardAPI;
import com.gmail.nuclearcat1337.anniPro.voting.VoteMapManager;

public class AnnihilationMain extends JavaPlugin implements Listener
{ 
	//public static boolean useProtocalHack = false;
	//public static final String Name = "Annihilation";
	private static JavaPlugin instance;
	public static JavaPlugin getInstance()
	{
		return instance;
	}
	
	
	@Override
	public void onEnable()
	{
		instance = this;
		loadLang();
		new InvisibilityListeners(this);
		Bukkit.getPluginManager().registerEvents(this,this);

        DamageControl.register(this);
		
		ConfigManager.load(this); //Enables the loading of the main config file, this is now different from the lobby config file
		
		loadMainValues(); //This will load values from the main config file and load the lobby from the lobby config file
		
		VoteMapManager.registerListener(this);
		AnniCommand.register(this);
		buildAnniCommand();
		//VoteMapManager.Enable(this);
		new MapBuilder(this);
		
		
		
		//DragonBar.registerListeners(this); //should come after the "use protocol hack" is loaded
		
		handleAutoAndVoting();

        if(GameVars.getUseAntiLog()) //Only register the logout prevention if its enabled from the config
            NPCMain.registerLogoutPrevention(this);
		
		AnniPlayer.RegisterListener(this); //needs to come after loading vars, checks players against game and lobby worlds
		
		if(Game.LobbyMap != null)
		{
			for(Player pl : Bukkit.getOnlinePlayers())
			{
//				final Player pl = p.getPlayer();
//				if(pl != null)
//				{
					Game.LobbyMap.sendToSpawn(pl);
//				}
			}
		}
		
		ScoreboardAPI.registerListener(this); //needs to come after anniplayer loading, checks anni players
		
		new TeamCommand(this);
		new GameListeners(this);
		new AreaCommand(this);
		//XPSystem.loadXPSystem(this,ConfigManager.getConfig().getConfigurationSection("XP-System"));
		new KitLoading(this); //No real reason to come last, but I kind of feel since its the heaviest processing power user, it should be last	
	}
	
	public void loadLang()
	{
		File lang = new File(getDataFolder(), "LanguageConfig.yml");
		if (!lang.exists())
		{
			try
			{
				getDataFolder().mkdir();
				lang.createNewFile();
//				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(lang);
//				defConfig.save(lang);
//				Lang.setFile(defConfig);
//				return;				
			}
			catch (IOException e)
			{
				e.printStackTrace(); // So they notice
				Bukkit.getLogger().severe("[Annihilation] Couldn't create language file.");
				Bukkit.getLogger().severe("[Annihilation] This is a fatal error. Now disabling");
				this.setEnabled(false); // Without it loaded, we can't send them messages
			}
		}
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
		conf.options().header(
				"This is the language config file. "
				+ "%# will be replaced with a number when needed. "
				+ "%w will be replaced with a word when needed. "
				+ "%n is the line separator. "
				+ "Normal MC color codes are supported.");
		for (Lang item : Lang.values())
		{
			if (conf.getString(item.getPath()) == null)
			{
				conf.set(item.getPath(), item.getDefault());
			}
		}
		Lang.setFile(conf);
		try
		{
			conf.save(lang);
		}
		catch (IOException e)
		{
//			log.log(Level.WARNING, "PluginName: Failed to save lang.yml.");
//			log.log(Level.WARNING,
//					"PluginName: Report this stack trace to <your name>.");
			e.printStackTrace();
		}
	}
	
	private void buildAnniCommand()
	{
		AnniCommand.registerArgument(new AnniArgument(){

			@Override
			public String getHelp() 
			{
				return ChatColor.LIGHT_PURPLE+"Start--"+ChatColor.GREEN+"Starts a game of Annihilation.";
			}

			@Override
			public boolean useByPlayerOnly() 
			{
				return false;
			}

			@Override
			public String getArgumentName() 
			{
				return "Start";
			}

			@Override
			public void executeCommand(CommandSender sender, String label,String[] args) 
			{
				if(!Game.isGameRunning())
				{
					if(Game.startGame())
						sender.sendMessage(ChatColor.GREEN+"The game has begun!");
					else 
						sender.sendMessage(ChatColor.RED+"The game was not started!");
				}
				else sender.sendMessage(ChatColor.RED+"The game is already running.");
			}

			@Override
			public String getPermission() 
			{
				return null;
			}

			@Override
			public MenuItem getMenuItem() 
			{
				return new ActionMenuItem("Start Game", new ItemClickHandler(){
					@Override
					public void onItemClick(ItemClickEvent event) {
						executeCommand(event.getPlayer(),null,null);
						event.setWillClose(true);
					}}, new ItemStack(Material.FEATHER), Game.isGameRunning() ? ChatColor.RED+"The game is already running." : ChatColor.GREEN+"Click to start the game.");
			}
			});
		AnniCommand.registerArgument(new AnniArgument(){

			@Override
			public String getHelp() 
			{
				return ChatColor.LIGHT_PURPLE+"Mapbuilder--"+ChatColor.GREEN+"Gives the mapbuilder item.";
			}

			@Override
			public boolean useByPlayerOnly() 
			{
				return true;
			}

			@Override
			public String getArgumentName() 
			{
				return "Mapbuilder";
			}

			@Override
			public void executeCommand(CommandSender sender, String label,String[] args)
			{
				if(sender instanceof Player)
				{
					((Player)sender).getInventory().addItem(CustomItem.MAPBUILDER.toItemStack(1));
				}
			}

			@Override
			public String getPermission()
			{
				return null;
			}

			@Override
			public MenuItem getMenuItem() 
			{
				return new ActionMenuItem("Get Mapbuilder", new ItemClickHandler(){
					@Override
					public void onItemClick(ItemClickEvent event) {
						executeCommand(event.getPlayer(),null,null);
						
					}},new ItemStack(Material.DIAMOND_PICKAXE),ChatColor.GREEN+"Click to get the mapbuilder item.");
			}});
		AnniCommand.registerArgument(new AnniArgument(){

			@Override
			public String getHelp() 
			{
				return ChatColor.LIGHT_PURPLE+"Save [Config,World,All]--"+ChatColor.GREEN+"Saves the specified item.";
			}

			@Override
			public boolean useByPlayerOnly() 
			{
				return false;
			}

			@Override
			public String getArgumentName() 
			{
				return "Save";
			}

			@Override
			public void executeCommand(CommandSender sender, String label,String[] args) 
			{	
				if(args != null && args.length > 0)
				{
					if(Game.getGameMap() != null)
					{
						GameMap map = Game.getGameMap();
						String name = map.getNiceWorldName();
						if(args[0].equalsIgnoreCase("config"))
						{
							map.saveToConfig();
							map.backupConfig();
							sender.sendMessage(ChatColor.GREEN+"Saved "+name+" config");
						}
						else if(args[0].equalsIgnoreCase("world"))
						{
							Game.getGameMap().backUpWorld();
							sender.sendMessage(ChatColor.GREEN+"Saved "+name+" world");
						}
						else if(args[0].equalsIgnoreCase("both") || args[0].equalsIgnoreCase("all"))
						{
							map.saveToConfig();
							Game.getGameMap().backupConfig();
							Game.getGameMap().backUpWorld();		
							sender.sendMessage(ChatColor.GREEN+"Saved "+name+" config and world");
						}
					}
					else sender.sendMessage(ChatColor.RED+"You do not have a game map loaded!");
				}
				
//				Game.saveMap();
//				saveMainValues();
//				sender.sendMessage(ChatColor.GREEN+"Saved Annihilation Main and Map Configs!");
//				if(args != null && args.length > 0 && args[0].equalsIgnoreCase("world"))
//				{
//					if(Game.getGameMap() != null)
//					{
//						World w = Game.getWorld(Game.getGameMap().getWorldName());
//						if(w != null)
//						{
//							w.save();
//							sender.sendMessage(ChatColor.GREEN+"Saved the Map "+Game.getGameMap().getNiceWorldName()+" to disk.");
//						}
//					}
//					
//					if(Game.LobbyMap != null)
//					{
//						World w = Game.getWorld(Game.LobbyMap.getWorldName());
//						if(w != null)
//						{
//							w.save();
//							sender.sendMessage(ChatColor.GREEN+"Saved the Lobby Map "+Game.LobbyMap.getNiceWorldName()+" to disk.");
//						}
//					}
//				}
			}

			@Override
			public String getPermission() 
			{
				return null;
			}

			@Override
			public MenuItem getMenuItem() 
			{
				return new ActionMenuItem("Save", new ItemClickHandler(){
					@Override
					public void onItemClick(ItemClickEvent event)
					{
						if(event.getClickType() == ClickType.LEFT)
							executeCommand(event.getPlayer(),null,new String[]{"Config"});
						else if(event.getClickType() == ClickType.RIGHT)
							executeCommand(event.getPlayer(),null,new String[]{"World"});
						else if(event.getClickType() == ClickType.SHIFT_LEFT || event.getClickType() == ClickType.SHIFT_RIGHT)
							executeCommand(event.getPlayer(),null,new String[]{"All"});
					}}, new ItemStack(Material.ANVIL), 
						ChatColor.GREEN+"Left click to save the Map Config.", ChatColor.GREEN+"Right click to save the Map World.",ChatColor.GREEN+"Shift click to save Both. (Config and World)");
			}});
	}
	
	private void handleAutoAndVoting()
	{
		if(GameVars.getAutoStart()) //This means they do want auto-start
			new AutoStarter(this,GameVars.getPlayersToStart(),GameVars.getCountdownToStart());

		if(GameVars.getVoting())
			VoteMapManager.beginVoting();	
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onGameStart(GameStartEvent event)
	{
        //Bukkit.getLogger().info("WELL WE GOT HERE!!!!");
		if(Game.getGameMap() != null)
		{
			for(AnniTeam t : AnniTeam.Teams)
			{
				t.setHealth(75);
			}
			GameMap map = Game.getGameMap();
			for(final AnniPlayer p : AnniPlayer.getPlayers())
			{
				final Player player = p.getPlayer();
				if(player != null && p.getTeam() != null)
				{
					player.setHealth(player.getHealth());
					player.setFoodLevel(20);
					player.setGameMode(GameVars.getDefaultGamemode());
					player.getInventory().clear();
					player.getInventory().setArmorContents(null);
					player.teleport(p.getTeam().getRandomSpawn());
					p.getKit().onPlayerSpawn(player);
					//Do the thing where we announce that the game has started
				}
			}
            AnnounceBar.getInstance().countDown(new Announcement(Lang.PHASEBAR.toStringReplacement(1) + " - {#}").setTime(map.getPhaseTime()).setCallback(new StandardPhaseHandler()));
			//MessageBar.countDown(Lang.PHASEBAR.toStringReplacement(1) + " - {#}", map.getPhaseTime(), new StandardPhaseHandler());
			map.setPhase(1);
			map.setCanDamageNexus(false);
		}
	}
	
	@EventHandler
	public void onGameEnd(GameEndEvent event)
	{
		GameMap map = Game.getGameMap(); // if gamemap is null at this point then we have a bigger problem
		//BarAPI.Reset();
		map.setPhase(0);
		map.setCanDamageNexus(false);
		
		if(event.getWinningTeam() != null)
			Bukkit.broadcastMessage(ChatColor.DARK_PURPLE+"The game has ended! "+event.getWinningTeam().getColor()+event.getWinningTeam().getName()+" Team "+ChatColor.DARK_PURPLE+"has won!");
		
		if(!GameVars.getEndOfGameCommand().equals(""))
            AnnounceBar.getInstance().countDown(new Announcement(ChatColor.DARK_PURPLE + "Game ends in: {#}").setTime(GameVars.getEndOfGameCountdown()).setCallback(new Runnable()
            {
                @Override
                public void run()
                {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), GameVars.getEndOfGameCommand());
                }
            }));
	}
	
	@Override
	public void onDisable()
	{
		for(AnniPlayer p : AnniPlayer.getPlayers())
		{
			if(p.isOnline())
				p.getKit().cleanup(p.getPlayer());
		}
		//XPSystem.disable();
		//TODO-------------------I dont know if we need a manual reset or not. There may be unintended consequences for not resetting it for a reload //MessageBar.Reset();
		saveMainValues();
		if(Game.getGameMap() != null)
			Game.getGameMap().unLoadMap();
		//Game.unloadGameMap();

        //AnniEvent.callEvent(new PluginDisableEvent());
        Bukkit.getPluginManager().callEvent(new PluginDisableEvent());
	}
	
	public void saveMainValues()
	{
//		YamlConfiguration config = ConfigManager.getConfig();
//		if(config != null)
//		{
//			//ConfigManager.reloadMainConfig(); //Not sure exactly what the point of this is
//			
//			//config.set("ProtocalHack", AnnihilationMain.useProtocalHack);
//			//config.set("DefaultGameMode", Game.defaultGamemode.name().toLowerCase());
//			//config.set("EndGameCommand", Game.endGameCommand);
//			
//			
//			//ConfigManager.saveMainConfig();
//		}
		
		if(Game.LobbyMap != null)
		{
			Game.LobbyMap.saveToConfig();
		}
	}
	
	private void loadMainValues()
	{
		//---------------------Main Configuration Values---------------------
		YamlConfiguration config = ConfigManager.getConfig();
		if(config != null)
		{	
			//Load all values here
//			AnnihilationMain.useProtocalHack = config.getBoolean("ProtocalHack");
//			if (!useProtocalHack)
//			{
//				if (v1_8Fake.isUsable())
//				{
//					useProtocalHack = true;
//					Util.detectVersion();
//				}
//			}
			
			GameVars.loadGameVars(config);
		}
		//Util.detectVersion();
		
		File lobbyFile = new File(this.getDataFolder(),"AnniLobbyConfig.yml");
		if(lobbyFile.exists())
		{
			Game.LobbyMap = new LobbyMap(lobbyFile);
			Game.LobbyMap.registerListeners(this);
		}
	}
}
