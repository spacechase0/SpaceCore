package com.spacechase0.minecraft.spacecore.mcp;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import com.google.common.io.Files;

public class ModInfoGenerator
{
	// Combines all the mcmod.info files into one, and places in the index of the source folder so the game can read it properly.
	public static void generate()
	{
		URL base = ModInfoGenerator.class.getResource( "/" );
		String baseAssets = base.getPath() + "/assets";
		File file = new File( baseAssets );
		
		String contents = "";
		
		for ( String dir : file.list() )
		{
			File file2 = new File( baseAssets + "/" + dir );
			if ( !file2.isDirectory() )
			{
				continue;
			}
			
			File modInfo = new File( baseAssets + "/" + dir + "/" + "mcmod.info" );
			if ( !modInfo.exists() )
			{
				continue;
			}
			
			try
			{
				String str = "";
				List< String > lines = Files.readLines( modInfo, Charset.defaultCharset() );
				for ( String line : lines )
				{
					str += line + '\n';
				}
				
				int beg = str.indexOf( '[' );
				int end = str.lastIndexOf( ']' );
				contents += str.substring( beg + 1, end ) + ',';
			}
			catch ( Exception exception )
			{
				exception.printStackTrace();
			}
		}
		
		try
		{
			contents = contents.substring( 0, contents.lastIndexOf( ',' ) );
			
			File baseInfo = new File( base.getPath() + "/mcmod.info" );
			Files.write( '[' + contents + ']', baseInfo, Charset.defaultCharset() );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
		
		//try{Thread.sleep(1000000);}catch(Throwable t){}
	}
}
