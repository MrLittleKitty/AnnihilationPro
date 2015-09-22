package com.gmail.nuclearcat1337.vaultPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gmail.nuclearcat1337.anniPro.voting.ConfigManager;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.anniPro.utils.Perm;

public class VaultHook extends JavaPlugin
{
	private Economy econ;

    private YamlConfiguration config;
    private File configFile;
	private List<Perm> perms;
	
	@Override
	public void onEnable()
	{
		if(!verifyEcon())
        {
            Bukkit.getLogger().info("[AnnihilationVault] Error! Could not hook into vault");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        Bukkit.getLogger().info("[AnnihilationVault] Successfully hooked into Vault");
        configFile = new File(AnnihilationMain.getInstance().getDataFolder().getAbsolutePath(),"AnnihilationVaultConfig.yml");
        checkFile(configFile);
        config = YamlConfiguration.loadConfiguration(configFile);

        int x = 0;
        x += ConfigManager.setDefaultIfNotSet(config, "Nexus-Hit-Money", 1.00D);
        x += ConfigManager.setDefaultIfNotSet(config, "Player-Kill-Money", 3.00D);
        x += ConfigManager.setDefaultIfNotSet(config, "Winning-Team-Money", 100.00D);
        x += ConfigManager.setDefaultIfNotSet(config, "Second-Place-Team-Money", 75.00D);
        x += ConfigManager.setDefaultIfNotSet(config, "Third-Place-Team-Money", 50.00D);
        x += ConfigManager.setDefaultIfNotSet(config, "Last-Place-Team-Money", 25.00D);
        x += ConfigManager.setDefaultIfNotSet(config, "Gave-Money-Message", "&a+%# Annihilation XP");
        if(!config.isConfigurationSection("Money-Multipliers"))
        {
            ConfigurationSection multipliers = config.createSection("Money-Multipliers");
            multipliers.set("Multiplier-1.Permission", "Anni.Money.2");
            multipliers.set("Multiplier-1.Multiplier", 2);
            x++;
        }

        if(x > 0)
            saveConfig();

        loadMultipliers(config.getConfigurationSection("Money-Multipliers"));
        loadXPVars(config);

        //AnniEvent.registerListener(this);
        //Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	private boolean verifyEcon()
	{
		if(Bukkit.getPluginManager().getPlugin("Vault") == null) 
			return false;
		//Bukkit.getLogger().info("Here 1");
		RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
		if (rsp == null) 
		    return false;
		//Bukkit.getLogger().info("Here 2");
		econ = rsp.getProvider();
		//Bukkit.getLogger().info("Here 3");
		return econ != null;
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

    public EconomyResponse giveMoney(Player player, double amount)
    {
        return econ.depositPlayer(player,amount);
    }

    private void loadXPVars(ConfigurationSection section)
    {
        assert section != null;

        double nexusHitXP = section.getDouble("Nexus-Hit-Money");
        double killXP = section.getDouble("Player-Kill-Money");
        String gaveXPMessage = section.getString("Gave-Money-Message");
        double[] teamXPs = new double[4];
        teamXPs[0] = section.getDouble("Winning-Team-Money");
        teamXPs[1] = section.getDouble("Second-Place-Team-Money");
        teamXPs[2] = section.getDouble("Third-Place-Team-Money");
        teamXPs[3] = section.getDouble("Last-Place-Team-Money");

        Listeners listeners = new Listeners(this,gaveXPMessage,killXP,nexusHitXP,teamXPs);
        Bukkit.getPluginManager().registerEvents(listeners,this);
    }

    private void loadMultipliers(ConfigurationSection multipliers)
    {
        assert multipliers != null;
        perms = new ArrayList<Perm>();
        //ConfigurationSection multipliers = config.getConfigurationSection("XP-Multipliers");
        //if(multipliers != null)
       // {
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
        //}
    }

    public double checkMultipliers(Player player, double initialXP)
	{
		if(perms.size() > 0)
		{
			//Bukkit.getLogger().info("Greater than zero");
			for(Perm p : perms)
			{
				//Bukkit.getLogger().info("Checking perm: "+p.perm);
				if(player.hasPermission(p.perm))
				{
					//Bukkit.getLogger().info("Player has perm: "+p.perm);
					initialXP = initialXP*p.multiplier;
					break;
				}
			}
		}
		return initialXP;
	}
}
