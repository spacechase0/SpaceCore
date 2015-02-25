package com.spacechase0.minecraft.spacecore.client.render;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;

public class TextureOverrides
{
	public static ResourceLocation intercept( ResourceLocation loc )
	{
		ResourceLocation result = overrides.get( loc );
		if ( result == null )
		{
			result = loc;
		}
		
		return result;
	}
	
	public static void addOverride( ResourceLocation orig, ResourceLocation override )
	{
		overrides.put( orig, override );
	}
	
	public static void removeOverride( ResourceLocation orig )
	{
		overrides.remove( orig );
	}
	
	private static Map< ResourceLocation, ResourceLocation > overrides = new HashMap< ResourceLocation, ResourceLocation >();
}
