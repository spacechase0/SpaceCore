package com.spacechase0.minecraft.spacecore.block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.server.management.ItemInWorldManager;

// NOTE: By players
public class BlockDestroyedNotifier
{
	public static void intercept( ItemInWorldManager iiwm, int x, int y, int z )
	{
		Iterator< IBlockDestroyedMonitor > it = monitors.iterator();
		while ( it.hasNext() )
		{
			IBlockDestroyedMonitor mod = it.next();
			mod.blockDestroyed( iiwm, x, y, z );
		}
	}
	
	public static void addMonitor( IBlockDestroyedMonitor mod )
	{
		monitors.add( mod );
	}
	
	private static List< IBlockDestroyedMonitor > monitors = new ArrayList< IBlockDestroyedMonitor >();
}
