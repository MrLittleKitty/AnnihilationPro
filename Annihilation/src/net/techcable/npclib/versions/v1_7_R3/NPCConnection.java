package net.techcable.npclib.versions.v1_7_R3;

import net.minecraft.server.v1_7_R3.Packet;
import net.minecraft.server.v1_7_R3.PlayerConnection;

public class NPCConnection extends PlayerConnection {

	public NPCConnection(EntityNPCPlayer npc) {
		super(NMS.getServer(), new NPCNetworkManager(), npc);
	}

	@Override
	public void sendPacket(Packet packet) {
		//Don't send packets to an npc
	}
}
