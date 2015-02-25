package com.spacechase0.minecraft.spacecore.client.render.item;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;

public class GenericItemRenderer implements IItemRenderer
{
	public GenericItemRenderer()
	{
	}
	
	@Override
	public boolean handleRenderType( ItemStack item, ItemRenderType type )
	{
		return !( type.equals( ItemRenderType.FIRST_PERSON_MAP ) );//type.equals( ItemRenderType.INVENTORY ) || type.equals( ItemRenderType.EQUIPPED ) || type.equals( ItemRenderType.EQUIPPED_FIRST_PERSON );
	}

	@Override
	public boolean shouldUseRenderHelper( ItemRenderType type, ItemStack item, ItemRendererHelper helper )
	{
		return helper.equals( ItemRendererHelper.ENTITY_ROTATION ) || helper.equals( ItemRendererHelper.ENTITY_BOBBING );
	}

	@Override
	public void renderItem( ItemRenderType type, ItemStack stack, Object... data )
	{
		GL11.glPushMatrix();
		Tessellator tessellator = Tessellator.instance;
		
		EntityPlayer player = ( data.length > 1 && data[ 1 ] instanceof EntityPlayer ) ? ( ( EntityPlayer ) data[ 1 ] ) : null;
		ItemStack inUse = null;
		int useCount = 0;
		if ( type.equals( ItemRenderType.INVENTORY ) )
		{
			player = FMLClientHandler.instance().getClient().thePlayer;
		}
		if ( player != null )
		{
			inUse = player.getItemInUse();
			if ( inUse == stack )
			{
				useCount = player.getItemInUseCount();
			}
		}
		
		//if(player!=null)System.out.println(player.getItemInUseCount());
		for ( int i = 0; i < stack.getItem().getRenderPasses( stack.getItemDamage() ); ++i )
		{
	        IIcon icon = stack.getItem().getIcon( stack, i, player, inUse, useCount );
	        if ( icon == null )
	        {
	        	continue;
	        }
	        
            float minU = icon.getMinU();
            float maxU = icon.getMaxU();
            float minV = icon.getMinV();
            float maxV = icon.getMaxV();
            
	    	FMLClientHandler.instance().getClient().renderEngine.bindTexture( TextureMap.locationItemsTexture );
	    	if ( type.equals( ItemRenderType.INVENTORY ) )
	    	{
	    		GL11.glEnable( GL11.GL_ALPHA_TEST ); // Isn't always set to this - especially after enchanted items
		    	tessellator.startDrawingQuads();
	    		tessellator.addVertexWithUV(  0,  0, 0, minU, minV );
	    		tessellator.addVertexWithUV(  0, 16, 0, minU, maxV );
	    		tessellator.addVertexWithUV( 16, 16, 0, maxU, maxV );
	    		tessellator.addVertexWithUV( 16,  0, 0, maxU, minV );
		    	tessellator.draw();
	    	}
	    	else
	    	{
	    		GL11.glPushMatrix();
	    		if ( type.equals( ItemRenderType.ENTITY ) )
	    		{
	    			GL11.glTranslatef( -0.5f, 0, 0 );
	    			//GL11.glTranslatef( -0.5f, 0.f, -0.5f );
	    		}
	    		RenderManager.instance.itemRenderer.renderItemIn2D( tessellator, maxU, minV, minU, maxV, icon.getIconWidth(), icon.getIconHeight(), 0.0625F );
	    		GL11.glPopMatrix();
	    	}
	    }
		GL11.glPopMatrix();
	}
}
