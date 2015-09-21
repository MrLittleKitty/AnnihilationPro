package com.gmail.nuclearcat1337.anniPro.mapBuilder;

import java.io.File;
import java.io.FilenameFilter;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniTeam;
import com.gmail.nuclearcat1337.anniPro.anniGame.Game;
import com.gmail.nuclearcat1337.anniPro.anniMap.AnniSign;
import com.gmail.nuclearcat1337.anniPro.anniMap.FacingObject;
import com.gmail.nuclearcat1337.anniPro.anniMap.GameMap;
import com.gmail.nuclearcat1337.anniPro.anniMap.LobbyMap;
import com.gmail.nuclearcat1337.anniPro.anniMap.SignType;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ActionMenuItem;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ComboMenuItem;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemClickEvent;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemClickHandler;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemMenu;
import com.gmail.nuclearcat1337.anniPro.itemMenus.MenuItem;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemMenu.Size;
import com.gmail.nuclearcat1337.anniPro.kits.CustomItem;
import com.gmail.nuclearcat1337.anniPro.kits.KitUtils;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.anniPro.mapBuilder.TeamBlock.TeamBlockHandler;
import com.gmail.nuclearcat1337.anniPro.utils.Loc;


public class MapBuilder implements Listener
{
	private enum X
	{
		AreaWand,
		Nexus,
		Spawns,
		SpectatorSpawns,
		TeamSigns,
		WeaponShop,
		BrewingShop,
		PhaseTime,
		EnderFurnaces,
		RegeneratingBlocks,
		Diamond,
		UnplaceableBlockWand,
	}
	private class Wrapper
	{
		public final ItemMenu main;
		public boolean useMap;
		public Wrapper(ItemMenu menu)
		{
			main = menu;
			useMap = false;
		}
	}
	//private final AnnihilationMain plugin;
	private final Map<UUID,Wrapper> mapBuilders;
	private final ConversationFactory factory;
	private final Map<X,MenuItem> items;
	
	public static TimeUnit getUnit(String input)
	{
		TimeUnit u;
		switch(input.toLowerCase())
		{
			case "error":
				default:
				return null;
				
			case "s":
			case "sec":
			case "secs":
			case "second":
			case "seconds":
				u = TimeUnit.SECONDS;
				break;
			
			case "m":
			case "min":
			case "mins":
			case "minute":
			case "minutes":
				u = TimeUnit.MINUTES;
				break;
				
			case "h":
			case "hr":
			case "hrs":
			case "hour":
			case "hours":
				u = TimeUnit.HOURS;
				break;
				
			case "d":
			case "day":
			case "days":
				u = TimeUnit.DAYS;
				break;
		}
		return u;
	}	
	
	public static String getReadableLocation(Location loc, ChatColor numColor, ChatColor normalColor, boolean withWorld)
	{
		return (numColor+""+loc.getBlockX()+normalColor+", "+numColor+loc.getBlockY()+normalColor+", "+numColor+loc.getBlockZ()+normalColor+
				(withWorld ? " in world "+numColor+loc.getWorld().getName()+normalColor : ""));
	}
	
	public static String getReadableLocation(Loc loc, ChatColor numColor, ChatColor normalColor, boolean withWorld)
	{
		return getReadableLocation(loc.toLocation(),numColor,normalColor, withWorld);
	}
	
	public MapBuilder(Plugin plugin)
	{
		//this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		this.mapBuilders = new HashMap<UUID,Wrapper>();
		factory = new ConversationFactory(plugin);
		items = new EnumMap<X,MenuItem>(X.class);
		this.items.put(X.AreaWand, new ActionMenuItem("Protected Area Helper", new ItemClickHandler(){
			@Override
			public void onItemClick(ItemClickEvent event)
			{
				event.getPlayer().getInventory().addItem(CustomItem.AREAWAND.toItemStack(1));
				event.setWillClose(true);
			}}, 
			new ItemStack(Material.BLAZE_ROD), ChatColor.GREEN+"Click to get the",ChatColor.GREEN+"Area Helper Wand"));
	
		this.items.put(X.Spawns,new ActionMenuItem("Set Team Spawns", new ItemClickHandler(){

			@Override
			public void onItemClick(ItemClickEvent event)
			{
				for(AnniTeam team : AnniTeam.Teams)
				{
					TeamBlock t = TeamBlock.getByTeam(team);
					t.clearLines();
					t.addLine(Action.LEFT_CLICK_BLOCK, ChatColor.DARK_PURPLE, "add a "+team.getColor()+team.getName()+" spawn.");
					t.giveToPlayer(event.getPlayer());
				}
				Object obj = new TeamBlockHandler()
				{
					@Override
					public void onBlockClick(Player player,
							AnniTeam team, Action action, Block block, BlockFace face)
					{
						if(action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR)
						{	
							team.addSpawn(player.getLocation());
							player.sendMessage(ChatColor.LIGHT_PURPLE+"A "+team.getColor()+team.getName()+" team "+ChatColor.LIGHT_PURPLE+"spawn has been set at "+getReadableLocation(player.getLocation(),ChatColor.GOLD,ChatColor.LIGHT_PURPLE, false));
						}
					}
				};
				setPlayerMeta(event.getPlayer(),"TeamHandler",obj);
				event.setWillClose(true);
			}},new ItemStack(Material.BED),ChatColor.GREEN+"Click to set:",ChatColor.GREEN+"Team Spawns"));
		
		this.items.put(X.SpectatorSpawns,new ActionMenuItem("Set Team Spectator Spawns", new ItemClickHandler(){

			@Override
			public void onItemClick(ItemClickEvent event)
			{
				for(AnniTeam team : AnniTeam.Teams)
				{
					TeamBlock t = TeamBlock.getByTeam(team);
					t.clearLines();
					t.addLine(Action.LEFT_CLICK_BLOCK, ChatColor.DARK_PURPLE, "set "+team.getColor()+team.getName()+" spectator location.");
					t.giveToPlayer(event.getPlayer());
				}
				Object obj = new TeamBlockHandler()
				{
					@Override
					public void onBlockClick(Player player,
							AnniTeam team, Action action, Block block, BlockFace face)
					{
						if(action == Action.LEFT_CLICK_AIR)
						{	
							Location loc = player.getLocation();
							team.setSpectatorLocation(loc);
							player.sendMessage(team.getColor()+team.getName()+" team "+ChatColor.LIGHT_PURPLE+"spectator spawn has been set at "+getReadableLocation(player.getLocation(),ChatColor.GOLD,ChatColor.LIGHT_PURPLE, false));
						}
					}
				};
				setPlayerMeta(event.getPlayer(),"TeamHandler",obj);
				event.setWillClose(true);
			}},new ItemStack(Material.FEATHER),ChatColor.GREEN+"Click to set:",ChatColor.GREEN+"Team Spectator Spawns"));
		
		this.items.put(X.Nexus, new ActionMenuItem("Nexus Helper", new ItemClickHandler(){

			@Override
			public void onItemClick(ItemClickEvent event)
			{
				for(AnniTeam team : AnniTeam.Teams)
				{
					TeamBlock t = TeamBlock.getByTeam(team);
					t.clearLines();
					t.addLine(Action.LEFT_CLICK_BLOCK, ChatColor.DARK_PURPLE, "set "+team.getColor()+team.getName()+" Nexus.");
					t.giveToPlayer(event.getPlayer());
				}
				Object obj = new TeamBlockHandler()
				{
					@Override
					public void onBlockClick(Player player,
							AnniTeam team, Action action, Block block, BlockFace face)
					{
						if(action == Action.LEFT_CLICK_BLOCK)
						{	
							Location loc = block.getLocation();
							team.getNexus().setLocation(new Loc(loc,false));
							player.sendMessage(team.getColor()+team.getName()+" Nexus "+ChatColor.LIGHT_PURPLE+"was set at "+getReadableLocation(loc,ChatColor.GOLD,ChatColor.LIGHT_PURPLE, false));
						}
					}
				};
				setPlayerMeta(event.getPlayer(),"TeamHandler",obj);
				event.setWillClose(true);
			}},new ItemStack(Material.BEACON),ChatColor.GREEN+"Click to setup Nexuses."));
		
		this.items.put(X.TeamSigns, new ActionMenuItem("Set Team Signs", new ItemClickHandler(){

			@Override
			public void onItemClick(ItemClickEvent event)
			{
				for(AnniTeam team : AnniTeam.Teams)
				{
					TeamBlock t = TeamBlock.getByTeam(team);
					t.clearLines();
					t.addLine(Action.LEFT_CLICK_BLOCK, ChatColor.DARK_PURPLE, "set a "+team.getColor()+team.getName()+" team join sign.");
					t.giveToPlayer(event.getPlayer());
				}
				Object obj = new TeamBlockHandler()
				{
					@Override
					public void onBlockClick(Player player,
							AnniTeam team, Action action, Block block, BlockFace face)
					{
						if(action == Action.RIGHT_CLICK_BLOCK)
						{	
							Block b = block.getRelative(face);
							if(b != null)
							{
								FacingObject obj = new FacingObject(getCardinalDirection(player).getOppositeFace(),new Loc(b.getLocation(),false));
								AnniSign sign = new AnniSign(obj,face == BlockFace.UP ? true : (face == BlockFace.DOWN ? true : false),SignType.newTeamSign(team));
								if(Game.getGameMap() != null && player.getWorld().getName().equals(Game.getGameMap().getWorldName()))
								{
									if(Game.getGameMap().getSigns().addSign(sign))
										player.sendMessage(team.getColor()+team.getName()+" team "+ChatColor.LIGHT_PURPLE+"join sign has been added at "+getReadableLocation(block.getLocation(),ChatColor.GOLD,ChatColor.LIGHT_PURPLE, false)
											+ChatColor.LIGHT_PURPLE+" in map: "+ChatColor.GOLD+Game.getGameMap().getNiceWorldName());
								}
								else if(Game.LobbyMap != null && player.getWorld().getName().equals(Game.LobbyMap.getWorldName()))
								{
									if(Game.LobbyMap.getSigns().addSign(sign))
										player.sendMessage(team.getColor()+team.getName()+" team "+ChatColor.LIGHT_PURPLE+"join sign has been added at "+getReadableLocation(block.getLocation(),ChatColor.GOLD,ChatColor.LIGHT_PURPLE, false)
											+ChatColor.LIGHT_PURPLE+" in map: "+ChatColor.GOLD+Game.LobbyMap.getNiceWorldName());
								}
								
							}
						}
						else if(action == Action.LEFT_CLICK_BLOCK)
						{
							if(Game.getGameMap() != null && player.getWorld().getName().equals(Game.getGameMap().getWorldName()))
							{
								if(Game.getGameMap().getSigns().removeSign(block.getLocation()))
									player.sendMessage(ChatColor.LIGHT_PURPLE+"Removed a join sign in world: "+ ChatColor.GOLD+Game.getGameMap().getNiceWorldName());
							}
							else if(Game.LobbyMap != null && player.getWorld().getName().equals(Game.LobbyMap.getWorldName()))
							{
								if(Game.LobbyMap.getSigns().removeSign(block.getLocation()));
									player.sendMessage(ChatColor.LIGHT_PURPLE+"Removed a join sign in world: "+ ChatColor.GOLD+Game.LobbyMap.getNiceWorldName());
							}
						}
					}
				};
				setPlayerMeta(event.getPlayer(),"TeamHandler",obj);
				event.setWillClose(true);
			}},new ItemStack(Material.SIGN),ChatColor.GREEN+"Click to set:",ChatColor.GREEN+"Team Join Signs"));
		
		this.items.put(X.BrewingShop, new ActionMenuItem("Set Brewing Shop", new ItemClickHandler(){

			@Override
			public void onItemClick(ItemClickEvent event)
			{
				event.getPlayer().getInventory().addItem(CustomItem.BREWINGSHOP.toItemStack(1));
				event.setWillClose(true);
			}},new ItemStack(Material.BREWING_STAND_ITEM),ChatColor.GREEN+"Click to set:",ChatColor.GREEN+"Brewing Shops"));
		
		this.items.put(X.WeaponShop, new ActionMenuItem("Set Weapon Shop", new ItemClickHandler(){

			@Override
			public void onItemClick(ItemClickEvent event)
			{
				event.getPlayer().getInventory().addItem(CustomItem.WEAPONSHOP.toItemStack(1));
				event.setWillClose(true);
			}},new ItemStack(Material.ARROW),ChatColor.GREEN+"Click to set:",ChatColor.GREEN+"Weapon Shops"));
		
		this.items.put(X.Diamond, new ActionMenuItem("Set Diamond Spawns", new ItemClickHandler(){

			@Override
			public void onItemClick(ItemClickEvent event)
			{
				event.getPlayer().getInventory().addItem(CustomItem.DIAMONDHELPER.toItemStack(1));
				event.setWillClose(true);
			}},new ItemStack(Material.DIAMOND),ChatColor.GREEN+"Click to set:",ChatColor.GREEN+"Diamond Spawns"));
		
		this.items.put(X.EnderFurnaces, new ActionMenuItem("Set Ender Furnace", new ItemClickHandler(){

			@Override
			public void onItemClick(ItemClickEvent event)
			{
				event.getPlayer().getInventory().addItem(CustomItem.ENDERFURNACE.toItemStack(1));
				event.setWillClose(true);
			}},new ItemStack(Material.EYE_OF_ENDER),ChatColor.GREEN+"Click to set:",ChatColor.GREEN+"Ender Furnaces"));
		
		this.items.put(X.PhaseTime, new ActionMenuItem("Set Phase Time", new ItemClickHandler(){

			@Override
			public void onItemClick(final ItemClickEvent event)
			{
				SingleQuestionPrompt.newPrompt(event.getPlayer(), ChatColor.LIGHT_PURPLE+"Please enter how long you want each phase to be.", new AcceptAnswer(){

					@Override
					public boolean onAnswer(String input)
					{
						String[] args = input.split(" ");
						if(args.length == 2 && Game.getGameMap() != null)
						{
							try
							{
								TimeUnit unit = MapBuilder.getUnit(args[1]);
								if(unit != null)
								{
									int time = Integer.parseInt(args[0]);
									Game.getGameMap().setPhaseTime((int) TimeUnit.SECONDS.convert(time, unit));
									event.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE+"Phases will now run for "+ChatColor.GOLD+time+" "+unit.toString()+ChatColor.LIGHT_PURPLE+".");
									return true;
								}
							}
							catch(IllegalArgumentException e)
							{
								
							}
						}
						return false;
					}});
				event.setWillClose(true);
			}},new ItemStack(Material.WATCH),ChatColor.GREEN+"Click to set:",ChatColor.GREEN+"Game Phase Time"));
		
		this.items.put(X.RegeneratingBlocks, new ActionMenuItem("Regenerating Block Helper", new ItemClickHandler(){

			@Override
			public void onItemClick(ItemClickEvent event)
			{
				event.getPlayer().getInventory().addItem(CustomItem.REGENBLOCKHELPER.toItemStack(1));
				event.setWillClose(true);
			}},new ItemStack(Material.STICK),ChatColor.GREEN+"Click to setup:",ChatColor.GREEN+"Regenerating Blocks"));
		
		this.items.put(X.UnplaceableBlockWand, new ActionMenuItem("Unplaceable Block Wand", new ItemClickHandler(){
			@Override
			public void onItemClick(ItemClickEvent event)
			{
				event.getPlayer().getInventory().addItem(CustomItem.UNPLACEABLEBLOCKSWAND.toItemStack(1));
				event.setWillClose(true);
			}},new ItemStack(Material.DIAMOND_SPADE),ChatColor.GREEN+"Click to setup:",ChatColor.GREEN+"Unplaceable Blocks"));
	}
	
	private void setPlayerMeta(Player player, String key, final Object meta)
	{
		Callable<Object> b = new Callable<Object>(){
			@Override
			public Object call() throws Exception
			{
				return meta;
			}};
		player.setMetadata(key, new LazyMetadataValue(AnnihilationMain.getInstance(),b));
	}
	
	private Wrapper getItemMenu(final Player player)
	{
		Wrapper wrap = this.mapBuilders.get(player.getUniqueId());
		if(wrap == null)
		{
			ItemMenu map = new ItemMenu("Annihilation Map Config Menu", Size.THREE_LINE);
			buildMapMenu(map);
			ItemMenu main = new ItemMenu("Annihilation Main Config Menu", Size.THREE_LINE, map);
			wrap = new Wrapper(main);
			this.mapBuilders.put(player.getUniqueId(), wrap);
		}
		return wrap;
	}
	
	private void buildMapMenu(ItemMenu menu)
	{
		menu.setItem(4, this.items.get(X.UnplaceableBlockWand));
		menu.setItem(9, this.items.get(X.EnderFurnaces));
		menu.setItem(10, this.items.get(X.Diamond));
		menu.setItem(11, this.items.get(X.RegeneratingBlocks));
		menu.setItem(12, this.items.get(X.AreaWand));
		
		menu.setItem(13, new ActionMenuItem("Back to Main Menu",new ItemClickHandler(){

			@Override
			public void onItemClick(ItemClickEvent event)
			{
				event.setWillClose(true);
				final UUID ID = event.getPlayer().getUniqueId();
				final Wrapper wrap = mapBuilders.get(event.getPlayer().getUniqueId());
				wrap.useMap = false;
				Bukkit.getScheduler().scheduleSyncDelayedTask(AnnihilationMain.getInstance(), new Runnable()
				{
					public void run()
					{
						Player p = Bukkit.getPlayer(ID);
						if (p != null)
						{
							updatePlayerMapBuilder(p,wrap).open(p);
						}
					}
				}, 3);
			}},new ItemStack(Material.STAINED_GLASS_PANE),"Click to return to the Main menu."));
		
		menu.setItem(14, this.items.get(X.Nexus));
		menu.setItem(15, this.items.get(X.Spawns));
		menu.setItem(16, this.items.get(X.TeamSigns));
		menu.setItem(17, this.items.get(X.SpectatorSpawns));
		
		menu.setItem(21, this.items.get(X.BrewingShop));
		menu.setItem(22, this.items.get(X.PhaseTime));
		menu.setItem(23, this.items.get(X.WeaponShop));
	}
	
	public void openMapBuilder(Player player)
	{
		Wrapper mainMenu = getItemMenu(player);
		updatePlayerMapBuilder(player,mainMenu).open(player);
	}
	
	private ItemMenu updatePlayerMapBuilder(final Player player, final Wrapper wrap)
	{
		final ItemMenu mainMenu = wrap.main;
		ActionMenuItem lobby = new ActionMenuItem("Set Lobby Location", new ItemClickHandler(){
			@Override
			public void onItemClick(ItemClickEvent event)
			{
				if(Game.LobbyMap == null)
				{
					Game.LobbyMap = new LobbyMap(event.getPlayer().getLocation());
					Game.LobbyMap.registerListeners(AnnihilationMain.getInstance());
					event.getPlayer().sendMessage(ChatColor.GREEN+"Lobby created and spawn set!");
					event.setWillUpdate(true);
					updatePlayerMapBuilder(player,wrap);
				}
				else
				{
					Game.LobbyMap.setSpawn(event.getPlayer().getLocation());
					event.getPlayer().sendMessage(ChatColor.GREEN+"Lobby spawn changed!");
				}
				
			}}, 
			new ItemStack(Material.WOOL), ChatColor.GREEN+"Click to set the Lobby to this location.");
		
		if(Game.LobbyMap == null)
		{
			lobby.getLore().add(0,ChatColor.RED+"You do NOT currently have a lobby set!");
			mainMenu.clearAllItems().setItem(13, lobby);
		}
		else
		{
			ActionMenuItem tpItem = new ActionMenuItem("Teleport to Lobby", new ItemClickHandler(){
				@Override
				public void onItemClick(ItemClickEvent event)
				{
					if(Game.LobbyMap != null && Game.LobbyMap.getSpawn() != null)
						event.getPlayer().teleport(Game.LobbyMap.getSpawn());
				}},new ItemStack(Material.FEATHER),ChatColor.GREEN+"Click to:",ChatColor.GREEN+"Teleport to the Lobby.");
			mainMenu.setItem(4, tpItem);
			mainMenu.setItem(13, lobby);
			mainMenu.setItem(11, items.get(X.TeamSigns));
			mainMenu.setItem(12, this.items.get(X.AreaWand));
			ActionMenuItem loadWorld = new ActionMenuItem("Load New Map", new ItemClickHandler(){
				@Override
				public void onItemClick(ItemClickEvent event)
				{
					if(areThereWorlds())
					{
						event.setWillClose(true);
						final UUID ID = event.getPlayer().getUniqueId();
						Bukkit.getScheduler().scheduleSyncDelayedTask(AnnihilationMain.getInstance(), new Runnable()
						{
							public void run()
							{
								Player p = Bukkit.getPlayer(ID);
								if (p != null)
								{
									getWorldMenu(mainMenu,new ItemClickHandler()
									{
										@Override
										public void onItemClick(ItemClickEvent event)
										{
											if(event.getClickedItem() != null)
											{
												//If we load a new map, we want to change icons on the main item menu, save and unload the old map, then load the new one
												String str = event.getClickedItem().getItemMeta().getDisplayName();
												if(!Game.loadGameMap(str))
													player.sendMessage(ChatColor.RED+"Failed to load the map: "+ChatColor.GREEN+str);
												else
													player.sendMessage(ChatColor.GREEN+"Map Loaded!");
												updatePlayerMapBuilder(player,wrap);
											}
										}}).open(player);
								}
							}
						}, 3);
					}
					else player.sendMessage(ChatColor.RED+"There are no worlds in the worlds folder.");
				}}, 
				new ItemStack(Material.GRASS), ChatColor.GREEN+"Click to load a new map.");
			if(Game.getGameMap() == null)
			{
				//There is no currently loaded map
				mainMenu.setItem(14, loadWorld);
			}
			else
			{
				//There is a currently loaded world. So we give them the choice to go to that one or load a new one
				mainMenu.setItem(15, loadWorld); //used to be 23
				@SuppressWarnings("deprecation")
				ComboMenuItem useWorld = new ComboMenuItem("Edit World: "+Game.getGameMap().getNiceWorldName(), mainMenu.getParent(), new ItemClickHandler(){
					@Override
					public void onItemClick(ItemClickEvent event)
					{
						//They clicked and want to use that map. Now we should switch them to item map for building a game map and teleport them to the game world
						if(!event.getPlayer().getWorld().getName().equalsIgnoreCase(Game.getGameMap().getWorldName()))
						{
							World w = Game.getWorld(Game.getGameMap().getWorldName());
							if(w != null)
								player.teleport(w.getSpawnLocation());
							player.setGameMode(GameMode.CREATIVE);
						}
						wrap.useMap = true;
					}}, 
					new ItemStack(Material.DIRT,1,(byte)0,(byte)2), ChatColor.GREEN+"Click to edit this map.");
				mainMenu.setItem(14, useWorld);
			}
		}
		return wrap.useMap ? wrap.main.getParent() : wrap.main;
	}

//	@Override
//	public void onOptionClick(OptionClickEvent event)
//	{
//		event.setWillClose(true);
//		final Player player = event.getPlayer();
//		AnniPlayer anniplayer = AnniPlayer.getPlayer(player.getUniqueId());
//		final String name = event.getName();
//		if(anniplayer == null)
//			anniplayer = AnniPlayer.forcePlayerLoad(player);
//		
//		switch(name)
//		{
//			case "Set Lobby Location":
//			{
//				Game.LobbyLocation = player.getLocation();
//				player.sendMessage(ChatColor.GREEN+"The game's Lobby Location has been set at "+ChatColor.LIGHT_PURPLE+getReadableLocation(Game.LobbyLocation,ChatColor.GOLD,ChatColor.LIGHT_PURPLE, true));
//				break;
//			}
//			case "Set Game World":
//			{
//				Game.GameWorld = player.getWorld().getName();
//				player.sendMessage(ChatColor.LIGHT_PURPLE+"The game world has been set as: "+ChatColor.GOLD+Game.GameWorld);
//				break;
//			}
//			case "Nexus Helper":
//			{
//				for(AnniTeam team : AnniTeam.Teams)
//				{
//					TeamBlock t = TeamBlock.getByTeam(team);
//					t.clearLines();
//					t.addLine(Action.LEFT_CLICK_BLOCK, ChatColor.DARK_PURPLE, "set "+team.Color+team.getName()+" Nexus.");
//					t.giveToPlayer(player);
//				}
//				anniplayer.setData("Team Handler", 
//						new TeamBlockHandler()
//						{
//							@Override
//							public void onBlockClick(Player player,
//									AnniTeam team, Action action, Block block, BlockFace face)
//							{
//								if(action == Action.LEFT_CLICK_BLOCK)
//								{	
//									Location loc = block.getLocation();
//									team.Nexus.setLocation(new Loc(loc));
//									player.sendMessage(team.Color+team.getName()+" Nexus "+ChatColor.LIGHT_PURPLE+"was set at "+getReadableLocation(loc,ChatColor.GOLD,ChatColor.LIGHT_PURPLE, false));
//								}
//							}
//						});
//				break;
//			}
//			case "Add Team Spawns":
//			{
//				for(AnniTeam team : AnniTeam.Teams)
//				{
//					TeamBlock t = TeamBlock.getByTeam(team);
//					t.clearLines();
//					t.addLine(Action.LEFT_CLICK_BLOCK, ChatColor.DARK_PURPLE, "add a "+team.Color+team.getName()+" spawn.");
//					t.giveToPlayer(player);
//				}
//				anniplayer.setData("Team Handler", 
//						new TeamBlockHandler()
//						{
//							@Override
//							public void onBlockClick(Player player,
//									AnniTeam team, Action action, Block block, BlockFace face)
//							{
//								if(action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR)
//								{	
//									team.addSpawn(player.getLocation());
//									player.sendMessage(ChatColor.LIGHT_PURPLE+"A "+team.Color+team.getName()+" team "+ChatColor.LIGHT_PURPLE+"spawn has been set at "+getReadableLocation(player.getLocation(),ChatColor.GOLD,ChatColor.LIGHT_PURPLE, false));
//								}
//							}
//						});
//				break;
//			}
//			case "Set Team Signs":
//			{
//				for(AnniTeam team : AnniTeam.Teams)
//				{
//					TeamBlock t = TeamBlock.getByTeam(team);
//					t.clearLines();
//					t.addLine(Action.RIGHT_CLICK_BLOCK, ChatColor.DARK_PURPLE, "add a "+team.Color+team.getName()+" join sign.");
//					t.addLine(Action.LEFT_CLICK_BLOCK, ChatColor.DARK_PURPLE, "remove a join sign.");
//					t.giveToPlayer(player);
//				}
//				anniplayer.setData("Team Handler", 
//						new TeamBlockHandler()
//						{
//							@Override
//							public void onBlockClick(Player player,
//									AnniTeam team, Action action, Block block, BlockFace face)
//							{
//								if(action == Action.RIGHT_CLICK_BLOCK)
//								{	
//									Block b = block.getRelative(face);
//									if(b != null)
//									{
//										SignHandler.addTeamSign(team, b, getCardinalDirection(player).getOppositeFace(),face == BlockFace.UP ? true : (face == BlockFace.DOWN ? true : false));
//										player.sendMessage(team.Color+team.getName()+" team "+ChatColor.LIGHT_PURPLE+"join sign has been added at "+getReadableLocation(player.getLocation(),ChatColor.GOLD,ChatColor.LIGHT_PURPLE, false));
//									}
//								}
//								else if(action == Action.LEFT_CLICK_BLOCK)
//								{
//									if(SignHandler.removeJoinSign(block.getLocation()))
//										player.sendMessage(ChatColor.LIGHT_PURPLE+"Removed a join sign.");
//								}
//							}
//						});
//				break;
//			}
//			case "Set Brewing Shop":
//			{
//				player.getInventory().addItem(CustomItem.BREWINGSHOP.toItemStack(1));
//				break;
//			}
//			case "Set Weapon Shop":
//			{
//				player.getInventory().addItem(CustomItem.WEAPONSHOP.toItemStack(1));
//				break;
//			}
//			case "Set Ender Furnace":
//			{
//				player.getInventory().addItem(CustomItem.ENDERFURNACE.toItemStack(1));
//				break;	
//			}
//			case "Regenerating Block Helper":
//			{
//				player.getInventory().addItem(CustomItem.REGENBLOCKHELPER.toItemStack(1));
//				break;
//			}
//			case "Area Helper":
//			{
//				player.getInventory().addItem(CustomItem.AREAWAND.toItemStack(1));
//				break;
//			}
//			case "Diamond Helper":
//			{
//				player.getInventory().addItem(CustomItem.DIAMONDHELPER.toItemStack(1));
//				break;
//			}
//			case "Phase Time":
//			{
//				new SingleQuestionPrompt(player,ChatColor.LIGHT_PURPLE+"Please enter how long you want each phase to be.",new AcceptAnswer(){
//
//					@Override
//					public boolean onAnswer(String input)
//					{
//						String[] args = input.split(" ");
//						if(args.length == 2)
//						{
//							try
//							{
//								TimeUnit unit = MapBuilder.getUnit(args[1]);
//								if(unit != null)
//								{
//									int time = Integer.parseInt(args[0]);
//									Game.PhaseTime = (int) TimeUnit.SECONDS.convert(time, unit);
//									player.sendMessage(ChatColor.LIGHT_PURPLE+"Phases will now run for "+ChatColor.GOLD+time+" "+unit.toString()+ChatColor.LIGHT_PURPLE+".");
//									return true;
//								}
//							}
//							catch(IllegalArgumentException e)
//							{
//								
//							}
//						}
//						return false;
//					}}).run();
//				//For the lite version only
//				//player.sendMessage(ChatColor.RED+"Sorry, changing of the phase time is disabled on the trial version.");
//				break;
//			}
//			case "Set Team Spectator Locations":
//			{
//				for(AnniTeam team : AnniTeam.Teams)
//				{
//					TeamBlock t = TeamBlock.getByTeam(team);
//					t.clearLines();
//					t.addLine(Action.LEFT_CLICK_BLOCK, ChatColor.DARK_PURPLE, "set "+team.Color+team.getName()+" spectator location.");
//					t.giveToPlayer(player);
//				}
//				anniplayer.setData("Team Handler", 
//						new TeamBlockHandler()
//						{
//							@Override
//							public void onBlockClick(Player player,
//									AnniTeam team, Action action, Block block, BlockFace face)
//							{
//								if(action == Action.LEFT_CLICK_AIR)
//								{	
//									Location loc = player.getLocation();
//									team.setSpectatorLocation(loc);
//									player.sendMessage(team.Color+team.getName()+" team "+ChatColor.LIGHT_PURPLE+"spectator spawn has been set at "+getReadableLocation(player.getLocation(),ChatColor.GOLD,ChatColor.LIGHT_PURPLE, false));
//								}
//							}
//						});
//				break;
//			}
//		}
//	}
	
	private boolean areThereWorlds()
	{
		File worldFolder = new File(AnnihilationMain.getInstance().getDataFolder().getAbsolutePath()+"/Worlds");
		if(!worldFolder.exists())
			worldFolder.mkdir();
		File[] files = worldFolder.listFiles(new FilenameFilter()
		{
			public boolean accept(File file, String name)
			{
				return file.isDirectory();
			}
		});
		return files.length > 0;
	}
	
	private ItemMenu getWorldMenu(ItemMenu parent, ItemClickHandler handler)
	{
		File worldFolder = new File(AnnihilationMain.getInstance().getDataFolder().getAbsolutePath()+"/Worlds");
		if(!worldFolder.exists())
			worldFolder.mkdir();
		File[] files = worldFolder.listFiles(new FilenameFilter()
		{
			public boolean accept(File file, String name)
			{
				return file.isDirectory();
			}
		});
		ItemMenu menu = new ItemMenu("Annihilation Maps Menu",Size.fit(files.length),parent);
		for(int x = 0; x < files.length; x++)
		{
			menu.setItem(x, new ComboMenuItem(files[x].getName(),menu.getParent(),handler,new ItemStack(Material.GRASS),"Click to select the map: "+files[x].getName()));
		}
		return menu;
	}
	
	public static BlockFace getCardinalDirection(Player player)
	{
		BlockFace f = null;
		double rotation = (player.getLocation().getYaw() - 90) % 360;
		if (rotation < 0)
		{
			rotation += 360.0;
		}
		if (0 <= rotation && rotation < 22.5)
		{
			f = BlockFace.EAST;
		}
			//
			else if (22.5 <= rotation && rotation < (67.5/2))
			{
				f = BlockFace.EAST;
			}
			else if ((67.5/2) <= rotation && rotation < 67.5)
			{
				f = BlockFace.SOUTH;
			}
			//
		else if (67.5 <= rotation && rotation < 112.5)
		{
			f = BlockFace.SOUTH;
		}
			//
			else if (112.5 <= rotation && rotation < (157.5/2))
			{
				f = BlockFace.SOUTH;
			}
			else if ((157.5/2) <= rotation && rotation < 157.5)
			{
				f = BlockFace.WEST;
			}
			//
		else if (157.5 <= rotation && rotation < 202.5)
		{
			f = BlockFace.WEST;
		}
			//
			else if (202.5 <= rotation && rotation < (247.5/2))
			{
				f = BlockFace.WEST;
			}
			else if ((247.5/2) <= rotation && rotation < 247.5)
			{
				f = BlockFace.NORTH;
			}
			//
		else if (247.5 <= rotation && rotation < 292.5)
		{
			f = BlockFace.NORTH;
		}
			//
			else if (292.5 <= rotation && rotation < (337.5/2))
			{
				f = BlockFace.NORTH;
			}
			else if ((337.5/2) <= rotation && rotation < 337.5)
			{
				f = BlockFace.EAST;
			}
			//
		else if (337.5 <= rotation && rotation < 360.0)
		{
			f = BlockFace.EAST;
		}
		else
		{
			f = null;
		}
		return f.getOppositeFace();
	}
	 
	
	@EventHandler(priority=EventPriority.HIGH)
	public void openMapBuilderCheck(PlayerInteractEvent event)
	{
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)
		{
			final Player player = event.getPlayer();
			if(KitUtils.itemHasName(player.getItemInHand(), CustomItem.MAPBUILDER.getName()))
			{
				this.openMapBuilder(player);
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGH, ignoreCancelled = true)
	public void brewingAndWeaponCheck(PlayerInteractEvent event)
	{
		if(Game.getGameMap() != null)
		{
			GameMap map = Game.getGameMap();
			if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				final Player player = event.getPlayer();
				if(KitUtils.itemHasName(player.getItemInHand(), CustomItem.BREWINGSHOP.getName()))
				{
					event.setCancelled(true);
					Block b = event.getClickedBlock().getRelative(event.getBlockFace());
					if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
					{
						BlockFace face = event.getBlockFace();
						if(b != null)
						{
							AnniSign sign = new AnniSign(new FacingObject(getCardinalDirection(player).getOppositeFace(), new Loc(b.getLocation(),false)),
									face == BlockFace.UP ? true : (face == BlockFace.DOWN ? true : false), SignType.Brewing);
							map.getSigns().addSign(sign);
						}
					}
					else
					{
						if(map.getSigns().removeSign(event.getClickedBlock().getLocation()))
							player.sendMessage(ChatColor.LIGHT_PURPLE+"Removed a Sign");
					}
				}
				else if(KitUtils.itemHasName(player.getItemInHand(), CustomItem.WEAPONSHOP.getName()))
				{
					event.setCancelled(true);
					Block b = event.getClickedBlock().getRelative(event.getBlockFace());
					if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
					{	
						BlockFace face = event.getBlockFace();
						if(b != null)
						{
							AnniSign sign = new AnniSign(new FacingObject(getCardinalDirection(player).getOppositeFace(), new Loc(b.getLocation(),false)),
									face == BlockFace.UP ? true : (face == BlockFace.DOWN ? true : false), SignType.Weapon);
							map.getSigns().addSign(sign);
						}
					}
					else
					{
						if(map.getSigns().removeSign(event.getClickedBlock().getLocation()))
							player.sendMessage(ChatColor.LIGHT_PURPLE+"Removed a Sign");
					}
				}
				else if(KitUtils.itemHasName(player.getItemInHand(), CustomItem.ENDERFURNACE.getName()))
				{
					event.setCancelled(true);
					Block b = event.getClickedBlock().getRelative(event.getBlockFace());
					if(event.getAction() == Action.RIGHT_CLICK_BLOCK)
					{	
						if(b != null)
							map.addEnderFurnace(new Loc(b.getLocation(),false), getCardinalDirection(player).getOppositeFace());
					}
					else
					{
						if(map.removeEnderFurnace(event.getClickedBlock().getLocation()))
							player.sendMessage(ChatColor.LIGHT_PURPLE+"Removed an Ender Furnace.");
					}
					
				}
				else if(KitUtils.itemHasName(player.getItemInHand(), CustomItem.REGENBLOCKHELPER.getName()))
				{
					event.setCancelled(true);
					if(!player.isConversing())
					{	
						Block b = event.getClickedBlock();
						Conversation conv = factory.withModality(true).withFirstPrompt(new RegenBlockPrompt(b)).withLocalEcho(true).buildConversation(player);
						conv.begin();
					}
				}
				else if(KitUtils.itemHasName(player.getItemInHand(), CustomItem.UNPLACEABLEBLOCKSWAND.getName()))
				{
					event.setCancelled(true);
					final Block b = event.getClickedBlock();
					if(event.getAction() == Action.LEFT_CLICK_BLOCK)
					{
						SingleQuestionPrompt.newPrompt(player, ChatColor.LIGHT_PURPLE+"Do you want to add "+ChatColor.GREEN+"This"+ChatColor.LIGHT_PURPLE+" or "+
								ChatColor.GREEN+"All"+ChatColor.LIGHT_PURPLE+" data values?", new AcceptAnswer(){
							@SuppressWarnings("deprecation")
							@Override
							public boolean onAnswer(String input)
							{
								byte dataval = -2;
								boolean go = false;
								if(input.equalsIgnoreCase("this"))
								{
									dataval = b.getData();
									go = true;
								}
								else if(input.equalsIgnoreCase("all"))
								{
									dataval = -1;
									go = true;
								}
								
								if(go)
								{
									if(Game.getGameMap() != null)
									{
										if(Game.getGameMap().addUnplaceableBlock(b.getType(), dataval))
											player.sendMessage(ChatColor.GREEN+"The block was added.");
										else player.sendMessage(ChatColor.RED+"The block was not added.");
									}
									return true;
								}
								return false;
							}});
					}
					else
					{
						SingleQuestionPrompt.newPrompt(player, ChatColor.LIGHT_PURPLE+"Do you want to remove "+ChatColor.RED+"This"+ChatColor.LIGHT_PURPLE+" or "+
								ChatColor.RED+"All"+ChatColor.LIGHT_PURPLE+" data values?", new AcceptAnswer(){
							@SuppressWarnings("deprecation")
							@Override
							public boolean onAnswer(String input)
							{
								byte dataval = -2;
								boolean go = false;
								if(input.equalsIgnoreCase("this"))
								{
									dataval = b.getData();
									go = true;
								}
								else if(input.equalsIgnoreCase("all"))
								{
									dataval = -1;
									go = true;
								}
								
								if(go)
								{
									if(Game.getGameMap() != null)
									{
										if(Game.getGameMap().removeUnplaceableBlock(b.getType(), dataval))
											player.sendMessage(ChatColor.GREEN+"The block was removed.");
										else player.sendMessage(ChatColor.RED+"The block was not removed.");
									}
									return true;
								}
								return false;
							}});
					}
				}
				else if(KitUtils.itemHasName(player.getItemInHand(), CustomItem.DIAMONDHELPER.getName()))
				{
					event.setCancelled(true);
					final Block b = event.getClickedBlock();
					if(event.getAction() == Action.LEFT_CLICK_BLOCK)
					{
						map.addDiamond(new Loc(b.getLocation(),false));
						player.sendMessage(ChatColor.LIGHT_PURPLE+"Diamond added!");
					}
					else
					{
						if(map.removeDiamond(new Loc(b.getLocation(),false)))
							player.sendMessage(ChatColor.LIGHT_PURPLE+"Diamond removed!");
					}
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public void nexusHelperCheck(PlayerInteractEvent event)
	{
		if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK
				|| event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			final Player player = event.getPlayer();
			TeamBlock t = null;
			if(KitUtils.itemHasName(player.getItemInHand(), TeamBlock.Red.getName()))
				t = TeamBlock.Red;
			else if(KitUtils.itemHasName(player.getItemInHand(), TeamBlock.Blue.getName()))
				t = TeamBlock.Blue;
			else if(KitUtils.itemHasName(player.getItemInHand(), TeamBlock.Green.getName()))
				t = TeamBlock.Green;
			else if(KitUtils.itemHasName(player.getItemInHand(), TeamBlock.Yellow.getName()))
				t = TeamBlock.Yellow;
			if(t != null)
			{
				//They made a click with a team block
				event.setCancelled(true);
				List<MetadataValue> vals = player.getMetadata("TeamHandler");
				if(vals != null && vals.size() == 1)
				{
					Object obj = vals.get(0).value();
					if(obj != null && obj instanceof TeamBlockHandler)
					{
						((TeamBlockHandler)obj).onBlockClick(player, t.Team, event.getAction(), event.getClickedBlock(),event.getBlockFace());
					}
				}
			}
		}
	}
}
