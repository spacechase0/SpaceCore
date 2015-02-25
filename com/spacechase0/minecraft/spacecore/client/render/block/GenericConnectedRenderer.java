package com.spacechase0.minecraft.spacecore.client.render.block;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.spacechase0.minecraft.spacecore.block.ConnectedBlock;

public class GenericConnectedRenderer extends GenericBlockRenderer
{
	public GenericConnectedRenderer( String theMod, Block theBlock )
	{
		mod = theMod;
		block = ( ConnectedBlock ) theBlock;
	}
	
	@Override
	public void renderInventoryBlock( Block block, int metadata, int modelId, RenderBlocks render )
	{
		GL11.glColor3f( 1, 1, 1 );
		this.block.setIconArray( this.block.getDefaultIconArray( metadata ) );
		super.renderInventoryBlock( block, metadata, modelId, render );
	}

	@Override
	public boolean renderWorldBlock( IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks render )
	{
		this.block.setIconArray( new IIcon[]
				                 {
				            	 	getIconForSide( world, x, y, z, ForgeDirection.DOWN ),
				            	 	getIconForSide( world, x, y, z, ForgeDirection.UP ),
				            	 	getIconForSide( world, x, y, z, ForgeDirection.NORTH ),
				            	 	getIconForSide( world, x, y, z, ForgeDirection.SOUTH ),
				            	 	getIconForSide( world, x, y, z, ForgeDirection.WEST ),
				            	 	getIconForSide( world, x, y, z, ForgeDirection.EAST ),
				                 } );
		
		return false;
	}
	
	protected IIcon getIconForSide( IBlockAccess world, int x, int y, int z, ForgeDirection dir )
	{
		return block.getConnectedIcon( world.getBlockMetadata( x, y, z ), getIndexForSide( world, x, y, z, dir ) );
	}
	
	private int getIndexForSide( IBlockAccess world, int x, int y, int z, ForgeDirection dir )
	{
		int[] upDirs = { 2, 2, 1, 1, 1, 1, };
		int[] leftDirs = { 4, 4, 5, 4, 2, 3 };

		// Added in 1.7 - not sure why it fixes the issue :P
		leftDirs[2]=ForgeDirection.getOrientation(leftDirs[2]).getOpposite().ordinal();
		leftDirs[5]=ForgeDirection.getOrientation(leftDirs[5]).getOpposite().ordinal();
		
		ForgeDirection up = ForgeDirection.getOrientation( upDirs[ dir.ordinal() ] );
		ForgeDirection down = up.getOpposite();
		ForgeDirection left = ForgeDirection.getOrientation( leftDirs[ dir.ordinal() ] );
		ForgeDirection right = left.getOpposite();
		
		int texIndex = 0;
		if ( world.getBlock( x + up.offsetX, y + up.offsetY, z + up.offsetZ ) == block )
		{
			texIndex |= UP;
		}
		if ( world.getBlock( x + down.offsetX, y + down.offsetY, z + down.offsetZ ) == block )
		{
			texIndex |= DOWN;
		}
		if ( world.getBlock( x + left.offsetX, y + left.offsetY, z + left.offsetZ ) == block )
		{
			texIndex |= LEFT;
		}
		if ( world.getBlock( x + right.offsetX, y + right.offsetY, z + right.offsetZ ) == block )
		{
			texIndex |= RIGHT;
		}
		
		return texIndex;
	}
	
	private final String mod;
	protected final ConnectedBlock block;

	private static final int LEFT = 1 << 0;
	private static final int RIGHT = 1 << 1;
	private static final int UP = 1 << 2;
	private static final int DOWN = 1 << 3;
}
