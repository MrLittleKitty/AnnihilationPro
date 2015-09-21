package com.gmail.nuclearcat1337.anniPro.anniMap;

import com.gmail.nuclearcat1337.anniPro.utils.Loc;
import javafx.util.Pair;
import org.bukkit.configuration.ConfigurationSection;

public class Area
{
    private boolean allowPVP = true;
    private boolean allowDamage = true;
    private boolean allowHunger = true;
    private Loc corner1;
    private Loc corner2;
    private String name;

    public Area(Loc one, Loc two, String name)
    {
        this.corner1 = one;
        this.corner2 = two;
        this.name = name;
    }

    public Area(ConfigurationSection section)
    {
        assert section != null;
        this.name = section.getString("Name");
        corner1 = new Loc(section.getConfigurationSection("Corner1"));
        corner2 = new Loc(section.getConfigurationSection("Corner2"));
        if(section.isSet("AllowPVP"))
            this.setAllowPVP(section.getBoolean("AllowPVP"));
        if(section.isSet("AllowHunger"))
            this.setAllowHunger(section.getBoolean("AllowHunger"));
        if(section.isSet("AllowDamage"))
            this.setAllowDamage(section.getBoolean("AllowDamage"));
    }

    public String getName()
    {
        return name;
    }

    public Pair<Loc,Loc> getCorners()
    {
        return new Pair<>(corner1,corner2);
    }

    public void setAllowPVP(boolean allowPvp)
    {
        this.allowPVP = allowPvp;
    }

    public void setAllowDamage(boolean allowDamage)
    {
        this.allowDamage = allowDamage;
    }

    public void setAllowHunger(boolean allowHunger)
    {
        this.allowHunger = allowHunger;
    }

    public boolean getAllowPVP()
    {
        return allowPVP;
    }

    public boolean getAllowDamage()
    {
        return allowDamage;
    }

    public boolean getAllowHunger()
    {
        return allowHunger;
    }

    public void saveToConfig(ConfigurationSection section)
    {
        section.set("Name",this.getName());
        corner1.saveToConfig(section.createSection("Corner1"));
        corner2.saveToConfig(section.createSection("Corner2"));
        section.set("AllowPVP",getAllowPVP());
        section.set("AllowHunger",getAllowHunger());
        section.set("AllowDamage",getAllowDamage());
    }

    private boolean fallsBetween(int one, int two, int num)
    {
        int min, max;
        if(one < two)
        {
            min = one;
            max = two;
        }
        else
        {
            min = two;
            max = one;
        }

        return num >= min && num <= max;
    }

    public boolean isInArea(Loc loc)
    {
        if(!corner1.getWorld().equals(loc.getWorld()))
            return false;

        return	fallsBetween(corner1.getBlockX(),corner2.getBlockX(),loc.getBlockX()) &&
                fallsBetween(corner1.getBlockY(),corner2.getBlockY(),loc.getBlockY()) &&
                fallsBetween(corner1.getBlockZ(),corner2.getBlockZ(),loc.getBlockZ());
    }
}
//	private static Map<String,Area> areas = new HashMap<String,Area>();
//	
//	
//	public static void registerListeners(JavaPlugin p)
//	{
//		areas.clear();
//		AreaListener l = new AreaListener();
//		Bukkit.getPluginManager().registerEvents(l, p);
//		p.getCommand("Area").setExecutor(l);
//	}
//	
//	public static void loadAreas(ConfigurationSection areaSection)
//	{
//		if(areaSection != null)
//		{
//			for(String key : areaSection.getKeys(false))
//			{
//				ConfigurationSection area = areaSection.getConfigurationSection(key);
//				String name = area.getString("Name");
//				Location loc1 = ConfigManager.getLocation(area.getConfigurationSection("Corner1Location"));
//				Location loc2 = ConfigManager.getLocation(area.getConfigurationSection("Corner2Location"));
//				if(name != null && loc1 != null && loc2 != null)
//				{
//					Area a = new Area(name,new Loc(loc1),new Loc(loc2));
//					if(area.isBoolean("AllowPVP"))
//						a.allowPVP = area.getBoolean("AllowPVP");
//					areas.put(name.toLowerCase(), a);
//				}
//			}
//		}
//	}
//	
//	public static void saveAreas(ConfigurationSection areaSection)
//	{
//		int counter = 1;
//		for(Area a : areas.values())
//		{
//			ConfigurationSection sec = areaSection.createSection(counter+"");
//			sec.set("Name", a.Name);
//			sec.set("AllowPVP", a.allowPVP);
//			ConfigManager.saveLocation(a.Corner1, sec.createSection("Corner1Location"));
//			ConfigManager.saveLocation(a.Corner2, sec.createSection("Corner2Location"));
//			counter++;
//		}
//	}
//	
//	private static boolean fallsBetween(int one, int two, int num)
//	{
//		int min, max;
//		if(one < two)
//		{
//			min = one;
//			max = two;
//		}
//		else
//		{
//			min = two;
//			max = one;
//		}
//		
//		return num >= min && num <= max;
//	}
//	
//	private static Area getArea(String name)
//	{
//		return areas.get(name.toLowerCase());
//	}
//	
//	private static Area getArea(Loc loc)
//	{
//		for(Area a : areas.values())
//		{
//			if(a.Corner1.getWorldName().equals(loc.getWorldName()))
//			{
//				if(	fallsBetween(a.Corner1.getBlockX(),a.Corner2.getBlockX(),loc.getBlockX()) &&
//					fallsBetween(a.Corner1.getBlockY(),a.Corner2.getBlockY(),loc.getBlockY()) &&
//					fallsBetween(a.Corner1.getBlockZ(),a.Corner2.getBlockZ(),loc.getBlockZ())
//						)
//					return a;
//			}
//		}
//		return null;
//	}
//	
//	private static class AreaListener implements Listener, CommandExecutor
//	{
//		@EventHandler(priority = EventPriority.LOW,ignoreCancelled = true)
//		public void playerCheck(PlayerInteractEvent event)
//		{
//			if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK)
//			{
//				final Player player = event.getPlayer();
//				if(KitUtils.itemHasName(player.getItemInHand(), CustomItem.AREAWAND.getName()))
//				{
//					final AnniPlayer p = AnniPlayer.getPlayer(player.getUniqueId());
//					if(p != null)
//					{
//						event.setCancelled(true);
//						if(event.getAction() == Action.LEFT_CLICK_BLOCK)
//						{
//							p.setData("A.Loc1", new Loc(event.getClickedBlock().getLocation()));
//							player.sendMessage(ChatColor.LIGHT_PURPLE+"Corner "+ChatColor.GOLD+"1 "+ChatColor.LIGHT_PURPLE+"set.");
//						}
//						else
//						{
//							p.setData("A.Loc2", new Loc(event.getClickedBlock().getLocation()));
//							player.sendMessage(ChatColor.LIGHT_PURPLE+"Corner "+ChatColor.GOLD+"2 "+ChatColor.LIGHT_PURPLE+"set.");
//						}
//					}
//				}
//			}
//		}
//		
//		@EventHandler(priority = EventPriority.LOWEST,ignoreCancelled = true)
//		public void checkBreaks(EntityDamageByEntityEvent e)
//		{
//			if(e.getDamager().getType() == EntityType.PLAYER && e.getEntity().getType() == EntityType.PLAYER)
//			{
//				Area a = Area.getArea(new Loc(e.getDamager().getLocation()));
//				if(a != null && !a.allowPVP)
//				{
//					e.setCancelled(true);
//					return;
//				}
//				a= Area.getArea(new Loc(e.getEntity().getLocation()));
//				if(a != null && !a.allowPVP)
//				{
//					e.setCancelled(true);
//					return;
//				}
//			}
//		}
//		
//		@EventHandler(priority = EventPriority.LOWEST,ignoreCancelled = true)
//		public void checkBreaks(BlockBreakEvent e)
//		{
//			if(e.getPlayer().getGameMode() != GameMode.CREATIVE)
//			{
//				Area a = Area.getArea(new Loc(e.getBlock().getLocation()));
//				if(a != null)
//				{
//					e.setCancelled(true);
//				}
//			}
//		}
//		
//		@EventHandler(priority = EventPriority.LOWEST,ignoreCancelled = true)
//		public void checkBreaks(BlockPlaceEvent e)
//		{
//			if(e.getPlayer().getGameMode() != GameMode.CREATIVE)
//			{
//				Area a = Area.getArea(new Loc(e.getBlock().getLocation()));
//				if(a != null)
//				{
//					e.setCancelled(true);
//				}
//			}
//		}
//		
//		@EventHandler(priority = EventPriority.LOWEST,ignoreCancelled = true)
//		public void checkBreaks(PlayerBucketEmptyEvent event)
//		{
//			if(event.getPlayer().getGameMode() != GameMode.CREATIVE)
//			{
//				Area a = Area.getArea(new Loc(event.getBlockClicked().getRelative(event.getBlockFace()).getLocation()));
//				if(a != null)
//				{
//					event.setCancelled(true);
//				}
//			}
//		}
//
//		@Override
//		public boolean onCommand(CommandSender sender, Command cmd, String label ,String[] args)
//		{
//			if(sender instanceof Player)
//			{
//				final Player player = (Player)sender;
//				if(player.hasPermission("A.Area"))
//				{
//					final AnniPlayer p = AnniPlayer.getPlayer(player.getUniqueId());
//					if(p != null)
//					{
//						if(args.length > 0)
//						{
//							if(args[0].equalsIgnoreCase("create") && args.length > 1)
//							{
//								Object obj = p.getData("A.Loc1");
//								Object obj2 =  p.getData("A.Loc2");
//								if(obj != null && obj2 != null && obj instanceof Loc && obj2 instanceof Loc)
//								{
//									p.setData("A.Loc1",null);
//									p.setData("A.Loc2",null);
//									Area a = new Area(args[1],(Loc)obj,(Loc)obj2);
//									if(args.length > 2)
//									{
//										if(args[2].equalsIgnoreCase("allow"))
//											a.allowPVP = true;
//										else if(args[2].equalsIgnoreCase("disallow"))
//											a.allowPVP = false;
//									}
//									areas.put(a.Name.toLowerCase(), a);
//									sender.sendMessage(ChatColor.LIGHT_PURPLE+"Area "+ChatColor.GOLD+a.Name+ChatColor.LIGHT_PURPLE+" added");
//								}
//								else sender.sendMessage(ChatColor.RED+"You don't have 2 corners set to create an area.");
//							}
//							else if(args[0].equalsIgnoreCase("delete") && args.length > 1)
//							{
//								Area a;
//								if(args[1].equalsIgnoreCase("this"))
//									a = getArea(new Loc(player.getLocation()));
//								else
//									a = getArea(args[1].toLowerCase());
//								if(a != null)
//								{
//									areas.remove(a.Name.toLowerCase());
//									sender.sendMessage(ChatColor.LIGHT_PURPLE+"Area "+ChatColor.GOLD+a.Name+ChatColor.LIGHT_PURPLE+" removed");
//								}
//								else sender.sendMessage(ChatColor.RED+"Could not locate the area you wanted to delete.");
//							}
//							else sender.sendMessage(ChatColor.LIGHT_PURPLE+"/Area "+ChatColor.GOLD+"[create,delete] [name] [allow-PvP?]");
//						}
//						else sender.sendMessage(ChatColor.LIGHT_PURPLE+"/Area "+ChatColor.GOLD+"[create,delete] [name] [allow-PvP?]");
//					}
//				}
//				else sender.sendMessage(ChatColor.RED+"You do not have permission to use this command.");
//			}
//			else sender.sendMessage("You must be a player to select areas.");
//			return true;
//		}
//	}
	
//	public final Loc Corner1;
//	public final Loc Corner2;
//	public final String Name;
//	
//	private boolean allowPVP;
//	//private List<String> allowedBreak;
//	//public boolean allowExplosionDamage;
//	
//	private Area(String name, Loc b1, Loc b2)
//	{
//		this.Name = name;
//		this.Corner1 = b1;
//		this.Corner2 = b2;
//		//allowedBreak = new ArrayList<String>();
//		this.allowPVP = true;
//		//this.allowExplosionDamage = false;
//	}
	
//	public boolean isPVPAllowed()
//	{
//		return allowPVP;
//	}
	
//	public boolean addAllowToBreak(Material mat)
//	{
//		if(!allowedBreak.contains(mat.toString().toLowerCase()))
//		{
//			allowedBreak.add(mat.toString().toLowerCase());
//			return true;
//		}
//		return false;
//	}
	
//	public boolean removeAllowToBreak(Material mat)
//	{
//		if(allowedBreak.contains(mat.toString().toLowerCase()))
//		{
//			allowedBreak.remove(mat.toString().toLowerCase());
//			return true;
//		}
//		return false;
//	}
	
//	public boolean isAllowToBreak(Material mat)
//	{
//		return allowedBreak.contains(mat.toString().toLowerCase());
//	}
	
//	public void Save(ConfigurationSection section)
//	{
//		section.set("Name", getName());
//		//section.set("AllowExplosions", this.allowExplosionDamage);
//		//section.set("allowedBreak", this.allowedBreak);
//		section.set("AllowPVP", this.allowPVP);
//		ConfigUtils.saveLocation(Corner1, section.createSection("Block1Location"));
//		ConfigUtils.saveLocation(Corner2, section.createSection("Block2Location"));
//	}
//	
//	public static Area Load(ConfigurationSection section)
//	{
//		Loc b1 = new Loc(ConfigUtils.getLocation(section.getConfigurationSection("Block1Location")));
//		Loc b2 = new Loc(ConfigUtils.getLocation(section.getConfigurationSection("Block2Location")));
//		String name = section.getString("Name");
//		Area a = new Area(name,b1,b2);
//		//a.allowExplosionDamage = section.getBoolean("AllowExplosions");
//		a.allowPVP = section.getBoolean("AllowPVP");
//		//a.allowedBreak = section.getStringList("allowedBreak");
//		return a;
//	}
