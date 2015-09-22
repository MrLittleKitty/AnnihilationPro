package com.gmail.nuclearcat1337.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.kits.KitUtils;
import com.gmail.nuclearcat1337.anniPro.kits.Loadout;
import com.gmail.nuclearcat1337.anniPro.voting.ConfigManager;
import com.gmail.nuclearcat1337.base.ConfigurableKit;

public class Scout extends ConfigurableKit
{
	private ItemStack grapple;
	private String grappleName;
	
	@Override
	protected void setUp()
	{
		grapple = KitUtils.addSoulbound(getIcon().clone());
		ItemMeta m = grapple.getItemMeta();
		m.setDisplayName(grappleName);
		grapple.setItemMeta(m);
	}

	@Override
	protected String getInternalName()
	{
		return "Scout";
	}

	@Override
	protected ItemStack getIcon()
	{
		return new ItemStack(Material.FISHING_ROD);
	}

	@Override
	protected int setDefaults(ConfigurationSection section)
	{
		//section.set("GrappleItemName", ChatColor.AQUA+"Grapple");
		return ConfigManager.setDefaultIfNotSet(section, "GrappleItemName", ChatColor.AQUA+"Grapple");
	}
	
	@Override
	protected void loadKitStuff(ConfigurationSection section)
	{
		super.loadKitStuff(section);
		grappleName = section.getString("GrappleItemName");
	}
	
	private boolean isGrappleItem(ItemStack stack)
	{
		if(stack != null && stack.hasItemMeta() && stack.getItemMeta().hasDisplayName())
		{
			String name = stack.getItemMeta().getDisplayName();
			if(name.contains(this.grappleName) && KitUtils.isSoulbound(stack))
				return true;
		}
		return false;
	}	

	@Override
	protected List<String> getDefaultDescription()
	{
		List<String> l = new ArrayList<String>();
		addToList(l,new String[]
				{
					aqua+"You are the feet.",
					"",
					aqua+"Use your permanent speed",
					aqua+"boost to maneuver around",
					aqua+"the battlefield, and your",
					aqua+"grapple to ascend to new",
					aqua+"heights and gain perspective",
					aqua+"on the battlefield.",
				});
		return l;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void fallDamage(EntityDamageEvent event)
	{
		if(event.getEntity().getType() == EntityType.PLAYER && event.getCause() == DamageCause.FALL)
		{
			Player p = (Player)event.getEntity();
			AnniPlayer pl = AnniPlayer.getPlayer(p.getUniqueId());
			if(pl != null && pl.getKit().equals(this))
			{
				event.setDamage(event.getDamage()/2);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void Grappler(PlayerFishEvent event)
	{
		Player player = event.getPlayer();
		if(event.getState() == State.IN_GROUND)
		{
			AnniPlayer p = AnniPlayer.getPlayer(player.getUniqueId());
			if(p != null && p.getKit().equals(this))
			{
				if(isGrappleItem(player.getItemInHand()))
				{
					//event.getHook().
					Location playerloc = player.getLocation();
					Location loc = event.getHook().getLocation();
					if (playerloc.distance(loc) < 3.0D) 
				        pullPlayerSlightly(player, loc);
				    else 
				        pullEntityToLocation(player, loc);
//					Vector vec = playerloc.toVector();
//					Vector vec2 = loc.toVector();
//					player.setVelocity(vec2.subtract(vec).normalize().multiply(1));
					player.getItemInHand().setDurability((short)0);
				}
			}
		}
	}
	
	private void pullPlayerSlightly(Player p, Location loc)
	{
		if (loc.getY() > p.getLocation().getY())
		{
			p.setVelocity(new Vector(0.0D, 0.25D, 0.0D));
			return;
		}

		Location playerLoc = p.getLocation();

		Vector vector = loc.toVector().subtract(playerLoc.toVector());
		p.setVelocity(vector);
	}

	private void pullEntityToLocation(Entity e, Location loc)
	{
		Location entityLoc = e.getLocation();

		entityLoc.setY(entityLoc.getY() + 0.5D);
		e.teleport(entityLoc);

		double g = -0.08D;
		double d = loc.distance(entityLoc);
		double t = d;
		double v_x = (1.0D + 0.07000000000000001D * t)
				* (loc.getX() - entityLoc.getX()) / t;
		double v_y = (1.0D + 0.03D * t) * (loc.getY() - entityLoc.getY()) / t
				- 0.5D * g * t;
		double v_z = (1.0D + 0.07000000000000001D * t)
				* (loc.getZ() - entityLoc.getZ()) / t;

		Vector v = e.getVelocity();
		v.setX(v_x);
		v.setY(v_y);
		v.setZ(v_z);
		e.setVelocity(v);

		//addNoFall(e, 100);
	}

	@Override
	public void cleanup(Player arg0)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected Loadout getFinalLoadout()
	{
		return new Loadout().addGoldSword().addWoodPick().addWoodAxe().addItem(this.grapple);
	}

//	@Override
//	public void onPlayerSpawn(Player player)
//	{
//		KitUtils.giveTeamArmor(player);
//		player.getInventory().addItem(KitUtils.getGoldSword());
//		player.getInventory().addItem(KitUtils.getWoodPick());
//		player.getInventory().addItem(KitUtils.getWoodAxe());
//		player.getInventory().addItem(this.grapple.clone());
//		player.getInventory().addItem(KitUtils.getNavCompass());
//	}

}
