package com.gmail.nuclearcat1337.anniPro.anniGame;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.Plugin;

public class ArmorStandListener implements Listener
{
    public ArmorStandListener(Plugin p)
    {
        Bukkit.getPluginManager().registerEvents(this,p);
    }

    @EventHandler
    public void armorStandStop(EntitySpawnEvent event)
    {
        if(event.getEntityType() == EntityType.ARMOR_STAND)
            event.setCancelled(true);
    }
}
