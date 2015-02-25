package com.spacechase0.minecraft.spacecore.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class FileUtils
{
	public static void saveBytes( String filename, byte[] bytes )
	{
		try
		{
			BufferedOutputStream out = new BufferedOutputStream( new FileOutputStream( new File( filename ) ) );
			out.write( bytes );
			out.close();
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
	}
}
