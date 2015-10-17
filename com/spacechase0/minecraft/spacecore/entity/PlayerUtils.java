package com.spacechase0.minecraft.spacecore.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.spacechase0.minecraft.spacecore.asm.SpaceCoreLog;

public class PlayerUtils
{
	public static void giveItemOnce( EntityPlayer player, String checkTag, ItemStack stack )
	{
		NBTTagCompound data = player.getEntityData();
		if ( !data.hasKey( EntityPlayer.PERSISTED_NBT_TAG ) )
		{
			data.setTag( EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound() );
		}
		NBTTagCompound persist = data.getCompoundTag( EntityPlayer.PERSISTED_NBT_TAG );
		
		if ( !persist.hasKey( checkTag ) )
		{
			SpaceCoreLog.severe( player.getName() + " has not yet " + checkTag + ", giving " + stack );
			player.inventory.addItemStackToInventory( stack.copy() );
			persist.setBoolean( checkTag, true );
		}
	}
}
