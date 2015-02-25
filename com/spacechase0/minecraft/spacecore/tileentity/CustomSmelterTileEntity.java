package com.spacechase0.minecraft.spacecore.tileentity;

import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;

import com.spacechase0.minecraft.spacecore.block.CustomSmelterBlock;

public abstract class CustomSmelterTileEntity extends TileEntity implements IInventory
{
	@Override
	public void updateEntity()
	{
		int oldProgress = progressNeeded;
		updateProgressNeeded();
		if ( oldProgress == 0 && progressNeeded > 0 )
		{
			// Hope this works
			markDirty(); // onInventoryChanged?
			worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
		}
		
		if ( progressNeeded > 0 && burnTimeLeft <= 0 && getStackInSlot( getFuelSlot() ) != null )
		{
			burnTimeTotal = burnTimeLeft = TileEntityFurnace.getItemBurnTime( getStackInSlot( getFuelSlot() ) );
			decrStackSize( getFuelSlot(), 1 );
			setBurnState( true );
			
			markDirty(); // onInventoryChanged?
			worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
		}
		
		if ( burnTimeLeft > 0 )
		{
			--burnTimeLeft;
			if ( burnTimeLeft <= 0 )
			{
				setBurnState( false );
			}
			
			if ( progressNeeded > 0 )
			{
				++progressAmount;
			}
		}
		else
		{
			burnTimeTotal = 0;
		}
		
		if ( progressNeeded > 0 && progressAmount >= progressNeeded )
		{
			recipeCompleted();
			
			progressAmount = 0;

			markDirty(); // onInventoryChanged?
			worldObj.markBlockForUpdate( xCoord, yCoord, zCoord );
		}
	}
	
    @Override
    public void readFromNBT( NBTTagCompound tag )
    {
		super.readFromNBT( tag );
		
        burnTimeTotal = ( int ) tag.getShort( "BurnTimeTotal" );
        burnTimeLeft = ( int ) tag.getShort( "BurnTimeLeft" );
        progressAmount = ( int ) tag.getShort( "ProgressAmount" );
        
        updateProgressNeeded();
    }

    @Override
    public void writeToNBT( NBTTagCompound tag )
    {
		super.writeToNBT( tag );
		
        tag.setShort( "BurnTimeTotal", ( short ) burnTimeTotal );
        tag.setShort( "BurnTimeLeft", ( short ) burnTimeLeft );
        tag.setShort( "ProgressAmount", ( short ) progressAmount );
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -1, nbttagcompound);
    }
    
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
    	if ( worldObj.isRemote )
    	{
    		readFromNBT( pkt.func_148857_g() );
    	}
    }
    
    public int getBurnTimeTotal()
    {
    	return burnTimeTotal;
    }
    
    public int getBurnTimeLeft()
    {
    	return burnTimeLeft;
    }
    
    public int getProgressNeeded()
    {
    	return progressNeeded;
    }
    
    public int getProgressAmount()
    {
    	return progressAmount;
    }
    
	public void setBurnTimeLeft( int theBurnTimeLeft )
	{
		burnTimeLeft = theBurnTimeLeft;
	}

	public void setBurnTimeTotal( int theBurnTimeTotal )
	{
		burnTimeTotal = theBurnTimeTotal;
	}

	public void setProgressNeeded( int theProgressNeeded )
	{
		progressNeeded = theProgressNeeded;
	}
	public void setProgressAmount( int theProgressAmount )
	{
		progressAmount = theProgressAmount;
	}
    
    protected void setBurnState( boolean burning )
    {
    	getBlock().updateBlockState( burning, worldObj, xCoord, yCoord, zCoord );
    }
    
    protected abstract CustomSmelterBlock getBlock();
    
    public abstract int getFuelSlot();
    
    protected abstract void updateProgressNeeded();
    protected abstract void recipeCompleted();
    
    private int burnTimeLeft;
    private int burnTimeTotal;
    private int progressAmount;
    protected int progressNeeded;
}
