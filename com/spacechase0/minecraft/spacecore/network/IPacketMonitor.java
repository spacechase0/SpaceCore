package com.spacechase0.minecraft.spacecore.network;

import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;


public interface IPacketMonitor
{
	public void monitorIncoming( INetHandler net, net.minecraft.network.Packet packet );
	public void monitorOutgoing( NetworkManager net, net.minecraft.network.Packet packet );
}
