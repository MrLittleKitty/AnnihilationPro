package com.gmail.nuclearcat1337.anniPro.announcementBar.versions.v1_8_R2;

import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R2.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Bar implements com.gmail.nuclearcat1337.anniPro.announcementBar.Bar
{
    @Override
    public void sendToPlayer(final Player player, final String message, final float percentOfTotal)
    {
        IChatBaseComponent actionComponent = ChatSerializer.a("{\"text\":\"" + cleanMessage(message) + "\"}");
        PacketPlayOutChat actionPacket = new PacketPlayOutChat(actionComponent, (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(actionPacket);
    }

    private static String cleanMessage(String message)
    {
        if (message.length() > 64)
            message = message.substring(0, 63);

        return message;
    }
}
