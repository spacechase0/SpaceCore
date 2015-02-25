package com.spacechase0.minecraft.spacecore.util;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

@SideOnly( Side.CLIENT )
public class ClientUtils
{
	public static void bindTexture( String str )
	{
		Minecraft.getMinecraft().renderEngine.bindTexture( new ResourceLocation( str ) );
	}
	
	public static void bindTexture( int id )
	{
		GL11.glBindTexture( GL11.GL_TEXTURE_2D, id );
	}
	
	public static void bindItemTexture( int num)
	{
		Minecraft.getMinecraft().renderEngine.bindTexture( Minecraft.getMinecraft().renderEngine.getResourceLocation( num ) );
	}
	
	public static void drawString( String str, int x, int y, int col )
	{
		Minecraft.getMinecraft().fontRenderer.drawString( str, x, y, col );
	}
	
	public static int getStringWidth( String str )
	{
		return Minecraft.getMinecraft().fontRenderer.getStringWidth( str );
	}

	public static void drawSplitString( String str, int x, int y, int w, int col )
	{
		Minecraft.getMinecraft().fontRenderer.drawSplitString( str, x, y, w, col );
	}
}
