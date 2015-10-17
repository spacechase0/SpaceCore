package com.spacechase0.minecraft.spacecore.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ItemStackInventory implements IInventory
{
	public ItemStackInventory( ItemStack theInvStack, int theSize )
	{
		invStack = theInvStack;
		size = theSize;
		stacks = new ItemStack[ size ];
		
		NBTTagCompound stackTag = invStack.getTagCompound();
		if ( stackTag == null )
		{
			invStack.setTagCompound( stackTag = new NBTTagCompound() );
		}
		
		NBTTagCompound invTag = stackTag.getCompoundTag( "Inventory" );
		if ( invTag == null )
		{
			stackTag.setTag( "Inventory", ( invTag = new NBTTagCompound() ) );
		}
		
		for ( int i = 0; i < size; ++i )
		{
			NBTTagCompound slotTag = invTag.getCompoundTag( "Slot" + i );
			if ( slotTag == null )
			{
				continue;
			}
			stacks[ i ] = ItemStack.loadItemStackFromNBT( slotTag );
		}
	}

	@Override
	public int getSizeInventory()
	{
		return size;
	}

	@Override
	public ItemStack getStackInSlot( int slot )
	{
		return stacks[ slot ];
	}

	@Override
	public ItemStack decrStackSize( int slot, int amt )
	{
		ItemStack stack = getStackInSlot( slot );
		if ( stack == null )
		{
			return null;
		}
		
		ItemStack ret = stack.copy();
		ret.stackSize = Math.min( amt, stack.stackSize );
		
		stack.stackSize -= ret.stackSize;
		
		if ( stack.stackSize <= 0 )
		{
			setInventorySlotContents( slot, null );
		}
		
		markDirty();
		
		return ret;
	}

	@Override
	public ItemStack getStackInSlotOnClosing( int slot )
	{
		return null;
	}

	@Override
	public void setInventorySlotContents( int slot, ItemStack stack )
	{
		stacks[ slot ] = stack;
		markDirty();
	}
	
	@Override
	public void clear()
	{
		for ( int i = 0; i < stacks.length; ++i )
		{
			stacks[ i ] = null;
		}
	}

	@Override
	public void markDirty()
	{
		NBTTagCompound stackTag = invStack.getTagCompound();
		if ( stackTag == null )
		{
			invStack.setTagCompound( stackTag = new NBTTagCompound() );
		}
		
		NBTTagCompound invTag = stackTag.getCompoundTag( "Inventory" );
		if ( invTag == null )
		{
			stackTag.setTag( "Inventory", ( invTag = new NBTTagCompound() ) );
		}
		
		for ( int i = 0; i < size; ++i )
		{
			NBTTagCompound itemTag = new NBTTagCompound();
			if ( stacks[ i ] == null )
			{
				// ???
			}
			else
			{
				stacks[ i ].writeToNBT( itemTag );
			}
			
			invTag.setTag( "Slot" + i, itemTag );
		}
		
		stackTag.setTag( "Inventory", invTag );
		invStack.setTagCompound( stackTag );
	}

	@Override
	public boolean isUseableByPlayer( EntityPlayer player )
	{
		return true;
		/*
		for ( int i = 0; i < player.inventory.mainInventory.length; ++i )
		{
			ItemStack stack = player.inventory.mainInventory[ i ];
			if ( stack == null )
			{
				continue;
			}
			
			if ( invStack == stack )
			{
				return true;
			}
		}
		
		return false;
		//*/
	}

	@Override
	public void openInventory( EntityPlayer player )
	{
	}

	@Override
	public void closeInventory( EntityPlayer player )
	{
	}

	@Override
	public boolean isItemValidForSlot( int slot, ItemStack stack )
	{
		return true;
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	public final ItemStack invStack;
	protected final ItemStack[] stacks;
	protected final int size;
}
