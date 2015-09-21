package com.gmail.nuclearcat1337.anniPro.announcementBar;

import com.gmail.nuclearcat1337.anniPro.main.AnnihilationMain;
import com.gmail.nuclearcat1337.anniPro.utils.VersionUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AnnounceBar
{
    private static AnnounceBar instance;
    public static AnnounceBar getInstance()
    {
        if(instance == null)
        {
            instance = new AnnounceBar();
        }
        return instance;
    }

    private Bar bar;

    private Announcement announcement;

    private Integer timer;
    private long timeLeft;
    private long lastUpdate;

    private AnnounceBar()
    {
        try
        {
            String version = VersionUtils.getVersion();
            String name = "com.gmail.nuclearcat1337.anniPro.announcementBar.versions."+version+".Bar";
            Class<?> cl = Class.forName(name);
            Class<? extends Bar> bar = cl.asSubclass(Bar.class);
            Bar manager = bar.newInstance();
            this.bar = manager;
        }
        catch (Throwable t)
        {
            //t.printStackTrace();
            bar = new FakeBar();
        }
    }

    public TempData getData()
    {
        TempData d = new TempData();
        d.announcement = announcement;
        d.timeLeft = timeLeft;
        return d;
    }

    //LOLOL lazy hack
    public void countDown(TempData d)
    {
        this.announcement = d.announcement;
        this.timeLeft = d.timeLeft;
        this.lastUpdate = System.currentTimeMillis();
        scheduleUpdater();
        //TODO-----------Not have this method at all. This is a pretty lazy hack for something that should be done better
    }

    public void countDown(Announcement announcement)
    {
        this.announcement = announcement;
        this.timeLeft = announcement.getTime()*1000;
        this.lastUpdate = System.currentTimeMillis();
        String mess = ChatColor.translateAlternateColorCodes('&', announcement.getMessage()).replace("{#}", format(timeLeft));
        for(Player pl : Bukkit.getOnlinePlayers())
            bar.sendToPlayer(pl, mess, 100);
        scheduleUpdater();
    }

    private void scheduleUpdater()
    {
        cancelUpdater();
        timer = Bukkit.getScheduler().runTaskTimer(AnnihilationMain.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                String m = ChatColor.translateAlternateColorCodes('&',announcement.getMessage());              // message.replace("{#}", format(timeLeft));
                if (announcement.isPermanent())
                {
                    for (Player player : Bukkit.getOnlinePlayers())
                        bar.sendToPlayer(player,m,100);
                    return;
                }


                //Bukkit.getLogger().info("This is also happening");
                timeLeft -= (System.currentTimeMillis() - lastUpdate);
                lastUpdate = System.currentTimeMillis();
                if (timeLeft <= 100) //if its less than a tenth of a second, then we can pretty msuch say its over
                {
                    timeLeft = 0;
                    cancelUpdater();
                    if (announcement.getCallBack() != null)
                        announcement.getCallBack().run();
                }
                else
                {
                    String mess = m.replace("{#}", format(timeLeft));
                    float percent = (float)((timeLeft/1000) / ((float)announcement.getTime()));
                    for(Player player : Bukkit.getOnlinePlayers())
                        bar.sendToPlayer(player,mess,percent);
//                    for (Player p : Bukkit.getOnlinePlayers())
//                        sendToPlayer(p, mess);

                }
            }
        }, 20L, 20L).getTaskId();
    }

    private void cancelUpdater()
    {
        if(timer != null)
        {
            Bukkit.getScheduler().cancelTask(timer);
            timer = null;
        }
    }

    private static String format(long miliseconds)
    {
        return DurationFormatUtils.formatDuration(miliseconds, "mm:ss");
    }
}
