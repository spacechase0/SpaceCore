package com.spacechase0.minecraft.spacecore.client.render.block;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;

import com.spacechase0.minecraft.spacecore.util.ClientUtils;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class ModelBlockRenderer implements ISimpleBlockRenderingHandler
{
	public ModelBlockRenderer( int theId, String theModelName, String theMod )
	{
		id = theId;
		modelName = theModelName;
		mod = theMod;
		model = AdvancedModelLoader.loadModel( new ResourceLocation( mod, "models/" + modelName + ".obj" ) );
	}
	
	@Override
	public void renderInventoryBlock( Block block, int metadata, int modelID, RenderBlocks renderer )
	{
	}

	@Override
	public boolean renderWorldBlock( IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer )
	{
		TextureManager re = FMLClientHandler.instance().getClient().renderEngine;
		
		glPushMatrix();
		{
			glTranslatef( x, y, z );
			ClientUtils.bindTexture( mod + ":textures/models/" + modelName + ".png" );
			
			// Dirty hack... What should I do? I don't want to make it a tile entity
			//cacheTessellator();
			model.renderAll();
			//uncacheTessellator();
			
			// Don't need this anymore?
			//re.resetBoundTexture();
		}
		glPopMatrix();
		
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory( int metadata)
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return id;
	}
	
	private final int id;
	private final String modelName;
	private final String mod;
	private IModelCustom model;
}
