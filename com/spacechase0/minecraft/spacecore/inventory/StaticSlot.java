package com.spacechase0.minecraft.spacecore.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class StaticSlot extends Slot
{
    public StaticSlot( IInventory inventory, int index, int x, int y )
    {
    	super( inventory, index, x, y );
    }
    
    @Override
    public boolean canTakeStack( EntityPlayer player )
    {
        return false;
    }
}
