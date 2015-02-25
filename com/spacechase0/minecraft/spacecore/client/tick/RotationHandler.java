package com.spacechase0.minecraft.spacecore.client.tick;

import java.lang.reflect.Field;
import java.util.EnumSet;

import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscatedField;
import com.spacechase0.minecraft.spacecore.client.event.OrientationRotationEvent;
import com.spacechase0.minecraft.spacecore.client.render.HelmetEffectsHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;

public class RotationHandler
{
	@SubscribeEvent
	public void tick( RenderTickEvent event )
	{
		if ( !event.phase.equals( Phase.START ) ) return;
		
		Minecraft mc = Minecraft.getMinecraft();
		EntityRenderer re = mc.entityRenderer;
		if ( re != null )
		{
			try
			{
				OrientationRotationEvent rotEvent = new OrientationRotationEvent( 0 );
				MinecraftForge.EVENT_BUS.post( rotEvent );
				
				ObfuscatedField obf = ObfuscatedField.fromMcp( "net/minecraft/client/renderer/EntityRenderer", "camRoll" );
				Field f = EntityRenderer.class.getDeclaredField( obf.srgName );
				f.setAccessible( true );
				f.set( re, rotEvent.rotation );
			}
			catch ( Exception exception )
			{
				exception.printStackTrace();
			}
		}
	}
}
