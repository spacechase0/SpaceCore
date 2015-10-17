package com.spacechase0.minecraft.spacecore.network;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Sharable // Netty complains without this... Does this make it work?
public class PacketProcesser extends ChannelInboundHandlerAdapter
{
	protected PacketProcesser( PacketCodec theCodec )
	{
		codec = theCodec;
	}
	
	@Override
	public void channelRead( ChannelHandlerContext context, Object message )
	{
		if ( !( message instanceof Packet ) ) return;
		Packet packet = ( Packet ) message;
		
		// Kinda from here: http://www.minecraftforge.net/wiki/Netty_Packet_Handling
		if ( FMLCommonHandler.instance().getEffectiveSide().equals( Side.SERVER ) )
		{
			INetHandler handler = context.channel().attr( NetworkRegistry.NET_HANDLER ).get();
			packet.processServer( ( ( NetHandlerPlayServer ) handler ).playerEntity );
		}
		else
		{
			packet.processClient( getClientPlayer() );
		}
	}
	
	@SideOnly( Side.CLIENT )
	private static EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}
	
	private PacketCodec codec;
}
