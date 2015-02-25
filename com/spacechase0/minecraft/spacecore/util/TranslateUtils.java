package com.spacechase0.minecraft.spacecore.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;

public class TranslateUtils
{
	public static String translate( String key, Object... args )
	{
		return StatCollector.translateToLocalFormatted( key, args );
	}
	
	public static void chat( EntityPlayer player, String key, Object... args )
	{
		player.addChatMessage( new ChatComponentTranslation( key, args ) );
	}
}
