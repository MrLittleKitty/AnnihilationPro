package net.techcable.npclib.versions.v1_7_R4;

import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.WorldServer;
import net.techcable.npclib.api.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class NMS implements net.techcable.npclib.api.NMS {

//    @Override
//    public Player spawnPlayer(Location toSpawn, String name, NPC npc) {
//    	EntityNPCPlayer player = new EntityNPCPlayer(npc, name, toSpawn);
//    	if (ProtocolHack.isProtocolHack()) {
//    		ProtocolHack.notifyOfSpawn(Bukkit.getOnlinePlayers(), player.getBukkitEntity());
//    	}
//    	WorldServer world = getHandle(toSpawn.getWorld());
//    	world.addEntity(player);
//    	look(player.getBukkitEntity(), toSpawn.getPitch(), toSpawn.getYaw());
//    	return player.getBukkitEntity();
//    }

    @Override
    public Player spawnPlayer(Player realPlayer, Location location, NPC npc)
    {
        EntityNPCPlayer player = new EntityNPCPlayer(realPlayer,location,npc);
        if (ProtocolHack.isProtocolHack()) {
            ProtocolHack.notifyOfSpawn(Bukkit.getOnlinePlayers(), player.getBukkitEntity());
        }
        WorldServer world = getHandle(location.getWorld());
        world.addEntity(player);
        //look(player.getBukkitEntity(), toSpawn.getPitch(), toSpawn.getYaw());
        return player.getBukkitEntity();
    }
    
    public static Entity getHandle(org.bukkit.entity.Entity bukkitEntity) {
        if (!(bukkitEntity instanceof CraftEntity))
            return null;
        return ((CraftEntity)bukkitEntity).getHandle();
    }

    public static EntityPlayer getHandle(Player bukkitPlayer) {
    	if (!(bukkitPlayer instanceof CraftPlayer)) return null;
    	return ((CraftPlayer)bukkitPlayer).getHandle();
    }

    public static EntityHuman getHandle(HumanEntity bukkitHuman) {
    	if (!(bukkitHuman instanceof CraftHumanEntity)) return null;
    	return ((CraftHumanEntity)bukkitHuman).getHandle();
    }
    
    public static MinecraftServer getHandle(org.bukkit.Server bukkitServer) {
    	if (bukkitServer instanceof CraftServer) {
    		return ((CraftServer)bukkitServer).getServer();
    	} else {
    		return null;
    	}
    }
    
    public static WorldServer getHandle(org.bukkit.World bukkitWorld) {
    	if (bukkitWorld instanceof CraftWorld) {
    		return ((CraftWorld)bukkitWorld).getHandle();
    	} else {
    		return null;
    	}
    }
	
	public static MinecraftServer getServer() {
		return getHandle(Bukkit.getServer());
	}

//	@Override
//	public NPC getAsNPC(org.bukkit.entity.Entity entity) {
//		if (getHandle(entity) instanceof EntityNPCPlayer) {
//			EntityNPCPlayer player = (EntityNPCPlayer) getHandle(entity);
//			return player.getNpc();
//		} else {
//			return null;
//		}
//	}

//	public static final int[] UPDATE_ALL_SLOTS = new int[] {0, 1, 2, 3, 4};
//	@Override
//	public void notifyOfEquipmentChange(Player[] toNotify, Player rawNpc, int... slots) {
//	    EntityPlayer npc = getHandle(rawNpc);
//	    slots = slots.length == 0 ? UPDATE_ALL_SLOTS : slots;
//	    List<Packet> packets = new ArrayList<>();
//	    for (int slot : slots) {
//	        packets.add(new PacketPlayOutEntityEquipment(npc.getId(), slot, npc.getEquipment(slot)));
//	    }
//	    sendPacketsTo(toNotify, packets.toArray(new Packet[packets.size()]));
//	}
	
	public void sendPacketsTo(Player[] recipients, Packet... packets) {
	    EntityPlayer[] nmsToSend = new EntityPlayer[recipients.length];
	    for (int i = 0; i < recipients.length; i++) {
	        nmsToSend[i] = getHandle(recipients[i]);
	    }
		for (EntityPlayer recipient : nmsToSend) {
			if (recipient == null) continue;
			for (Packet packet : packets) {
			    if (packet == null) continue;
			    recipient.playerConnection.sendPacket(packet);
			}
		}
	}

//	@Override
//	public boolean isSupported(OptionalFeature feature) {
//		switch (feature) {
//			case SKINS :
//				return true;
//			default :
//				return false;
//		}
//	}

//	@Override
//	public void onJoin(Player joined, Collection<? extends NPC> npcs) {
//		if (ProtocolHack.isProtocolHack()) {
//			npcs = Collections2.filter(npcs, new Predicate<NPC>() {
//				@Override
//				public boolean apply(NPC arg0) {
//					return arg0.isSpawned() && arg0 instanceof Player;
//				}
//			});
//			Collection<? extends Player> npcEntities = Collections2.transform(npcs, new Function<NPC, Player>() {
//				@Override
//				public Player apply(NPC npc) {
//					return (Player) npc.getEntity();
//				}
//			});
//			ProtocolHack.notifyOfSpawn(new Player[] {joined}, npcEntities.toArray(new Player[npcEntities.size()]));
//		}
//	}

	@Override
	public void onDespawn(NPC npc) {
		if (ProtocolHack.isProtocolHack()) {
			ProtocolHack.notifyOfDespawn(Bukkit.getOnlinePlayers(), (Player) npc.getEntity());
        }
        WorldServer world = getHandle(npc.getLocation().getWorld());
        world.removeEntity(getHandle(npc.getEntity()));
	}
}
