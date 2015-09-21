package com.gmail.nuclearcat1337.anniPro.announcementBar.versions.v1_7_R4;

import java.io.IOException;

import net.minecraft.server.v1_7_R4.*;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.util.CraftChatMessage;
import org.bukkit.entity.Player;

import java.io.IOException;

class PacketPlayOutActionBar extends PacketPlayOutChat
{
    public String json;

    /**
     * Creates an action bar message
     * @param message The message to display
     */
    public PacketPlayOutActionBar(String message)
    {
        super();
        this.json = "{\"text\":\""+message+"\"}";
    }

    /**
     * When using md_5's chat API
     * @param components Components of which the chat consists of
     * @deprecated Doesn't support colours and formatting
     */
    @Deprecated
    public PacketPlayOutActionBar(BaseComponent...components)
    {
        super();
        this.json = ComponentSerializer.toString(components);
    }

    public void a(PacketDataSerializer packetdataserializer) throws IOException
    {
        this.json = ChatSerializer.a(ChatSerializer.a(packetdataserializer.c(32767)));
    }

    public void b(PacketDataSerializer packetdataserializer) throws IOException
    {
        packetdataserializer.a(this.json);
        packetdataserializer.writeByte(0x2);
    }

    public void a(PacketPlayOutListener packetplayoutlistener)
    {
        packetplayoutlistener.a(this);
    }

    public String b()
    {
        return String.format("actionbar='%s'", new Object[] { this.json });
    }

    public void handle(PacketListener packetlistener)
    {
        a((PacketPlayOutListener)packetlistener);
    }

    // Added some helper methods
    public void send(Player player)
    {
        if(((CraftPlayer)player).getHandle().playerConnection.networkManager.getVersion() < 47)
            return;

        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(this);
    }

    public void broadcast()
    {
        for(Player player : Bukkit.getOnlinePlayers())
        {
            send(player);
        }
    }
}
