package com.spacechase0.minecraft.spacecore.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtils
{
	public static < T > List< T > asList( T... ts )
	{
		List< T > list = new ArrayList< T >();
		for ( T t : ts ) list.add( t );
		return list;
	}
	
	public static < T > List< T > clone( List< T > toClone )
	{
		List< T > list = new ArrayList< T >();
		for ( T t : toClone ) list.add( t );
		return list;
	}
	
	public static < T > List< T > combine( List< T > a, List< T > b )
	{
		List< T > list = new ArrayList< T >();
		for ( T t : a ) list.add( t );
		for ( T t : b ) list.add( t );
		return list;
	}
	
	/*public static < T > List< T > getListOf( T t, int count )
	{
		List< T > list = new ArrayList< T >();
		for ( int i = 0; i < count; ++i )
		{
			list.add( t );
		}
		return list;
	}*/
}
