package com.spacechase0.minecraft.spacecore.client.tick;

import java.lang.reflect.Field;
import java.util.EnumSet;

import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscatedField;
import com.spacechase0.minecraft.spacecore.client.event.OrientationRotationEvent;
import com.spacechase0.minecraft.spacecore.client.render.HelmetEffectsHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import static org.lwjgl.opengl.GL11.*;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;

public class DebugLightHandler
{
	@SubscribeEvent
	public void tick( RenderTickEvent event )
	{
		if ( !event.phase.equals( Phase.END ) ) return;
		
		Minecraft mc = Minecraft.getMinecraft();
		EntityRenderer er = mc.entityRenderer;
		if ( mc.gameSettings.showDebugInfo )
		{
			DynamicTexture lightmap = getLightmapTexture( er );
			if ( lightmap == null ) return;
			
			ScaledResolution sr = new ScaledResolution( mc, mc.displayWidth, mc.displayHeight );
			
			final float SIZE = 128 / sr.getScaleFactor();
			
			glPushMatrix();
			//glBindTexture( GL_TEXTURE_2D, lightmap.getGlTextureId() );
			mc.getTextureManager().bindTexture( getLightmapTextureLocation( er ) );
			glTranslatef( 0, sr.getScaledHeight() - SIZE, 0 );
			glBegin( GL_QUADS );
			{
				glColor3f( 1, 1, 1 );
				
				glTexCoord2f( 0, 0 );
				glVertex2f( 0, 0 );

				glTexCoord2f( 0, 1 );
				glVertex2f( 0, SIZE );

				glTexCoord2f( 1, 1 );
				glVertex2f( SIZE, SIZE );

				glTexCoord2f( 1, 0 );
				glVertex2f( SIZE, 0 );
			}
			glEnd();
			glPopMatrix();
		}
	}
	
	private DynamicTexture getLightmapTexture( EntityRenderer er )
	{
		try
		{
			ObfuscatedField field = ObfuscatedField.fromMcp( "net/minecraft/client/renderer/EntityRenderer", "lightmapTexture" );
			Field f = EntityRenderer.class.getDeclaredField( field.srgName );
			f.setAccessible( true );
			return ( DynamicTexture ) f.get( er );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
		
		return null;
	}
	
	private ResourceLocation getLightmapTextureLocation( EntityRenderer er )
	{
		try
		{
			ObfuscatedField field = ObfuscatedField.fromMcp( "net/minecraft/client/renderer/EntityRenderer", "locationLightMap" );
			Field f = EntityRenderer.class.getDeclaredField( field.srgName );
			f.setAccessible( true );
			return ( ResourceLocation ) f.get( er );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
		
		return null;
	}
}
