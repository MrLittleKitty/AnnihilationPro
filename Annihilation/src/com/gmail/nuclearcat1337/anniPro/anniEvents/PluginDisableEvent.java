package com.gmail.nuclearcat1337.anniPro.anniEvents;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PluginDisableEvent extends Event
{
    private static final HandlerList list = new HandlerList();

    public static HandlerList getHandlerList()
    {
        return list;
    }

    @Override
    public HandlerList getHandlers()
    {
        return list;
    }
}
