package com.spacechase0.minecraft.spacecore.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import com.spacechase0.minecraft.spacecore.BaseMod;
import com.spacechase0.minecraft.spacecore.SpaceCore;

import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.client.config.GuiConfigEntries.CategoryEntry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

// Unlike BaseConfigGui, this isn't inherited from - it is used as a base for ASM generation.
@SideOnly( Side.CLIENT )
public class BaseConfigGui extends GuiConfig
{
	public BaseConfigGui( GuiScreen parent )
	{
		super( parent, getConfigElements(), getModId(), false, true, getConfigTitle() );
		//super( parent, new ConfigElement( SpaceCore.config.getCategory( "general" ) ).getChildElements(), "SC0_SpaceCore", false, true, GuiConfig.getAbridgedConfigPath( SpaceCore.config.toString() ) );
	}
	
	private static List< IConfigElement > getConfigElements()
	{
		List< IConfigElement > list = new ArrayList< IConfigElement >();
		
		// This will be repeated by the generator for each category
		list.add( new DummyCategoryElement( getAutoConfig().categories()[ 0 ], getConfigTitle() + ":" + getAutoConfig().categories()[ 0 ], DummyEntry.class ) );
		
		return list;
	}
	
	protected static String getModId()
	{
		return getMod().getClass().getAnnotation( Mod.class ).modid();
	}
	
	protected static BaseMod getMod()
	{
		return null;
	}
	
	protected static String getConfigTitle()
	{
		return GuiConfig.getAbridgedConfigPath( getMod().getConfig().toString() );
	}
	
	protected static AutoConfig getAutoConfig()
	{
		return getMod().getClass().getAnnotation( AutoConfig.class );
	}
	
	// Duplicated and modified for each respective category.
	private static class DummyEntry extends CategoryEntry
	{
		public DummyEntry( GuiConfig parent, GuiConfigEntries entries, IConfigElement element )
		{
			super( parent, entries, element );
		}
		
		@Override
		protected GuiScreen buildChildScreen()
		{
			return new GuiConfig( owningScreen, new ConfigElement( getMod().getConfig().getCategory( "general" ) ).getChildElements(),
			                      getModId(), "general", false, true, getConfigTitle() );
		}
	}
}
