package com.gmail.nuclearcat1337.anniPro.anniEvents;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.anniGame.Nexus;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class NexusHitEvent extends Event implements Cancellable
{
    private  static  final HandlerList list = new HandlerList();

    private  AnniPlayer player;
    private final Nexus nexus;
	private int damage;
    private boolean cancelled;

	public NexusHitEvent(AnniPlayer player, Nexus hitNexus, int damage)
	{
		this.player = player;
		this.nexus = hitNexus;
		this.damage = damage;
		
	}

    public AnniPlayer getPlayer()
    {
        return player;
    }
	
	public Nexus getHitNexus()
	{
		return nexus;
	}
	
	public int getDamage()
	{
		return damage;
	}
	
	public void setDamage(int damage)
	{
		this.damage = damage;
	}

	public boolean willKillTeam()
	{
		return (nexus.Team.getHealth()-damage) <= 0;
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
}
