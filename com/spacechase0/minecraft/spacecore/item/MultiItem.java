package com.spacechase0.minecraft.spacecore.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MultiItem extends Item
{
	public MultiItem( String theMod, String theName, String[] theTypes )
	{
		mod = theMod;
		name = theName;
		types = theTypes;
		
		setUnlocalizedName( mod + ":" + name );
		setCreativeTab( CreativeTabs.tabMaterials );
	}
	
	@Override
    public void getSubItems( Item item, CreativeTabs tabs, List list )
    {
		for ( int i = 0; i < types.length; ++i )
		{
			list.add( new ItemStack( this, 1, i ) );
		}
    }

	@Override
    public String getUnlocalizedName( ItemStack stack )
    {
        return "item." + mod + ":" + types[ stack.getItemDamage() ] + name.substring( 0, 1 ).toUpperCase() + name.substring( 1 );
    }
	
	public final String mod;
	public final String name;
	public final String[] types;
}
