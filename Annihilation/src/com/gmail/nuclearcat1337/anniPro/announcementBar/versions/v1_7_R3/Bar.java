package com.gmail.nuclearcat1337.anniPro.announcementBar.versions.v1_7_R3;

import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.sun.corba.se.impl.ior.ObjectAdapterIdArray;
import net.minecraft.server.v1_7_R3.DataWatcher;
import net.minecraft.server.v1_7_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_7_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.UUID;

public class Bar implements com.gmail.nuclearcat1337.anniPro.announcementBar.Bar, Listener
{
    private HashMap<UUID, IDragon> bars;

    public Bar()
    {
        bars = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, AnnihilationMain.getInstance());
    }

    @Override
    public void sendToPlayer(final Player player, final String message, final float percentOfTotal)
    {

        for(Player p : Bukkit.getOnlinePlayers())
        {
            IDragon drag;
            if(hasBar(p))
                drag = getDragon(p, "");
            else
                drag = getDragon(p, message);
            drag.setName(message);

            drag.setHealth((int)percentOfTotal);
            sendDragon(drag, p);
        }
    }

    private String cleanMessage(String message)
    {
        if (message.length() > 64)
            message = message.substring(0, 63);

        return message;
    }

    private void sendDragon(IDragon dragon, Player player)
    {
        Util.sendPacket(player, dragon.getMetaPacket(dragon.getWatcher()));
        Util.sendPacket(player, dragon.getTeleportPacket(getDragonLocation(player.getLocation())));
    }

    private IDragon getDragon(Player player, String message)
    {
        if (hasBar(player))
            return bars.get(player.getUniqueId());
        else
            return addDragon(player, cleanMessage(message));
    }

    private IDragon addDragon(Player player, String message)
    {
        IDragon dragon = new Dragon(message, getDragonLocation(player.getLocation()));
        Util.sendPacket(player, dragon.getSpawnPacket());

        bars.put(player.getUniqueId(), dragon);

        return dragon;
    }

    private IDragon addDragon(Player player, Location loc, String message)
    {
        IDragon dragon = new Dragon(message, getDragonLocation(loc));

        Util.sendPacket(player, dragon.getSpawnPacket());

        bars.put(player.getUniqueId(), dragon);

        return dragon;
    }

    public boolean hasBar(Player player)
    {
        if(player == null)
            return false;
        return bars.get(player.getUniqueId()) != null;
    }

    private Location getDragonLocation(Location loc)
    {
        loc.add(0, -300, 0);
        return loc;
    }

//    //Other methods
//    public static void displayTextBar(String text, final Player player){
//        PacketPlayOutSpawnEntityLiving mobPacket = getMobPacket(text, player.getLocation());
//
//        sendPacket(player, mobPacket);
//        hasHealthBar.put(player.getName(), true);
//
//        new BukkitRunnable(){
//            @Override
//            public void run(){
//                PacketPlayOutEntityDestroy destroyEntityPacket = getDestroyEntityPacket();
//
//                sendPacket(player, destroyEntityPacket);
//                hasHealthBar.put(player.getName(), false);
//            }
//        }.runTaskLater(yourMainClass, 120L);
//    }
//
//    public static void displayLoadingBar(final String text, final String completeText, final Player player, final int healthAdd, final long delay, final boolean loadUp){
//        PacketPlayOutSpawnEntityLiving mobPacket = getMobPacket(text, player.getLocation());
//
//        sendPacket(player, mobPacket);
//        hasHealthBar.put(player.getName(), true);
//
//        new BukkitRunnable()
//        {
//            int health = (loadUp ? 0 : 300);
//
//            @Override
//            public void run(){
//                if((loadUp ? health < 300 : health > 0))
//                {
//                    DataWatcher watcher = getWatcher(text, health);
//                    PacketPlayOutEntityMetadata metaPacket = getMetadataPacket(watcher);
//
//                    sendPacket(player, metaPacket);
//
//                    if(loadUp){
//                        health += healthAdd;
//                    } else {
//                        health -= healthAdd;
//                    }
//                } else {
//                    DataWatcher watcher = getWatcher(text, (loadUp ? 300 : 0));
//                    PacketPlayOutEntityMetadata metaPacket = getMetadataPacket(watcher);
//                    PacketPlayOutEntityDestroy destroyEntityPacket = getDestroyEntityPacket();
//
//                    sendPacket(player, metaPacket);
//                    sendPacket(player, destroyEntityPacket);
//                    hasHealthBar.put(player.getName(), false);
//
//                    //Complete text
//                    PacketPlayOutSpawnEntityLiving mobPacket = getMobPacket(completeText, player.getLocation());
//
//                    sendPacket(player, mobPacket);
//                    hasHealthBar.put(player.getName(), true);
//
//                    DataWatcher watcher2 = getWatcher(completeText, 300);
//                    PacketPlayOutEntityMetadata metaPacket2 = getMetadataPacket(watcher2);
//
//                    sendPacket(player, metaPacket2);
//
//                    new BukkitRunnable(){
//                        @Override
//                        public void run(){
//                            PacketPlayOutEntityDestroy destroyEntityPacket = getDestroyEntityPacket();
//
//                            sendPacket(player, destroyEntityPacket);
//                            hasHealthBar.put(player.getName(), false);
//                        }
//                    }.runTaskLater(yourMainClass, 40L);
//
//                    this.cancel();
//                }
//            }
//        }.runTaskTimer(yourMainClass, delay, delay);
//    }
//
//    public static void displayLoadingBar(final String text, final String completeText, final Player player, final int secondsDelay, final boolean loadUp){
//        final int healthChangePerSecond = 300 / secondsDelay;
//
//        displayLoadingBar(text, completeText, player, healthChangePerSecond, 20L, loadUp);
//    }
}
