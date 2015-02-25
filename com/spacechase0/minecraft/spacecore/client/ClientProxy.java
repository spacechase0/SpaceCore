package com.spacechase0.minecraft.spacecore.client;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.common.MinecraftForge;

import com.spacechase0.minecraft.spacecore.CommonProxy;
import com.spacechase0.minecraft.spacecore.SpaceCore;
import com.spacechase0.minecraft.spacecore.client.render.HelmetEffectsHandler;
import com.spacechase0.minecraft.spacecore.client.tick.DebugLightHandler;
import com.spacechase0.minecraft.spacecore.client.tick.RotationHandler;
import com.spacechase0.minecraft.spacecore.client.tick.SplashHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		FMLCommonHandler.instance().bus().register( splash = new SplashHandler() );
		FMLCommonHandler.instance().bus().register( rotation = new RotationHandler() );
		
		if ( SpaceCore.config.getBoolean( "helmetNameEffects", "general", true, "Does special things when helmets are named certain things." ) )
		{
			MinecraftForge.EVENT_BUS.register( helmetEffects = new HelmetEffectsHandler() );
		}
		
		if ( SpaceCore.config.getBoolean( "debugLightDisplay", "general", false, "Shows the light map in the bottom-left corner of the F3 display." ) )
		{
			FMLCommonHandler.instance().bus().register( debugLight = new DebugLightHandler() );
		}

		// Why was this here?
		//Minecraft.getMinecraft().refreshResources();
	}
	
	@Override
	public boolean isClient()
	{
		return true;
	}
	
	private SplashHandler splash;
	private RotationHandler rotation;
	private DebugLightHandler debugLight;
	
	private HelmetEffectsHandler helmetEffects;
}
