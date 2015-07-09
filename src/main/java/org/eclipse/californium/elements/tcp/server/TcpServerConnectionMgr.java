package org.eclipse.californium.elements.tcp.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Sharable
public class TcpServerConnectionMgr extends ChannelInboundHandlerAdapter{	
	private static final Logger LOG = Logger.getLogger( TcpServerConnectionMgr.class.getName() );

	/**
	 * this is not very efficient, but will suffice for POC
	 */
	private final ConcurrentHashMap<InetSocketAddress, Channel> connections = new ConcurrentHashMap<InetSocketAddress, Channel>();

	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		final InetSocketAddress remote = (InetSocketAddress)ctx.channel().remoteAddress();
		connections.put(remote, ctx.channel());
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
		final InetSocketAddress remote = (InetSocketAddress)ctx.channel().remoteAddress();
		final Channel ch = connections.remove(remote);
		super.channelInactive(ctx);
	}

	public Channel getChannel(final InetSocketAddress address) {
		LOG.finest("request for Channel " + address.toString());
		return connections.get(address);
	}
}
