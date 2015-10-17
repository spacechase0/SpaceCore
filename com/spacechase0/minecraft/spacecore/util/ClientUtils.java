package com.spacechase0.minecraft.spacecore.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly( Side.CLIENT )
public class ClientUtils
{
	public static void bindTexture( String str )
	{
		Minecraft.getMinecraft().renderEngine.bindTexture( new ResourceLocation( str ) );
	}
	
	public static void drawString( String str, int x, int y, int col )
	{
		Minecraft.getMinecraft().fontRendererObj.drawString( str, x, y, col );
	}
	
	public static int getStringWidth( String str )
	{
		return Minecraft.getMinecraft().fontRendererObj.getStringWidth( str );
	}

	public static void drawSplitString( String str, int x, int y, int w, int col )
	{
		Minecraft.getMinecraft().fontRendererObj.drawSplitString( str, x, y, w, col );
	}
}
