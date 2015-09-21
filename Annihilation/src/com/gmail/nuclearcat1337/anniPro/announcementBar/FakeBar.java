package com.gmail.nuclearcat1337.anniPro.announcementBar;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class FakeBar implements Bar
{
    //This bar should tell the player that the server is using an unsuported version, but also send the phase message
    private Map<UUID,Long> timers;
    public FakeBar()
    {
        timers = new HashMap<>();
    }

    @Override
    public void sendToPlayer(final Player player, final String message, final float percentOfTotal)
    {
        Long l = timers.get(player.getUniqueId());
        if(l == null || System.currentTimeMillis() >= l.longValue())
        {
            player.sendMessage("This server is using an unsupported version for the phase bar!");
            player.sendMessage(message);
            timers.put(player.getUniqueId(),System.currentTimeMillis()+(20*1000)); //Send them the message every 20 seconds
        }
    }
}
