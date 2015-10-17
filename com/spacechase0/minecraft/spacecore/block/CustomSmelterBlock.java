package com.spacechase0.minecraft.spacecore.block;

import java.lang.reflect.Field;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.spacechase0.minecraft.spacecore.tileentity.CustomSmelterTileEntity;

public abstract class CustomSmelterBlock extends BlockContainer
{
	public CustomSmelterBlock( Material mat )
	{
		super( mat );
	}
	
	@Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float par7, float par8, float par9)
    {
        if ( world.isRemote )
        {
            return true;
        }
        else
        {
        	openGui( world, pos, player );
            return true;
        }
    }
	
	@Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		CustomSmelterTileEntity smelter = ( CustomSmelterTileEntity ) world.getTileEntity( pos );
        if ( smelter == null )
        {
        	return;
        }
        
    	for ( int i = 0; i < smelter.getSizeInventory(); ++i )
    	{
        	ItemStack stack = smelter.getStackInSlot( i );
        	if ( stack != null )
        	{
        		Random rand = new Random();
        		
                float fx = rand.nextFloat() * 0.8F + 0.1F;
                float fy = rand.nextFloat() * 0.8F + 0.1F;
                float fz = rand.nextFloat() * 0.8F + 0.1F;
                
        		EntityItem entity = new EntityItem( world, (double)((float)pos.getX() + fx), (double)((float)pos.getY() + fy), (double)((float)pos.getZ() + fz), stack.copy());
        		
                float f3 = 0.05F;
                entity.motionX = (double)((float)rand.nextGaussian() * f3);
                entity.motionY = (double)((float)rand.nextGaussian() * f3 + 0.2F);
                entity.motionZ = (double)((float)rand.nextGaussian() * f3);
                
                world.spawnEntityInWorld(entity);
                
            	smelter.decrStackSize( i, stack.stackSize );
        	}
    	}
    	
    	world.updateComparatorOutputLevel( pos, this );
    	super.breakBlock( world, pos, state );
	}

	@Override
    public boolean hasComparatorInputOverride()
    {
        return true;
    }
	
	@Override
    public int getComparatorInputOverride(World worldIn, BlockPos pos)
    {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }
	
	//@Override
	public abstract TileEntity createNewTileEntity( World world, int i );
	
	protected abstract void openGui( World world, BlockPos pos, EntityPlayer player );
}
