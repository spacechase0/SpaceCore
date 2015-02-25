package com.spacechase0.minecraft.spacecore.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public abstract class ItemStackContainer extends Container
{
	public ItemStackContainer( InventoryPlayer thePlayer )
	{
		player = thePlayer;
	}
	
	@Override
    public void onContainerClosed( EntityPlayer player )
	{
		getItemInventory().markDirty();
	}
	
	@Override
	public boolean canInteractWith( EntityPlayer player )
	{
		return getItemInventory().isUseableByPlayer( player );
	}
    
    protected abstract ItemStackInventory getItemInventory();
	
    protected final InventoryPlayer player;
}
