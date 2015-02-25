package com.spacechase0.minecraft.spacecore.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvFile
{
	public CsvFile( InputStream stream ) throws IOException
	{
		loadFromStream( stream );
	}
	
	public void loadFromStream( InputStream stream ) throws IOException
	{
		InputStreamReader reader = new InputStreamReader( stream );
		BufferedReader buffer = new BufferedReader( reader );
		
		while ( true )
		{
			String line = buffer.readLine();
			if ( line == null )
			{
				break;
			}
			
			if ( line.length() == 0 )
			{
				rows.add( new ArrayList< String >() );
			}
			
			// TODO: Make work for everything
			
			List< String > cells = new ArrayList< String >();
			
			String data = "";
			boolean escaped = false;
			boolean quoted = false;
			for ( int ic = 0; ic < line.length(); ++ic )
			{
				char c = line.charAt( ic );
				if ( c == '\\' )
				{
					escaped = !escaped;
					data += c;
				}
				else if ( c == '"' && !escaped )
				{
					quoted = !quoted;
				}
				else if ( c == ',' && !quoted )
				{
					cells.add( data );
					data = "";
				}
				else
				{
					data += c;
				}
			}
			cells.add( data );
			
			rows.add( cells );
		}
	}
	
	public int getRowCount()
	{
		return rows.size();
	}
	
	public int getCellCount( int row )
	{
		return rows.get( row ).size();
	}
	
	public String getCell( int row, int cell )
	{
		return rows.get( row ).get( cell );
	}
	
	private List< List< String > > rows = new ArrayList< List< String > >();
}
