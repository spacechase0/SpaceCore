package com.spacechase0.minecraft.spacecore.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import org.lwjgl.opengl.GL11;

import com.spacechase0.minecraft.spacecore.client.sound.SoundUtil;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;

public class NumberGui extends Gui
{
	public NumberGui( int theX, int theY, int theWidth, int theMinData, int theMaxData, int theData, String thePrefix )
	{
		x = theX;
		y = theY;
		width = theWidth;
		
		minData = theMinData;
		maxData = theMaxData;
		data = theData;
		
		prefix = thePrefix;
	}

	public void mouseClicked( int mouseX, int mouseY, int button )
	{
		mx = mouseX;
		my = mouseY;
		
		if ( button != 0 )
		{
			return;
		}
		
		if ( getButtonState( x, y, minData ) == 2 )
		{
			--data;
			SoundUtil.playSound( "gui.button.press" );
		}
		else if ( getButtonState( x + width - 9, y, maxData ) == 2 )
		{
			++data;
			SoundUtil.playSound( "gui.button.press" );
		}
	}
	
	public void draw( FontRenderer fontRenderer, int mouseX, int mouseY )
	{
		ClientUtils.bindTexture( "spacecore:textures/gui/number.png" );
		
		mx = mouseX;
		my = mouseY;
		
        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
        
        drawButton( x, y, 0, minData );
        drawButton( x + width - 9, y, 1, maxData );
        
        String str = prefix + ": " + String.valueOf( data );
        int w = fontRenderer.getStringWidth( str );
        ClientUtils.drawString( str, x + ( width / 2 ) - ( w / 2 ), y + 1, 0x00A0A0A0 );
	}
	
	private void drawButton( int x, int y, int num, int stop )
	{
        drawTexturedModalRect( x, y, num * 9, getButtonState( x, y, stop ) * 9, 9, 9 );
	}
	
	private int getButtonState( int x, int y, int stop )
	{
		if ( stop == data )
		{
			return 0;
		}
		
		if ( mx >= x && my >= y && mx < x + 9 && my < y + 9 )
		{
			return 2;
		}
		
		return 1;
	}
	
	public int x;
	public int y;
	public int width;
	
	public int minData;
	public int maxData;
	public int data;
	
	private String prefix;
	
	private int mx, my;
}
