package net.techcable.npclib.versions.v1_7_R4;

import net.minecraft.util.io.netty.channel.AbstractChannel;
import net.minecraft.util.io.netty.channel.ChannelConfig;
import net.minecraft.util.io.netty.channel.ChannelMetadata;
import net.minecraft.util.io.netty.channel.ChannelOutboundBuffer;
import net.minecraft.util.io.netty.channel.EventLoop;

import java.net.SocketAddress;

public class NullChannel extends AbstractChannel {

	public NullChannel() {
		super(null);
	}

	@Override
	public ChannelConfig config() {
		return null;
	}

	@Override
	public boolean isActive() {
		return false;
	}

	@Override
	public boolean isOpen() {
		return false;
	}

	@Override
	public ChannelMetadata metadata() {
		return null;
	}

	@Override
	protected void doBeginRead() throws Exception {
	}

	@Override
	protected void doBind(SocketAddress arg0) throws Exception {
	}

	@Override
	protected void doClose() throws Exception {
	}

	@Override
	protected void doDisconnect() throws Exception {
	}

	@Override
	protected void doWrite(ChannelOutboundBuffer arg0) throws Exception {
	}

	@Override
	protected boolean isCompatible(EventLoop arg0) {
		return false;
	}

	@Override
	protected SocketAddress localAddress0() {
		return null;
	}

	@Override
	protected AbstractUnsafe newUnsafe() {
		return null;
	}

	@Override
	protected SocketAddress remoteAddress0() {
		return null;
	}

}
