package com.spacechase0.minecraft.spacecore;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import com.spacechase0.minecraft.spacecore.asm.obf.ObfuscationUtils;
import com.spacechase0.minecraft.spacecore.block.Blocks;
import com.spacechase0.minecraft.spacecore.item.Items;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.LanguageRegistry;

public abstract class BaseMod
{
	// If access is needed to the config file, blocks, or items just create the
	// appropriate fields in the sub-class.
	public BaseMod( String theMod )
	{
		mod = theMod;
		
		init( "Blocks" );
		init( "Items" );
		
		setSubClassField( "blocks" );
		setSubClassField( "items" );
	}
	
	// You still have to override these methods, because @EventHandler only checks that class :(
	@EventHandler
	public void preInit( FMLPreInitializationEvent event )
	{
		config = new Configuration( event.getSuggestedConfigurationFile() );
		config.load();

		setSubClassField( "config" );

		if ( blocks != null ) blocks.register( this, config );
		if ( items != null ) items.register( this, config );
	}
	
	@EventHandler
	public void init( FMLInitializationEvent event )
	{
		registerLanguage();
		
		// TODO: Allow a way for sub-mods to override this? Is it needed?
		FMLCommonHandler.instance().bus().register( configChangeHandler = new BaseConfigChangeHandler( this ) );
	}
	
	@EventHandler
	public void postInit( FMLPostInitializationEvent event )
	{
		config.save();
	}
	
	public void syncConfig()
	{
		if ( config.hasChanged() )
		{
			config.save();
		}
	}
	
	// Example: init( "Blocks" )
	// Finds <sub-class package>.block.Blocks and instantiates it into the local "blocks" field
	private void init( String str )
	{
		String singular = str.substring( 0, str.length() - 1 );
		String singularLower = singular.toLowerCase();
		try
		{
			Class c = this.getClass();
			String pkg = c.getName().substring( 0, c.getName().lastIndexOf( '.' ) );
			Class objClass = Class.forName( pkg + "." + singularLower + "." + ( mod.equals( "spacecore" ) ? "Core" : "" ) + str );
			
			Object obj = objClass.getConstructor().newInstance();
			BaseMod.class.getDeclaredField( str.toLowerCase() ).set( this, obj );
		}
		catch ( ClassNotFoundException exception )
		{
		}
		catch ( InstantiationException exception )
		{
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
	}
	
	private void setSubClassField( String str )
	{
		try
		{
			Field thisField = BaseMod.class.getDeclaredField( str );
			Object obj = thisField.get( this );
			if ( obj != null )
			{
				Field otherField = this.getClass().getDeclaredField( str );
				otherField.set( this, obj );
			}
		}
		catch ( NoSuchFieldException exception )
		{
			// Don't want to error if so
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
	}
	
	private void registerLanguage()
	{
		try
		{
			String path = "/assets/" + mod + "/lang/lang.txt";
			InputStream stream = this.getClass().getResourceAsStream( path );
			InputStreamReader reader = new InputStreamReader( stream );
			BufferedReader buffer = new BufferedReader( reader );
			
			while ( true )
			{
				String line = buffer.readLine();
				if ( line == null )
				{
					break;
				}
				
				registerLanguage( line );
			}
		}
		catch ( Exception exception )
		{
		}
	}
	
	protected void registerLanguage( String lang )
	{
		String path = "/assets/" + mod + "/lang/" + lang + ".lang";
		LanguageRegistry.instance().loadLocalization( path, lang, false );
	}
	
	// For @AutoConfig
	public Configuration getConfig()
	{
		return config;
	}

	protected final String mod;
	private Configuration config;
	private Blocks blocks;
	private Items items;

	private BaseConfigChangeHandler configChangeHandler;
}
