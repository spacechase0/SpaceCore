package com.spacechase0.minecraft.spacecore.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

// FMLModContainer

public abstract class Packet
{
	public abstract byte getId();

	public void processClient( EntityPlayer player ) {}
	public void processServer( EntityPlayerMP player ) {}
	
	public abstract void write( ByteBuf buffer );
	public abstract void read( ByteBuf buffer );
}
