package com.gmail.nuclearcat1337.base;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.voting.ConfigManager;
import com.google.common.base.Function;


public abstract class SpecialItemKit extends ConfigurableKit
{
	private ItemStack specialItem;
	private String specialItemName;
	protected Delays delays;
	
	@Override
	protected void setUp()
	{
		delays = Delays.getInstance();
		specialItem = specialItem();
		if(getDelayLength() > 0 && useDefaultChecking())
		{
			delays.createNewDelay(getInternalName(), new StandardItemUpdater(getSpecialItemName(),specialItem.getType(),new Function<ItemStack,Boolean>(){
				@Override
				public Boolean apply(ItemStack stack)
				{
					return isSpecialItem(stack);
				}}));
		}
		onInitialize();
	}
	
	protected abstract void onInitialize();
	
	//getSpecialItem() has a guarantee that the special item name 
	//has a value
	protected abstract ItemStack specialItem();
	protected abstract String defaultSpecialItemName();
	protected abstract boolean isSpecialItem(ItemStack stack);
	protected abstract boolean performSpecialAction(Player player, AnniPlayer p);
	protected abstract long getDelayLength();
	protected abstract boolean useDefaultChecking();
	
	public ItemStack getSpecialItem()
	{
		return specialItem;
	}
	
	//This will be called before setUp
	@Override
	protected void loadKitStuff(ConfigurationSection section)
	{
		super.loadKitStuff(section);
		specialItemName = section.getString("SpecialItemName");
	}
	
	@Override
	protected int setDefaults(ConfigurationSection section)
	{
		//section.set("SpecialItemName", defaultSpecialItemName());
		return ConfigManager.setDefaultIfNotSet(section, "SpecialItemName", defaultSpecialItemName());
	}
	
	public String getSpecialItemName()
	{
		return specialItemName;
	}
	
//	public void giveSpecialItem(Player player)
//	{
//		if(player != null)
//			player.getInventory().addItem(specialItem.clone());
//	}
	
//	public ItemStack getSpecialItem()
//	{
//		return this.specialItem.clone();
//	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void specialItemActionCheck(final PlayerInteractEvent event)
	{
		if(useDefaultChecking())
		{
			if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				if(event.getItem() != null && event.getItem().getType() == specialItem.getType())
				{
					AnniPlayer p = AnniPlayer.getPlayer(event.getPlayer().getUniqueId());
					if(p != null && p.getKit().equals(this) && isSpecialItem(event.getItem()))
					{
						event.setCancelled(true);
						if(!delays.hasActiveDelay(event.getPlayer(), getInternalName()))
						{
							//Do firestorm
							if(performSpecialAction(event.getPlayer(),p) && getDelayLength() > 0)
								delays.addDelay(event.getPlayer(), System.currentTimeMillis()+getDelayLength(), getInternalName());
						}
					}
				}
			}
		}
	}
	
}
