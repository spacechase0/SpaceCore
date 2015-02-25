package com.spacechase0.minecraft.spacecore.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class DummyContainer extends Container implements IInventory
{
	public DummyContainer( int size )
	{
		stacks = new ItemStack[ size ];
		for ( int i = 0; i < size; ++i )
		{
			addSlotToContainer( new Slot( this, i, i * 16, 0 ) );
		}
	}
	
	@Override
	public boolean canInteractWith( EntityPlayer entityplayer )
	{
		return true;
	}
	
	public ItemStack[] stacks;

	@Override
	public int getSizeInventory()
	{
		return stacks.length;
	}

	@Override
	public ItemStack getStackInSlot( int slot )
	{
		return stacks[ slot ];
	}

	@Override
	public ItemStack decrStackSize( int slot, int amt )
	{
		if ( stacks[ slot ] == null )
		{
			return null;
		}
		
		ItemStack ret = stacks[ slot ].copy();
		ret.stackSize = Math.min( amt, stacks[ slot ].stackSize );
		
		stacks[ slot ].stackSize -= ret.stackSize;
		if ( stacks[ slot ].stackSize <= 0 )
		{
			stacks[ slot ] = null;
		}
		
		return ret;
	}

	@Override
	public ItemStack getStackInSlotOnClosing( int i )
	{
		return null;
	}

	@Override
	public void setInventorySlotContents( int slot, ItemStack stack )
	{
		stacks[ slot ] = stack;
		onCraftMatrixChanged( this );
	}

	@Override
	public String getInventoryName()
	{
		return "";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return true;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public void markDirty()
	{
	}

	@Override
	public boolean isUseableByPlayer( EntityPlayer player )
	{
		return true;
	}

	@Override
	public void openInventory()
	{
	}

	@Override
	public void closeInventory()
	{
	}

	@Override
	public boolean isItemValidForSlot( int slot, ItemStack stack )
	{
		return true;
	}
}
