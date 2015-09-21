package net.techcable.npclib.versions.v1_8_R1;

import net.techcable.npclib.api.NPC;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;

import net.minecraft.server.v1_8_R1.DamageSource;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.EnumGamemode;
import net.minecraft.server.v1_8_R1.PlayerInteractManager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class EntityNPCPlayer extends EntityPlayer {
    private NPC npc;
    public EntityNPCPlayer(Player player, Location location, NPC npc)
    {
        //super(NMS.getServer(), NMS.getHandle(location.getWorld()), makeProfile(player.getName(), player.getUniqueId()), new PlayerInteractManager(NMS.getHandle(location.getWorld())));
        super(NMS.getServer(), NMS.getHandle(location.getWorld()), ((CraftPlayer)player).getProfile(), new PlayerInteractManager(NMS.getHandle(location.getWorld())));
        this.npc = npc;
        playerInteractManager.b(EnumGamemode.SURVIVAL); //MCP = initializeGameType ---- SRG=func_73077_b
        playerConnection = new NPCConnection(this);
        setPosition(location.getX(), location.getY(), location.getZ());
    }

    @Override
    public boolean damageEntity(DamageSource source, float damage) {
        Player p = null;
        if(source.getEntity() != null && source.getEntity().getBukkitEntity().getType() == EntityType.PLAYER)
            p = (Player)source.getEntity().getBukkitEntity();
        if(npc.onKill(p))
            npc = null;
        return false;
        //return super.damageEntity(source,damage);
    }
}
