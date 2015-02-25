package com.spacechase0.minecraft.spacecore.block;

import net.minecraft.server.management.ItemInWorldManager;

public interface IBlockDestroyedMonitor
{
	public void blockDestroyed( ItemInWorldManager manager, int x, int y, int z );
}
