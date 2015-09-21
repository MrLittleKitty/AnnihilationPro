package com.gmail.nuclearcat1337.anniPro.announcementBar;

import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R2.PacketPlayOutChat;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;

public class MessageBar
{
//	public static class DragonData
//	{
//        private final String message;
//        private final boolean permanentBar;
//        private final Runnable runable;
//        private final long timeLeft;
//
//		private DragonData(String message, boolean permDragon, Runnable run, long timeLeft)
//		{
//			this.message = message;
//			this.permanentBar = permDragon;
//			this.runable = run;
//            this.timeLeft = timeLeft;
//		}
//	}
//
//	//private static Map<UUID, FakeDragon> players = new ConcurrentHashMap<UUID, FakeDragon>();
//    //private static PhaseHandler handler;
//
//    private static Integer timer;
//	private static String message;
//	private static boolean permanentBar;
//	private static Runnable runable;
//    private static long timeLeft;
//    private static long lastUpdate;
//
//    private static String format(long miliseconds)
//    {
//       return DurationFormatUtils.formatDuration(miliseconds,"mm:ss");
//    }
//
////	public static void registerListeners(Plugin plugin)
////	{
//////		//players.clear();
//////		//Bukkit.getPluginManager().registerEvents(new Loader(), plugin);
//////        Bukkit.getScheduler().scheduleSyncRepeatingTask(AnnihilationMain.getInstance(),
//////                new Runnable()
//////                {
//////                    public void run()
//////                    {
//////                        String m = cleanMessage(message.replace("{#}", format(timeLeft)));
//////                        for (Player player : Bukkit.getOnlinePlayers())
//////                        {
//////                            IChatBaseComponent actionComponent = ChatSerializer.a("{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', actionText) + "\"}");
//////                            PacketPlayOutChat actionPacket = new PacketPlayOutChat(actionComponent, (byte) 2);
//////                            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(actionPacket);
//////                        }
//////                    }
//////                }, 0L, 5L);
////	}
//
//	public static DragonData getCurrentData()
//	{
//		return new DragonData(message,permanentBar,runable,timeLeft);
//	}
//
//
//	public static void Reset()
//	{
//		message = null;
//		permanentBar = false;
//		runable = null;
//		cancelTimer();
//	}
//
//	public static void permanentBar(final String message)
//	{
//		runable = null;
//		permanentBar = true;
//		MessageBar.message = message;
//		cancelTimer();
//		for(Player p : Bukkit.getOnlinePlayers())
//			sendToPlayer(p,message);
//
//        timer = Bukkit.getScheduler().runTaskTimer(AnnihilationMain.getInstance(), new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                //   Bukkit.getLogger().info("This is also happening");
//                //                timeLeft -= (System.currentTimeMillis()-lastUpdate);
//                //                if (timeLeft <= 100) //if its less than a tenth of a second, then we can pretty msuch say its over
//                //                {
//                //                    timeLeft = 0;
//                //                    cancelTimer();
//                //                    if (runable != null)
//                //                        runable.run();
//                //                }
//                //                else
//                //                {
//                for (Player p : Bukkit.getOnlinePlayers())
//                {
//                    sendToPlayer(p, message);
//                }
//                //}
//            }
//        }, 20L, 20L).getTaskId();
//
//	}
//
//	public static void countDown(DragonData data)
//	{
//		if(data.permanentBar)
//			MessageBar.permanentBar(data.message);
//		else
//        {
//            count(data.message, data.timeLeft, data.runable);
//        }
//	}
//
//	public static void countDown(final String message, int time, Runnable callback)
//	{
//		count(message, time * 1000, callback);
//	}
//
//	private static void count(final String message, long time, Runnable callback)
//    {
//       // Bukkit.getLogger().info("This thing atleast happened once");
//        permanentBar = false;
//        cancelTimer();
//        runable = callback;
//        timeLeft = time;
//
//        String m = message.replace("{#}", format(timeLeft));
//        for (Player p : Bukkit.getOnlinePlayers())
//        {
//           sendToPlayer(p,m);
//        }
//        MessageBar.lastUpdate = System.currentTimeMillis();
//        timer = Bukkit.getScheduler().runTaskTimer(AnnihilationMain.getInstance(), new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                //Bukkit.getLogger().info("This is also happening");
//                timeLeft -= (System.currentTimeMillis() - lastUpdate);
//                lastUpdate = System.currentTimeMillis();
//                if (timeLeft <= 100) //if its less than a tenth of a second, then we can pretty msuch say its over
//                {
//                    timeLeft = 0;
//                    cancelTimer();
//                    if (runable != null)
//                        runable.run();
//                }
//                else
//                {
//                    String mess = message.replace("{#}", format(timeLeft));
//                    for (Player p : Bukkit.getOnlinePlayers())
//                    sendToPlayer(p, mess);
//                }
//            }
//        }, 20L, 20L).getTaskId();
//    }
//
//	private static void cancelTimer()
//	{
//		if(timer != null)
//		{
//			Bukkit.getScheduler().cancelTask(timer);
//			timer = null;
//		}
//	}
//
//	private static String cleanMessage(String message)
//	{
//		if (message.length() > 64)
//			message = message.substring(0, 63);
//
//		return message;
//	}
//
//    private static void sendToPlayer(Player player, String message)
//    {
//      //  Bukkit.getLogger().info("Sending messsage to player: "+player.getName());
//       // Bukkit.getLogger().info("Message is: "+message);
//        IChatBaseComponent actionComponent = ChatSerializer.a("{\"text\":\"" + ChatColor.translateAlternateColorCodes('&', cleanMessage(message)) + "\"}");
//        PacketPlayOutChat actionPacket = new PacketPlayOutChat(actionComponent, (byte) 2);
//        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(actionPacket);
//    }

}
