package com.gmail.nuclearcat1337.anniPro.anniEvents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class GameStartEvent extends Event
{
    private static final HandlerList list = new HandlerList();
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
