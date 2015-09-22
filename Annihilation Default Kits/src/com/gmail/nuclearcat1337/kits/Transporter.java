package com.gmail.nuclearcat1337.kits;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniTeam;
import com.gmail.nuclearcat1337.anniPro.anniGame.Game;
import com.gmail.nuclearcat1337.anniPro.anniGame.Nexus;
import com.gmail.nuclearcat1337.anniPro.kits.KitUtils;
import com.gmail.nuclearcat1337.anniPro.kits.Loadout;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.anniPro.utils.Loc;
import com.gmail.nuclearcat1337.anniPro.voting.ConfigManager;
import com.gmail.nuclearcat1337.base.ConfigurableKit;

public class Transporter extends ConfigurableKit
{
	private ItemStack transporterItem;
	private String transporterItemName;
	private Map<UUID,Teleporter> teleporters;
	
	public void setBlockOwner(Block b, UUID player)
	{
		MetadataValue val = new FixedMetadataValue(AnnihilationMain.getInstance(), player.toString());
		b.setMetadata("Owner", val);
	}
	
	public UUID getBlocksOwner(Block b)
	{
		List<MetadataValue> list = b.getMetadata("Owner");
		if(list == null || list.isEmpty())
			return null;
		return UUID.fromString(list.get(0).asString());
	}
	
	@Override
	protected void setUp()
	{
		teleporters = new HashMap<UUID,Teleporter>();
		transporterItem = KitUtils.addSoulbound(new ItemStack(Material.QUARTZ));
		ItemMeta m = transporterItem.getItemMeta();
		m.setDisplayName(transporterItemName);
		transporterItem.setItemMeta(m);
	}

	@Override
	protected String getInternalName()
	{
		return "Transporter";
	}

	@Override
	protected ItemStack getIcon()
	{
		return new ItemStack(Material.QUARTZ);
	}

	@Override
	protected int setDefaults(ConfigurationSection section)
	{
		//section.set("TransporterItemName", aqua+"Portal Maker");
		return ConfigManager.setDefaultIfNotSet(section, "TransporterItemName", aqua+"Portal Maker");
	}
	
	@Override
	protected void loadKitStuff(ConfigurationSection section)
	{
		super.loadKitStuff(section);
		transporterItemName = section.getString("TransporterItemName");
	}

	@Override
	protected List<String> getDefaultDescription()
	{
		List<String> l = new ArrayList<String>();
		addToList(l,new String[]
			{
				aqua+"You are the snake.", 
				"",
				aqua+"Link two parts of the",
				aqua+"battlefield with portals",
				aqua+"that your team can use",
				aqua+"to get the one up on the",
				aqua+"enemy.",
				"",
				aqua+"Your portals are removed",
				aqua+"when you die.",
			});
		return l;
	}

	@Override
	public void cleanup(Player player)
	{
		Teleporter tele = this.teleporters.remove(player.getUniqueId());
		if(tele != null)
			tele.clear();
	}

//	@Override
//	public void onPlayerSpawn(Player player)
//	{
//		KitUtils.giveTeamArmor(player);
//		player.getInventory().addItem(KitUtils.getStoneSword());
//		player.getInventory().addItem(KitUtils.getWoodPick());
//		player.getInventory().addItem(KitUtils.getWoodAxe());
//		player.getInventory().addItem(this.transporterItem.clone());
//		player.getInventory().addItem(KitUtils.getNavCompass());
//	}
	
	@Override
	protected Loadout getFinalLoadout()
	{
		return new Loadout().addStoneSword().addWoodPick().addWoodAxe().addItem(this.transporterItem);
	}
	
	private boolean isTransporterItem(ItemStack stack)
	{
		if(stack != null && stack.hasItemMeta() && stack.getItemMeta().hasDisplayName())
		{
			String name = stack.getItemMeta().getDisplayName();
			if(name.contains(this.transporterItemName) && KitUtils.isSoulbound(stack))
				return true;
		}
		return false;
	}
	
	private Location getMiddle(Location loc)
	{
		Location k = loc.clone();
		k.setX(k.getBlockX()+0.5);
		//k.setY(k.getBlockY()+0.5);
		k.setZ(k.getBlockZ()+0.5);
		return k;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
	public void Teleport(PlayerToggleSneakEvent event)
	{
		if(event.isSneaking())
		{
			Player player = event.getPlayer();
			Block b = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
			if(b.getType() == Material.QUARTZ_ORE)
			{
				UUID owner = getBlocksOwner(b);
				if(owner != null)
				{
					AnniPlayer p = AnniPlayer.getPlayer(player.getUniqueId());
					if(p != null)
					{
						Teleporter tele = teleporters.get(owner);
						if(tele.isLinked() && tele.getOwner().getTeam() == p.getTeam() && tele.canUse())
						{
							Location loc;
							//if(new Loc(b.getLocation()).isEqual(tele.getLoc1()))
							if(tele.getLoc1().equals(b.getLocation()))
								loc = tele.getLoc2().toLocation();
							else
								loc = tele.getLoc1().toLocation();
							loc.setY(loc.getY()+1);
							player.teleport(this.getMiddle(loc));
							loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
							tele.getLoc1().toLocation().getWorld().playSound(tele.getLoc1().toLocation(), Sound.ENDERMAN_TELEPORT, 1F,(float)Math.random());
							tele.getLoc2().toLocation().getWorld().playSound(tele.getLoc2().toLocation(), Sound.ENDERMAN_TELEPORT, 1F,(float)Math.random());
							tele.delay();
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void specialItemActionCheck(final PlayerInteractEvent event)
	{
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)
		{
			if(event.getClickedBlock().getType() != Material.QUARTZ_ORE && event.getItem() != null && event.getItem().getType() == this.transporterItem.getType() && event.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				AnniPlayer p = AnniPlayer.getPlayer(event.getPlayer().getUniqueId());
				if(p != null && p.getKit().equals(this) && isTransporterItem(event.getItem()) && Game.getGameMap() != null && 
						Game.getGameMap().getAreas().getArea(new Loc(event.getClickedBlock().getLocation(),false)) == null)
				{
					event.setCancelled(true);
					//------------------------------------------
					Block b = event.getClickedBlock();
					Block other = b.getRelative(BlockFace.UP);
					Block other2 = other.getRelative(BlockFace.UP);
					if(other.getType() == Material.AIR &&  other2.getType() == Material.AIR && canPlace(b.getType()))
					{
						for(AnniTeam t : AnniTeam.Teams)
						{
							//Loc loc = new Loc(b.getLocation());
							Nexus n = t.getNexus();
							if(n != null)
							{
								//if(loc.isEqual(n.getLocation()))
								if(n.getLocation().equals(b.getLocation()))
								{
									event.getPlayer().sendMessage(ChatColor.RED+"You cannot place a portal on a nexus");
									event.setCancelled(true);
									return;
								}
							}
						}
						
						Teleporter tele = this.teleporters.get(event.getPlayer().getUniqueId());
						if(tele == null)
						{
							tele = new Teleporter(p);
							this.teleporters.put(event.getPlayer().getUniqueId(), tele);
						}
						
						if(tele.isLinked())
						{
							tele.clear();
							tele.setLoc1(b.getLocation(), b.getState());
						}
						else if(tele.hasLoc1())
							tele.setLoc2(b.getLocation(), b.getState());
						else
							tele.setLoc1(b.getLocation(), b.getState());
						
						b.setType(Material.QUARTZ_ORE);
						setBlockOwner(b, p.getID());
						event.getPlayer().playSound(b.getLocation(), Sound.BLAZE_HIT, 1F, 1.9F);
						event.setCancelled(true);
					}
					else event.getPlayer().sendMessage(ChatColor.RED+"You cannot place that here.");
					//------------------------------------------
				}
			}
			else if(event.getClickedBlock().getType() == Material.QUARTZ_ORE && event.getPlayer().getGameMode() != GameMode.CREATIVE)
			{
				AnniPlayer p = AnniPlayer.getPlayer(event.getPlayer().getUniqueId());
				if(p != null)
				{
					event.setCancelled(true);
					UUID owner = getBlocksOwner(event.getClickedBlock());
					if(owner != null)
					{
						Teleporter tele = this.teleporters.get(owner);
						if(tele != null)
						{
							if(owner.equals(event.getPlayer().getUniqueId()))
							{
								tele.clear();
								return;
							}
							else if(p.getTeam() != tele.getOwner().getTeam())
							{
								tele.clear();
								tele.getOwner().sendMessage(ChatColor.AQUA+"Your teleporter was broken by "+p.getTeam().getColor()+p.getName());
							}
						}
					}
				}
			}
		}
	}
	
	private boolean canPlace(Material type)
	{
		//This tells if a transporter block can be placed at this type of block
		switch(type)
		{
			default:
				return true;
			
			case BEDROCK:
			case OBSIDIAN:
			case CHEST:
			case TRAPPED_CHEST:
			case FURNACE:
			case DISPENSER:
			case DROPPER:
			case WORKBENCH:
			case BURNING_FURNACE:
			case HOPPER:
			case BEACON:
			case ANVIL:
			case SIGN_POST:
			case WALL_SIGN:
			case ENDER_PORTAL:
			case QUARTZ_ORE:
				return false;
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
	public void MoveListeners(PlayerMoveEvent event)
	{
		///block under your feet
		Block to = event.getTo().getBlock().getRelative(BlockFace.DOWN);
		if(to.getType() == Material.QUARTZ_ORE)
		{
			Location x = event.getTo();
			Location y = event.getFrom();
			if(x.getBlockX() != y.getBlockX() || x.getBlockY() != y.getBlockY() || x.getBlockZ() != y.getBlockZ())
			{
				AnniPlayer user = AnniPlayer.getPlayer(event.getPlayer().getUniqueId());
				UUID owner = getBlocksOwner(to);
				if(owner != null && user != null)
				{
					Teleporter tele = this.teleporters.get(owner);
					if(tele != null && tele.isLinked() && tele.getOwner().getTeam() == user.getTeam())
					{
						event.getPlayer().sendMessage(ChatColor.AQUA+"This is a teleporter owned by "+ChatColor.WHITE+tele.getOwner().getName()+ChatColor.AQUA+", Sneak to go through it.");
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
	public void TeleporterProtect(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		Block b = event.getBlock();
		if(b.getType() == Material.QUARTZ_ORE)
		{
			event.setCancelled(true);
			AnniPlayer p = AnniPlayer.getPlayer(player.getUniqueId());
			if(p != null)
			{
				UUID owner = getBlocksOwner(b);
				if(owner == null)
					return;
				
				Teleporter tele = teleporters.get(owner);
				if(p.getID().equals(owner))
				{
					tele.clear();
				}
				else if(p.getTeam() != tele.getOwner().getTeam())
				{
					tele.clear();
					tele.getOwner().sendMessage(ChatColor.AQUA+"Your teleporter was broken by "+p.getTeam().getColor()+p.getName());
				}
			}
		}
	}
}

class Teleporter
{
	private AnniPlayer owner;
	private Loc loc1;
	private BlockState state1;
	private Loc loc2;
	private BlockState state2;
	private long nextUse;
	
	public Teleporter(AnniPlayer owner)
	{
		this.owner = owner;
		loc1 = null;
		loc2 = null;
		this.nextUse = System.currentTimeMillis();
	}
	
	public void setLoc1(Loc loc, BlockState old)
	{
		this.loc1 = loc;
		this.state1 = old;
	}
	
	public void setLoc1(Location loc, BlockState old)
	{
		this.loc1 = new Loc(loc,false);
		this.state1 = old;
	}
	
	public void setLoc2(Loc loc, BlockState old)
	{
		this.loc2 = loc;	
		this.state2 = old;
	}
	
	public void setLoc2(Location loc, BlockState old)
	{
		this.loc2 = new Loc(loc,false);
		this.state2 = old;
	}
	
	public Loc getLoc1()
	{
		return this.loc1;
	}
	
	public Loc getLoc2()
	{
		return this.loc2;
	}
	
	public void clear()
	{
		this.loc1 = null;
		this.loc2 = null;
		nextUse = System.currentTimeMillis();
		if(state1 != null)
		{
			World w = state1.getWorld();
			w.playEffect(state1.getLocation(), Effect.STEP_SOUND, 153);
			//w.playSound(state1.getLocation(), sound, volume, pitch) The sound
			this.state1.update(true);
		}
		if(state2 != null)
		{
			World w = state2.getWorld();
			w.playEffect(state2.getLocation(), Effect.STEP_SOUND, 153);
			//w.playSound(state2.getLocation(), sound, volume, pitch) The sound
			this.state2.update(true);
		}
	}
	
	public void delay()
	{
		this.nextUse = System.currentTimeMillis()+TimeUnit.MILLISECONDS.convert(5000, TimeUnit.MILLISECONDS); //Added this incase I ever want to change the delay for the transporter.
	}
	
	public boolean canUse()
	{
		return System.currentTimeMillis() >= this.nextUse;
	}
	
	public boolean hasLoc1()
	{
		return this.loc1 != null;
	}
	
	public AnniPlayer getOwner()
	{
		return this.owner;
	}
	
	public boolean isLinked()
	{
		return this.loc1 != null && this.loc2 != null;
	}
}
