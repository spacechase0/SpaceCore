package com.spacechase0.minecraft.spacecore.client.render.item;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import com.spacechase0.minecraft.spacecore.util.ClientUtils;

public class ModelItemRenderer implements IItemRenderer
{
	public ModelItemRenderer( String theModelName, String theMod )
	{
		modelName = theModelName;
		mod = theMod;
		model = AdvancedModelLoader.loadModel( new ResourceLocation( mod, "models/" + modelName + ".obj" ) );
	}
	
	@Override
	public boolean handleRenderType( ItemStack item, ItemRenderType type )
	{
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper( ItemRenderType type, ItemStack item, ItemRendererHelper helper )
	{
		return true;//( helper == IItemRenderer.ItemRendererHelper.ENTITY_ROTATION || helper == IItemRenderer.ItemRendererHelper.ENTITY_BOBBING );
	}

	@Override
	public void renderItem( ItemRenderType type, ItemStack stack, Object... data )
	{
		TextureManager re = null;
		
		if ( data[ 0 ] instanceof RenderBlocks )
		{
			RenderBlocks blocks = ( RenderBlocks ) data[ 0 ];
			re = blocks.minecraftRB.renderEngine;
		}
		else if ( data.length > 1 && data[ 1 ] instanceof TextureManager )
		{
			re = ( TextureManager ) data[ 1 ];
		}
		
		ClientUtils.bindTexture( mod + ":" + "/textures/models/" + modelName + ".png" );
		
        GL11.glPushMatrix();
        {
        	if ( type == ItemRenderType.EQUIPPED )
        	{
        		GL11.glTranslatef( ( float ) 0.5f, ( float ) 0.5f, ( float ) 0.5f );
        	}
	        
	        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
	        model.renderAll();
        }
        GL11.glPopMatrix();
		
		GL11.glColor3f( 1.f, 1.f, 1.f );
		
		//re.resetBoundTexture();
	}

	protected final String modelName;
	protected final String mod;
	protected IModelCustom model;
}
