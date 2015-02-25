package com.spacechase0.minecraft.spacecore.network;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;

public class PacketInterceptor
{
	public static void intercept( INetHandler net, net.minecraft.network.Packet packet )
	{
		//System.out.println("Incoming" );
		Iterator< IPacketMonitor > it = monitors.iterator();
		while ( it.hasNext() )
		{
			IPacketMonitor mon = it.next();
			mon.monitorIncoming( net, packet );
		}
	}
	
	public static void intercept( NetworkManager net, net.minecraft.network.Packet packet )
	{
		System.out.println("Outgoing!");
		Iterator< IPacketMonitor > it = monitors.iterator();
		while ( it.hasNext() )
		{
			IPacketMonitor mon = it.next();
			mon.monitorOutgoing( net, packet );
		}
	}
	
	public static void addMonitor( IPacketMonitor mod )
	{
		monitors.add( mod );
	}
	
	private static List< IPacketMonitor > monitors = new ArrayList< IPacketMonitor >();
}
