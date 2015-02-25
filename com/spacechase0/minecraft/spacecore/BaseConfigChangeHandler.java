package com.spacechase0.minecraft.spacecore;

import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BaseConfigChangeHandler
{
	public BaseConfigChangeHandler( BaseMod theMod )
	{
		mod = theMod;
	}
	
	@SubscribeEvent
	public void configChanged( OnConfigChangedEvent event )
	{
		if ( event.modID.equals( mod.getClass().getAnnotation( Mod.class ).modid() ) )
		{
			mod.syncConfig();
		}
	}
	
	private BaseMod mod;
}
