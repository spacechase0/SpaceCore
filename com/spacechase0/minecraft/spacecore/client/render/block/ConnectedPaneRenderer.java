package com.spacechase0.minecraft.spacecore.client.render.block;

import java.lang.reflect.Method;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscatedMethod;
import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscationUtils;
import com.spacechase0.minecraft.spacecore.block.ConnectedBlock;

public class ConnectedPaneRenderer extends GenericConnectedRenderer
{
	public ConnectedPaneRenderer( String theMod, Block theBlock )
	{
		super( theMod, theBlock );
	}
	
	@Override
	public void renderInventoryBlock( Block block, int metadata, int modelId, RenderBlocks render )
	{
		super.renderInventoryBlock( block, metadata, modelId, render );
	}

	@Override
	public boolean renderWorldBlock( IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks render )
	{
		super.renderWorldBlock( world, x, y, z, block, modelId, render );
		renderBlockPane( ( BlockPane ) block, world, x, y, z, render );
		
		return true;
	}

	private void renderBlockPane( BlockPane pane, IBlockAccess world, int x, int y, int z, RenderBlocks render )
	{
		// Editing didn't work, let's try doing it from scratch :P

		// Used to be cleaner
		boolean connU = pane.canPaneConnectTo( world, x, y + 1, z, ForgeDirection.UP );
		boolean connD = pane.canPaneConnectTo( world, x, y - 1, z, ForgeDirection.DOWN );
		boolean connN = pane.canPaneConnectTo( world, x, y, z - 1, ForgeDirection.NORTH );
		boolean connS = pane.canPaneConnectTo( world, x, y, z + 1, ForgeDirection.SOUTH );
		boolean connW = pane.canPaneConnectTo( world, x - 1, y, z, ForgeDirection.WEST );
		boolean connE = pane.canPaneConnectTo( world, x + 1, y, z, ForgeDirection.EAST );
		
		float vStart = 0.f, vEnd = 1.f;
		float hStart = 0.f, hEnd = 1.f;
		
		// Probably could be cleaner
		if ( connN || connS || connW || connE )
		{
			vStart = vEnd = hStart = hEnd = -1.f;
			if ( connN )
			{
				vStart = 0.f;
				if ( !connS )
				{
					vEnd = 0.5675f;
				}
			}
			if ( connS )
			{
				vEnd = 1.f;
				if ( !connN )
				{
					vStart = 0.4375f;
				}
			}
			if ( connW )
			{
				hStart = 0.f;
				if ( !connE )
				{
					hEnd = 0.5675f;
				}
			}
			if ( connE )
			{
				hEnd = 1.f;
				if ( !connW )
				{
					hStart = 0.4375f;
				}
			}
		}
		
		Tessellator tess = Tessellator.instance;

		int meta = world.getBlockMetadata( x, y, z );
		IIcon paneTop = ( ( ConnectedBlock ) pane ).getBorderIcon( meta );
		
		if ( vStart >= 0.f && vEnd >= 0.f )
		{
			IIcon[] icons = new IIcon[]
					        {
					        	paneTop,
					        	paneTop,
					        	paneTop,
					        	paneTop,
					        	pane.getIcon( ForgeDirection.WEST.ordinal(), 0 ),
					        	pane.getIcon( ForgeDirection.EAST.ordinal(), 0 ),
					        };
			
			renderStrip( icons, x, y, z, new boolean[] { !connD, !connU, !connN, !connS, true, true }, 4, render, vStart, vEnd );
		}
		if ( hStart >= 0.f && hEnd >= 0.f )
		{
			IIcon[] icons = new IIcon[]
			                {
			                	paneTop,
			                	paneTop,
			                	pane.getIcon( ForgeDirection.NORTH.ordinal(), 0 ),
			                	pane.getIcon( ForgeDirection.SOUTH.ordinal(), 0 ),
			                	paneTop,
			                	paneTop,
			                };
		
			renderStrip( icons, x, y, z, new boolean[] { !connD, !connU, true, true, !connW, !connE }, 2, render, hStart, hEnd );
		}
	}
	
	// 0.4375
	// 0.5625
	
	private void renderStrip( IIcon[] icons, float x, float y, float z, boolean[] render, int dirIndex, RenderBlocks blocks, float primStart, float primEnd )
	{
		Tessellator tess = Tessellator.instance;
		
		float rx = 0, ry = 0, rz = 0;
		float rw = 1, rh = 1, rd = 1;
		
		if ( dirIndex == 2 )
		{
			rz += 0.4375f;
			rd = 0.125f;
			
			rx = primStart;
			rw = primEnd - primStart;
			
			blocks.uvRotateTop = blocks.uvRotateBottom = 1;
		}
		else if ( dirIndex == 4 )
		{
			rx += 0.4375f;
			rw = 0.125f;
			
			rz = primStart;
			rd = primEnd - primStart;
		}
		
		blocks.setRenderBounds( rx, ry, rz, rx + rw, ry + rh, rz + rd );
		
		//String[] funcs = new String[] { "renderFaceYNeg", "renderFaceYPos", "renderFaceZNeg", "renderFaceZPos", "renderFaceXNeg", "renderFaceXPos" };
		String[] funcs = new String[] { "func_147768_a", "func_147806_b", "func_147761_c", "func_147734_d", "func_147798_e", "func_147764_f" };
		FaceRenderer[] faces = new FaceRenderer[ funcs.length ];
		for ( int i = 0; i < faces.length; ++i )
		{
			faces[ i ] = new FaceRenderer( funcs[ i ], i );
		}
		FaceRenderer.block = ( Block ) block;
		FaceRenderer.render = blocks;
		
		tess.setColorOpaque_I( 0xFFFFFFFF );
		
		for ( int i = 0; i < 6; ++i )
		{
			if ( !render[ i ] ) continue;
			faces[ i ].render( x, y, z, icons[ i ] );
		}
		
		if ( dirIndex == 2 ) blocks.uvRotateTop = blocks.uvRotateBottom = 0;
	}
	
	private static class FaceRenderer
	{
		public FaceRenderer( String theMethod, int theSide )
		{
			try
			{
				Class c = RenderBlocks.class;
				method = c.getDeclaredMethod( theMethod, Block.class, double.class, double.class, double.class, IIcon.class );
			}
			catch ( Exception exception )
			{
				exception.printStackTrace();
				method = null;
			}
			side = theSide;
		}
		
		public void render( double x, double y, double z, IIcon icon )
		{
			try
			{
				method.invoke( render, block, x, y, z, icon );
			}
			catch ( Exception exception )
			{
				exception.printStackTrace();
			}
		}
		
		private Method method;
		private int side;
		public static RenderBlocks render;
		public static Block block;
	}
}
