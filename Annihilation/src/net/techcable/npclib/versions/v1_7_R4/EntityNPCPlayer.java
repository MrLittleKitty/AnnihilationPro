package net.techcable.npclib.versions.v1_7_R4;

import net.minecraft.server.v1_7_R4.DamageSource;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EnumGamemode;
import net.minecraft.server.v1_7_R4.PlayerInteractManager;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.com.mojang.authlib.properties.Property;
import net.techcable.npclib.api.NPC;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EntityNPCPlayer extends EntityPlayer {

    private NPC npc;
    public EntityNPCPlayer(Player player, Location location, NPC npc)
    {
        //super(NMS.getServer(), NMS.getHandle(location.getWorld()), makeProfile(player.getName(), player.getUniqueId()), new PlayerInteractManager(NMS.getHandle(location.getWorld())));
        super(NMS.getServer(), NMS.getHandle(location.getWorld()), ((CraftPlayer) player).getProfile(), new PlayerInteractManager(NMS.getHandle(location.getWorld())));
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
	
	public static GameProfile makeProfile(String name, UUID skinId) {
		GameProfile profile = new GameProfile(UUID.randomUUID(), name);
		if (skinId != null) {
			GameProfile skin = new GameProfile(skinId, null);
			skin = NMS.getServer().av().fillProfileProperties(skin, true); //Srg = func_147130_as
			if (skin.getProperties().get("textures") == null || !skin.getProperties().get("textures").isEmpty()) {
				Property textures = skin.getProperties().get("textures").iterator().next();
				profile.getProperties().put("textures", textures);
			}
		}
		return profile;	
	}
}
