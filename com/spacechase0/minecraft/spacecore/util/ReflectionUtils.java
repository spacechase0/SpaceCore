package com.spacechase0.minecraft.spacecore.util;

import java.lang.reflect.Field;

public class ReflectionUtils
{
	public static Object getStatic( Class c, String name ) throws Exception
	{
		Field f = c.getDeclaredField( name );
		f.setAccessible( true );
		return f.get( null );
	}
	
	public static Object getObj( Object obj, String name ) throws Exception
	{
		Class c = obj.getClass();
		Field f = c.getDeclaredField( name );
		f.setAccessible( true );
		return f.get( obj );
	}
}
