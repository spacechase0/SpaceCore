package com.spacechase0.minecraft.spacecore.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;

import com.spacechase0.minecraft.spacecore.tileentity.CustomSmelterTileEntity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CustomSmelterContainer extends Container
{
	public CustomSmelterContainer( CustomSmelterTileEntity theSmelter )
	{
		smelter = theSmelter;
	}
	
	@Override
	public boolean canInteractWith( EntityPlayer player )
	{
		return smelter.isUseableByPlayer( player );
	}
	
    public void addCraftingToCrafters( ICrafting crafting )
    {
        super.addCraftingToCrafters( crafting );
        crafting.sendProgressBarUpdate(this, 0, smelter.getBurnTimeLeft() );
        crafting.sendProgressBarUpdate(this, 1, smelter.getBurnTimeTotal() );
        crafting.sendProgressBarUpdate(this, 2, smelter.getProgressAmount() );
    }
    
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for ( int i = 0; i < this.crafters.size(); ++i )
        {
            ICrafting crafting = ( ICrafting ) crafters.get( i );

            if ( lastBurnTimeLeft != smelter.getBurnTimeLeft() )
            {
                crafting.sendProgressBarUpdate( this, 0, smelter.getBurnTimeLeft() );
            }
            else if ( lastBurnTimeTotal != smelter.getBurnTimeTotal() )
            {
                crafting.sendProgressBarUpdate( this, 1, smelter.getBurnTimeTotal() );
            }
            else if ( lastProgressAmount != smelter.getProgressAmount() )
            {
                crafting.sendProgressBarUpdate( this, 2, smelter.getProgressAmount() );
            }
        }

        lastBurnTimeLeft = smelter.getBurnTimeLeft();
        lastBurnTimeTotal = smelter.getBurnTimeTotal();
        lastProgressAmount = smelter.getProgressAmount();
    }

    @SideOnly( Side.CLIENT )
    public void updateProgressBar( int id, int value )
    {
        if ( id == 0 )
        {
            smelter.setBurnTimeLeft( value );
        }
        else if ( id == 1 )
        {
        	smelter.setBurnTimeTotal( value );
        }
        else if ( id == 2 )
        {
            smelter.setProgressAmount( value );
        }
    }

    protected void bindPlayerInventory( InventoryPlayer player )
    {
		for (int iy = 0; iy < 3; iy++)
		{
			for (int ix = 0; ix < InventoryPlayer.getHotbarSize(); ix++)
			{
				addSlotToContainer( new Slot( player, ix + iy * InventoryPlayer.getHotbarSize() + InventoryPlayer.getHotbarSize(), 8 + ix * 18, 84 + iy * 18 ) );
			}
		}
		
		for (int ix = 0; ix < InventoryPlayer.getHotbarSize(); ix++)
		{
			addSlotToContainer( new Slot( player, ix, 8 + ix * 18, 142 ) );
		}
	}
	
	private final CustomSmelterTileEntity smelter;
    private int lastBurnTimeLeft = 0;
    private int lastBurnTimeTotal = 0;
    private int lastProgressAmount = 0;
}
