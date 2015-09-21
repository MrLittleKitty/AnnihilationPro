package net.techcable.npclib.versions.v1_7_R3;

import net.minecraft.server.v1_7_R3.NetworkManager;
import net.techcable.npclib.util.ReflectUtil;

import java.lang.reflect.Field;


public class NPCNetworkManager extends NetworkManager {

	public NPCNetworkManager() {
		super(false); //MCP = isClientSide
		Field channel = ReflectUtil.makeField(NetworkManager.class, "m"); //MCP = channel
		Field address = ReflectUtil.makeField(NetworkManager.class, "n"); //MCP = socketAddress
		
		ReflectUtil.setField(channel, this, new NullChannel());
		ReflectUtil.setField(address, this, new NullSocketAddress());
		
	}

}
