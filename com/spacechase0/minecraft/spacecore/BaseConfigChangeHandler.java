package com.spacechase0.minecraft.spacecore;

import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
