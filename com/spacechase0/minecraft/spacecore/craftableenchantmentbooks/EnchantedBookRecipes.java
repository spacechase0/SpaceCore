package com.spacechase0.minecraft.spacecore.craftableenchantmentbooks;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class EnchantedBookRecipes implements IRecipe
{
	@Override
	public boolean matches( InventoryCrafting inv, World world )
	{
		return ( findMatchingRecipe( inv ) != null );
	}

	@Override
	public ItemStack getCraftingResult( InventoryCrafting inv )
	{
		return findMatchingRecipe( inv );
	}

	@Override
	public int getRecipeSize()
	{
		return 3;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return null;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting craftInv)
	{
        ItemStack[] stacks = new ItemStack[craftInv.getSizeInventory()];

        for (int i = 0; i < stacks.length; ++i)
        {
            ItemStack itemstack = craftInv.getStackInSlot(i);
            stacks[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
        }

        return stacks;
	}
    
    private ItemStack findMatchingRecipe( InventoryCrafting inv )
    {
    	for ( int r = 0; r < recipes.size(); ++r )
    	{
    		Recipe recipe = recipes.get( r );

	    	boolean hasBook = false;
	    	boolean[] matches = new boolean[ recipe.recipe.length ];
	    	for ( int i = 0; i < recipe.recipe.length; ++i )
	    	{
	    		matches[ i ] = ( recipe.recipe[ i ] == null ) ? true : false;
	    	}
	    	
	    	boolean stop = false;
	        for ( int i = 0; i < inv.getSizeInventory(); ++i )
	        {
	            ItemStack stack = inv.getStackInSlot( i );
	            if ( stack == null )
	            {
	            	continue;
	            }
	            
            	if ( !hasBook && recipe.in == null && stack.getItem() == Items.book )
            	{
            		hasBook = true;
            	}
            	else if ( !hasBook && stack.getItem() == Items.enchanted_book && recipe.in != null )
            	{
            		ItemStack in = Items.enchanted_book.getEnchantedItemStack( recipe.in );
            		NBTTagList inTag = ( NBTTagList ) in.getTagCompound().getTag( "StoredEnchantments" );
            		if ( stack.isItemEqual( in ) && stack.getTagCompound().getTag( "StoredEnchantments" ).equals( inTag ) )
            		{
            			hasBook = true;
            		}
            	}
	            else
	            {
	            	boolean matched = false;
	            	for ( int m = 0; m < recipe.recipe.length; ++m )
	            	{
	            		ItemStack mat = recipe.recipe[ m ];
	            		if ( mat == null )
	            		{
	            			continue;
	            		}
	            		
	            		if ( !matches[ m ] && stack.isItemEqual( mat ) )
	            		{
	            			matches[ m ] = true;
	            			matched = true;
	            			break;
	            		}
	            	}
	            	
	            	if ( !matched )
	            	{
	            		stop = true;
	            		break;
	            	}
	            }
	        }
	        
	        if ( stop )
	        {
	        	continue;
	        }
	        
	        boolean found = hasBook;
	    	for ( int i = 0; i < matches.length; ++i )
	    	{
	    		found = found && matches[ i ];
	    	}
	    	
	    	if ( found )
	    	{
	    		return Items.enchanted_book.getEnchantedItemStack( recipe.out );
	    	}
    	}
        
        return null;
    }
	
	private static class Recipe
	{
		public EnchantmentData out;
		public EnchantmentData in;
		public ItemStack[] recipe = new ItemStack[ 8 ];
	}
	
	public static void addEnchantmentRecipe( ItemStack[] items, EnchantmentData out, EnchantmentData in )
	{
		if ( items.length > 8 )
		{
			throw new IllegalArgumentException( "8 itemstacks must be provided!" );
		}
		else if ( out == null )
		{
			throw new IllegalArgumentException( "Enchantment must be provided for output!" );
		}
		
		Recipe r = new Recipe();
		r.out = out;
		r.in = in;
		r.recipe = items;
		
		if ( items.length < 8 )
		{
			ItemStack[] newItems = new ItemStack[ 8 ];
			for ( int i = 0; i < items.length; newItems[ i ] = items[ i++ ] );
			r.recipe = newItems;
		}
		
		recipes.add( r );
	}
	
	private static List< Recipe > recipes = new ArrayList< Recipe >();
}
