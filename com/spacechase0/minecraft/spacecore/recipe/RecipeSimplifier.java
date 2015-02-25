package com.spacechase0.minecraft.spacecore.recipe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.spacechase0.minecraft.spacecore.SpaceCore;
import com.spacechase0.minecraft.spacecore.asm.SpaceCoreLog;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeSimplifier
{
    public static class DisplayStack
	{
		public DisplayStack( int theX, int theY, ItemStack theStack )
		{
			x = theX;
			y = theY;
			stack = new ItemStack[] { theStack };
		}
		
		public DisplayStack( int theX, int theY, ItemStack... theStack )
		{
			x = theX;
			y = theY;
			stack = theStack;
			
			if ( stack.length == 0 )
			{
				throw new IllegalArgumentException( "There must be at least one item to display." );
			}
		}
		
		public DisplayStack( int theX, int theY, List< ItemStack > theStack )
		{
			x = theX;
			y = theY;
			stack = theStack.toArray( new ItemStack[] {} );
			
			if ( stack.length == 0 )
			{
				throw new IllegalArgumentException( "There must be at least one item to display." );
			}
		}
		
		public ItemStack getCurrent()
		{
    		int displayIndex = ( int )( ( System.currentTimeMillis() / 1000 ) % stack.length );
    		return stack[ displayIndex ];
		}
		
		@Override
		public String toString()
		{
			return "DisplayStack{x=" + x + " y=" + y + " stack=" + stack + "}";
		}
		
		public int x;
		public int y;
		public int z = 0;
		public ItemStack[] stack;
	}

	public static void handleRecipe( List< DisplayStack > items, IRecipe curr, int craftLeft, int craftTop )
	{
		if ( curr instanceof ShapedRecipes )
		{
			ShapedRecipes recipe = ( ShapedRecipes ) curr;
			handleShaped( items, recipe, craftLeft, craftTop );
		}
		else if ( curr instanceof ShapelessRecipes )
		{
			ShapelessRecipes recipe = ( ShapelessRecipes ) curr;
			handleShapeless( items, recipe, craftLeft, craftTop );
		}
		else if ( curr instanceof ShapelessOreRecipe )
		{
			ShapelessOreRecipe recipe = ( ShapelessOreRecipe ) curr;
			handleShapelessOre( items, recipe, craftLeft, craftTop );
		}
		else if ( curr instanceof ShapedOreRecipe )
		{
			ShapedOreRecipe recipe = ( ShapedOreRecipe ) curr;
			handleShapedOre( items, recipe, craftLeft, craftTop );
		}
	}
	
	private static void handleShaped( List< DisplayStack > items, ShapedRecipes recipe, int craftLeft, int craftTop )
	{
		for ( int ix = 0; ix < recipe.recipeWidth; ++ix )
		{
    		for ( int iy = 0; iy < recipe.recipeHeight; ++iy )
    		{
    			ItemStack stack = recipe.recipeItems[ ix + iy * recipe.recipeWidth ];
    			if ( stack == null )
    			{
    				continue;
    			}
    			
    			ItemStack[] displayItems = getAliases( stack );
    			
    			int x = craftLeft + 1 + ix * 18;
    			int y = craftTop + 1 + iy * 18;
    			
    			items.add( new DisplayStack( x, y, displayItems ) );
    		}
		}
		
		items.add( new DisplayStack( craftLeft + 18 - 4 + 5, craftTop + 90 + 5, recipe.getRecipeOutput() ) );
	}
	
	private static void handleShapeless( List< DisplayStack > items, ShapelessRecipes recipe, int craftLeft, int craftTop )
	{
		for ( int i = 0; i < recipe.recipeItems.size(); ++i )
		{
			int ix = i % 3;
			int iy = i / 3;
	
			ItemStack stack = ( ItemStack ) recipe.recipeItems.get( i );
			if ( stack == null )
			{
				continue;
			}
			
			ItemStack[] displayItems = getAliases( stack );
			
			int x = craftLeft + 1 + ix * 18;
			int y = craftTop + 1 + iy * 18;
			
			items.add( new DisplayStack( x, y, stack ) );
		}
		
		items.add( new DisplayStack( craftLeft + 18 - 4 + 5, craftTop + 90 + 5, recipe.getRecipeOutput() ) );
	}
	
	private static void handleShapedOre( List< DisplayStack > items, ShapedOreRecipe recipe, int craftLeft, int craftTop )
	{
		int width = -1;
		int height = -1;
		Class c = ShapedOreRecipe.class;
		Field f1 = c.getDeclaredFields()[ 4 ]; f1.setAccessible( true );
		Field f2 = c.getDeclaredFields()[ 5 ]; f2.setAccessible( true );
		try
		{
			width = ( Integer ) f1.get( recipe );
			height = ( Integer ) f2.get( recipe );
		}
		catch ( IllegalAccessException exception )
		{
		}
		
		for ( int ix = 0; ix < width; ++ix )
		{
    		for ( int iy = 0; iy < height; ++iy )
    		{
    			Object obj = recipe.getInput()[ ix + iy * width ];
    			if ( obj == null )
    			{
    				continue;
    			}
    			//System.out.println( obj );
    			
    			ItemStack[] stack = getAliases( obj );
    			
    			int x = craftLeft + 1 + ix * 18;
    			int y = craftTop + 1 + iy * 18;
    			
    			items.add( new DisplayStack( x, y, stack ) );
    		}
		}
		
		items.add( new DisplayStack( craftLeft + 18 - 4 + 5, craftTop + 90 + 5, recipe.getRecipeOutput() ) );
	}
	
	private static void handleShapelessOre( List< DisplayStack > items, ShapelessOreRecipe recipe, int craftLeft, int craftTop )
	{
		for ( int i = 0; i < recipe.getInput().size(); ++i )
		{
			int ix = i % 3;
			int iy = i / 3;
			
			Object obj = recipe.getInput().get( i );
			ItemStack[] stack = getAliases( obj );
			
			int x = craftLeft + 1 + ix * 18;
			int y = craftTop + 1 + iy * 18;
			
			
			items.add( new DisplayStack( x, y, stack ) );
		}
		
		items.add( new DisplayStack( craftLeft + 18 - 4 + 5, craftTop + 90 + 5, recipe.getRecipeOutput() ) );
	}
	
	public static ItemStack[] getAliases( Object obj )
	{
		ItemStack[] stack = null;
		if ( obj instanceof ItemStack )
		{
			ItemStack s = ( ItemStack ) obj;
			if ( s.getItemDamage() == OreDictionary.WILDCARD_VALUE )
			{
				List< ItemStack > results = new ArrayList< ItemStack >();
				if ( SpaceCore.proxy.isClient() )
				{
					addSubItems( results, s );
				}
				results.add( s );
				stack = ( ItemStack[] ) results.toArray( new ItemStack[] {} );
			}
			else
			{
				stack = new ItemStack[] { s };
			}
		}
		else if ( obj instanceof Item || obj instanceof Block )
		{
			Item item = null;
			if ( obj instanceof Item )
			{
				item = ( Item ) obj;
			}
			else
			{
				item = new ItemStack( ( Block ) obj ).getItem();
			}
			
			List list = new ArrayList();
			if ( SpaceCore.proxy.isClient() )
			{
				addSubItems( list, new ItemStack( item ) );
			}
			list.add( new ItemStack( item, 1, OreDictionary.WILDCARD_VALUE ) );
			stack = ( ItemStack[] ) list.toArray( new ItemStack[] {} );
		}
		else if ( obj instanceof ArrayList )
		{
			List< ItemStack > results = new ArrayList< ItemStack >();
			for ( Object o : ( ArrayList ) obj )
			{
				ItemStack tmp = ( ItemStack ) o;
				Item item = ( Item ) tmp.getItem();
				
				if ( tmp.getItemDamage() == OreDictionary.WILDCARD_VALUE )
				{
					if ( SpaceCore.proxy.isClient() )
					{
						addSubItems( results, tmp );
					}
					results.add( tmp );
				}
				else
				{
					results.add( tmp );
				}
			}
			stack = ( ItemStack[] ) results.toArray( new ItemStack[] {} );
		}
		else if ( obj instanceof String )
		{
			return getAliases( OreDictionary.getOres( ( String ) obj ) );
		}
		else
		{
			throw new IllegalArgumentException( "Bad item? " + obj.getClass() + " " + obj );
		}
		
		if ( stack.length <= 0 ) SpaceCoreLog.severe("EMPTY ALIASES! " + obj);
		
		return stack;
	}

	public static boolean isOre( String type, ItemStack stack )
	{
		List< ItemStack > stacks = OreDictionary.getOres( type );
		for ( ItemStack ore : stacks )
		{
			if ( ore.getItem() != stack.getItem() ) continue;
			
			if ( ore.getItemDamage() == OreDictionary.WILDCARD_VALUE )
			{
				return true;
			}
			else if ( ore.getItemDamage() == stack.getItemDamage() )
			{
				// Do I need to check NBT?
				return true;
			}
		}
		
		return false;
	}

	public static boolean matches( ItemStack a, ItemStack b )
	{
		if ( a.getItem() == b.getItem() )
		{
			/*if ( ( a.getTagCompound() == null ) != ( b.getTagCompound() == null ) )
			{
				return false;
			}
			else if ( a.getTagCompound() != null && !a.getTagCompound().equals( b.getTagCompound() ) )
			{
				return false;
			}*/
			
			if ( a.getItemDamage() == OreDictionary.WILDCARD_VALUE )
			{
				return true;
			}
			
			return ( a.getItemDamage() == b.getItemDamage() );
		}
		
		return false;
	}
	
	private static void addSubItems( List list, ItemStack stack )
	{
		stack.getItem().getSubItems( stack.getItem(), getCreativeTab( stack.getItem() ), list );
	}
	
	private static CreativeTabs getCreativeTab( Item item )
	{
		try
		{
			Field f = Item.class.getDeclaredFields()[ 2 ];
			f.setAccessible( true );
			return ( CreativeTabs ) f.get( item );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
			return null;
		}
	}
}
