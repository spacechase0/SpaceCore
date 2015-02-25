package com.spacechase0.minecraft.spacecore.block;

import java.lang.reflect.Field;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.spacechase0.minecraft.spacecore.tileentity.CustomSmelterTileEntity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class CustomSmelterBlock extends BlockFurnace
{
	public CustomSmelterBlock( boolean theActive )
	{
		super( theActive );
		active = theActive;
		
		//setUnlocalizedName( "kiln" );
	}
	
	public abstract String getIconBase();

	@Override
	@SideOnly( Side.CLIENT )
    public void registerBlockIcons( IIconRegister register )
    {
		mainIcon = register.registerIcon( getIconBase() + ":" + getUnlocalizedName().substring( 5 ) + ( active ? "Active" : "Idle" ) );
    }
	
	@Override
    public IIcon getIcon( int side, int meta )
    {
        return ( side != meta ? this.blockIcon : this.mainIcon );
    }
	
	@Override
    public Item getItemDropped(int par1, Random par2Random, int par3)
    {
        return Item.getItemFromBlock( getIdleBlock() );
    }
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if ( world.isRemote )
        {
            return true;
        }
        else
        {
        	openGui( world, x, y, z, player );

            return true;
        }
    }
	
	@Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
	{
		CustomSmelterTileEntity smelter = ( CustomSmelterTileEntity ) world.getTileEntity(x, y, z);
        if ( smelter == null || getKeepFurnaceInventory() )
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
                
        		EntityItem entity = new EntityItem( world, (double)((float)x + fx), (double)((float)y + fy), (double)((float)z + fz), stack.copy());
        		
                float f3 = 0.05F;
                entity.motionX = (double)((float)rand.nextGaussian() * f3);
                entity.motionY = (double)((float)rand.nextGaussian() * f3 + 0.2F);
                entity.motionZ = (double)((float)rand.nextGaussian() * f3);
                
                world.spawnEntityInWorld(entity);
                
            	smelter.decrStackSize( i, stack.stackSize );
        	}
    	}
    	
    	world.removeTileEntity( x, y, z );
	}
	
	//@Override
	public abstract TileEntity createNewTileEntity( World world, int i );
	
	//@Override
    public static void updateBlockState( boolean burning, World world, int x, int y, int z )
    {
        int meta = world.getBlockMetadata( x, y, z );
        TileEntity entity = world.getTileEntity( x, y, z );
        setKeepFurnaceInventory( true );

        CustomSmelterBlock block = ( CustomSmelterBlock ) world.getBlock( x, y, z );
    	if ( block.getActiveBlock() == block.getIdleBlock() )
    	{
    		// The block only has 1 state
    		setKeepFurnaceInventory( false );
    		return;
    	}
    	
        if (burning)
        {
            world.setBlock( x, y, z, block.getActiveBlock() );
        }
        else
        {
            world.setBlock( x, y, z, block.getIdleBlock() );
        }

        setKeepFurnaceInventory( false );
        world.setBlockMetadataWithNotify( x, y, z, meta, 2 );

        if ( entity != null )
        {
            entity.validate();
            world.setTileEntity( x, y, z, entity );
        }
    }
	
	private static boolean getKeepFurnaceInventory()
	{
		try
		{
			Class c = BlockFurnace.class;
			Field field = c.getDeclaredFields()[ 2 ];
			field.setAccessible( true );
			return ( Boolean ) field.get( null );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
			return false;
		}
	}
	
	private static void setKeepFurnaceInventory( boolean keep )
	{
		try
		{
			Class c = BlockFurnace.class;
			Field field = c.getDeclaredFields()[ 2 ];
			field.setAccessible( true );
			field.setBoolean( null, keep );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
		}
	}
	
	protected abstract void openGui( World world, int x, int y, int z, EntityPlayer player );
	protected abstract Block getActiveBlock();
	protected abstract Block getIdleBlock();
	
	protected final boolean active;
	protected IIcon mainIcon;
}
