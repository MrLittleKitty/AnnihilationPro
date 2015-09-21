package com.gmail.nuclearcat1337.anniPro.anniEvents;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.anniMap.RegeneratingBlock;

public class ResourceBreakEvent extends Event implements Cancellable
{
    private static final HandlerList list = new HandlerList();
	private RegeneratingBlock resource;
	private int xp;
	private ItemStack[] endresult;
    private boolean cancelled;

    public AnniPlayer getPlayer()
    {
        return player;
    }

    private AnniPlayer player;

    @Override
    public boolean isCancelled()
    {
        return cancelled;
    }

    @Override
    public void setCancelled(final boolean b)
    {
        cancelled = b;
    }

    public ResourceBreakEvent(AnniPlayer player, RegeneratingBlock resource, int XP, ItemStack... endResult)
	{
	    this.player = player;
		this.resource = resource;
		this.xp = XP;
		this.endresult = endResult;
	}
	
	public RegeneratingBlock getResource()
	{
		return resource;
	}
	
	public int getXP()
	{
		return xp;
	}
	
	public void setXP(int XP)
	{
		xp = XP;
	}
	
	public ItemStack[] getProducts()
	{
		return endresult;
	}
	
	public void setProducts(ItemStack[] results)
	{
		endresult = results;
	}

    @Override
    public HandlerList getHandlers()
    {
        return list;
    }

    public static HandlerList getHandlerList()
    {
        return list;
    }
}
