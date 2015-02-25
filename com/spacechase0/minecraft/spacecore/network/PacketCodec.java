package com.spacechase0.minecraft.spacecore.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

import com.spacechase0.minecraft.spacecore.asm.SpaceCoreLog;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.handshake.FMLHandshakeMessage;
import cpw.mods.fml.common.network.internal.FMLMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class PacketCodec extends FMLIndexedMessageToMessageCodec< Packet > implements ChannelHandler
{
	public PacketCodec()
	{
		channels = NetworkRegistry.INSTANCE.newChannel( Loader.instance().activeModContainer().getModId(), this, new PacketProcesser( this ) );
	}
	
	@Override
	public void encodeInto( ChannelHandlerContext context, Packet packet, ByteBuf buffer ) throws Exception
	{
		packet.write( buffer );
	}
	
	@Override
	public void decodeInto( ChannelHandlerContext context, ByteBuf buffer, Packet packet )
	{
		packet.read( buffer );
	}
	
	// These sending functions are from here: http://www.minecraftforge.net/wiki/Netty_Packet_Handling
	public void sendToAll( Packet packet )
	{
		Channel channel = channels.get( Side.SERVER );
		channel.attr( FMLOutboundHandler.FML_MESSAGETARGET ).set( FMLOutboundHandler.OutboundTarget.ALL );
		channel.writeAndFlush( packet );
	}
	
	public void sendToPlayer( EntityPlayerMP player, Packet packet )
	{
		Channel channel = channels.get( Side.SERVER );
		channel.attr( FMLOutboundHandler.FML_MESSAGETARGET ).set( FMLOutboundHandler.OutboundTarget.PLAYER );
		channel.attr( FMLOutboundHandler.FML_MESSAGETARGETARGS ).set( player );
		channel.writeAndFlush( packet );
	}
	
	public void sendToNearby( NetworkRegistry.TargetPoint pos, Packet packet )
	{
		Channel channel = channels.get( Side.SERVER );
		channel.attr( FMLOutboundHandler.FML_MESSAGETARGET ).set( FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT );
		channel.attr( FMLOutboundHandler.FML_MESSAGETARGETARGS ).set( pos );
		channel.writeAndFlush( packet );
	}
	
	public void sendToDimension( int dim, Packet packet )
	{
		Channel channel = channels.get( Side.SERVER );
		channel.attr( FMLOutboundHandler.FML_MESSAGETARGET ).set( FMLOutboundHandler.OutboundTarget.DIMENSION );
		channel.attr( FMLOutboundHandler.FML_MESSAGETARGETARGS ).set( dim );
		channel.writeAndFlush( packet );
	}
	
	@SideOnly( Side.CLIENT )
	public void sendToServer( Packet packet )
	{
		Channel channel = channels.get( Side.CLIENT );
		channel.attr( FMLOutboundHandler.FML_MESSAGETARGET ).set( FMLOutboundHandler.OutboundTarget.TOSERVER );
		channel.writeAndFlush( packet );
	}
	
	protected void addPacket( Packet packet )
	{
		addDiscriminator( packet.getId(), packet.getClass() );
	}
	
	private EnumMap< Side, FMLEmbeddedChannel > channels;
}
