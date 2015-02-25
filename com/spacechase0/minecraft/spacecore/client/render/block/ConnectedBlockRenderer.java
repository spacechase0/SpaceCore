package com.spacechase0.minecraft.spacecore.client.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public class ConnectedBlockRenderer extends GenericConnectedRenderer
{
	public ConnectedBlockRenderer( String theMod, Block theBlock )
	{
		super( theMod, theBlock );
	}

	@Override
	public boolean renderWorldBlock( IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks render )
	{
		super.renderWorldBlock( world, x, y, z, block, modelId, render );
		render.renderStandardBlock( block, x, y, z );
		
		return true;
	}
}
