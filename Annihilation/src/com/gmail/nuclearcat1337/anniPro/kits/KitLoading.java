package com.gmail.nuclearcat1337.anniPro.kits;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.gmail.nuclearcat1337.anniPro.main.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.nuclearcat1337.anniPro.anniEvents.AnniEvent;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniTeam;
import com.gmail.nuclearcat1337.anniPro.anniGame.Game;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemMenu;
import com.gmail.nuclearcat1337.anniPro.itemMenus.ItemMenu.Size;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.anniPro.utils.Loc;

public class KitLoading implements Listener, CommandExecutor
{
	//private final Map<String,Kit> Kits;
	private final KitMenuItem[] items;
	private final Map<UUID,ItemMenu> menus;
	
	public KitLoading(final JavaPlugin p)
	{
		redcompass = ChatColor.RED+Lang.COMPASSTEXT.toString()+" "+AnniTeam.Red.getExternalName()+"'s Nexus";
		bluecompass = ChatColor.BLUE+Lang.COMPASSTEXT.toString()+" "+AnniTeam.Blue.getExternalName()+"'s Nexus";
		greencompass = ChatColor.GREEN+Lang.COMPASSTEXT.toString()+" "+AnniTeam.Green.getExternalName()+"'s Nexus";
		yellowcompass = ChatColor.YELLOW+Lang.COMPASSTEXT.toString()+" "+AnniTeam.Yellow.getExternalName()+"'s Nexus";

		Bukkit.getPluginManager().registerEvents(this, p);
		p.getCommand("Kit").setExecutor(this);
		
		menus = new HashMap<UUID,ItemMenu>();
		
		File classes = new File(p.getDataFolder().getAbsolutePath());
		if(!classes.exists() || !classes.isDirectory())
				classes.mkdir();
		classes = new File(p.getDataFolder().getAbsolutePath()+"/Kits");
		
		if(classes != null && classes.exists() && classes.isDirectory())
		{
			File[] files = classes.listFiles();
			URL[] urls = new URL[files.length];
			for(int x = 0; x < files.length; x++)
			{
				try
				{
					urls[x] = files[x].toURI().toURL();
				}
				catch(Exception e)
				{
					
				}
			}
			URLClassLoader loader = new URLClassLoader(urls,this.getClass().getClassLoader());
			
			for(File file : files)
			{
				List<String> names = getClassNames(file);
				try
				{
					for(String name : names)
					{
						Class<?> cl = null;
						try
						{
							cl = loader.loadClass(name);
						}
						catch(Exception e)
						{
							e.printStackTrace();
							Bukkit.getLogger().info("[Annihilation] Error loading class: "+name);
							continue;
						}
						
						if(cl != null && !Modifier.isAbstract(cl.getModifiers()) && !cl.isAnonymousClass())
						{
							if(Kit.class.isAssignableFrom(cl))
							{
								@SuppressWarnings("unchecked")
								Class<Kit> k = (Class<Kit>)cl;
								Kit kit = k.newInstance();
								if(kit.Initialize())
								{
									//AnniEvent.registerListener(kit);
									Bukkit.getPluginManager().registerEvents(kit, p);	
									Bukkit.getLogger().info("[Annihilation] --"+kit.getName());
									Kit.registerKit(kit);
								}
							}
//							else if(AnniPlugin.class.isAssignableFrom(cl))
//							{
//								@SuppressWarnings("unchecked")
//								Class<AnniPlugin> pl = (Class<AnniPlugin>) cl;
//								AnniPlugin plugin = pl.newInstance();
//								if(plugin.onEnable())
//								{
//									AnniEvent.registerListener(plugin);
//									Bukkit.getPluginManager().registerEvents(plugin, p);
//								}								
//							}
						}
					}
					//loader.close();
				}
				catch (InstantiationException | IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}
			
			if(loader != null)
			{
				try
				{
					loader.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		else
			classes.mkdir();

		Collection<Kit> kits = Kit.getKits();
		items = new KitMenuItem[kits.size()];
		int counter = 0;
		Iterator<Kit> it = kits.iterator();
		while(it.hasNext())
		{
			items[counter] = new KitMenuItem(it.next());
			counter++;
		}
	}
	
	private List<String> getClassNames(File file)
	{
		List<String> classNames = new ArrayList<String>();
		ZipInputStream zip = null;
		try
		{
			zip = new ZipInputStream(new FileInputStream(file));
			ZipEntry entry;
			while((entry = zip.getNextEntry()) != null)
			{
				if (entry.getName().endsWith(".class") && !entry.isDirectory())
				{
					// This ZipEntry represents a class. Now, what class
					// does it represent?
					StringBuilder className = new StringBuilder();
					for (String part : entry.getName().split("/"))
					{
						if (className.length() != 0)
							className.append(".");
						className.append(part);
						if (part.endsWith(".class"))
							className.setLength(className.length()
									- ".class".length());
					}
					classNames.add(className.toString());
				}
			}
		}
		catch(IOException e)
		{
			classNames = null;
		}
		finally
		{
			try
			{
			if(zip != null)
				zip.close();
			}catch(IOException e){}
		}
		return classNames;
	}
	
	public void openKitMap(Player player)
	{
		refreshMenu(player).open(player);
	}
	
	private ItemMenu refreshMenu(Player player)
	{
		ItemMenu menu = menus.get(player.getUniqueId());
		if(menu == null)
		{
			menu = new ItemMenu(player.getName()+"'s Kits",Size.fit(items.length));
			for(int x = 0; x < items.length; x++)
				menu.setItem(x, items[x]);
			menus.put(player.getUniqueId(), menu);
		}
		return menu;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void ClassChanger(final PlayerPortalEvent event)
	{
		if(Game.isGameRunning() && event.getPlayer().getGameMode() != GameMode.CREATIVE)
		{
			AnniPlayer p = AnniPlayer.getPlayer(event.getPlayer().getUniqueId());
			if(p != null)
			{
				event.setCancelled(true);
				if(p.getTeam() != null)
				{
					final Player pl = event.getPlayer();
					pl.teleport(p.getTeam().getRandomSpawn());
					Bukkit.getScheduler().runTaskLater(AnnihilationMain.getInstance(), new Runnable(){

						@Override
						public void run()
						{
							openKitMap(pl);
						}}, 40);
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void openKitMenuCheck(PlayerInteractEvent event)
	{
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)
		{
			final Player player = event.getPlayer();
			if(KitUtils.itemHasName(player.getItemInHand(), CustomItem.KITMAP.getName()))
			{
				openKitMap(player);
				event.setCancelled(true);
			}
		}
	}

	private String redcompass,bluecompass,greencompass,yellowcompass;
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void navCompassCheck(PlayerInteractEvent event)
	{
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)
		{
			final Player player = event.getPlayer();
			ItemStack item = player.getItemInHand();
			String name = null;
			Loc target = null;
			if(KitUtils.itemHasName(item, CustomItem.NAVCOMPASS.getName()))
			{
				name = redcompass;
				target = AnniTeam.Red.getNexus().getLocation();
			}
			else if(KitUtils.itemHasName(item, redcompass))
			{
				name = bluecompass;
				target = AnniTeam.Blue.getNexus().getLocation();
			}
			else if(KitUtils.itemHasName(item, bluecompass))
			{
				name = greencompass;
				target = AnniTeam.Green.getNexus().getLocation();
			}
			else if(KitUtils.itemHasName(item, greencompass))
			{
				name = yellowcompass;
				target = AnniTeam.Yellow.getNexus().getLocation();
			}
			else if(KitUtils.itemHasName(item, yellowcompass))
			{
				name = redcompass;
				target = AnniTeam.Red.getNexus().getLocation();
			}
			
			if(name != null && target != null)
			{
				ItemMeta m = item.getItemMeta();
				m.setDisplayName(name);
				item.setItemMeta(m);
				player.setCompassTarget(target.toLocation());
			}
		}
	}

	//stops players from dropping the items (soulbound items)
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void StopDrops(PlayerDropItemEvent event)
	{
	    Player player = event.getPlayer();
	    Item item = event.getItemDrop();
	    if(item != null)
	    {
		    ItemStack stack = item.getItemStack();
		    if(stack != null)
		    {
			    if(KitUtils.isSoulbound(stack))
			    {
				    player.playSound(player.getLocation(), Sound.BLAZE_HIT, 1.0F, 0.3F);
				    event.getItemDrop().remove();
			    }
		    }
	    }
	}
	
	//removes the drops on death (soulbound drops)
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void RemoveDeathDrops(PlayerDeathEvent event)
	{
		//Player player = event.getEntity();
		
		for(ItemStack s : new ArrayList<ItemStack>(event.getDrops()))
		{
			if(KitUtils.isSoulbound(s))
				event.getDrops().remove(s);
		}
	}
	
	//Stops Clicking of soulbound items
	@EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
	public void StopClicking(InventoryClickEvent event)
	{
	    HumanEntity entity = event.getWhoClicked();
	    ItemStack stack = event.getCurrentItem();
	    InventoryType top = event.getView().getTopInventory().getType();
	    
	    if (stack != null && (entity instanceof Player)) 
	    {
	    	if (top == InventoryType.PLAYER || top == InventoryType.WORKBENCH || top == InventoryType.CRAFTING) 
	    		return;

	    	if(KitUtils.isSoulbound(stack))
	          event.setCancelled(true); 
	    }
	 }

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player player = (Player)sender;
			if(player.hasPermission("Anni.ChangeKit"))
			{
				this.openKitMap(player);
				return true;
			}
		}
		return false;
	}
}
