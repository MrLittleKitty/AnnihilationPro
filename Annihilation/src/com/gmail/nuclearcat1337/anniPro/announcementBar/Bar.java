package com.gmail.nuclearcat1337.anniPro.announcementBar;

import org.bukkit.entity.Player;

public interface Bar
{
    void sendToPlayer(Player player, String message, float percentOfTotal);
}
