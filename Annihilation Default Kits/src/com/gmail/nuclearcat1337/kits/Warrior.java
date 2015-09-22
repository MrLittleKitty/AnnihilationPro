package com.gmail.nuclearcat1337.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.kits.Loadout;
import com.gmail.nuclearcat1337.base.ConfigurableKit;

public class Warrior extends ConfigurableKit
{
	//private final ItemStack Sword = KitUtils.addEnchant(KitUtils.getWoodSword(), Enchantment.KNOCKBACK, 1);
	
	@Override
	protected void setUp()
	{
		
	}

	@Override
	protected String getInternalName()
	{
		return "Warrior";
	}

	@Override
	protected ItemStack getIcon()
	{
		return new ItemStack(Material.STONE_SWORD);
	}
	
	@Override
	protected int setDefaults(ConfigurationSection section)
	{
		return 0;
	}

	@Override
	protected List<String> getDefaultDescription()
	{
		List<String> l = new ArrayList<String>();
		addToList(l,new String[]
				{
					aqua+"You are the sword.", 
					"",
					aqua+"You deal +1 damage with",
					aqua+"any melee weapon.", 
					"", 
					aqua+"Spawn with a sword and",
					aqua+"a health potion which",
					aqua+"enable you to immediately",
					aqua+"move on the enemy and",
					aqua+"attack!",
					"",
					aqua+"If you do not have a good",
					aqua+"support back at base gathering",
					aqua+"better gear for you, you",
					aqua+"will be useless in the",
					aqua+"late game."
				});
		return l;
	}

//	@Override
//	public String getName()
//	{
//		return "Warrior";
//	}
//
//	@Override
//	public IconPackage getIconPackage()
//	{
//		return new IconPackage(new ItemStack(Material.STONE_SWORD), 
//								new String[]{
//												aqua+"You are the sword.", 
//												"",
//												aqua+"You deal +1 damage with",
//												aqua+"any melee weapon.", 
//												"", 
//												aqua+"Spawn with a sword and",
//												aqua+"a health potion which",
//												aqua+"enable you to immediately",
//												aqua+"move on the enemy and",
//												aqua+"attack!",
//												"",
//												aqua+"If you do not have a good",
//												aqua+"support back at base gathering",
//												aqua+"better gear for you, you",
//												aqua+"will be useless in the",
//												aqua+"late game."
//											});
//	}

//	@Override
//	public void onPlayerSpawn(Player player)
//	{
//		KitUtils.giveTeamArmor(player);
//		player.getInventory().addItem(Sword);
//		player.getInventory().addItem(KitUtils.getWoodPick());
//		player.getInventory().addItem(KitUtils.getWoodAxe());
//		player.getInventory().addItem(KitUtils.getHealthPotion1());
//		player.getInventory().addItem(KitUtils.getNavCompass());
//	}
	
	@Override
	protected Loadout getFinalLoadout()
	{
		return new Loadout().addSoulboundEnchantedItem(new ItemStack(Material.WOOD_SWORD), Enchantment.KNOCKBACK, 1).addWoodPick().addWoodAxe().addHealthPotion1();
	}

	@Override
	public void cleanup(Player player)
	{
		
	}
	
	//Adds the +1 melee damage. May need to be changed to work just for melee WEAPONS and not every melee attack. idk.
	@EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
	public void damageListener(final EntityDamageByEntityEvent event)
	{
		Entity one = event.getDamager();
		if(one.getType() == EntityType.PLAYER)
		{
			Player damager = (Player)one;
			AnniPlayer d = AnniPlayer.getPlayer(damager.getUniqueId());
			if(d != null && d.getKit().equals(this))
				event.setDamage(event.getDamage()+1);
		}
	}
}

