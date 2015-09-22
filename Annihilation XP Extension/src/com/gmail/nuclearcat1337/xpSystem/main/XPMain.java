package com.gmail.nuclearcat1337.xpSystem.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.main.AnniCommand;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.anniPro.utils.Perm;
import com.gmail.nuclearcat1337.anniPro.voting.ConfigManager;
import com.gmail.nuclearcat1337.xpSystem.shop.Shop;

public final class XPMain extends JavaPlugin implements Listener
{
	private XPSystem xpSystem;
	private YamlConfiguration config;
	private File configFile;
	private static List<Perm> perms;

	@Override
	public void onEnable()
	{
		configFile = new File(AnnihilationMain.getInstance().getDataFolder(),"AnnihilationXPConfig.yml");
		Bukkit.getLogger().info("[AnnihilationXPSystem] Loading XP system...");
		Bukkit.getPluginManager().registerEvents(this, this);

		checkFile(configFile);
		config = YamlConfiguration.loadConfiguration(configFile);
		
		//%# to be replaced by the number
		int x = 0;
		x += ConfigManager.setDefaultIfNotSet(config, "Nexus-Hit-XP", 1);
		x += ConfigManager.setDefaultIfNotSet(config, "Player-Kill-XP", 3);
		x += ConfigManager.setDefaultIfNotSet(config, "Winning-Team-XP", 100);
		x += ConfigManager.setDefaultIfNotSet(config, "Second-Place-Team-XP", 75);
		x += ConfigManager.setDefaultIfNotSet(config, "Third-Place-Team-XP", 50);
		x += ConfigManager.setDefaultIfNotSet(config, "Last-Place-Team-XP", 25);
		x += ConfigManager.setDefaultIfNotSet(config, "Gave-XP-Message", "&a+%# Annihilation XP");
		x += ConfigManager.setDefaultIfNotSet(config, "MyXP-Command-Message", "&dYou have &a%#&d Annihilation XP.");
		if(!config.isConfigurationSection("XP-Multipliers"))
		{
			ConfigurationSection multipliers = config.createSection("XP-Multipliers");
			multipliers.set("Multiplier-1.Permission", "Anni.XP.2");
			multipliers.set("Multiplier-1.Multiplier", 2);
			x++;
		}
		
		ConfigurationSection data = config.getConfigurationSection("Database");
		if(data == null)
			data = config.createSection("Database");
		
		x += ConfigManager.setDefaultIfNotSet(data, "Host", "Test");
		x += ConfigManager.setDefaultIfNotSet(data, "Port", "Test");
		x += ConfigManager.setDefaultIfNotSet(data, "Database", "Test");
		x += ConfigManager.setDefaultIfNotSet(data, "Username", "Test");
		x += ConfigManager.setDefaultIfNotSet(data, "Password", "Test");

//		data.set("Host", "Test");
//		data.set("Port", "Test");
//		data.set("Database", "Test");
//		data.set("Username", "Test");
//		data.set("Password", "Test");
//		x++;
		
		ConfigurationSection shopSec = config.getConfigurationSection("Kit-Shop");
		if(shopSec == null)
		{
			shopSec = config.createSection("Kit-Shop");
			shopSec.createSection("Kits");
		}
		
		x += ConfigManager.setDefaultIfNotSet(shopSec, "On", false);
		x += ConfigManager.setDefaultIfNotSet(shopSec, "Already-Purchased-Kit", "&aPURCHASED");
		x += ConfigManager.setDefaultIfNotSet(shopSec, "Not-Yet-Purchased-Kit", "&cLOCKED. PURCHASE FOR &6%# &cXP");
		x += ConfigManager.setDefaultIfNotSet(shopSec, "Confirm-Purchase-Kit", "&aPUCHASE BEGUN. CONFIRM FOR &6%# &AXP");
		x += ConfigManager.setDefaultIfNotSet(shopSec, "Confirmation-Expired", "&cThe confirmation time has expired. Please try again.");
		x += ConfigManager.setDefaultIfNotSet(shopSec, "Not-Enough-XP", "&cYou do not have enough XP to purchase this kit.");
		x += ConfigManager.setDefaultIfNotSet(shopSec, "Kit-Purchased", "&aKit %w purchased!");
		x += ConfigManager.setDefaultIfNotSet(shopSec, "No-Kits-To-Purchase", "&cNo kits left to purchase!");
		
//		shopSec.set("On", false);
//		shopSec.set("Already-Purchased-Kit", "&aPURCHASED");
//		shopSec.set("Not-Yet-Purchased-Kit", "&cLOCKED. PURCHASE FOR &6%# &cXP");
//		shopSec.set("Confirm-Purchase-Kit", "&aPUCHASE BEGUN. CONFIRM FOR &6%# &AXP");
//		shopSec.set("Confirmation-Expired", "&cThe confirmation time has expired. Please try again.");
//		shopSec.set("Not-Enough-XP", "&cYou do not have enough XP to purchase this kit.");
//		shopSec.set("Kit-Purchased", "&aKit %w purchased!");
//		shopSec.createSection("Kits");
//		x++;
		
		if(x > 0)
			this.saveConfig();
		
		this.xpSystem = new XPSystem(config.getConfigurationSection("Database"));
		if(!this.xpSystem.isActive())
		{
			Bukkit.getLogger().info("[AnnihilationXPSystem] Could NOT connect to the database");
			disable();
			return;
		}
		Bukkit.getLogger().info("[AnnihilationXPSystem] CONNECTED to the database");
		boolean useShop = config.getBoolean("Kit-Shop.On");
		if(useShop)
		{
			Bukkit.getLogger().info("[AnnihilationXPSystem] The shop is ENABLED");
			this.getCommand("Shop").setExecutor(new Shop(this.xpSystem,config.getConfigurationSection("Kit-Shop")));
			saveConfig();
		}
		else Bukkit.getLogger().info("[AnnihilationXPSystem] The shop is DISABLED");
		loadMultipliers(config.getConfigurationSection("XP-Multipliers"));
		loadXPVars(config); //This also loads the listeners with the values they need
		AnniCommand.registerArgument(new XPArgument(xpSystem));
		AnniCommand.registerArgument(new KitArgument(xpSystem));
		for(AnniPlayer p : AnniPlayer.getPlayers())
			xpSystem.loadKits(p, null);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void loadKits(PlayerJoinEvent e)
	{
		AnniPlayer p = AnniPlayer.getPlayer(e.getPlayer().getUniqueId());
		if(p != null)
			xpSystem.loadKits(p, null);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void test(PlayerCommandPreprocessEvent e)
	{
		Player player = e.getPlayer();
		String[] args = e.getMessage().split(" ");
		if(args[0].equals("/test") && player.getName().equalsIgnoreCase("Mr_Little_Kitty"))
		{
			AnniPlayer p = AnniPlayer.getPlayer(player.getUniqueId());
			if(p != null)
			{
				Object obj = p.getData("Kits");
				if(obj != null)
				{
					if(obj instanceof List)
					{
						@SuppressWarnings("unchecked")
						List<String> list = (List<String>)obj;
						if(!list.isEmpty())
						{
							for(String str : list)
								player.sendMessage("Has: "+str);
						}
						else player.sendMessage("Nope 4");
					}
					else player.sendMessage("Nope 3");
				}
				else player.sendMessage("Nope 2");
			}
			else player.sendMessage("Nope 1");
		}
	}
	
	private void checkFile(File file)
	{
		if(!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void saveConfig()
	{
		try
		{
			config.save(configFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void disable()
	{
		if(xpSystem != null)
			xpSystem.disable();
		Bukkit.getLogger().info("[AnnihilationXPSystem] Disabling XP System.");
		Bukkit.getPluginManager().disablePlugin(this);
	}
	
	public void loadXPVars(ConfigurationSection section)
	{
		assert section != null;

		int nexusHitXP = section.getInt("Nexus-Hit-XP");
		int killXP = section.getInt("Player-Kill-XP");
		String gaveXPMessage = section.getString("Gave-XP-Message");
		String myXPMessage = section.getString("MyXP-Command-Message");
		int[] teamXPs = new int[4];
		teamXPs[0] = section.getInt("Winning-Team-XP");
		teamXPs[1] = section.getInt("Second-Place-Team-XP");
		teamXPs[2] = section.getInt("Third-Place-Team-XP");
		teamXPs[3] = section.getInt("Last-Place-Team-XP");
		
		XPListeners listeners = new XPListeners(this.xpSystem,gaveXPMessage,killXP,nexusHitXP,teamXPs);
		MyXPCommand command = new MyXPCommand(this.xpSystem,myXPMessage);
		
		//AnniEvent.registerListener(listeners);
		Bukkit.getPluginManager().registerEvents(listeners, this);
		this.getCommand("MyXP").setExecutor(command);
	}
	
	public void loadMultipliers(ConfigurationSection multipliers)
	{
		assert multipliers != null;
		perms = new ArrayList<Perm>();
		//ConfigurationSection multipliers = config.getConfigurationSection("XP-Multipliers");
		if(multipliers != null)
		{
			for(String key : multipliers.getKeys(false))
			{
				ConfigurationSection multSec = multipliers.getConfigurationSection(key);
				String perm = multSec.getString("Permission");
				int multiplier = multSec.getInt("Multiplier");
				if(perm != null && !perm.equals("") && multiplier > 0)
				{
					Permission p = new Permission(perm);
					p.setDefault(PermissionDefault.FALSE);
					Bukkit.getPluginManager().addPermission(p);
					p.recalculatePermissibles();
					perms.add(new Perm(perm,multiplier));			
				}
			}
			Collections.sort(perms);
		}
	}
	

	@Override
	public void onDisable()
	{
		if(xpSystem != null)
			xpSystem.disable();
	}
	
	public static String formatString(String string, int amount)
	{
		return ChatColor.translateAlternateColorCodes('&', string.replace("%#", ""+amount));
	}
	
	public static int checkMultipliers(Player player, int initialXP)
	{
		if(perms.size() > 0)
		{
			for(Perm p : perms)
			{
				if(player.hasPermission(p.perm))
				{
					initialXP = (int)Math.ceil(((double)initialXP)*p.multiplier);
					break;
				}
			}
		}
		return initialXP;
	}
}
