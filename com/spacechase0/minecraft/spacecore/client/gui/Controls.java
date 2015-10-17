package com.spacechase0.minecraft.spacecore.client.gui;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class Controls
{
	public static void addKeyBinding( KeyBinding key )
	{
		/*
		Minecraft mc = FMLClientHandler.instance().getClient();
		GameSettings settings = mc.gameSettings;
		
		KeyBinding[] keys = new KeyBinding[ settings.keyBindings.length + 1 ];
		for ( int i = 0; i < settings.keyBindings.length; ++i )
		{
			keys[ i ] = settings.keyBindings[ i ];
		}
		keys[ keys.length - 1 ] = key;
		
		settings.keyBindings = keys;
		*/
		// How did I miss this?
		ClientRegistry.registerKeyBinding( key );
	}
}
