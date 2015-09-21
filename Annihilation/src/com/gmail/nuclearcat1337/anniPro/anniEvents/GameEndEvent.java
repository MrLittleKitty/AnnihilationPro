package com.gmail.nuclearcat1337.anniPro.anniEvents;

import com.gmail.nuclearcat1337.anniPro.anniGame.AnniTeam;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class GameEndEvent extends Event
{
    private static final HandlerList list = new HandlerList();

	private final AnniTeam winner;
	
	public GameEndEvent(AnniTeam winningTeam)
	{
		this.winner = winningTeam;
	}
	
	public AnniTeam getWinningTeam()
	{
		return winner;
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
