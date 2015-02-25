package com.spacechase0.minecraft.spacecore.client.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class GenericBlockRenderer implements ISimpleBlockRenderingHandler
{
	@Override
	public void renderInventoryBlock( Block block, int metadata, int modelID, RenderBlocks render )
	{
		// Copy+paste from RenderBlocks. Bleh
		// The primary reason of this class is this :P
		Block p_147800_1_ = block;
		Tessellator tessellator = Tessellator.instance;
		RenderBlocks this_ = render;
		boolean flag = false;
		int p_147800_2_ = metadata;
		float p_147800_3_ = 1.f;
		
		int k;
		float f2, f3;

        p_147800_1_.setBlockBoundsForItemRender();
        this_.setRenderBoundsFromBlock(p_147800_1_);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        this_.renderFaceYNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, this_.getBlockIconFromSideAndMetadata(p_147800_1_, 0, p_147800_2_));
        tessellator.draw();

        if (flag && this_.useInventoryTint)
        {
            k = p_147800_1_.getRenderColor(p_147800_2_);
            f2 = (float)(k >> 16 & 255) / 255.0F;
            f3 = (float)(k >> 8 & 255) / 255.0F;
            float f4 = (float)(k & 255) / 255.0F;
            GL11.glColor4f(f2 * p_147800_3_, f3 * p_147800_3_, f4 * p_147800_3_, 1.0F);
        }

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        this_.renderFaceYPos(p_147800_1_, 0.0D, 0.0D, 0.0D, this_.getBlockIconFromSideAndMetadata(p_147800_1_, 1, p_147800_2_));
        tessellator.draw();

        if (flag && this_.useInventoryTint)
        {
            GL11.glColor4f(p_147800_3_, p_147800_3_, p_147800_3_, 1.0F);
        }

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        this_.renderFaceZNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, this_.getBlockIconFromSideAndMetadata(p_147800_1_, 2, p_147800_2_));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        this_.renderFaceZPos(p_147800_1_, 0.0D, 0.0D, 0.0D, this_.getBlockIconFromSideAndMetadata(p_147800_1_, 3, p_147800_2_));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        this_.renderFaceXNeg(p_147800_1_, 0.0D, 0.0D, 0.0D, this_.getBlockIconFromSideAndMetadata(p_147800_1_, 4, p_147800_2_));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        this_.renderFaceXPos(p_147800_1_, 0.0D, 0.0D, 0.0D, this_.getBlockIconFromSideAndMetadata(p_147800_1_, 5, p_147800_2_));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	public boolean renderWorldBlock( IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks render )
	{
		render.renderStandardBlock( block, x, y, z );
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory( int metadata )
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return renderId;
	}
	
	private final int renderId = RenderingRegistry.getNextAvailableRenderId();
}
