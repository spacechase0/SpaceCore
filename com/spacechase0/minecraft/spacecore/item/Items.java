package com.spacechase0.minecraft.spacecore.item;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import com.spacechase0.minecraft.spacecore.asm.SpaceCoreLog;
import com.spacechase0.minecraft.spacecore.block.BlockItem;
import com.spacechase0.minecraft.spacecore.util.AutoRegister;
import com.spacechase0.minecraft.spacecore.util.ModObject;

import cpw.mods.fml.common.registry.GameRegistry;

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
