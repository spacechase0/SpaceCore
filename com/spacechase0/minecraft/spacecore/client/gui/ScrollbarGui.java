package com.spacechase0.minecraft.spacecore.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import org.lwjgl.opengl.GL11;

import com.spacechase0.minecraft.spacecore.util.ClientUtils;

public class ScrollbarGui extends Gui
{
	public ScrollbarGui( int theX, int theY, int theHeight )
	{
		x = theX;
		y = theY;
		height = theHeight - 15;
		pos = 0;
	}

	public void mouseClickMove( int mouseX, int mouseY, int button, long timeSinceClick )
	{
		//System.out.println(x+" "+y+" "+height+"   "+mouseX+" "+mouseY);
		if ( button != 0 || mouseX < x || mouseX >= x + 12 || mouseY < y || mouseY >= y + height + 15 )
		{
			return;
		}
		
		pos = mouseY - y - 7;
		if ( pos < 0 ) pos = 0;
		if ( pos > height ) pos = height;
	}
	
	public void draw( FontRenderer fontRenderer, int mouseX, int mouseY )
	{
        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
        ClientUtils.bindTexture( "spacecore:textures/gui/scrollbar.png" );
        drawTexturedModalRect( x, y + pos, 0, 0, 12, 15 );
	}
	
	public int x;
	public int y;
	public int height;
	public int pos;
}
