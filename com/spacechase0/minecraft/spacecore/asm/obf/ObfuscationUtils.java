package com.spacechase0.minecraft.spacecore.asm.obf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.spacechase0.minecraft.spacecore.asm.SpaceCoreLog;
import com.spacechase0.minecraft.spacecore.util.CsvFile;

public class ObfuscationUtils
{
	protected static ObfuscatedType getTypeFromObf( String name )
	{
		ObfuscatedType obj = obfTypes.get( name );
		return ( obj != null ) ? obj : new ObfuscatedType( name, name );
	}
	
	protected static ObfuscatedType getTypeFromDeobf( String name )
	{
		ObfuscatedType obj = deobfTypes.get( name );
		return ( obj != null ) ? obj : new ObfuscatedType( name, name );
	}
	
	protected static ObfuscatedMethod getMethodFromObf( String owner, String name, String desc )
	{
		ObfuscatedMethod obj = obfMethods.get( owner + "." + name + desc );
		return ( obj != null ) ? obj : new ObfuscatedMethod( owner, name, desc, name, owner, name, desc );
	}
	
	protected static ObfuscatedMethod getMethodFromSrg( String owner, String name, String desc )
	{
		ObfuscatedMethod obj = srgMethods.get( owner + "." + name + desc );
		return ( obj != null ) ? obj : new ObfuscatedMethod( owner, name, desc, name, owner, name, desc );
	}
	
	protected static ObfuscatedMethod getMethodFromMcp( String owner, String name, String desc )
	{
		ObfuscatedMethod obj = mcpMethods.get( owner + "." + name + desc );
		return ( obj != null ) ? obj : new ObfuscatedMethod( owner, name, desc, name, owner, name, desc );
	}
	
	protected static ObfuscatedField getFieldFromObf( String owner, String name )
	{
		ObfuscatedField obj = obfFields.get( owner + "." + name );
		return ( obj != null ) ? obj : new ObfuscatedField( owner, name, UNKNOWN, name, owner, name, UNKNOWN );
	}
	
	protected static ObfuscatedField getFieldFromSrg( String owner, String name )
	{
		ObfuscatedField obj = srgFields.get( owner + "." + name );
		return ( obj != null ) ? obj : new ObfuscatedField( owner, name, UNKNOWN, name, owner, name, UNKNOWN );
	}
	
	protected static ObfuscatedField getFieldFromMcp( String owner, String name )
	{
		ObfuscatedField obj = mcpFields.get( owner + "." + name );
		return ( obj != null ) ? obj : new ObfuscatedField( owner, name, UNKNOWN, name, owner, name, UNKNOWN );
	}
	
	public static String asmify( String name )
	{
		return name.replace( '.', '/' );
	}
	
	private static void readMappings()
	{
		SpaceCoreLog.fine( "Reading MCP data..." );
		try
		{
			// Cache SRG -> MCP names, since packaged.srg does not have them.
			Map< String, String > srgToMcp = new HashMap< String, String >();
			{
				InputStream stream = ObfuscationUtils.class.getResourceAsStream( "/assets/spacecore/data/mcp/mcp-srg.srg" );
				InputStreamReader reader = new InputStreamReader( stream );
				BufferedReader buffer = new BufferedReader( reader );
				
				while ( true )
				{
					String line = buffer.readLine();
					if ( line == null )
					{
						break;
					}
					
					String[] tokens = line.split( " " );
					
					String cmd = tokens[ 0 ];
					if ( cmd.equals( "FD:" ) )
					{
						String mcpName = tokens[ 1 ].substring( tokens[ 1 ].lastIndexOf( "/" ) + 1 );
						String srgName = tokens[ 2 ].substring( tokens[ 2 ].lastIndexOf( "/" ) + 1 );
						
						srgToMcp.put( srgName, mcpName );
					}
					else if ( cmd.equals( "MD:" ) )
					{
						String mcpName = tokens[ 1 ].substring( tokens[ 1 ].lastIndexOf( "/" ) + 1 );
						String srgName = tokens[ 3 ].substring( tokens[ 3 ].lastIndexOf( "/" ) + 1 );
						
						srgToMcp.put( srgName, mcpName );
					}
				}
				
				stream.close();
			}
			
			// TODO: Read a field type cache that we will generate
			
			// Pass 1, obf/srg names
			{
				InputStream stream = ObfuscationUtils.class.getResourceAsStream( "/assets/spacecore/data/mcp/packaged.srg" );
				InputStreamReader reader = new InputStreamReader( stream );
				BufferedReader buffer = new BufferedReader( reader );
				
				while ( true )
				{
					String line = buffer.readLine();
					if ( line == null )
					{
						break;
					}
					
					String[] tokens = line.split( " " );
					
					String cmd = tokens[ 0 ];
					if ( cmd.equals( "CL:" ) )
					{
						String obf = tokens[ 1 ];
						String mcp = tokens[ 2 ];
						
						ObfuscatedType type = new ObfuscatedType( obf, mcp );
						obfTypes.put( obf, type );
						deobfTypes.put( mcp, type );
					}
					else if ( cmd.equals( "FD:" ) )
					{
						String obfOwner = tokens[ 1 ].substring( 0, tokens[ 1 ].lastIndexOf( "/" ) );
						String obfName = tokens[ 1 ].substring( tokens[ 1 ].lastIndexOf( "/" ) + 1 );
						String deobfOwner = tokens[ 2 ].substring( 0, tokens[ 2 ].lastIndexOf( "/" ) );
						String srgName = tokens[ 2 ].substring( tokens[ 2 ].lastIndexOf( "/" ) + 1 );
						
						ObfuscatedField field = new ObfuscatedField( obfOwner, obfName, UNKNOWN,
						                                             srgName, 
						                                             deobfOwner, srgToMcp.get( srgName ), UNKNOWN );
						obfFields.put( obfOwner + "." + obfName, field );
						srgFields.put( deobfOwner + "." + srgName, field );
						mcpFields.put( deobfOwner + "." + field.mcpName, field );
					}
					else if ( cmd.equals( "MD:" ) )
					{
						String obfOwner = tokens[ 1 ].substring( 0, tokens[ 1 ].lastIndexOf( "/" ) );
						String obfName = tokens[ 1 ].substring( tokens[ 1 ].lastIndexOf( "/" ) + 1 );
						String obfDesc = tokens[ 2 ];
						String deobfOwner = tokens[ 3 ].substring( 0, tokens[ 3 ].lastIndexOf( "/" ) );
						String srgName = tokens[ 3 ].substring( tokens[ 3 ].lastIndexOf( "/" ) + 1 );
						String deobfDesc = tokens[ 4 ];
						
						ObfuscatedMethod method = new ObfuscatedMethod( obfOwner, obfName, obfDesc,
						                                                srgName,
						                                                deobfOwner, srgToMcp.get( srgName ), deobfDesc );
						
						obfMethods.put( obfOwner + "." + obfName + obfDesc, method );
						srgMethods.put( deobfOwner + "." + srgName + deobfDesc, method );
						mcpMethods.put( deobfOwner + "." + method.mcpName, method );
					}
				}
				
				stream.close();
			}
			
			// TODO: Read a cache we generate of the field types. Cache should be made only in MCP.
		}
		catch ( IOException exception )
		{
			SpaceCoreLog.severe( "Exception while reading MCP data: " );
			exception.printStackTrace();
		}
	}
	
	public static boolean isRuntimeDeobfuscated()
	{
		return runtimeDeobf;
	}
	
	public static void setRuntimeDeobfuscated( boolean theDeobf )
	{
		SpaceCoreLog.fine( "Setting runtime deobfuscation to " + theDeobf );
		runtimeDeobf = theDeobf;

		readMappings();
	}

	private static Map< String, ObfuscatedType > obfTypes = new HashMap< String, ObfuscatedType >();
	private static Map< String, ObfuscatedType > deobfTypes = new HashMap< String, ObfuscatedType >();
	
	private static Map< String, ObfuscatedMethod > obfMethods = new HashMap< String, ObfuscatedMethod >();
	private static Map< String, ObfuscatedMethod > srgMethods = new HashMap< String, ObfuscatedMethod >();
	private static Map< String, ObfuscatedMethod > mcpMethods = new HashMap< String, ObfuscatedMethod >();
	
	private static Map< String, ObfuscatedField > obfFields = new HashMap< String, ObfuscatedField >();
	private static Map< String, ObfuscatedField > srgFields = new HashMap< String, ObfuscatedField >();
	private static Map< String, ObfuscatedField > mcpFields = new HashMap< String, ObfuscatedField >();
	
	private static boolean runtimeDeobf = true;
	
	private static final String UNKNOWN = "*";
	
}
