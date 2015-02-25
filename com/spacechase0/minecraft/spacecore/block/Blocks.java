package com.spacechase0.minecraft.spacecore.block;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.config.Configuration;

import com.spacechase0.minecraft.spacecore.BaseMod;
import com.spacechase0.minecraft.spacecore.asm.SpaceCoreLog;
import com.spacechase0.minecraft.spacecore.util.AutoRegister;
import com.spacechase0.minecraft.spacecore.util.ModObject;

import cpw.mods.fml.common.registry.GameRegistry;

public abstract class Blocks extends AutoRegister
{
	@Override
	protected boolean isValid( Class c )
	{
		if ( c.getSuperclass() == null )
		{
			return false;
		}
		
		return ( c.getSuperclass().equals( Block.class ) || isValid( c.getSuperclass() ) );
	}

	@Override
	protected void registerSpecific( Field field, ModObject annot, Object result )
	{
		String name = annot.name();
		if ( annot.name().equals( "" ) )
		{
			name = field.getName();
		}
		
		Class item = ItemBlock.class;
		if ( field.isAnnotationPresent( BlockItem.class ) )
		{
			BlockItem block = field.getAnnotation( BlockItem.class );
			item = block.item();
		}

		GameRegistry.registerBlock( ( Block ) result, item, name );
	}
}
