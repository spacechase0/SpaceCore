package com.spacechase0.minecraft.spacecore.item;

import java.lang.reflect.Field;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.spacechase0.minecraft.spacecore.util.AutoRegister;
import com.spacechase0.minecraft.spacecore.util.ModObject;

public abstract class Items extends AutoRegister
{
	@Override
	protected boolean isValid( Class c )
	{
		if ( c.getSuperclass() == null )
		{
			return false;
		}
		
		return ( c.getSuperclass().equals( Item.class ) || isValid( c.getSuperclass() ) );
	}

	@Override
	protected void registerSpecific( Field field, ModObject annot, Object result )
	{
		String name = annot.name();
		if ( annot.name().equals( "" ) )
		{
			name = field.getName();
		}
		
		GameRegistry.registerItem( ( Item ) result, name );
	}
}
