package com.gmail.nuclearcat1337.anniPro.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DamageControl implements Listener
{
    private static DamageControl instance;
    static  boolean registered;
    public static void register(Plugin plugin)
    {
        if(!registered)
        {
            registered = true;
            instance = new DamageControl();
            Bukkit.getPluginManager().registerEvents(instance,plugin);
        }
    }

    private final Map<UUID, Map<DamageCause, Long>> controllers;

    private DamageControl()
    {
        controllers = new HashMap<>();
    }

    public static void addImmunity(Player player, DamageCause source)
    {
        addImmunity(player.getUniqueId(), source);
    }

    public static void addImmunity(UUID id, DamageCause source)
    {
        instance.getImmunities(id).put(source, Long.MAX_VALUE);
    }

    public static void addTempImmunity(Player player, DamageCause source, long time)
    {
        addTempImmunity(player.getUniqueId(),source,time);
    }

    public static void addTempImmunity(UUID id, DamageCause source, long time)
    {
       instance.getImmunities(id).put(source,time);
    }

    public static void removeImmunity(Player player, DamageCause source)
    {
        removeImmunity(player.getUniqueId(),source);
    }

    public static void removeImmunity(UUID id, DamageCause source)
    {
        instance.getImmunities(id).remove(source);
    }

    @EventHandler(ignoreCancelled = true)
    public void immunityCheck(EntityDamageEvent event)
    {
        if(event.getEntityType() == EntityType.PLAYER)
        {
            if(hasImmunity(event.getEntity().getUniqueId(),event.getCause()))
                event.setCancelled(true);
        }
    }

    private boolean hasImmunity(UUID id, DamageCause source)
    {
        if(!controllers.containsKey(id))
            return false;
        Map<DamageCause, Long> imm = controllers.get(id);
        if(!imm.containsKey(source))
            return false;
        if(imm.get(source) <= System.currentTimeMillis())
        {
            imm.remove(source);
            return false;
        }
        return true;
    }


    public Map<DamageCause, Long> getImmunities(UUID id)
    {
        Map<DamageCause, Long> immunities = controllers.get(id);
        if(immunities == null)
        {
            immunities = new HashMap<>(2);
            controllers.put(id,immunities);
        }
        return immunities;
    }

//    private class Control
//    {
//        private final DamageSource source;
//        private final long expiration;
//
//        public  Control(DamageSource source, long expiration)
//        {
//            this.source = source;
//            this.expiration = expiration;
//        }
//    }
}
