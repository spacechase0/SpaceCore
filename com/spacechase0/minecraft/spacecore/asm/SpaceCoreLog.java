package com.spacechase0.minecraft.spacecore.asm;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

public class SpaceCoreLog
{
	public static void info( String str )
	{
		logger.info( str );
	}
	
	public static void fine( String str )
	{
		logger.debug( str );
	}
	
	public static void warning( String str )
	{
		logger.warn( str );
	}
	
	public static void severe( String str )
	{
		logger.error( str );
	}
	
	private static Logger makeLogger()
	{
		Logger logger = LogManager.getLogger( "SC0_SpaceCore" );
		
		return logger;
	}
	
	private static final Logger logger = makeLogger();
}
