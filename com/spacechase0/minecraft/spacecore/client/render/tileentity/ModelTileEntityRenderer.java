package com.spacechase0.minecraft.spacecore.client.render.tileentity;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import com.spacechase0.minecraft.spacecore.util.ClientUtils;

public class ModelTileEntityRenderer extends TileEntitySpecialRenderer
{
	public ModelTileEntityRenderer( String theModelName, String theMod )
	{
		modelName = theModelName;
		mod = theMod;
		model = AdvancedModelLoader.loadModel( new ResourceLocation( mod, "models/" + modelName + ".obj" ) );
	}
	
	@Override
	public void renderTileEntityAt( TileEntity entity, double x, double y, double z, float f )
	{
		ClientUtils.bindTexture( mod + ":textures/models/" + modelName + ".png" );
		
        GL11.glPushMatrix();
        {
	        GL11.glTranslatef( ( float ) x + 0.5f, ( float ) y + 0.5f, ( float ) z + 0.5f );
	        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
	        model.renderAll();
        }
        GL11.glPopMatrix();
	}

	protected final String modelName;
	protected final String mod;
	protected IModelCustom model;
}
