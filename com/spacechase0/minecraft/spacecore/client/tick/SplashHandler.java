package com.spacechase0.minecraft.spacecore.client.tick;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class SplashHandler
{
	@SubscribeEvent
	public void tick( TickEvent.ClientTickEvent event )
	{
		if ( !event.phase.equals( Phase.END ) ) return;
		
		Minecraft mc = Minecraft.getMinecraft();
		if ( mc.currentScreen instanceof GuiMainMenu )
		{
			GuiMainMenu mainMenu = ( GuiMainMenu ) mc.currentScreen;

	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime( new Date() );

	        int month = calendar.get( Calendar.MONTH ) + 1;
	        int day = calendar.get( Calendar.DAY_OF_MONTH );
	        
	        calendar.setTime( getEasterOf( calendar.get( Calendar.YEAR ) ) );
	        int easterMonth = calendar.get( Calendar.MONTH + 1 );
	        int easterDay = calendar.get( Calendar.DAY_OF_MONTH );
	        
	        if ( month == 12 && ( day == 24 || day == 25 ) )
	        {
	        	setSplashText( mainMenu, ( day == 24 ) ? "Merry Christmas!" : "Merry CHRISTmas!" );
	        }
	        else if ( month == 2 && day == 9 )
	        {
	        	setSplashText( mainMenu, "Meow!" );
	        }
	        else if ( month == easterMonth && day == easterDay )
	        {
	        	setSplashText( mainMenu, "He has risen!" );
	        }
		}
	}
	
	private void setSplashText( GuiMainMenu mainMenu, String str )
	{
		try
		{
			Field[] fields = GuiMainMenu.class.getDeclaredFields();
			for ( Field f : fields )
			{
				f.setAccessible( true );
				Object obj = f.get( mainMenu );
				if ( obj instanceof String )
				{
					f.set( mainMenu, str );
					return;
				}
			}
		}
		catch ( Exception exception )
		{
			
		}
	}
	
	private Date getEasterOf( int year )
	{
		// http://answers.yahoo.com/question/index?qid=20070923084502AAXw9Og
		Date result = null;

		int a = year % 19;
		int b = year / 100;
		int c = year % 100;
		int d = b / 4;
		int e = b % 4;
		int f = ( b + 8 ) / 25;
		int g = ( b - f + 1 ) / 3;
		int h = ( 19 * a + b - d - g + 15 ) % 30;
		int i = c / 4;
		int k = c % 4;
		int l = (32 + 2 * e + 2 * i - h - k) % 7;
		int m = (a + 11 * h + 22 * l) / 451;
		int p = (h + l - 7 * m + 114) % 31;

		int month = ( h + l - 7 * m + 114 ) / 31;
		int day = p + 1;

		GregorianCalendar gc = new GregorianCalendar(year, month - 1, day);
		return gc.getTime();
	}
}
