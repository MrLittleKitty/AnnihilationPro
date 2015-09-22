package com.gmail.nuclearcat1337.kits;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.projectiles.ProjectileSource;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.kits.Loadout;
import com.gmail.nuclearcat1337.base.ConfigurableKit;

public class Archer extends ConfigurableKit
{
//	private final ItemStack Bow = KitUtils.addEnchant(KitUtils.getBow(),Enchantment.ARROW_KNOCKBACK,1);
//	private final ItemStack Arrows = KitUtils.addSoulbound(new ItemStack(Material.ARROW,16));
	
	@Override
	protected void setUp()
	{
		ShapelessRecipe recipe = new ShapelessRecipe(new ItemStack(Material.ARROW,3)).addIngredient(Material.FLINT).addIngredient(Material.STICK);
		Bukkit.addRecipe(recipe);	
	}

	@Override
	protected String getInternalName()
	{
		return "Archer";
	}

	@Override
	protected ItemStack getIcon()
	{
		return new ItemStack(Material.BOW);
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
					aqua+"You are the rain.", 
					"",
					aqua+"Obliterate your enemies",
					aqua+"from a distance.", 
					"", 
					aqua+"You deal +1 damage with",
					aqua+"any bow and you have the",
					aqua+"ability to create arrows",
					aqua+"from flint and stucks.",
					"",
					aqua+"Just put a flint on top",
					aqua+"of a stick in your crafting",
					aqua+"interface to create 3",
					aqua+"arrows!",
				});
		return l;
	}
	

//	@Override
//	public String getName()
//	{
//		return "Archer";
//	}
//
//	@Override
//	public IconPackage getIconPackage()
//	{
//		return new IconPackage(new ItemStack(Material.BOW), 
//								new String[]{
//												aqua+"You are the rain.", 
//												"",
//												aqua+"Obliterate your enemies",
//												aqua+"from a distance.", 
//												"", 
//												aqua+"You deal +1 damage with",
//												aqua+"any bow and you have the",
//												aqua+"ability to create arrows",
//												aqua+"from flint and stucks.",
//												"",
//												aqua+"Just put a flint on top",
//												aqua+"of a stick in your crafting",
//												aqua+"interface to create 3",
//												aqua+"arrows!",
//											});
//	}

//	@Override
//	public void onPlayerSpawn(Player player)
//	{
//		KitUtils.giveTeamArmor(player);
//		player.getInventory().addItem(KitUtils.getWoodSword());
//		player.getInventory().addItem(KitUtils.getWoodPick());
//		player.getInventory().addItem(KitUtils.getWoodAxe());
//		player.getInventory().addItem(KitUtils.getWoodShovel());
//		player.getInventory().addItem(Bow.clone());
//		player.getInventory().addItem(Arrows.clone());
//		player.getInventory().addItem(KitUtils.getHealthPotion1());
//		player.getInventory().addItem(KitUtils.getNavCompass());
//	}
	
	@Override
	protected Loadout getFinalLoadout()
	{
		return new Loadout().addWoodSword().addWoodPick().addWoodAxe().addWoodShovel().addSoulboundEnchantedItem(new ItemStack(Material.BOW),Enchantment.ARROW_DAMAGE, 1)
				.addSoulboundItem(new ItemStack(Material.ARROW,16)).addHealthPotion1();
	}

	@Override
	public void cleanup(Player player)
	{
		
	}

	//Stops non-archers from crafting arrows using the archer recipe
	@EventHandler(priority = EventPriority.HIGHEST)
	public void arrowCraftingStopper(CraftItemEvent event)
	{
		if(event.getRecipe().getResult().getType() == Material.ARROW && event.getRecipe().getResult().getAmount() == 3)
		{
			AnniPlayer player = AnniPlayer.getPlayer(event.getWhoClicked().getUniqueId());
			if(player != null && (player.getKit() == null || !player.getKit().equals(this)))
				event.setCancelled(true);
		}
	}
	
	//Adds the +1 arrow damage
	@EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
	public void damageListener(final EntityDamageByEntityEvent event)
	{
		if(event.getDamager().getType() == EntityType.ARROW)
		{
			ProjectileSource s = ((Projectile)event.getDamager()).getShooter();
			if(s instanceof Player)
			{
				AnniPlayer shooter = AnniPlayer.getPlayer(((Player) s).getUniqueId());
				if(shooter != null && shooter.getKit() != null && shooter.getKit().equals(this))
					event.setDamage(event.getDamage()+1);
			}
		}
	}
}

