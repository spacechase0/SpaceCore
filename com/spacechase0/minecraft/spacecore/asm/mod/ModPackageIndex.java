package com.spacechase0.minecraft.spacecore.asm.mod;

import java.util.HashMap;
import java.util.Map;

public class ModPackageIndex
{
	public static String getModForPackage( String pkg )
	{
		return mods.get( pkg );
	}
	
	public static void setModForPackage( String pkg, String mod )
	{
		mods.put( pkg, mod );
	}
	
	private static Map< String, String > mods = new HashMap< String, String >();
}
