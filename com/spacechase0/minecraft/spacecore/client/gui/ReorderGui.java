package com.spacechase0.minecraft.spacecore.client.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.spacechase0.minecraft.spacecore.client.sound.SoundUtil;
import com.spacechase0.minecraft.spacecore.util.ClientUtils;
import com.spacechase0.minecraft.spacecore.util.INotifiable;

import cpw.mods.fml.client.FMLClientHandler;

public class ReorderGui extends Gui
{
	public ReorderGui( int theX, int theY, int theWidth, List< String > theData )
	{
		x = theX;
		y = theY;
		width = theWidth;
		
		data = theData;
	}
	
	public void pleaseNotify( INotifiable obj )
	{
		toNotify = obj;
	}

	public void mouseClicked( int mouseX, int mouseY, int button )
	{
		mx = mouseX;
		my = mouseY;
		
		if ( button != 0 )
		{
			return;
		}
        for ( int i = 0; i < data.size(); ++i )
        {
        	int upX = x + width - 18;
        	int downX = x + width - 9;
        	int buttonY = y + ( i * 9 );
        	
        	int swapA = -1, swapB = -1;
        	if ( getButtonState( upX, buttonY, ( i == 0 ) ) == 2 )
        	{
        		swapA = i;
        		swapB = i - 1;
        	}
        	else if ( getButtonState( downX, buttonY, ( i == data.size() - 1 ) ) == 2 )
        	{
        		swapA = i;
        		swapB = i + 1;
        	}
        	
        	if ( swapA != -1 )
        	{
    			SoundUtil.playSound( "gui.button.press" );
                
        		String tmpA = data.get( swapA );
        		String tmpB = data.get( swapB );
        		data.set( swapA, tmpB );
        		data.set( swapB, tmpA );
        		
        		toNotify.notify( this, swapA, swapB );
        		
        		break;
        	}
        }
	}
	
	public void draw( FontRenderer fontRenderer, int mouseX, int mouseY )
	{
		mx = mouseX;
		my = mouseY;
		
        GL11.glColor4f( 1.0F, 1.0F, 1.0F, 1.0F );
        
        for ( int i = 0; i < data.size(); ++i )
        {
        	ClientUtils.bindTexture( "spacecore:textures/gui/reorder.png" );
        	drawButton( x + width - 18, y + ( i * 9 ), 0, ( i == 0 ) );
        	drawButton( x + width -  9, y + ( i * 9 ), 1, ( i == data.size() - 1 ) );
        	ClientUtils.drawString( data.get( i ), x, y + ( i * 9 ) , 0xFFFFFFFF );
        }
	}
	
	private void drawButton( int x, int y, int type, boolean valid )
	{
        drawTexturedModalRect( x, y, type * 9, getButtonState( x, y, valid ) * 9, 9, 9 );
	}
	
	private int getButtonState( int x, int y, boolean cancel )
	{
		if ( cancel )
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
	
	private List< String > data;
	
	private int mx, my;
	
	private INotifiable toNotify;
}
