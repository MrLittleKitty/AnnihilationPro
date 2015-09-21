package com.gmail.nuclearcat1337.anniPro.anniGame.autoRespawn;

import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.anniPro.utils.VersionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnHandler implements Listener
{
    private static RespawnHandler instance;
    public static void register(Plugin plugin)
    {
        if(instance == null)
        {
            instance = new RespawnHandler();
            Bukkit.getPluginManager().registerEvents(instance,plugin);
        }
    }

    private RespawnPacket packet;
    private RespawnHandler()
    {
        try
        {
            String version = VersionUtils.getVersion();
            String className = "com.gmail.nuclearcat1337.anniPro.anniGame.autoRespawn.versions."+version+"Packet";
            Class<?> cl = Class.forName(className);
            Class<? extends RespawnPacket> pack = cl.asSubclass(RespawnPacket.class);
            RespawnPacket p = pack.newInstance();
            packet = p;
        }
        catch(Throwable t)
        {
            packet = new FakePacket();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void autoRespawn(PlayerDeathEvent event)
    {
        new AutoRespawnTask(event.getEntity()).runTaskLater(AnnihilationMain.getInstance(), 2L);
    }

    private class AutoRespawnTask extends BukkitRunnable
    {
        private Player player;
        public AutoRespawnTask(Player player)
        {
            this.player = player;
        }

        @Override
        public void run()
        {
            packet.sendToPlayer(player);
            player = null;
        }
    }

    private class FakePacket implements RespawnPacket
    {
        @Override
        public void sendToPlayer(final Player player)
        {
            player.sendMessage("WARNING: This server is using a version that is not supported.");
        }
    }

    //        try
    //        {
    //            Object nmsPlayer = player.getClass().getMethod("getHandle").invoke(player);
    //            Object packet = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".PacketPlayInClientCommand").newInstance();
    //            Class<?> enumClass = Class.forName(nmsPlayer.getClass().getPackage().getName() + ".EnumClientCommand");
    //
    //            for(Object ob : enumClass.getEnumConstants())
    //            {
    //                if(ob.toString().equals("PERFORM_RESPAWN"))
    //                {
    //                    packet = packet.getClass().getConstructor(enumClass).newInstance(ob);
    //                }
    //            }
    //
    //            Object con = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
    //            con.getClass().getMethod("a", packet.getClass()).invoke(con, packet);
    //            return;
    //        }
    //        catch(Throwable t)
    //        {
    //            return;
    //        }
}
