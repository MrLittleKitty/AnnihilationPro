package com.gmail.nuclearcat1337.kits;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.kits.KitUtils;
import com.gmail.nuclearcat1337.anniPro.kits.Loadout;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.anniPro.voting.ConfigManager;
import com.gmail.nuclearcat1337.base.ConfigurableKit;
import com.gmail.nuclearcat1337.base.DelayUpdate;
import com.gmail.nuclearcat1337.base.Delays;
import com.gmail.nuclearcat1337.base.Direction;


public class Scorpio extends ConfigurableKit
{
	private ItemStack hookItem;
	private String hookItemName;
	
	@Override
	protected void setUp()
	{
		hookItem = KitUtils.addSoulbound(getIcon().clone());
		ItemMeta m = hookItem.getItemMeta();
		m.setDisplayName(hookItemName);
		hookItem.setItemMeta(m);
		Delays.getInstance().createNewDelay(getInternalName(), new DelayUpdate(){
			@Override
			public void update(Player player, int secondsLeft)
			{
				//Do nothing	
			}});
	}
	
	private boolean isHookItem(ItemStack stack)
	{
		if(stack != null && stack.hasItemMeta() && stack.getItemMeta().hasDisplayName())
		{
			String name = stack.getItemMeta().getDisplayName();
			if(name.contains(this.hookItemName) && KitUtils.isSoulbound(stack))
				return true;
		}
		return false;
	}	

	@Override
	protected String getInternalName()
	{
		return "Scorpio";
	}

	@Override
	protected ItemStack getIcon()
	{
		return new ItemStack(Material.NETHER_STAR);
	}

	@Override
	protected int setDefaults(ConfigurationSection section)
	{
		//section.set("HookItemName", "Hook");
		return ConfigManager.setDefaultIfNotSet(section, "HookItemName", "Hook");
	}
	
	@Override
	protected void loadKitStuff(ConfigurationSection section)
	{
		super.loadKitStuff(section);
		hookItemName = section.getString("HookItemName");
	}

	@Override
	protected List<String> getDefaultDescription()
	{
		List<String> l = new ArrayList<String>();
		addToList(l,new String[]
				{
					aqua+"You are the hook.",
					"",
					aqua+"Use your hook to quickly",
					aqua+"reach allies by pulling",
					aqua+"yourself to them, or use",
					aqua+"it on enemies to pull",
					aqua+"the enemy to you.",
					
				});
		return l;
	}

	@Override
	public void cleanup(Player arg0)
	{

	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void specialItemActionCheck(final PlayerInteractEvent event)
	{
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			Player player = event.getPlayer();
			AnniPlayer pl = AnniPlayer.getPlayer(player.getUniqueId());
			if(pl != null && pl.getKit().equals(this))
			{
				if(this.isHookItem(player.getItemInHand()))
				{
					if(!Delays.getInstance().hasActiveDelay(player, this.getInternalName()))
					{
						Delays.getInstance().addDelay(player, System.currentTimeMillis()+5000, this.getInternalName());//kits.addDelay(player.getName(), DelayType.SCORPIO, 10, TimeUnit.SECONDS);
						Item item = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.NETHER_STAR,1));
						item.setPickupDelay(Integer.MAX_VALUE);
						item.setVelocity(player.getEyeLocation().getDirection().multiply(2));
						Bukkit.getScheduler().scheduleSyncDelayedTask(AnnihilationMain.getInstance(), new HookTracer(item,pl,90,this.getName()), 1);
					}
				}
			}
		}
	}
	
//	@Override
//	public void onPlayerSpawn(Player player)
//	{
//		KitUtils.giveTeamArmor(player);
//		player.getInventory().addItem(KitUtils.getStoneSword());
//		player.getInventory().addItem(KitUtils.getWoodPick());
//		player.getInventory().addItem(KitUtils.getWoodAxe());
//		player.getInventory().addItem(this.hookItem.clone());
//		player.getInventory().addItem(KitUtils.getNavCompass());
//	}
	
	@Override
	protected Loadout getFinalLoadout()
	{
		return new Loadout().addStoneSword().addWoodPick().addWoodAxe().addItem(this.hookItem);
	}
	
	private class HookTracer implements Runnable
	{
		private final String scorpioName;
		private final AnniPlayer owner;
		private final Item item;
		private final int maxTicks;
		public HookTracer(Item item, AnniPlayer owner, int maxTicks, String scorpioName)
		{
			this.item = item;
			this.owner = owner;
			this.maxTicks = maxTicks;
			this.scorpioName = scorpioName;
		}
		
		@Override
		public void run()
		{
			//maxTicks--;
			if(maxTicks <= 0 || !owner.getKit().getName().equals(scorpioName))
			{
				item.remove();
				return;
			}
			
			for(Entity entity : item.getNearbyEntities(1, 1, 1))
			{
				if(entity.getType() == EntityType.PLAYER)
				{
					Player target = (Player)entity;
					AnniPlayer p = AnniPlayer.getPlayer(target.getUniqueId());
					if(p != null && !p.equals(owner))
					{
						Player user = owner.getPlayer();
						if(user != null)
						{
							if(owner.getTeam() == p.getTeam())
							{			
								Location loc1 = user.getLocation();
								Location loc2 = target.getLocation();
								if(loc2.getY() >= loc1.getY())
								{
									target.getWorld().playSound(target.getLocation(), Sound.DOOR_OPEN, 1F, 0.1F);
									user.getWorld().playSound(user.getLocation(), Sound.DOOR_OPEN, 1F, 0.1F);
									loc2.setY(loc1.getY());
									Vector vec = loc2.toVector().subtract(loc1.toVector()).setY(.08D).multiply(7);
									user.setVelocity(vec);
								}
							}
							else
							{
								target.getWorld().playSound(target.getLocation(), Sound.DOOR_OPEN, 1F, 0.1F);
								user.getWorld().playSound(user.getLocation(), Sound.DOOR_OPEN, 1F, 0.1F);
								//plugin.getKits().stopNextFallDamage(target.getName());
								Location loc = user.getLocation();
								Location tele;
								Direction dec = Direction.getDirection(loc.getDirection());
								if(dec == Direction.North)
									tele = loc.getBlock().getRelative(BlockFace.NORTH).getLocation();
								else if(dec == Direction.South)
									tele = loc.getBlock().getRelative(BlockFace.SOUTH).getLocation();
								else if(dec == Direction.East)
									tele = loc.getBlock().getRelative(BlockFace.EAST).getLocation();
								else if(dec == Direction.West)
									tele = loc.getBlock().getRelative(BlockFace.WEST).getLocation();
								else if(dec == Direction.NorthWest)
									tele = loc.getBlock().getRelative(BlockFace.NORTH_WEST).getLocation();
								else if(dec == Direction.NorthEast)
									tele = loc.getBlock().getRelative(BlockFace.NORTH_EAST).getLocation();
								else if(dec == Direction.SouthEast)
									tele = loc.getBlock().getRelative(BlockFace.SOUTH_EAST).getLocation();
								else tele = loc.getBlock().getRelative(BlockFace.SOUTH_WEST).getLocation();
								tele.setPitch(0);
								tele.setYaw(loc.getYaw()+180);
								target.teleport(tele);
							}
						}
						item.remove();
						return;
					}
				}
			}
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(AnnihilationMain.getInstance(), new HookTracer(item,owner,maxTicks-1,scorpioName), 1);
		}
	}
}
