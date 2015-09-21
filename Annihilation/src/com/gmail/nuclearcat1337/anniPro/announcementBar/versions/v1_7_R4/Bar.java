package com.gmail.nuclearcat1337.anniPro.announcementBar.versions.v1_7_R4;

import com.gmail.nuclearcat1337.anniPro.anniEvents.PluginDisableEvent;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;
import net.minecraft.util.com.google.common.collect.BiMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import net.minecraft.server.v1_7_R4.EnumProtocol;
import net.minecraft.server.v1_7_R4.NetworkManager;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.util.io.netty.channel.Channel;

public class Bar implements com.gmail.nuclearcat1337.anniPro.announcementBar.Bar, Listener
{
    private HashMap<UUID, IDragon> bars;

    private BiMap map;
    private ProxyHashBiMap<Integer, Class> special;


    public Bar()
    {
        bars = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, AnnihilationMain.getInstance());
        overrideI();
        injectCustomPacket(PacketPlayOutChat.class, PacketPlayOutActionBar.class);
        refreshPlayers(true);
    }


    @EventHandler
    public void onDisable(PluginDisableEvent event)
    {
        refreshPlayers(false);
        ejectCustomPacket(PacketPlayOutActionBar.class);
        returnI();
    }

//    @EventHandler
//    public void updateCheck(PlayerJoinEvent event)
//    {
//        refreshPlayers(true);
//    }


    @Override
    public void sendToPlayer(final Player player, final String message, final float percentOfTotal)
    {
        CraftPlayer p = (CraftPlayer)player;
        if(p.getHandle().playerConnection.networkManager.getVersion() >= 47)
        {
            new PacketPlayOutActionBar(message).send(player);
        }
        else
        {
            IDragon drag;
            if (hasBar(player))
                drag = getDragon(player, "");
            else
                drag = getDragon(player, message);

            drag.setName(message);
            drag.setHealth(percentOfTotal);

            //Bukkit.getLogger().info("Percent was "+percentOfTotal);
            //Bukkit.getLogger().info("Health was "+drag.health);

            sendDragon(drag, player);
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
        Packet p = dragon.getTeleportPacket(getDragonLocation(player.getLocation()));
        if(p != null)
            Util.sendPacket(player, p);
        else
        {
            Bukkit.getLogger().warning("[Annihilation] An ERROR has occured with the phase bar.");
        }
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

    public void removeBar(Player player)
    {
        if (!hasBar(player))
            return;

        Util.sendPacket(player, getDragon(player, "").getDestroyPacket());

        bars.remove(player.getUniqueId());

        //cancelTimer(player);
    }

    private Location getDragonLocation(Location loc)
    {
        loc.add(0, -300, 0);
        return loc;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void PlayerLoggout(PlayerQuitEvent event)
    {
        removeBar(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event)
    {
        removeBar(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(final PlayerTeleportEvent event)
    {
        if(event.getTo() != null && event.getPlayer() != null)
        {
            handleTeleport(event.getPlayer(), event.getTo().clone());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerTeleport(final PlayerRespawnEvent event)
    {
        if(event.getPlayer() != null && event.getRespawnLocation() != null)
        {
           // final AnniPlayer p = AnniPlayer.getPlayer(event.getPlayer().getUniqueId());
           // if(p != null)
           // {
                //					if(permanentBar)
                //						BarAPI.sendPermDragon(event.getPlayer());
                //					else
                handleTeleport(event.getPlayer(), event.getRespawnLocation().clone());
           // }
        }
    }

    //@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    //public void onPlayerJoin(final PlayerJoinEvent event)
    //{
        //final AnniPlayer p = AnniPlayer.getPlayer(event.getPlayer().getUniqueId());
        //if(p != null)
       // {
           // if(!hasBar(event.getPlayer()))
          //  {
                //					if(permanentBar)
                //						sendPermDragon(event.getPlayer());
       //					else
              //  handleTeleport(event.getPlayer(), event.getPlayer().getLocation().clone());
            ////}

      //  }

   // }

    private void handleTeleport(final Player player, final Location loc)
    {
        if (!hasBar(player))
            return;

        Bukkit.getScheduler().runTaskLater(AnnihilationMain.getInstance(), new Runnable()
        {

            @Override
            public void run()
            {
                // Check if the player still has a dragon after the two ticks!
                // ;)
                if (!hasBar(player))
                    return;

                IDragon oldDragon = getDragon(player, "");

                float health = oldDragon.health;
                String message = oldDragon.name;

                Util.sendPacket(player, getDragon(player, "").getDestroyPacket());

                bars.remove(player.getUniqueId());

                IDragon dragon = addDragon(player, loc, message);
                dragon.health = health;

                sendDragon(dragon, player);
            }

        }, 2L);
    }

    private void overrideI()
    {
        try
        {
            Field field = EnumProtocol.class.getDeclaredField("i");
            field.setAccessible(true);
            this.map = (BiMap) field.get(EnumProtocol.PLAY);
            special = new ProxyHashBiMap<Integer, Class>(map);
            set(field, EnumProtocol.PLAY, special);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            throw new ExceptionInInitializerError("Nope, not allowed.");
        }
    }

    private void set(Field f, Object o, Object v) throws NoSuchFieldException, IllegalAccessException
    {
        boolean accessible = f.isAccessible();
        f.setAccessible(true);
        if ((f.getModifiers() & Modifier.FINAL) != 0)
        {
            set(Field.class.getDeclaredField("modifiers"), f, f.getModifiers() & ~Modifier.FINAL);
        }
        f.set(o, v);
    }

    public <T extends Packet> void injectCustomPacket(Class<T> mc, Class<? extends T> custom)
    {
        special.inverse().injectSpecial(PacketPlayOutActionBar.class, PacketPlayOutChat.class);
        try
        {
            Field field = EnumProtocol.class.getDeclaredField("f");
            field.setAccessible(true);
            ((Map)field.get(null)).put(custom, EnumProtocol.PLAY);
        }
        catch (NoSuchFieldException ex)
        {
            ex.printStackTrace();
        }
        catch (IllegalAccessException ex)
        {
            ex.printStackTrace();
        }
    }

    public <T extends Packet> void ejectCustomPacket(Class<? extends T> custom)
    {
        special.inverse().ejectSpecial(custom);
        //Validate.notNull(id, "You can only eject existing packets");
        try
        {
            Field field = EnumProtocol.class.getDeclaredField("f");
            field.setAccessible(true);
            ((Map)field.get(null)).remove(custom);
        }
        catch (NoSuchFieldException ex)
        {
            ex.printStackTrace();
        }
        catch (IllegalAccessException ex)
        {
            ex.printStackTrace();
        }
    }

    private void returnI()
    {
        try
        {
            Field field = EnumProtocol.class.getDeclaredField("i");
            field.setAccessible(true);
            set(field, EnumProtocol.PLAY, this.map);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void refreshPlayers(boolean enable)
    {
        try
        {
            Field channelField = NetworkManager.class.getDeclaredField("m");
            channelField.setAccessible(true);
            Channel channel;
            for (Player player : Bukkit.getOnlinePlayers())
            {
                channel = (Channel) channelField.get(((CraftPlayer) player).getHandle().playerConnection.networkManager);
                channel.attr(NetworkManager.f).set(enable ? this.special : this.map);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
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
