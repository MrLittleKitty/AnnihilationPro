package com.gmail.nuclearcat1337.kits;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.kits.KitUtils;
import com.gmail.nuclearcat1337.anniPro.kits.Loadout;
import com.gmail.nuclearcat1337.base.SpecialItemKit;

public class Swapper extends SpecialItemKit
{
	@Override
	protected void onInitialize()
	{
	}

	@Override
	protected ItemStack specialItem()
	{
		ItemStack swapper = KitUtils.addSoulbound(new ItemStack(Material.GREEN_RECORD));
		ItemMeta meta = swapper.getItemMeta();
		meta.setDisplayName(getSpecialItemName()+" "+ChatColor.GREEN+"READY");
		swapper.setItemMeta(meta);	
		return swapper;
	}

	@Override
	protected String defaultSpecialItemName()
	{
		return ChatColor.AQUA+"Swapper";
	}

	@Override
	protected boolean isSpecialItem(ItemStack stack)
	{
		if(stack != null && stack.hasItemMeta() && stack.getItemMeta().hasDisplayName())
		{
			String name = stack.getItemMeta().getDisplayName();
			if(name.contains(getSpecialItemName()) && KitUtils.isSoulbound(stack))
				return true;
		}
		return false;
	}	

	//TODO----Apparently this also switches with teammates
	@Override
	protected boolean performSpecialAction(Player player, AnniPlayer p)
	{
		if(p.getTeam() != null)
		{
			Player e = instance.getPlayerInSightTest(player, 15);
			if(e != null)
			{
				AnniPlayer pl = AnniPlayer.getPlayer(e.getUniqueId());
				if(pl != null && !pl.getTeam().equals(p.getTeam()))
				{
					Location playerLoc = player.getLocation().clone();
					Location entityLoc = e.getLocation().clone();
					
					Vector playerLook = playerLoc.getDirection();
					Vector playerVec = playerLoc.toVector();
					Vector entityVec = entityLoc.toVector();
					Vector toVec = playerVec.subtract(entityVec).normalize();
					
					e.teleport(playerLoc.setDirection(playerLook.normalize()));
					player.teleport(entityLoc.setDirection(toVec));
					e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3, 1));
					return true;
				}
			}	
		}
		return false;
	}

	@Override
	protected long getDelayLength()
	{
		return 20000;
	}

	@Override
	protected String getInternalName()
	{
		return "Swapper";
	}

	@Override
	protected ItemStack getIcon()
	{
		return new ItemStack(Material.GREEN_RECORD);
	}

	@Override
	protected List<String> getDefaultDescription()
	{
		List<String> l = new ArrayList<String>();
		addToList(l,new String[]
				{
					aqua+"The swapper is able to",
					aqua+"swap places with a nearby",
					aqua+"enemy every 20 seconds.",
					"",
					aqua+"The enemy that is swapped",
					aqua+"has slowness 2 applied",
					aqua+"for three seconds.",
					"",
					aqua+"The swapper is a team",
					aqua+"based ganking class that",
					aqua+"is best suited for players",
					aqua+"moving in groups and can",
					aqua+"be used to bring enemies",
					aqua+"to a location or bring",
					aqua+"yourself to a more advantageous",
					aqua+"position once held by",
					aqua+"your enemy.",
				});
		return l;
	}

	@Override
	public void cleanup(Player arg0)
	{
	}

//	@Override
//	public void onPlayerSpawn(Player player)
//	{
//		KitUtils.giveTeamArmor(player);
//		player.getInventory().addItem(KitUtils.getWoodSword());
//		player.getInventory().addItem(KitUtils.getWoodPick());
//		player.getInventory().addItem(KitUtils.getWoodAxe());
//		super.giveSpecialItem(player);
//		player.getInventory().addItem(KitUtils.getNavCompass());
//	}
	
	@Override
	protected Loadout getFinalLoadout()
	{
		return new Loadout().addWoodSword().addWoodPick().addWoodAxe().addItem(super.getSpecialItem());
	}

	@Override
	protected boolean useDefaultChecking()
	{
		return true;
	}
	
}
//	private ItemStack swapperItem;
//	private String swapperItemName;
//
//	@Override
//	public void Initialize() 
//	{
//		ConfigurationSection sec = KitConfig.getInstance().getKitSection("Swapper");
//		this.loadKitStuff(sec);
//		this.swapperItemName = sec.getString("SwapperItemName");
//		this.setIcon(new ItemStack(Material.GREEN_RECORD));
//		swapperItem = KitUtils.addSoulbound(new ItemStack(Material.GREEN_RECORD));
//		
//		ItemMeta meta = swapperItem.getItemMeta();
//		meta.setDisplayName(swapperItemName+" "+ChatColor.GREEN+"READY");
//		Delays.getInstance().createNewDelay("Swapper", new StandardItemUpdater(swapperItemName,Material.GREEN_RECORD,new Function<ItemStack,Boolean>(){
//
//			@Override
//			public Boolean apply(ItemStack staack)
//			{
//				return isSwapperItem(staack);
//			}}));
//		swapperItem.setItemMeta(meta);
//		
//	}
//	
//	private boolean isSwapperItem(ItemStack stack)
//	{
//		if(stack != null && stack.hasItemMeta() && stack.getItemMeta().hasDisplayName())
//		{
//			String name = stack.getItemMeta().getDisplayName();
//			if(name.contains(swapperItemName) && KitUtils.isSoulbound(stack))
//				return true;
//		}
//		return false;
//	}
//
//	//TODO-----Remove
//	@Override
//	public boolean hasPermission(Player arg0)
//	{
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public void cleanup(Player player) 
//	{
//
//	}
//
//	@Override
//	public void onPlayerSpawn(Player player)
//	{
//		player.getInventory().addItem(KitUtils.WoodSword);
//		player.getInventory().addItem(KitUtils.WoodPick);
//		player.getInventory().addItem(KitUtils.WoodAxe);
//		player.getInventory().addItem(swapperItem); //Give the record for switching
//	}
//	
//	//TODO---need to implement extra damage when the player has 40% of health or less
//	
//	//checks for player death and increments max health
////	@EventHandler(priority = EventPriority.MONITOR)
////	public void damageListener(final PlayerDeathEvent event)
////	{
////	}

