package com.gmail.nuclearcat1337.anniPro.anniGame.autoRespawn.versions;

import com.gmail.nuclearcat1337.anniPro.anniGame.autoRespawn.RespawnPacket;
import net.minecraft.server.v1_8_R2.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R2.PacketPlayInClientCommand.EnumClientCommand;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class v1_8_R2Packet implements RespawnPacket
{
    private final PacketPlayInClientCommand packet;
    public v1_8_R2Packet()
    {
        packet = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
    }

    @Override
    public void sendToPlayer(final Player player)
    {
        CraftPlayer p = (CraftPlayer)player;
        p.getHandle().playerConnection.a(packet);
    }
}
