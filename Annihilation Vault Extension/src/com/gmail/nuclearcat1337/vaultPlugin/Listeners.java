package com.gmail.nuclearcat1337.vaultPlugin;

import com.gmail.nuclearcat1337.anniPro.anniEvents.GameEndEvent;
import com.gmail.nuclearcat1337.anniPro.anniEvents.NexusHitEvent;
import com.gmail.nuclearcat1337.anniPro.anniEvents.PlayerKilledEvent;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniPlayer;
import com.gmail.nuclearcat1337.anniPro.anniGame.AnniTeam;
import com.gmail.nuclearcat1337.anniPro.anniGame.Game;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.text.DecimalFormat;

class Listeners implements Listener
{
    private static final DecimalFormat format = new DecimalFormat("#.##");

    private final VaultHook plugin;
    private final String message;

    private final double killMoney;
    private final double nexusHitMoney;
    private final double[] teamMonies;

    public Listeners(VaultHook system, String message,double killXP,double nexusXP,double[] teamXPs)
    {
        this.plugin = system;
        this.message = message;
        this.killMoney = killXP;
        this.nexusHitMoney = nexusXP;
        this.teamMonies = teamXPs;
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void gameEnd(GameEndEvent e)
    {
        if(e.getWinningTeam() != null)
        {
            for(AnniPlayer p : e.getWinningTeam().getPlayers())
            {
                Player player = p.getPlayer();
                if(player != null)
                {
                    double amount = plugin.checkMultipliers(player,teamMonies[0]);
                    this.giveMoney(player,amount);
                }
            }
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void xpNexusHit(NexusHitEvent e)
    {
        if(Game.isGameRunning() && !e.isCancelled())
        {
            Player player = e.getPlayer().getPlayer();
            assert player != null;

            for(AnniPlayer pl : e.getPlayer().getTeam().getPlayers())
            {
                Player player1 = pl.getPlayer();
                if(player1 != null)
                {
                    double amount = plugin.checkMultipliers(player, e.getDamage() * nexusHitMoney);
                    this.giveMoney(player1,amount);
                }
            }


            if(e.willKillTeam())
            {
                AnniTeam t = e.getHitNexus().Team;
                int alive = 0;
                for(AnniTeam team : AnniTeam.Teams)
                {
                    if(!team.isTeamDead() && !team.equals(t))
                        alive++;
                }
                for(AnniPlayer p : t.getPlayers())
                {
                    Player player1 = p.getPlayer();
                    if(player1 != null)
                    {
                        double amount = plugin.checkMultipliers(player, teamMonies[alive]);
                        this.giveMoney(player1,amount);
                    }
                }
            }
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void xpPlayerKill(PlayerKilledEvent e)
    {
        if(Game.isGameRunning())
        {
            Player player = e.getKiller().getPlayer();
            if(player != null)
            {
                double amount = plugin.checkMultipliers(player, killMoney);
                this.giveMoney(player,amount);
            }
        }
    }

    private void giveMoney(Player player, double amount)
    {
        if(plugin.giveMoney(player,amount).transactionSuccess())
            sendMoneyMessage(player,amount);
        else
            sendMoneyMessage(player,0);
    }

    private void sendMoneyMessage(Player player, double money)
    {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message.replace("%#", "" + format.format(money))));
    }


}
