package net.techcable.npclib.versions.v1_7_R4;

import net.minecraft.server.v1_7_R4.NetworkManager;
import net.techcable.npclib.util.ReflectUtil;

import java.lang.reflect.Field;

public class NPCNetworkManager extends NetworkManager {

	public NPCNetworkManager() {
		super(false); //MCP = isClientSide
		
		Field channel = ReflectUtil.makeField(NetworkManager.class, "m"); //MCP = channel
		Field address = ReflectUtil.makeField(NetworkManager.class, "n"); //MCP = address
		
		ReflectUtil.setField(channel, this, new NullChannel());
		ReflectUtil.setField(address, this, new NullSocketAddress());
		
	}

}
